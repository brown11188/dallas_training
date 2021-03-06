package com.api.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.api.model.TblMessage;
import com.api.model.TblUser;

public class UserDAOImp implements UserDAO {

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TblUser checkLogin(String userName, String password, String registrationId) {
        Session session = this.sessionFactory.openSession();
        List<TblUser> users = new ArrayList<TblUser>();
        users = session.createQuery("from TblUser where userName = ? AND password = ?").setParameter(0, userName)
                .setParameter(1, password).list();
        if (users.size() > 0) {
        	updateRegId(userName, registrationId);
        	users.get(0).setRegistrationId(registrationId);
            users.get(0).setPassword("");
            session.close();
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String storePassword(String password) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] mgsDigest = digest.digest();
            for (byte aMgsDigest : mgsDigest) {
                hexString.append(Integer.toHexString(0xFF & aMgsDigest));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    @Override
    public boolean addUser(TblUser user) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(user);
        try {
            tx.commit();
            session.close();
            return true;
        } catch (Exception e) {
        	session.close();
            return false;
        }

    }

    @Override
    public boolean addMessage(String message, Date expires_date, int user_id, int sender_id) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        TblMessage messageObj = new TblMessage();
        messageObj.setMessage(message);
        messageObj.setExpiresTime(expires_date);
        messageObj.setUserId(user_id);
        messageObj.setSenderId(sender_id);
        if (messageObj != null) {
            session.persist(messageObj);
            tx.commit();
            session.close();
            return true;
        }
      //  session.close();
        return false;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TblUser> getUsers(String userName) {
        Session session = this.sessionFactory.openSession();
        List<TblUser> users = new ArrayList<TblUser>();
        users = session.createQuery("from TblUser where userName != ?").setParameter(0, userName).list();
        session.close();
        return users;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TblUser getUser(String username) {
        // TODO Auto-generated method stub
        Session session = this.sessionFactory.openSession();
        List<TblUser> users = new ArrayList<TblUser>();
        users = session.createQuery("from TblUser where userName = ?").setParameter(0, username).list();
        if (users.size() > 0) {
        	session.close();
            return users.get(0);
        } else {
        	session.close();
            return null;
        }
    }

    public void updateRegId(String username, String registrationId){
    	Session session = this.sessionFactory.openSession();
    	org.hibernate.Query query = session.createQuery("UPDATE TblUser SET registrationId =:registrationId"+
    								" WHERE userName =:userName");
    	query.setParameter("registrationId", registrationId);
    	query.setParameter("userName", username);
    	query.executeUpdate();
    	
    	org.hibernate.Query queryDelRegId =  session.createQuery("UPDATE TblUser SET registrationId =:registrationId"+
				" WHERE userName !=:userName AND registrationId =:regId");
    	queryDelRegId.setParameter("registrationId", null);
    	queryDelRegId.setParameter("userName", username);
    	queryDelRegId.setParameter("regId", registrationId);	
    	queryDelRegId.executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TblUser getUserByUserId(int user_id) {
        Session session = this.sessionFactory.openSession();
        List<TblUser> users = new ArrayList<TblUser>();
        users = session.createQuery("from TblUser where userId = ?").setParameter(0, user_id).list();
        if (users.size() > 0) {
        	session.close();
            return users.get(0);
        } else {
        	session.close();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TblMessage> getMessges(int user_id, int sender_id) {
        Session session = this.sessionFactory.openSession();
        List<TblMessage> messages = session
                .createQuery("from TblMessage where ( user_id = ? and sender_id =? ) or (user_id=? and sender_id =?) ")
                .setInteger(0, user_id).setInteger(1, sender_id).setInteger(2, sender_id).setInteger(3, user_id).list();
        if (messages.size() > 0) {
        	session.close();
            return messages;
        } else {
        	session.close();
            return null;
        }

    }

    @Override
    public TblMessage getLastMessage(int user_id, int sender_id) {
        Session session = this.sessionFactory.openSession();
        TblMessage message = (TblMessage) session
                .createQuery(
                        "from TblMessage where ( user_id = ? and sender_id =? ) or (user_id=? and sender_id =?) order by message_id DESC LIMIT 1")
                .setInteger(0, user_id).setInteger(1, sender_id).setInteger(2, sender_id).setInteger(3, user_id)
                .setMaxResults(1).uniqueResult();
        if (message != null) {
        	session.close();
            return message;
        } else {
        	session.close();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TblMessage> getLastTenMessages(int user_id, int sender_id, int offsetNumber) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        String queryString = "select * from (select * from tbl_message where (user_id= " + user_id + "  and sender_id= "
                + sender_id + ") or (user_id= " + sender_id + " and sender_id= " + user_id
                + ")  order by message_id desc limit 10 offset " + offsetNumber
                + " ) as temp order by temp.message_id asc";
        SQLQuery sql = session.createSQLQuery(queryString);
        sql.addEntity("tbl_message", TblMessage.class);
        List<TblMessage> messages = sql.list();
        tx.commit();
        if (messages.size() > 0) {
        	session.close();
            return messages;
        } else {
        	session.close();
            return null;
        }
    }

}
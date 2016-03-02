package com.api.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.jdbc.support.GeneratedKeyHolder;

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

	public boolean addUser(TblUser user) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(user);
		try {
			tx.commit();
			session.close();
			return true;			
		} catch (Exception e) {
			return false;
		}
		
		
	}

	public void addMessage(String message, String expires_date, int user_id, int sender_id) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TblUser> getUsers(String userName) {
		Session session = this.sessionFactory.openSession();
		List<TblUser> users = new ArrayList<TblUser>();
		users = session.createQuery("from TblUser where userName != ?").setParameter(0, userName).list();
		return users;
	}

	@SuppressWarnings("unchecked")
	public TblUser getUser(String username) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.openSession();
		List<TblUser> users = new ArrayList<TblUser>();
		users = session.createQuery("from TblUser where userName = ?").setParameter(0, username).list();
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public TblUser getUserByUserId(int user_id) {
		Session session = this.sessionFactory.openSession();
		List<TblUser> users = new ArrayList<TblUser>();
		users = session.createQuery("from TblUser where userId = ?").setParameter(0, user_id).list();
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	public List<TblMessage> getMessges(int user_id, int sender_id) {
		// TODO Auto-generated method stub
		return null;
	}

	public TblMessage getLastMessage(int user_id, int sender_id) {
		// TODO Auto-generated method stub
		return null;
	}

	public TblUser checkLogin(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TblMessage> getLastTenMessages(int user_id, int sender_id, int offsetNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public String storePassword(String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblUser> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}

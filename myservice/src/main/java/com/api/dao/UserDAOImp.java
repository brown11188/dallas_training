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

	public void addUser(TblUser user) {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(user);
		tx.commit();
		session.close();
	}

	public void addMessage(String message, String expires_date, int user_id, int sender_id) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TblUser> getUsers() {
		Session session = this.sessionFactory.openSession();
		List<TblUser> users = new ArrayList<TblUser>();
		users = session.createQuery("from TblUser").list();
		return users;
	}

	public TblUser getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public TblUser getUserByUserId(int user_id) {
		// TODO Auto-generated method stub
		return null;
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

}

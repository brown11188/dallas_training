package com.api.dao;

import java.util.Date;
import java.util.List;

import com.api.model.TblMessage;
import com.api.model.TblUser;

public interface UserDAO {
    boolean addUser(TblUser user);

    boolean addMessage(String message, Date expires_date, int user_id, int sender_id);

    TblUser getUser(String username);

    TblUser getUserByUserId(int user_id);

    List<TblMessage> getMessges(int user_id, int sender_id);

    TblMessage getLastMessage(int user_id, int sender_id);

    TblUser checkLogin(String userName, String password,String registrationId);

    List<TblMessage> getLastTenMessages(int user_id, int sender_id, int offsetNumber);

    String storePassword(String password);

    List<TblUser> getUsers(String userName);
	
	void updateRegId(String username,String registrationId);
}

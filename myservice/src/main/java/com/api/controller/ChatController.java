package com.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.dao.UserDAOImp;
import com.api.model.TblUser;

@Controller
public class ChatController {
	@Autowired
	private UserDAOImp userDAO;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<TblUser> getUsers(@RequestParam("userName") String userName) {

		List<TblUser> users = new ArrayList<TblUser>();
		users = userDAO.getUsers(userName);
		return users;
	}

	@RequestMapping(value = "/getuser/byname", method = RequestMethod.GET)
	public @ResponseBody TblUser getUserByName(@RequestParam("userName") String userName) {
		TblUser user = new TblUser();
		user = userDAO.getUser(userName);
		return user;
	}

	@RequestMapping(value = "/getuser/byid", method = RequestMethod.GET)
	public @ResponseBody TblUser getUserById(@RequestParam("userId") int userId) {
		TblUser user = new TblUser();
		user = userDAO.getUserByUserId(userId);
		return user;
	}

	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public @ResponseBody TblUser login(@RequestParam("userName") String userName,
			@RequestParam("password") String password){
		TblUser user = new TblUser();
		password = userDAO.storePassword(password);
		user = userDAO.checkLogin(userName, password);
		return user;
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public @ResponseBody String addUser(@RequestParam("userName") String userName,
			@RequestParam("password") String password, @RequestParam("registrationId") String registrationId) {
		TblUser user = new TblUser();
		user.setUserName(userName);
		password = userDAO.storePassword(password);
		user.setPassword(password);
		user.setRegistrationId(registrationId);
		if (userDAO.addUser(user) == true) {
			return "{'result' : 'successful'}";
		} else {
			return "{'result' : 'fail'}";
		}

	}

}

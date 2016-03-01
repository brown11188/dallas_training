package com.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.dao.UserDAOImp;
import com.api.model.TblUser;

@Controller
public class ChatController {
	@Autowired
	UserDAOImp userDAO;
	
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public @ResponseBody List<TblUser> getUsers(){
		
		List<TblUser> users = new ArrayList<TblUser>();
		users = userDAO.getUsers();
		return users;
	}
	
}

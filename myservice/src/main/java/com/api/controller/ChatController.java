package com.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.common.TimeUtil;
import com.api.dao.UserDAOImp;
import com.api.model.TblMessage;
import com.api.model.TblUser;

@Controller
public class ChatController {

    @Autowired
    UserDAOImp userDAO;

    private TimeUtil timeUtil;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public @ResponseBody TblUser login(@RequestParam("userName") String userName,
            @RequestParam("password") String password) {
        TblUser user = new TblUser();

        password = userDAO.storePassword(password);
        user = userDAO.checkLogin(userName, password);
        return user;

    }

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

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public @ResponseBody String addUser(@RequestParam("userName") String userName,
            @RequestParam("password") String password, @RequestParam("registrationId") String registrationId) {
        TblUser user = new TblUser();
        user.setUserName(userName);
        user.setPassword(password);
        user.setRegistrationId(registrationId);
        if (userDAO.addUser(user) == true) {
            return "successful";
        } else {
            return "fail";
        }

    }

    @RequestMapping(value = "/getmessages", method = RequestMethod.GET)
    public @ResponseBody List<TblMessage> getMessges(@RequestParam("user_id") int user_id,
            @RequestParam("sender_id") int sender_id) {
        List<TblMessage> messages = userDAO.getMessges(user_id, sender_id);
        return messages;
    }

    @RequestMapping(value = "/addmessage", method = RequestMethod.GET)
    public @ResponseBody String addMessage(@RequestParam("content") String content,
            @RequestParam("user_id") int user_id, @RequestParam("sender_id") int sender_id) throws ParseException {
        timeUtil = new TimeUtil();
        if (userDAO.addMessage(content, timeUtil.formatDateTime(timeUtil.getCurrentTime()), sender_id,
                user_id) == true) {
            return "true";
        } else {
            return "false";
        }

    }

    @RequestMapping(value = "/getlastmessage", method = RequestMethod.GET)
    public @ResponseBody TblMessage getLastMessage(@RequestParam("user_id") int user_id,
            @RequestParam("sender_id") int sender_id) {
        TblMessage message = userDAO.getLastMessage(user_id, sender_id);

        return message;
    }

    @RequestMapping(value = "/getlasttenmessage", method = RequestMethod.GET)
    public @ResponseBody List<TblMessage> getMessges(@RequestParam("user_id") int user_id,
            @RequestParam("sender_id") int sender_id, @RequestParam("offset_number") int offset_number) {
        List<TblMessage> messages = userDAO.getLastTenMessages(user_id, sender_id, offset_number);
        return messages;

    }
}

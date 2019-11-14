package com.khal.intern_survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.UserService;
import com.khal.intern_survey.service.UserServiceImpl;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/hello")
	public String greetings() {
		return "hello";
	}
	
	@RequestMapping("/list")
	public String listAllUsers(Model theModel) {
		
		List<User> users = userService.findAll();
		theModel.addAttribute("users", users);
		return "list-users";
	}

}

package com.krzhal.PhysicianInternshipSurveyApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/hello")
	public String greetings() {
		return "hello";
	}

}

package com.coder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.coder.entity.User;
import com.coder.repository.UserRepository;
import com.coder.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session, Model m, HttpServletRequest request) {
	    
	    String url = request.getRequestURL().toString(); // Get the full URL
	    url = url.replace(request.getServletPath(), ""); // Remove the servlet path from the URL
	    
	    User saveUser = userService.saveUser(user, url);
	    
	    if (saveUser != null) {
	        session.setAttribute("msg", "Register Successfully");
	    } else {
	        session.setAttribute("msg", "Something went wrong");
	    }
	    
	    return "redirect:/register";
	}

	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model m) {
		
		boolean b = userService.verifyAccount(code);
		if(b) {
			m.addAttribute("msg", "Successfully your account verified");
		}else {
			m.addAttribute("msg", "may be your verification code is incorrect");
		}
		
		return"message";
	}
}

package com.coder.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coder.entity.User;
import com.coder.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		
		String email = p.getName();
		User user = userRepo.findByEmail(email);
		m.addAttribute("user", user);
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "profile";
	}
}

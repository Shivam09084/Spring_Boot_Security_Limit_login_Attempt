package com.coder.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.coder.entity.User;
import com.coder.repository.UserRepository;
import com.coder.service.UserService;
import com.coder.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		String email = request.getParameter("username");
		User user = userRepo.findByEmail(email);
		
		if(user != null) {
			
			if(user.isEnable()) {
				
				if(user.isAccountNonLocked()) {
					
					if(user.getFailedAttempt() < UserServiceImpl.Attempt_Time-1) {
						
						userService.increaseFailedAttempt(user);
					}else {
						
						userService.lock(user);
						exception = new LockedException("Your Account is Locked !! failed Attempt 3");
					}
				}else if(!user.isAccountNonLocked()) {
					
					if(userService.unlockAccountTimeExpired(user)) {
						
						exception = new LockedException("Account is unlocked !!! kindly try to login");
					}else {
						
						exception = new LockedException("Account is locked kindly try after some time");
					}
				}
			}else {
				exception = new LockedException("Account is inactive verify your Account");
			}
		}
		
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}

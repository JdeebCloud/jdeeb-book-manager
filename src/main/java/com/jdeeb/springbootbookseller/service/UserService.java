package com.jdeeb.springbootbookseller.service;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdeeb.springbootbookseller.model.User;
import com.jdeeb.springbootbookseller.model.Role;
import com.jdeeb.springbootbookseller.repository.IUserRepository;

@Service
@Slf4j
public class UserService implements IUserService{

	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User saveUser(User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		user.setCreateTime(LocalDateTime.now());
		
		return userRepository.save(user);
	}
	
	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	@Transactional	// @Transactional required for update/delete query
	public void makeAdmin(String username) {
		log.info("UserService:makeAdmin started {} ", username);
		userRepository.updateUserRole(username, Role.ADMIN);
	}
}

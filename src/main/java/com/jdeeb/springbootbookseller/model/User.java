package com.jdeeb.springbootbookseller.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="app_users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username", unique = true, nullable = false, length = 180)
	private String username;
	
	@Column(name = "password", nullable = false, length = 180)
	private String password;
	
	@Column(name = "name", nullable = false, length = 180)
	private String name;
	
	@Column(name = "create_time", nullable = false)
	private LocalDateTime createTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Transient
	private String token;
}

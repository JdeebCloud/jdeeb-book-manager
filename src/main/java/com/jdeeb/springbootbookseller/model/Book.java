package com.jdeeb.springbootbookseller.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="app_books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false, length = 100)
	private String title;
	
	@Column(name = "description", nullable = false, length = 1000)
	private String description;
	
	@Column(name = "author", nullable = false, length = 100)
	private String author;
	
	@Column(name = "price", nullable = false)
	private Double price; 
	
	@Column(name = "create_time", nullable = false)
	private LocalDateTime createTime;

}

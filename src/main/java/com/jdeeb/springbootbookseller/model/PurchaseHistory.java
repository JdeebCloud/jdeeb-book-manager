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
@Table(name = "app_purchase_history")
public class PurchaseHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	@Column(name = "book_id", nullable = false)
	private Long bookId;
	
	@Column(name = "price", nullable = false)
	private Double price;
	
	@Column(name = "purchase_time", nullable = false)
	private LocalDateTime purchaseTime;

}

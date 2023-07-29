package com.jdeeb.springbootbookseller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jdeeb.springbootbookseller.model.PurchaseHistory;
import com.jdeeb.springbootbookseller.repository.projection.IPurchaseItem;

public interface IPurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long>{

	@Query("select "
			+ " b.title as title, ph.price as price, ph.purchaseTime as purchaseTime "
			+ " from PurchaseHistory ph left join Book b on b.id = ph.bookId "
			+ " where ph.userId = :userId")
	List<IPurchaseItem> findAllPurchaseOfUser(@Param("userId") Long userId);
}

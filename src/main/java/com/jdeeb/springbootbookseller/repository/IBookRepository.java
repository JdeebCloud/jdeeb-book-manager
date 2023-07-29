package com.jdeeb.springbootbookseller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jdeeb.springbootbookseller.model.Book;

public interface IBookRepository extends JpaRepository<Book, Long>{

}

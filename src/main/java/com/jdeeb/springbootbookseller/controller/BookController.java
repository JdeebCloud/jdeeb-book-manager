package com.jdeeb.springbootbookseller.controller;

import com.jdeeb.springbootbookseller.model.Book;
import com.jdeeb.springbootbookseller.service.IBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/book")
@Slf4j
public class BookController {
    @Autowired
    private IBookService bookService;

    @PostMapping("save")   //  api/book/
    public ResponseEntity<?> saveBook(@RequestBody Book book)
    {
        log.info("BookController::saveBook called for {}", book);
        return new ResponseEntity<>(bookService.saveBook(book), HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{bookId}") //api/book/{bookId}
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId)
    {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping //api/book
    public ResponseEntity<?> getAllBooks()
    {
        return new ResponseEntity<>(bookService.findAllBooks(), HttpStatus.OK);
    }
}

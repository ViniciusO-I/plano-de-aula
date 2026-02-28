package com.challenge.library.controllers;

import com.challenge.library.entities.Book;
import com.challenge.library.entities.BookStatusEnum;
import com.challenge.library.services.BookService;

import java.util.List;

public class BookController {
    private BookService service;

    public BookController() {
        this.service = new BookService();
    }

    public Book save(Book book) {
        return service.save(book);
    }

    public Book makeReservation(Book book) {
        return service.makeReservation(book);
    }

    public boolean returnBook(Book book) {
        return service.returnBook(book);


    }

    public List<Book> listByStatus(BookStatusEnum status) {
        return service.listByStatus(status);

    }
}

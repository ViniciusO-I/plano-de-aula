package com.challenge.library.services;

import com.challenge.library.entities.Book;
import com.challenge.library.entities.BookStatusEnum;
import com.challenge.library.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private BookRepository repository;
    public BookService() {
        this.repository = new BookRepository();
    }
    public Book save(Book book) {
        return repository.save(book);
    }

    public Book makeReservation(Book book) {
        //TODO  escrever logica  se livro disponiveis
        boolean isvalid = false;
        List<Book> filteredBooks = this.listByStatus(BookStatusEnum.AVAILABLE);
        for (Book b : filteredBooks) {
            if(b.getId().equals(book.getId())){
               isvalid = true;
            }
        }
        if(isvalid){
            book.setStatus(BookStatusEnum.RESERVED);
            return repository.update(book.getId(), book);
        }
        return null;

    }

    public boolean returnBook(Book book) {
        return repository.delete(book.getId());

    }

    public List<Book> listByStatus(BookStatusEnum status) {
        return repository.findByStatus(status);

    }
}

package com.challenge.library.repositories;

import com.challenge.library.entities.Book;
import com.challenge.library.entities.BookStatusEnum;

import java.util.*;

public class BookRepository {

    // Fake database - storing books in memory
    private static final Map<Integer, Book> database = new HashMap<>();
    private static Integer nextId;

    // Initialize with sample data
    static {
        database.put(1, new Book(1, "Clean Code", "Robert C. Martin",
            com.challenge.library.entities.BookStatusEnum.AVAILABLE));
        database.put(2, new Book(2, "Design Patterns", "Gang of Four",
            com.challenge.library.entities.BookStatusEnum.AVAILABLE));
        database.put(3, new Book(3, "The Pragmatic Programmer", "David Thomas",
            com.challenge.library.entities.BookStatusEnum.RESERVED));
        nextId = 4;
    }

    // CREATE - Add a new book
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(nextId++);
        }
        database.put(book.getId(), book);
        return book;
    }

    // READ - Find book by ID
    public Book findById(Integer id) {
        return database.get(id);
    }

    // READ - Find all books
    public List<Book> findAll() {
        return new ArrayList<>(database.values());
    }

    // READ - Find books by status

    public List<Book> findByStatus(BookStatusEnum status) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : database.values()) {
            if (book.getStatus().equals(status)) {
                filteredBooks.add(book);
            }
        }

        return filteredBooks;
    }


    // UPDATE - Update an existing book
    public Book update(Integer id, Book book) {
        if (database.containsKey(id)) {
            book.setId(id);
            database.put(id, book);
            return book;
        }
        return null;
    }

    // DELETE - Remove a book by ID
    public boolean delete(Integer id) {
        return database.remove(id) != null;
    }

    // DELETE - Remove all books
    public void deleteAll() {
        database.clear();
    }

    // Check if a book exists
    public boolean exists(Integer id) {
        return database.containsKey(id);
    }

    // Count total books
    public long count() {
        return database.size();
    }

}

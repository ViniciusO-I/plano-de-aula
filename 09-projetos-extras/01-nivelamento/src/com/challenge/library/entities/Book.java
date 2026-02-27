package com.challenge.library.entities;

public class Book {

    //Attributes
    private Integer id;
    private String title;
    private String author;
    private BookStatusEnum status;

    //Constructors
    public Book() {}
    public Book(Integer id, String title, String author, BookStatusEnum status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
    }

    //Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BookStatusEnum status) {
        this.status = status;
    }

    //ToString
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status=" + status +
                '}';
    }
}

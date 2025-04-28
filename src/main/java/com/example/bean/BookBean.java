package com.example.bean;

import com.example.entity.Book;
import com.example.service.BookService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class BookBean {

    @EJB
    private BookService bookService;

    private Book book = new Book();

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    public String save() {
        bookService.createBook(book);
        return "list?faces-redirect=true";
    }

    public String edit(Book book) {
        this.book = book;
        return "edit";
    }

    public String update() {
        bookService.updateBook(book);
        return "list?faces-redirect=true";
    }

    public void delete(Book book) {
        bookService.deleteBook(book);
    }
}
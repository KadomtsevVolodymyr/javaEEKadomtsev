package com.example.bean;

import com.example.entity.Book;
import com.example.service.BookService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class BookBean implements Serializable {

    @Inject
    private BookService bookService;

    private List<Book> books;

    @PostConstruct
    public void init() {
        refreshBooks();
    }

    public List<Book> getBooks() {
        if (books == null) {
            refreshBooks();
        }
        return books;
    }

    public void refreshBooks() {
        books = bookService.getAllBooks();
    }

    public String delete(Book book) {
        bookService.deleteBook(book);
        refreshBooks();
        return "list?faces-redirect=true";
    }
}
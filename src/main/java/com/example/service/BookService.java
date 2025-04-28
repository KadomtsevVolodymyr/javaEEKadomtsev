package com.example.service;

import com.example.entity.Book;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Book> getAllBooks() {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    public Book createBook(Book book) {
        entityManager.persist(book);
        return book;
    }

    public Book updateBook(Book book) {
        return entityManager.merge(book);
    }

    public void deleteBook(Book book) {
        entityManager.remove(entityManager.merge(book));
    }
}
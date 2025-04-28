package com.example.service;

import com.example.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public void register(User user) throws Exception {
        // Check if user already exists
        TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", user.getEmail());
        if (!query.getResultList().isEmpty()) {
            throw new Exception("User with this email already exists");
        }

        // Hash the password
        user.setPassword(hashPassword(user.getPassword()));

        // Save the user
        em.persist(user);
    }

    public User authenticate(String email, String password) throws Exception {
        TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);

        try {
            User user = query.getSingleResult();
            if (user != null && user.getPassword().equals(hashPassword(password))) {
                return user;
            }
        } catch (Exception e) {
            // User not found or multiple users found
        }

        return null;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
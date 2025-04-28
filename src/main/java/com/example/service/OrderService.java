package com.example.service;

import com.example.entity.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> getAllOrders() {
        return entityManager.createQuery("SELECT o FROM Order o", Order.class)
                .getResultList();
    }

    public Order createOrder(Order order) {
        entityManager.persist(order);
        return order;
    }

    public Order updateOrder(Order order) {
        return entityManager.merge(order);
    }

    public void deleteOrder(Order order) {
        entityManager.remove(entityManager.merge(order));
    }
}
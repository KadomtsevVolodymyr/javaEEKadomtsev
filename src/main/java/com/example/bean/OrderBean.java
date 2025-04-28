package com.example.bean;

import com.example.entity.Order;
import com.example.service.OrderService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrderService orderService;

    private Order order = new Order();
    private Order selectedOrder;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    public String save() {
        orderService.createOrder(order);
        return "list?faces-redirect=true";
    }

    public String edit(Order order) {
        this.selectedOrder = order;
        return "edit?faces-redirect=true";
    }

    public String update() {
        orderService.updateOrder(selectedOrder);
        return "list?faces-redirect=true";
    }

    public void delete(Order order) {
        orderService.deleteOrder(order);
    }
}
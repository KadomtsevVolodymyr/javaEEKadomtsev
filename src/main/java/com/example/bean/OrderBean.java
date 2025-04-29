package com.example.bean;

import com.example.entity.Book;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.service.OrderService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OrderService orderService;

    @Inject
    private BookBean bookBean;

    private Order selectedOrder;
    private List<OrderItem> selectedBooks;
    private Map<Long, Integer> quantities;

    @PostConstruct
    public void init() {
        selectedOrder = new Order();
        selectedBooks = new ArrayList<>();
        quantities = new HashMap<>();
        initializeQuantities();
    }

    private void initializeQuantities() {
        if (bookBean != null) {
            List<Book> books = bookBean.getBooks();
            if (books != null) {
                for (Book book : books) {
                    if (!quantities.containsKey(book.getId())) {
                        quantities.put(book.getId(), 1);
                    }
                }
            }
        }
    }

    public String edit(Order order) {
        this.selectedOrder = order;
        this.selectedBooks = new ArrayList<>(order.getItems());
        this.quantities = new HashMap<>();
        // Initialize quantities for all available books
        List<Book> books = bookBean.getBooks();
        if (books != null) {
            for (Book book : books) {
                quantities.put(book.getId(), 1);
            }
        }
        // Update quantities for books in the order
        for (OrderItem item : selectedBooks) {
            quantities.put(item.getBook().getId(), item.getQuantity());
        }
        return "edit?faces-redirect=true";
    }

    public void delete(Order order) {
        orderService.deleteOrder(order);
    }

    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    public List<Book> getBooks() {
        return bookBean.getBooks();
    }

    public void addBookToOrder(Book book) {
        if (book == null) {
            return;
        }

        if (quantities == null) {
            quantities = new HashMap<>();
        }
        if (selectedBooks == null) {
            selectedBooks = new ArrayList<>();
        }

        Object quantityObj = quantities.get(book.getId());
        if (quantityObj == null) {
            return;
        }

        // Convert the quantity to Integer, handling both String and Integer inputs
        Integer quantity;
        if (quantityObj instanceof String) {
            try {
                quantity = Integer.parseInt((String) quantityObj);
            } catch (NumberFormatException e) {
                return;
            }
        } else {
            quantity = (Integer) quantityObj;
        }

        if (quantity <= 0) {
            return;
        }

        if (quantity > book.getStock()) {
            return;
        }

        // Check if book is already in the order
        boolean bookFound = false;
        for (OrderItem item : selectedBooks) {
            if (item.getBook().getId().equals(book.getId())) {
                // Update quantity if book already exists
                item.setQuantity(quantity);
                bookFound = true;
                break;
            }
        }

        // Add new book to order if not found
        if (!bookFound) {
            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(quantity);
            item.setOrder(selectedOrder);
            selectedBooks.add(item);
        }
    }

    public void removeBookFromOrder(OrderItem item) {
        selectedBooks.remove(item);
        // Reset quantity to default
        quantities.put(item.getBook().getId(), 1);
    }

    public String save() {
        selectedOrder.setOrderDate(java.time.LocalDateTime.now());
        selectedOrder.setItems(new ArrayList<>(selectedBooks)); // Create a new list to avoid reference issues

        // Calculate total amount
        BigDecimal total = selectedBooks.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        selectedOrder.setTotalAmount(total);

        if (selectedOrder.getId() == null) {
            // This is a new order
            orderService.createOrder(selectedOrder);
        } else {
            // This is an existing order being edited
            orderService.updateOrder(selectedOrder);
        }

        // Reset the state
        init();

        return "list?faces-redirect=true";
    }

    public String create() {
        init();
        return "create?faces-redirect=true";
    }

    // Getters and Setters
    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public List<OrderItem> getSelectedBooks() {
        return selectedBooks;
    }

    public void setSelectedBooks(List<OrderItem> selectedBooks) {
        this.selectedBooks = selectedBooks;
    }

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Long, Integer> quantities) {
        this.quantities = quantities;
    }
}
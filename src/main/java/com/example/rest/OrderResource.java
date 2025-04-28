package com.example.rest;

import com.example.entity.Order;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Stateless
public class OrderResource {
    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> getAllOrders() {
        return em.createNamedQuery("Order.findAll", Order.class).getResultList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Order getOrder(@PathParam("id") Long id) {
        return em.find(Order.class, id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(Order order) {
        em.persist(order);
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") Long id, Order order) {
        Order existingOrder = em.find(Order.class, id);
        if (existingOrder == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        order.setId(id);
        em.merge(order);
        return Response.ok(order).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long id) {
        Order order = em.find(Order.class, id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        em.remove(order);
        return Response.noContent().build();
    }
}
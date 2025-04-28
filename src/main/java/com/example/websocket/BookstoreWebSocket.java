package com.example.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/bookstore")
public class BookstoreWebSocket {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final Logger logger = Logger.getLogger(BookstoreWebSocket.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.log(Level.INFO, "New WebSocket connection established");
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        logger.log(Level.INFO, "WebSocket connection closed");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.log(Level.INFO, "Received message: {0}", message);
        broadcast(message);
    }

    private void broadcast(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error broadcasting message", e);
            }
        }
    }
}
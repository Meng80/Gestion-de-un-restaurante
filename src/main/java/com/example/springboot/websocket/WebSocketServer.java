package com.example.springboot.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {
    private static Map<String, Session> sessionMap = new HashMap();

    /**
     * Called when a new connection is established
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("Client " + sid + " connected");
        sessionMap.put(sid, session);
    }

    /**
     * Called when a message is received from a client
     *
     * @param message message sent by client
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("Message from client " + sid + ": " + message);
    }

    /**
     * Called when a connection is closed
     *
     * @param sid session id
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("Connection closed: " + sid);
        sessionMap.remove(sid);
    }

    /**
     * Broadcast message to all connected clients
     *
     * @param message message to send
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


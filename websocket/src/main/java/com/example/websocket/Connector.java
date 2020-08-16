package com.example.websocket;

import javax.websocket.Session;

public class Connector {
    private Session session;
    private String username;

    public Connector(Session session, String username) {
        this.session = session;
        this.username = username;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

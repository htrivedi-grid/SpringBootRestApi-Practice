package com.example.restapi.models;

public class UserEntity {
    private String name;
    private String email;
    private String password;
    private String token;
    private String sessionId;

    public UserEntity(String name, String email, String password, String token, String sessionId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}

package com.vladm.demoservlet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private String id;
    private String name;
    private String email;

    private List<Message> messages = new ArrayList<>();

    public User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(){

    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(messages, user.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, messages);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", messages=" + messages +
                '}';
    }
}

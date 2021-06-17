package com.example.lectury;

public class User {
    public String name, email, pass;

    public User() {

    }

    public User(String email, String name, String pass) {
        this.email = email;
        this.name = name;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public void setEmail() {
        this.email = email;
    }

    public void setName() {
        this.name = name;
    }

    public void setPass() {
        this.pass = pass;
    }
}

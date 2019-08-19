package com.mitrais.cdc.bloggatewayapplicationv1.entity;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String password;
    private boolean enabled;
    private String role;
    private String email;
    private String firstname;
    private String lastname;

    public User(){

    }

    public User(int id, String username, String password, boolean enabled, String role) {
        this.id = id;
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRole(role);
    }

    public User(int id, String username, String password, boolean enabled, String role, String email) {
        this.id = id;
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRole(role);
        this.setEmail(email);
    }

    public User(int id, String username, String password, boolean enabled, String role, String email, String firstname, String lastname) {
        this.id = id;
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRole(role);
        this.setEmail(email);
        this.setFirstname(firstname);
        this.setLastname(lastname);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}

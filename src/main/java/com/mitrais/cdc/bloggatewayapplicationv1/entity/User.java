package com.mitrais.cdc.bloggatewayapplicationv1.entity;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String password;
    private boolean enabled;
    private String email;
    private String firstname;
    private String lastname;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public User(){

    }

    public User(int id, String username, String password, boolean enabled, List<String> role) {
        this.id = id;
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRoles(role);
    }

    public User(String username, String password, boolean enabled, List<String> role) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRoles(role);
    }

    public User(int id, String username, String password, boolean enabled, List<String> role, String email) {
        this.id = id;
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRoles(role);
        this.setEmail(email);
    }

    public User(String username, String password, boolean enabled, List<String> role, String email) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRoles(role);
        this.setEmail(email);
    }

    public User(String username, String password, boolean enabled, List<String> role, String email, String firstname, String lastname) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRoles(role);
        this.setEmail(email);
        this.setFirstname(firstname);
        this.setLastname(lastname);
    }

    public User(int id, String username, String password, boolean enabled, List<String> role, String email, String firstname, String lastname) {
        this.id = id;
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(enabled);
        this.setRoles(role);
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

package com.example.app.model;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * User entity model.
 * Uses: commons-lang3, hibernate-validator
 */
public class User {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Size(min = 8)
    private String password;

    public User() {}

    public User(String username, String email) {
        this.username = StringUtils.defaultString(username);
        this.email = StringUtils.defaultString(email);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

package com.g9team10.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long Id;

   @Column(nullable = false)
   private String username;

   @Column(nullable = false, unique = true)
   private String email;

   @Column(nullable = false)
   private String password;

    public User() {
    }

    public User(Long id, String username, String email, String password) {
        Id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}

package com.patika.model;

import java.sql.Date;

public class User {

    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private Date birthDate;
    private boolean isCorporate;
    private String role;

    // === BOŞ CONSTRUCTOR ===
    public User(int id, String email, String passwordHash, Date birthDate, boolean isCorporate, String role){

    }//int id, String username, String email, String passwordHash, Date birthDate, boolean isCorporate, String role

    // === PARAMETRELİ CONSTRUCTOR ===
    public User(int id, String username, String email, String passwordHash, Date birthDate, boolean isCorporate, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
        this.isCorporate = isCorporate;
        this.role = role;
    }




    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", isCorporate=" + isCorporate +
                ", role='" + role + '\'' +
                '}';
    }




}

package com.iesfranciscodelosrios.model;

import java.io.Serial;
import java.io.Serializable;

public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected Long id;
    protected String email;
    protected String password;
    protected boolean isManager;

    public User(){
        this.id = -1L;
        this.email = "no_mail";
        this.password = "no_password";
        this.isManager = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}

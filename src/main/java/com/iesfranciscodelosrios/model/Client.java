package com.iesfranciscodelosrios.model;

import java.io.Serial;
import java.io.Serializable;

public class Client extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Double balance;

    public Client(){
        super();
        this.balance = 0.0;
    }

    public Client(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}

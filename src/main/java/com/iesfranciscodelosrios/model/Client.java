package com.iesfranciscodelosrios.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Client extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Double balance;
    private List<Book> pBooks;

    public Client(){
        super();
        this.balance = 0.0;
    }

    public Client(Double balance, List<Book> pBooks) {
        this.balance = balance;
        this.pBooks = pBooks;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Book> getpBooks() {
        return pBooks;
    }

    public void setpBooks(List<Book> pBooks) {
        this.pBooks = pBooks;
    }
}

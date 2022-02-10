package com.iesfranciscodelosrios.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Client extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long balance;
    private List<Book> pBooks;

    public Client(){
        super();
    }

    public Client(Long balance, List<Book> pBooks) {
        this.balance = balance;
        this.pBooks = pBooks;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public List<Book> getpBooks() {
        return pBooks;
    }

    public void setpBooks(List<Book> pBooks) {
        this.pBooks = pBooks;
    }
}

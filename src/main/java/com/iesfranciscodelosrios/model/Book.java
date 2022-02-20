package com.iesfranciscodelosrios.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String author;
    //Imagen guardada en Base64
    private String frontPage;

    private Double price;

    private LocalDateTime releasedDate;

    private LocalDateTime pDate;

    private boolean stock;

    private Client buyer;

    public Book() {
        this.id = -1L;
    }

    public Book(Long id, String title,String author, String frontPage, Double price, LocalDateTime releasedDate, LocalDateTime pDate, boolean stock, Client buyer) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.frontPage = frontPage;
        this.price = price;
        this.releasedDate = releasedDate;
        this.pDate = pDate;
        this.stock = stock;
        this.buyer = buyer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFrontPage() {
        return frontPage;
    }

    public void setFrontPage(String frontPage) {
        this.frontPage = frontPage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(LocalDateTime releasedDate) {
        this.releasedDate = releasedDate;
    }

    public LocalDateTime getpDate() {
        return pDate;
    }

    public void setpDate(LocalDateTime pDate) {
        this.pDate = pDate;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public Client getBuyer() {
        return buyer;
    }

    public void setBuyer(Client buyer) {
        this.buyer = buyer;
    }
}

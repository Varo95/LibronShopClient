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

    private boolean stock;


    public Book() {
        this.id = -1L;
    }

    public Book(Long id, String title,String author, String frontPage, Double price, LocalDateTime releasedDate, boolean stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.frontPage = frontPage;
        this.price = price;
        this.releasedDate = releasedDate;
        this.stock = stock;
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

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

}

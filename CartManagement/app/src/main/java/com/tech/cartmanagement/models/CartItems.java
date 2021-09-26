package com.tech.cartmanagement.models;

public class CartItems {

    String id;
    String name;
    String price;
    String details;
    String qty;
    String image;

    public CartItems(String id, String name, String price, String details, String qty, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.qty = qty;
        this.image = image;
    }

    public CartItems() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

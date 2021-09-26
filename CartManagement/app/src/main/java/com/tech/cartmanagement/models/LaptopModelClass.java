package com.tech.cartmanagement.models;

public class LaptopModelClass {

    String id;
    String name;
    String price;
    String details;
    String image;

    public LaptopModelClass(String id, String name, String price, String details, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.image = image;
    }

    public LaptopModelClass() {
    }
//get  & set

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

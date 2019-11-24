package com.example.kaldijava.cafe.http;

import com.google.gson.annotations.SerializedName;

public  class Menu {

    @SerializedName("price")
    private String price;
    @SerializedName("coffee")
    private String coffee;
    @SerializedName("address")
    private String address;
    @SerializedName("cafe_name")
    private String cafe_name;
    @SerializedName("id")
    private String id;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoffee() {
        return coffee;
    }

    public void setCoffee(String coffee) {
        this.coffee = coffee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCafe_name() {
        return cafe_name;
    }

    public void setCafe_name(String cafe_name) {
        this.cafe_name = cafe_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

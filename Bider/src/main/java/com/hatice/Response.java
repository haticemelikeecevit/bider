package com.hatice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("bids")
    List<Bid> bids;

    @SerializedName("products")
    List<Product> products;

    @SerializedName("login")
    int login;
}

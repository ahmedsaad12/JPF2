package com.app.jpf.models;

import java.io.Serializable;

public class SliderModel implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String image;
    private String action_link;
    private String action_link_title;
    private String type;
    private String product_id;
    private ProductModel product;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getAction_link() {
        return action_link;
    }

    public String getAction_link_title() {
        return action_link_title;
    }

    public String getType() {
        return type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public ProductModel getProduct() {
        return product;
    }
}

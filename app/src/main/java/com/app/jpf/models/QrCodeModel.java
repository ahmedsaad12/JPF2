package com.app.jpf.models;

import java.io.Serializable;

public class QrCodeModel extends StatusResponse implements Serializable {
    public Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
    private int id;
    private String qr_code;
    private String qr_code_image;
    private int qrcode_id;
    private int product_id;
    private String is_used;
    private String created_at;
    private String updated_at;
    private ProductModel product;

    public int getId() {
        return id;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getQr_code_image() {
        return qr_code_image;
    }

    public int getQrcode_id() {
        return qrcode_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getIs_used() {
        return is_used;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public ProductModel getProduct() {
        return product;
    }
    }
}

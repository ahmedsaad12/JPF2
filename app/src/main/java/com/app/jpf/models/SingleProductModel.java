package com.app.jpf.models;

import java.io.Serializable;

public class SingleProductModel extends StatusResponse implements Serializable {
    private ProductModel data;

    public ProductModel getData() {
        return data;
    }
}

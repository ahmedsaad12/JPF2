package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class ShopGalleryDataModel extends StatusResponse implements Serializable {
    private List<ShopGalleryModel> data;

    public List<ShopGalleryModel> getData() {
        return data;
    }
}

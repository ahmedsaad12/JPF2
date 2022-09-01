package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class MyPointsDataModel extends StatusResponse implements Serializable {
    private List<MyPointsModel> data;
    public List<MyPointsModel> getData() {
        return data;
    }
}

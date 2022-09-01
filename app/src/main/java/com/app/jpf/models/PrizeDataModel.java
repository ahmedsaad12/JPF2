package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class PrizeDataModel extends StatusResponse implements Serializable {
    private List<MyPointsModel.Prize> data;

    public List<MyPointsModel.Prize> getData() {
        return data;
    }
}

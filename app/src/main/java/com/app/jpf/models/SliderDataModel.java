package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel extends StatusResponse implements Serializable {
    private List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }
}

package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class AreaModel extends StatusResponse implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable{
        public int id;
        public int governorate_id;
        public String city_name_ar;
        public String city_name_en;
        public String created_at;
        public String updated_at;
       private GovernmentModel.Data governorate;

        public Data(String city_name_ar, String city_name_en) {
            this.city_name_ar = city_name_ar;
            this.city_name_en = city_name_en;
        }

        public int getId() {
            return id;
        }

        public int getGovernorate_id() {
            return governorate_id;
        }

        public String getCity_name_ar() {
            return city_name_ar;
        }

        public String getCity_name_en() {
            return city_name_en;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public GovernmentModel.Data getGovernorate() {
            return governorate;
        }
    }
}

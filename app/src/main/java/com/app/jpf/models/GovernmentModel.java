package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class GovernmentModel extends StatusResponse implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable{
        private int id;
        private String governorate_name_ar;
        private String governorate_name_en;
        private String created_at;
        private String updated_at;

        public Data(String governorate_name_ar, String governorate_name_en) {
            this.governorate_name_ar = governorate_name_ar;
            this.governorate_name_en = governorate_name_en;
        }

        public int getId() {
            return id;
        }

        public String getGovernorate_name_ar() {
            return governorate_name_ar;
        }

        public String getGovernorate_name_en() {
            return governorate_name_en;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}

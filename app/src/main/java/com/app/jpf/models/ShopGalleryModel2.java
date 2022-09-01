package com.app.jpf.models;

import java.io.Serializable;

public class ShopGalleryModel2 implements Serializable {
    public Data data;
    public String message;
    public int status;
    public class Data{
        public int id;
        public String title;
        public String desc;
        public String image;
        public String phone_number1;
        public Object phone_number2;
        public int governorate_id;
        public int city_id;
        public String address;
        public String latitude;
        public String longitude;

        public Governorate governorate;
        public City city;

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

        public String getPhone_number1() {
            return phone_number1;
        }

        public Object getPhone_number2() {
            return phone_number2;
        }

        public int getGovernorate_id() {
            return governorate_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public String getAddress() {
            return address;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public Governorate getGovernorate() {
            return governorate;
        }

        public City getCity() {
            return city;
        }

        public class Governorate{
            public int id;
            public String governorate_name_ar;
            public String governorate_name_en;
            public Object created_at;
            public Object updated_at;
        }

        public class City{
            public int id;
            public int governorate_id;
            public String city_name_ar;
            public String city_name_en;
            public Object created_at;
            public Object updated_at;
        }
    }

}

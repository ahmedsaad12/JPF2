package com.app.jpf.models;

import java.io.Serializable;

public class MyPointsModel implements Serializable {

    private int id;
    private int user_id;
    private int prize_id;
    private String product_id;
    private String type;
    private int points_count;
    private String date;
    private String created_at;
    private String updated_at;
    private ProductModel product;
    private Prize prize;
    private UserModel.User user;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getPrize_id() {
        return prize_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getType() {
        return type;
    }

    public int getPoints_count() {
        return points_count;
    }

    public String getDate() {
        return date;
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

    public Prize getPrize() {
        return prize;
    }

    public UserModel.User getUser() {
        return user;
    }

    public static class Prize implements Serializable{
        private int id;
        private String title;
        private String desc;
        private String image;
        private int points_count;
        private String is_shown;
        private String created_at;
        private String updated_at;

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

        public int getPoints_count() {
            return points_count;
        }

        public String getIs_shown() {
            return is_shown;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

}

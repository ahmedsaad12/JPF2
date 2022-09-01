package com.app.jpf.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable{
    private int id;
    private String title;
    private String desc;
    private String main_image;
    private String video;
    private int category_id;
    private int points_count;
    private String have_points_offer;
    private String points_offer_type;
    private double points_offer_value;
    private double price;
    private double old_price;
    private String have_offer;
    private double offer_value_rate;
    private double offer_value_value;
    private String offer_started_at;
    private String offer_finished_at;
    private double rating_value;
    private int amount;
    private String is_shown;
    private String created_at;
    private String updated_at;
    private int user_qrcodes_count;
    private Category category;
    private List<Image> images;
    private List<Color> colors;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getVideo() {
        return video;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getPoints_count() {
        return points_count;
    }

    public String getHave_points_offer() {
        return have_points_offer;
    }

    public String getPoints_offer_type() {
        return points_offer_type;
    }

    public double getPoints_offer_value() {
        return points_offer_value;
    }

    public double getPrice() {
        return price;
    }

    public double getOld_price() {
        return old_price;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public double getOffer_value_rate() {
        return offer_value_rate;
    }

    public double getOffer_value_value() {
        return offer_value_value;
    }

    public String getOffer_started_at() {
        return offer_started_at;
    }

    public String getOffer_finished_at() {
        return offer_finished_at;
    }

    public double getRating_value() {
        return rating_value;
    }

    public int getAmount() {
        return amount;
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

    public int getUser_qrcodes_count() {
        return user_qrcodes_count;
    }

    public Category getCategory() {
        return category;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Color> getColors() {
        return colors;
    }

    public static class Category implements Serializable {
        private int id;
        private String title;
        private String desc;
        private String image;
        private String parent_id;
        private String level;
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

        public String getParent_id() {
            return parent_id;
        }

        public String getLevel() {
            return level;
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

    public static class Image implements Serializable{
       private int id;
       private String image;
       private int product_id;
       private String created_at;
       private String updated_at;

        public int getId() {
            return id;
        }

        public String getImage() {
            return image;
        }

        public int getProduct_id() {
            return product_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

    public static class Color implements Serializable{
        private int id;
        private String color_code;
        private int product_id;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getColor_code() {
            return color_code;
        }

        public int getProduct_id() {
            return product_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }


}

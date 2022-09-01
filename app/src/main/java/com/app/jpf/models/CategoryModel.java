package com.app.jpf.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {
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

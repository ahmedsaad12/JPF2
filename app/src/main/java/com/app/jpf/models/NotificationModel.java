package com.app.jpf.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int id;
    private String title;
    private String desc;
    private int to_user_id;
    private String action_type;
    private String notification_date;
    private String is_read;
    private String created_at;
    private String updated_at;
    private UserModel.User user;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public String getAction_type() {
        return action_type;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.User getUser() {
        return user;
    }
}

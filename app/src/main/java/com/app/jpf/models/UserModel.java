package com.app.jpf.models;

import java.io.Serializable;

public class UserModel extends StatusResponse implements Serializable {
    private User data;

    public User getData() {
        return data;
    }

    public class User implements Serializable {
        private int id;
        private String user_type;
        private String phone_code;
        private String phone;
        private String first_name;
        private String second_name;
        private String last_name;
        private String national_ID;
        private String email;
        private String logo;
        private String banner;
        private String address;
        private double latitude;
        private double longitude;
        private int governorate_id;
        private int city_id;
        private int exchange_points_balance;
        private int current_points_balance;
        private int total_points_balance;
        private String is_confirmed;
        private String is_block;
        private String is_login;
        private int logout_time;
        private String software_type;
        private String email_verified_at;
        private String created_at;
        private String updated_at;
        private double user_scan_count;
        private double confirmed_prizes_count;
        private String token;
        private String full_name;
        private Governorate governorate;
        private City city;
        private String firebase_token;

        public int getId() {
            return id;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getSecond_name() {
            return second_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getNational_ID() {
            return national_ID;
        }

        public String getEmail() {
            return email;
        }

        public String getLogo() {
            return logo;
        }

        public String getBanner() {
            return banner;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getGovernorate_id() {
            return governorate_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public int getExchange_points_balance() {
            return exchange_points_balance;
        }

        public int getCurrent_points_balance() {
            return current_points_balance;
        }

        public int getTotal_points_balance() {
            return total_points_balance;
        }

        public String getIs_confirmed() {
            return is_confirmed;
        }

        public String getIs_block() {
            return is_block;
        }

        public String getIs_login() {
            return is_login;
        }

        public int getLogout_time() {
            return logout_time;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public double getUser_scan_count() {
            return user_scan_count;
        }

        public double getConfirmed_prizes_count() {
            return confirmed_prizes_count;
        }

        public String getToken() {
            return token;
        }

        public String getFull_name() {
            return full_name;
        }

        public Governorate getGovernorate() {
            return governorate;
        }

        public City getCity() {
            return city;
        }

        public void setFirebase_token(String token) {
            this.firebase_token = token;
        }

        public class Governorate implements Serializable {
            private int id;
            private String governorate_name_ar;
            private String governorate_name_en;
            private String created_at;
            private String updated_at;

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

        public class City implements Serializable {
            private int id;
            private int governorate_id;
            private String city_name_ar;
            private String city_name_en;
            private String created_at;
            private String updated_at;

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
        }
    }
}

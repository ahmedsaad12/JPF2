package com.app.jpf.models;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private Settings data;

    public Settings getData() {
        return data;
    }

    public static class Settings implements Serializable
    {
        private int id;
        private String terms_condition_link;
        private String about_us_link;
        private String privacy_policy_link;
        private String header_logo;
        private String footer_logo;
        private String login_banner;
        private String image_slider;
        private String ar_title;
        private String en_title;
        private String ar_desc;
        private String en_desc;
        private String ar_footer_desc;
        private String en_footer_desc;
        private String company_profile_pdf;
        private String address1;
        private String address2;
        private int latitude;
        private int longitude;
        private String phone1;
        private String phone2;
        private String fax;
        private String android_app;
        private String ios_app;
        private String email1;
        private String email2;
        private String link;
        private String sms_user_name;
        private String sms_user_pass;
        private String sms_sender;
        private String publisher;
        private String default_language;
        private String default_theme;
        private String offer_muted;
        private int offer_notification;
        private String facebook;
        private String twitter;
        private String instagram;
        private String linkedin;
        private String telegram;
        private String youtube;
        private String google_plus;
        private String snapchat_ghost;
        private String whatsapp;
        private String ar_about_app;
        private String en_about_app;
        private String ar_terms_condition;
        private String en_terms_condition;
        private String ar_privacy_policy;
        private String en_privacy_policy;
        private int site_commission;
        private int debt_limit;
        private int user_start_points;
        private String created_at;
        private String updated_at;
        private String title;
        private String desc;
        private String about_app;
        private String terms_condition;

        public int getId() {
            return id;
        }

        public String getTerms_condition_link() {
            return terms_condition_link;
        }

        public String getAbout_us_link() {
            return about_us_link;
        }

        public String getPrivacy_policy_link() {
            return privacy_policy_link;
        }

        public String getHeader_logo() {
            return header_logo;
        }

        public String getFooter_logo() {
            return footer_logo;
        }

        public String getLogin_banner() {
            return login_banner;
        }

        public String getImage_slider() {
            return image_slider;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_desc() {
            return ar_desc;
        }

        public String getEn_desc() {
            return en_desc;
        }

        public String getAr_footer_desc() {
            return ar_footer_desc;
        }

        public String getEn_footer_desc() {
            return en_footer_desc;
        }

        public String getCompany_profile_pdf() {
            return company_profile_pdf;
        }

        public String getAddress1() {
            return address1;
        }

        public String getAddress2() {
            return address2;
        }

        public int getLatitude() {
            return latitude;
        }

        public int getLongitude() {
            return longitude;
        }

        public String getPhone1() {
            return phone1;
        }

        public String getPhone2() {
            return phone2;
        }

        public String getFax() {
            return fax;
        }

        public String getAndroid_app() {
            return android_app;
        }

        public String getIos_app() {
            return ios_app;
        }

        public String getEmail1() {
            return email1;
        }

        public String getEmail2() {
            return email2;
        }

        public String getLink() {
            return link;
        }

        public String getSms_user_name() {
            return sms_user_name;
        }

        public String getSms_user_pass() {
            return sms_user_pass;
        }

        public String getSms_sender() {
            return sms_sender;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDefault_language() {
            return default_language;
        }

        public String getDefault_theme() {
            return default_theme;
        }

        public String getOffer_muted() {
            return offer_muted;
        }

        public int getOffer_notification() {
            return offer_notification;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getLinkedin() {
            return linkedin;
        }

        public String getTelegram() {
            return telegram;
        }

        public String getYoutube() {
            return youtube;
        }

        public String getGoogle_plus() {
            return google_plus;
        }

        public String getSnapchat_ghost() {
            return snapchat_ghost;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public String getAr_about_app() {
            return ar_about_app;
        }

        public String getEn_about_app() {
            return en_about_app;
        }

        public String getAr_terms_condition() {
            return ar_terms_condition;
        }

        public String getEn_terms_condition() {
            return en_terms_condition;
        }

        public String getAr_privacy_policy() {
            return ar_privacy_policy;
        }

        public String getEn_privacy_policy() {
            return en_privacy_policy;
        }

        public int getSite_commission() {
            return site_commission;
        }

        public int getDebt_limit() {
            return debt_limit;
        }

        public int getUser_start_points() {
            return user_start_points;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getAbout_app() {
            return about_app;
        }

        public String getTerms_condition() {
            return terms_condition;
        }
    }
}

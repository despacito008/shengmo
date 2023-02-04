package com.aiwujie.shengmo.bean;

import java.util.ArrayList;
import java.util.List;

public class HighUserModel {
    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int retcode;
    public String msg;
    public DataBean data;


    public class DataBean {


        private String top_id;
        private String uid;
        private String top_status;
        private String serial_id;
        private String top_photo;
        private String top_photo_auth;
        public String top_desc;
        private String is_gaussian;
        private String top_red_desc;
        private String is_top_mine;
        private String is_match_mine;

        private String top_cc_status;
        private String top_jk_status;
        private String top_xl_status;
        private String top_jn_status;
        private String top_qt_status;
        private String top_overtime;
        private String is_match;
        private String top_chat_status;
        private String top_info_status;

        private String top_desc_hidden;
        private String top_red_desc_hidden;

        private String show_status;

        private String top_match_status;
        private String top_user_status;
        private String match_service_string;
        private String is_over_mine;
        private String chat_id;

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        private int is_follow;
        private ArrayList<String> top_photo_arr;

        private UserInfoBean.DataBean user_info;


        public String getShow_status() {
            return show_status;
        }

        public void setShow_status(String show_status) {
            this.show_status = show_status;
        }
        public String getTop_desc_hidden() {
            return top_desc_hidden;
        }

        public void setTop_desc_hidden(String top_desc_hidden) {
            this.top_desc_hidden = top_desc_hidden;
        }

        public String getTop_red_desc_hidden() {
            return top_red_desc_hidden;
        }

        public void setTop_red_desc_hidden(String top_red_desc_hidden) {
            this.top_red_desc_hidden = top_red_desc_hidden;
        }


        public String getTop_match_status() {
            return top_match_status;
        }

        public void setTop_match_status(String top_match_status) {
            this.top_match_status = top_match_status;
        }


        public String getTop_user_status() {
            return top_user_status;
        }

        public void setTop_user_status(String top_user_status) {
            this.top_user_status = top_user_status;
        }


        public String getTop_info_status() {
            return top_info_status;
        }

        public void setTop_info_status(String top_info_status) {
            this.top_info_status = top_info_status;
        }


        public String getTop_chat_status() {
            return top_chat_status;
        }

        public void setTop_chat_status(String top_chat_status) {
            this.top_chat_status = top_chat_status;
        }


        public String getMatch_service_string() {
            return match_service_string;
        }

        public void setMatch_service_string(String match_service_string) {
            this.match_service_string = match_service_string;
        }


        public String getIs_over_mine() {
            return is_over_mine;
        }

        public void setIs_over_mine(String is_over_mine) {
            this.is_over_mine = is_over_mine;
        }


        public String getIs_match() {
            return is_match;
        }

        public void setIs_match(String is_match) {
            this.is_match = is_match;
        }


        public String getChat_id() {
            return chat_id;
        }

        public void setChat_id(String chat_id) {
            this.chat_id = chat_id;
        }


        public String getTop_id() {
            return top_id;
        }

        public void setTop_id(String top_id) {
            this.top_id = top_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTop_status() {
            return top_status;
        }

        public void setTop_status(String top_status) {
            this.top_status = top_status;
        }

        public String getSerial_id() {
            return serial_id;
        }

        public void setSerial_id(String serial_id) {
            this.serial_id = serial_id;
        }

        public String getTop_photo() {
            return top_photo;
        }

        public void setTop_photo(String top_photo) {
            this.top_photo = top_photo;
        }

        public String getTop_photo_auth() {
            return top_photo_auth;
        }

        public void setTop_photo_auth(String top_photo_auth) {
            this.top_photo_auth = top_photo_auth;
        }

        public String getTop_desc() {
            return top_desc;
        }

        public void setTop_desc(String top_desc) {
            this.top_desc = top_desc;
        }

        public String getIs_gaussian() {
            return is_gaussian;
        }

        public void setIs_gaussian(String is_gaussian) {
            this.is_gaussian = is_gaussian;
        }

        public String getTop_red_desc() {
            return top_red_desc;
        }

        public void setTop_red_desc(String top_red_desc) {
            this.top_red_desc = top_red_desc;
        }

        public String getTop_cc_status() {
            return top_cc_status;
        }

        public void setTop_cc_status(String top_cc_status) {
            this.top_cc_status = top_cc_status;
        }

        public String getTop_jk_status() {
            return top_jk_status;
        }

        public void setTop_jk_status(String top_jk_status) {
            this.top_jk_status = top_jk_status;
        }

        public String getTop_xl_status() {
            return top_xl_status;
        }

        public void setTop_xl_status(String top_xl_status) {
            this.top_xl_status = top_xl_status;
        }

        public String getTop_jn_status() {
            return top_jn_status;
        }

        public void setTop_jn_status(String top_jn_status) {
            this.top_jn_status = top_jn_status;
        }

        public String getTop_qt_status() {
            return top_qt_status;
        }

        public void setTop_qt_status(String top_qt_status) {
            this.top_qt_status = top_qt_status;
        }

        public String getTop_overtime() {
            return top_overtime;
        }

        public void setTop_overtime(String top_overtime) {
            this.top_overtime = top_overtime;
        }

        public ArrayList<String> getTop_photo_arr() {
            return top_photo_arr;
        }

        public void setTop_photo_arr(ArrayList<String> top_photo_arr) {
            this.top_photo_arr = top_photo_arr;
        }

        public UserInfoBean.DataBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean.DataBean user_info) {
            this.user_info = user_info;
        }

        public String getIs_top_mine() {
            return is_top_mine;
        }

        public void setIs_top_mine(String is_top_mine) {
            this.is_top_mine = is_top_mine;
        }

        public String getIs_match_mine() {
            return is_match_mine;
        }

        public void setIs_match_mine(String is_match_mine) {
            this.is_match_mine = is_match_mine;
        }

    }
}

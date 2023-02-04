package com.aiwujie.shengmo.bean;

import java.util.List;

public class HighUserDetailModel {

    public int retcode;
    public String  code;
    public DataBean  data;


    public  class DataBean {
        public String getTop_id() {
            return top_id;
        }

        public void setTop_id(String top_id) {
            this.top_id = top_id;
        }

        public String getSerial_id() {
            return serial_id;
        }

        public void setSerial_id(String serial_id) {
            this.serial_id = serial_id;
        }

        public String getTop_desc() {
            return top_desc;
        }

        public void setTop_desc(String top_desc) {
            this.top_desc = top_desc;
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




        public String getTop_status() {
            return top_status;
        }

        public void setTop_status(String top_status) {
            this.top_status = top_status;
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

        public String getIs_gaussian() {
            return is_gaussian;
        }

        public void setIs_gaussian(String is_gaussian) {
            this.is_gaussian = is_gaussian;
        }

        public String getTop_overtime() {
            return top_overtime;
        }

        public void setTop_overtime(String top_overtime) {
            this.top_overtime = top_overtime;
        }

        public List<String> getTop_photo_arr() {
            return top_photo_arr;
        }

        public void setTop_photo_arr(List<String> top_photo_arr) {
            this.top_photo_arr = top_photo_arr;
        }

        private String top_id;
        private String top_status;
        private String serial_id;
        private String top_photo;
        private String top_photo_auth;
        private String is_gaussian;
        private String top_overtime;
        private String top_desc;
        private String top_red_desc;
        private String top_cc_status;
        private String top_jk_status;
        private String top_xl_status;
        private String top_jn_status;
        private String top_qt_status;
        private List<String> top_photo_arr;


    }
}

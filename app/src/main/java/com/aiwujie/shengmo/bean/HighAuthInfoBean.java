package com.aiwujie.shengmo.bean;

import java.util.ArrayList;
import java.util.List;

import kotlin.ExperimentalMultiplatform;

public class HighAuthInfoBean {


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
        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
        }

        public DataStatusBean getUser_top_data() {
            return user_top_data;
        }

        public void setUser_top_data(DataStatusBean user_top_data) {
            this.user_top_data = user_top_data;
        }

        public ArrayList<DataInfoBean> getAudit_data() {
            return audit_data;
        }

        public void setAudit_data(ArrayList<DataInfoBean> audit_data) {
            this.audit_data = audit_data;
        }

        public String is_top;
        public DataStatusBean user_top_data;
        public ArrayList<DataInfoBean> audit_data;
    }

    public class DataStatusBean {
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

        private String top_cc_status;
        private String top_jk_status;
        private String top_xl_status;
        private String top_jn_status;
        private String top_qt_status;
    }

    public class DataInfoBean {

        private String audit_id;

        private String status_string;
        private String audit_type;
        private String name;
        private String desc;
        private String examin_time;

        private String school_name	;
        private String education_name	;
        private String profession_name	;
        private String admission_time	;
        private String graduation_time	;



        private String cert_num	;


        private ArrayList<String> img;

        public String getStatus_string() {
            return status_string;
        }

        public void setStatus_string(String status_string) {
            this.status_string = status_string;
        }



        public String getCert_num() {
            return cert_num;
        }

        public void setCert_num(String cert_num) {
            this.cert_num = cert_num;
        }


        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getEducation_name() {
            return education_name;
        }

        public void setEducation_name(String education_name) {
            this.education_name = education_name;
        }

        public String getProfession_name() {
            return profession_name;
        }

        public void setProfession_name(String profession_name) {
            this.profession_name = profession_name;
        }

        public String getAdmission_time() {
            return admission_time;
        }

        public void setAdmission_time(String admission_time) {
            this.admission_time = admission_time;
        }

        public String getGraduation_time() {
            return graduation_time;
        }

        public void setGraduation_time(String graduation_time) {
            this.graduation_time = graduation_time;
        }





        public String getAudit_id() {
            return audit_id;
        }

        public void setAudit_id(String audit_id) {
            this.audit_id = audit_id;
        }

        public String getAudit_type() {
            return audit_type;
        }

        public void setAudit_type(String audit_type) {
            this.audit_type = audit_type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public ArrayList<String> getImg() {
            return img;
        }

        public void setImg(ArrayList<String> img) {
            this.img = img;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getExamin_time() {
            return examin_time;
        }

        public void setExamin_time(String examin_time) {
            this.examin_time = examin_time;
        }

        public ArrayList<String> getImgList() {
            return imgList;
        }

        public void setImgList(ArrayList<String> imgList) {
            this.imgList = imgList;
        }

        private ArrayList<String> imgList = new ArrayList();
        private String status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }




    }


}

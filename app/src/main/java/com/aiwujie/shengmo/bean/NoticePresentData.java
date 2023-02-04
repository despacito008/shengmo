package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/26.
 */

public class NoticePresentData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"12","fuid":"175","nickname":"小清新","fnickname":"深藏功与名.3","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","fhead_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg","type":"19","beans":"520","num":"1"},{"uid":"12","fuid":"175","nickname":"小清新","fnickname":"深藏功与名.3","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","fhead_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg","type":"15","beans":"528","num":"6"}]
     */

    private int retcode;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 12
         * fuid : 175
         * nickname : 小清新
         * fnickname : 深藏功与名.3
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg
         * fhead_pic : http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg
         * type : 19
         * beans : 520
         * num : 1
         */

        private String uid;
        private String fuid;
        private String nickname;
        private String fnickname;
        private String head_pic;
        private String fhead_pic;
        private String type;
        private String beans;
        private String num;
        private String addtime;
        private String source_type;
        private String gift_name;
        private String gift_image;
        private String state;
        private String source_type_int;
        private String gid;

        public String getSource_type_int() {
            return source_type_int;
        }

        public void setSource_type_int(String source_type_int) {
            this.source_type_int = source_type_int;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public String getGift_image() {
            return gift_image;
        }

        public void setGift_image(String gift_image) {
            this.gift_image = gift_image;
        }

        public String getSource_type() {
            return source_type;
        }

        public void setSource_type(String source_type) {
            this.source_type = source_type;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFnickname() {
            return fnickname;
        }

        public void setFnickname(String fnickname) {
            this.fnickname = fnickname;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getFhead_pic() {
            return fhead_pic;
        }

        public void setFhead_pic(String fhead_pic) {
            this.fhead_pic = fhead_pic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}

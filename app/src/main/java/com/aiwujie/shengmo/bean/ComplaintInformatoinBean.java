package com.aiwujie.shengmo.bean;

import java.util.List;

public class ComplaintInformatoinBean {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"rpid":"53811","uid":"90300","report":"辱骂","state":"广告骚扰","images":["Uploads/Picture/2021-03-01/20210301214736415.jpg"],"otheruid":"279303","addtime":"1614606457","returntime":"1614675980","othersex":"1","othername":"吃瓜LSP","manageway":"2","image":["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-01/20210301214736415.jpg"],"nickname":"末深","sex":"2","hfreportcount":"0","hfbreportcount":"2"}]
     */

    private Integer retcode;
    private String msg;
    private List<ComplaintInformatoin> data;

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ComplaintInformatoin> getData() {
        return data;
    }

    public void setData(List<ComplaintInformatoin> data) {
        this.data = data;
    }

    public static class ComplaintInformatoin {
        /**
         * rpid : 53811
         * uid : 90300
         * report : 辱骂
         * state : 广告骚扰
         * images : ["Uploads/Picture/2021-03-01/20210301214736415.jpg"]
         * otheruid : 279303
         * addtime : 1614606457
         * returntime : 1614675980
         * othersex : 1
         * othername : 吃瓜LSP
         * manageway : 2
         * image : ["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-01/20210301214736415.jpg"]
         * nickname : 末深
         * sex : 2
         * hfreportcount : 0
         * hfbreportcount : 2
         */

        private String rpid;
        private String uid;
        private String report;
        private String state;
        private List<String> images;
        private String otheruid;
        private String addtime;
        private String returntime;
        private String othersex;
        private String othername;
        private String manageway;
        private List<String> image;
        private String nickname;
        private String sex;
        private String hfreportcount;
        private String hfbreportcount;
        private String manageway_info;

        public String getManageway_info() {
            return manageway_info;
        }

        public void setManageway_info(String manageway_info) {
            this.manageway_info = manageway_info;
        }

        public String getRpid() {
            return rpid;
        }

        public void setRpid(String rpid) {
            this.rpid = rpid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getOtheruid() {
            return otheruid;
        }

        public void setOtheruid(String otheruid) {
            this.otheruid = otheruid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getReturntime() {
            return returntime;
        }

        public void setReturntime(String returntime) {
            this.returntime = returntime;
        }

        public String getOthersex() {
            return othersex;
        }

        public void setOthersex(String othersex) {
            this.othersex = othersex;
        }

        public String getOthername() {
            return othername;
        }

        public void setOthername(String othername) {
            this.othername = othername;
        }

        public String getManageway() {
            return manageway;
        }

        public void setManageway(String manageway) {
            this.manageway = manageway;
        }

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHfreportcount() {
            return hfreportcount;
        }

        public void setHfreportcount(String hfreportcount) {
            this.hfreportcount = hfreportcount;
        }

        public String getHfbreportcount() {
            return hfbreportcount;
        }

        public void setHfbreportcount(String hfbreportcount) {
            this.hfbreportcount = hfbreportcount;
        }
    }
}

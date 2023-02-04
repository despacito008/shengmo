package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/27.
 */

public class NoticeVipData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"12","fuid":"7598","nickname":"小清新","fnickname":"Arleen","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-08-21/20170821150522505.jpg","type":"4","num":"1","addtime":"23秒前","state":"3"},{"uid":"12","fuid":"7598","nickname":"小清新","fnickname":"Arleen","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-08-21/20170821150522505.jpg","type":"1","num":"1","addtime":"7分钟前","state":"3"},{"uid":"12","fuid":"7598","nickname":"小清新","fnickname":"Arleen","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-08-21/20170821150522505.jpg","type":"1","num":"1","addtime":"8分钟前","state":"3"},{"uid":"12","fuid":"7598","nickname":"小清新","fnickname":"Arleen","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-08-21/20170821150522505.jpg","type":"1","num":"1","addtime":"34分钟前","state":"3"},{"uid":"11","fuid":"12","nickname":"绿色斯慕","fnickname":"小清新","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-21/20171121172935299.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg","type":"1","num":"1","addtime":"5天前","state":"3"},{"uid":"12","fuid":"175","nickname":"小清新","fnickname":"深藏功与名.3","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg","fhead_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg","type":"1","num":"1","addtime":"6天前","state":"3"}]
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
         * fuid : 7598
         * nickname : 小清新
         * fnickname : Arleen
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2017-11-08/20171108101910898.jpg
         * fhead_pic : http://hao.shengmo.org:888/Uploads/Picture/2017-08-21/20170821150522505.jpg
         * type : 4
         * num : 1
         * addtime : 23秒前
         * state : 3
         */

        private String uid;
        private String fuid;
        private String nickname;
        private String fnickname;
        private String head_pic;
        private String fhead_pic;
        private String type;
        private String num;
        private String addtime;
        private String state;

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

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}

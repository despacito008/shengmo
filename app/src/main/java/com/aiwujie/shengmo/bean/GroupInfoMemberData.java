package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/17.
 */
public class GroupInfoMemberData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"12","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-12/2017011220502321.jpg","state":"3","onlinestate":0,"last_login_time":"47年前","introduce":""}]
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
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-12/2017011220502321.jpg
         * state : 3
         * onlinestate : 0
         * last_login_time : 47年前
         * introduce :
         */

        private String uid;
        private String head_pic;
        private String state;
        private int onlinestate;
        private String last_login_time;
        private String introduce;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }
    }
}

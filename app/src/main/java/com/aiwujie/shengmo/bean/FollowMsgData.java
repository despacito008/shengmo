package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/9/28.
 */

public class FollowMsgData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"564","nickname":"深浅。","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-27/20170527113901611.jpg","addtime":"52分钟前"}]
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
         * uid : 564
         * nickname : 深浅。
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-05-27/20170527113901611.jpg
         * addtime : 52分钟前
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String addtime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}

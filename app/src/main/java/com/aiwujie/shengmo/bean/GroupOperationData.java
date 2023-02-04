package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/18.
 */
public class GroupOperationData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"9","ugid":"35","uid":"12","other_uid":"11","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","content":"哈哈哈请求加入群最美群屌爆","msg":"哈哈","state":"1"},{"id":"12","ugid":"38","uid":"12","other_uid":"13","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/20170114163418856.jpg","content":"测试请求加入群最美群屌爆","msg":"哈哈","state":"1"}]
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
         * id : 9
         * ugid : 35
         * uid : 12
         * other_uid : 11
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg
         * content : 哈哈哈请求加入群最美群屌爆
         * msg : 哈哈
         * state : 1
         */

        private String mid;
        private String ugid;
        private String uid;
        private String other_uid;
        private String head_pic;
        private String content;
        private String msg;
        private String state;
        private String gid;
        private String nickname;
        private String operatortime;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOperatortime() {
            return operatortime;
        }

        public void setOperatortime(String operatortime) {
            this.operatortime = operatortime;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getUgid() {
            return ugid;
        }

        public void setUgid(String ugid) {
            this.ugid = ugid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOther_uid() {
            return other_uid;
        }

        public void setOther_uid(String other_uid) {
            this.other_uid = other_uid;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}

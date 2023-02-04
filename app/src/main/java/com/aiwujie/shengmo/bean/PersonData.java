package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2016/12/31.
 */
public class PersonData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"5","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg","nickname":"屌","fans_num":"0","follow_num":"0","group_num":"0"}
     */

    private int retcode;
    private String msg;
    private DataBean data;

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

    public static class DataBean {
        /**
         * uid : 5
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg
         * nickname : 屌
         * fans_num : 0
         * follow_num : 0
         * group_num : 0
         */

        private String uid;
        private String head_pic;
        private String nickname;
        private String fans_num;
        private String follow_num;
        private String group_num;
        private String marknametwo;
        private String markname;

        public String getTop_follow_num() {
            return top_follow_num;
        }

        public void setTop_follow_num(String top_follow_num) {
            this.top_follow_num = top_follow_num;
        }

        public String getTop_fans_num() {
            return top_fans_num;
        }

        public void setTop_fans_num(String top_fans_num) {
            this.top_fans_num = top_fans_num;
        }

        private String top_follow_num;
        private String top_fans_num;

        public String getIs_top() {
            return is_top;
        }

        public void setIs_top(String is_top) {
            this.is_top = is_top;
        }

        public String getTop_id() {
            return top_id;
        }

        public void setTop_id(String top_id) {
            this.top_id = top_id;
        }

        public String getTop_head_pic() {
            return top_head_pic;
        }

        public void setTop_head_pic(String top_head_pic) {
            this.top_head_pic = top_head_pic;
        }

        private String is_top;
        private String top_id ;
        private String top_head_pic ;

        public String getSerial_id() {
            return serial_id;
        }

        public void setSerial_id(String serial_id) {
            this.serial_id = serial_id;
        }

        private String serial_id ;

        public String getIs_over() {
            return is_over;
        }

        public void setIs_over(String is_over) {
            this.is_over = is_over;
        }

        private String is_over ;

        public String getMarkname() {
            return markname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }

        public String getMarknametwo() {
            return marknametwo;
        }

        public void setMarknametwo(String marknametwo) {
            this.marknametwo = marknametwo;
        }

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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFans_num() {
            return fans_num;
        }

        public void setFans_num(String fans_num) {
            this.fans_num = fans_num;
        }

        public String getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(String follow_num) {
            this.follow_num = follow_num;
        }

        public String getGroup_num() {
            return group_num;
        }

        public void setGroup_num(String group_num) {
            this.group_num = group_num;
        }
    }
}

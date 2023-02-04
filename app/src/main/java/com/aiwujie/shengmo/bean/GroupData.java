package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/16.
 */
public class GroupData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"gid":"10000021","groupname":"最美群屌爆","group_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-17/20170117203859655.jpg","introduce":"你mins你明明倪敏明宫ing哦尼过敏","member":"3","distance":"0.00","state":"3"},{"gid":"10000025","groupname":"嘻嘻哈哈","group_pic":"http://59.110.28.150:888/","introduce":"啥玩意","member":"2","distance":"0.10","state":-1},{"gid":"10000026","groupname":"了了了了了了了了了了了了了了了","group_pic":"http://59.110.28.150:888/","introduce":"了了了了了了了了了了了","member":"1","distance":"685.52","state":-1},{"gid":"10000024","groupname":"还是挺好的自建卢建奎","group_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-17/20170117193215873.jpg","introduce":"123老累了无图回家路途天童年卢建奎了哦哦哦卢建奎快乐摩羯旅途兔兔卢建奎了卢建奎卢建奎卢建奎卢建奎卢建奎卢建奎卢卡咯多头木木木OK","member":"2","distance":2323,"state":"1"}]
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
         * gid : 10000021
         * groupname : 最美群屌爆
         * group_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-17/20170117203859655.jpg
         * introduce : 你mins你明明倪敏明宫ing哦尼过敏
         * member : 3
         * distance : 0.00
         * state : 3
         */

        private String gid;
        private String groupname;
        private String group_pic;
        private String introduce;
        private String member;
        private String distance;
        private String group_num;
        private String new_group_uid;
        private String new_group_nick;
        private String new_group_head_pic;
        private String is_claim;
        private String new_change_group_uid_time;

        public String getNew_change_group_uid_time() {
            return new_change_group_uid_time;
        }

        public void setNew_change_group_uid_time(String new_change_group_uid_time) {
            this.new_change_group_uid_time = new_change_group_uid_time;
        }

        public String getIs_claim() {
            return is_claim;
        }

        public void setIs_claim(String is_claim) {
            this.is_claim = is_claim;
        }

        public String getNew_group_uid() {
            return new_group_uid;
        }

        public void setNew_group_uid(String new_group_uid) {
            this.new_group_uid = new_group_uid;
        }

        public String getNew_group_nick() {
            return new_group_nick;
        }

        public void setNew_group_nick(String new_group_nick) {
            this.new_group_nick = new_group_nick;
        }

        public String getNew_group_head_pic() {
            return new_group_head_pic;
        }

        public void setNew_group_head_pic(String new_group_head_pic) {
            this.new_group_head_pic = new_group_head_pic;
        }

        public String getGroup_num() {
            return group_num;
        }

        public void setGroup_num(String group_num) {
            this.group_num = group_num;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroup_pic() {
            return group_pic;
        }

        public void setGroup_pic(String group_pic) {
            this.group_pic = group_pic;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

    }
}

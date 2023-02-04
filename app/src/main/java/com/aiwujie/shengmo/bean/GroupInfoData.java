package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/1/16.
 */
public class GroupInfoData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"12","groupname":"最美群屌爆了","group_pic":"http://59.110.28.150:888/","member":"1","max_member":"50","introduce":"你mins你明明倪敏明宫ing哦尼过敏","province":"北京市","city":"北京市","manager":"0","userpower":"2"}
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
         * uid : 12
         * groupname : 最美群屌爆了
         * group_pic : http://59.110.28.150:888/
         * member : 1
         * max_member : 50
         * introduce : 你mins你明明倪敏明宫ing哦尼过敏
         * province : 北京市
         * city : 北京市
         * manager : 0
         * userpower : 2
         */

        private String uid;
        private String groupname;
        private String group_pic;
        private String member;
        private String max_member;
        private String introduce;
        private String province;
        private String city;
        private String manager;
        private String userpower;
        private String group_num;
        private String managerStr;
        private String cardname;
        private String is_auto_check;
        private String create_time;
        private String is_claim;
        private String lat;
        private String lng;
        private String group_leader_let;
        private String is_fanclub;
        private String gid;
        private String im_gid;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getIm_gid() {
            return im_gid;
        }

        public void setIm_gid(String im_gid) {
            this.im_gid = im_gid;
        }

        public String getIs_fanclub() {
            return is_fanclub;
        }

        public void setIs_fanclub(String is_fanclub) {
            this.is_fanclub = is_fanclub;
        }

        public String getGroup_leader_let() {
            return group_leader_let;
        }

        public void setGroup_leader_let(String group_leader_let) {
            this.group_leader_let = group_leader_let;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getIs_claim() {
            return is_claim;
        }

        public void setIs_claim(String is_claim) {
            this.is_claim = is_claim;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getIs_auto_check() {
            return is_auto_check;
        }

        public void setIs_auto_check(String is_auto_check) {
            this.is_auto_check = is_auto_check;
        }

        public String getCardname() {
            return cardname;
        }

        public void setCardname(String cardname) {
            this.cardname = cardname;
        }

        public String getManagerStr() {
            return managerStr;
        }

        public void setManagerStr(String managerStr) {
            this.managerStr = managerStr;
        }

        public String getGroup_num() {
            return group_num;
        }

        public void setGroup_num(String group_num) {
            this.group_num = group_num;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getMax_member() {
            return max_member;
        }

        public void setMax_member(String max_member) {
            this.max_member = max_member;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getUserpower() {
            return userpower;
        }

        public void setUserpower(String userpower) {
            this.userpower = userpower;
        }
    }
}

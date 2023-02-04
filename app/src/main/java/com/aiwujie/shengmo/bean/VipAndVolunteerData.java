package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2016/12/25.
 */
public class VipAndVolunteerData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"vip":"1","svip":"0","is_volunteer":"0","realname":"1","is_admin":"1"}
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
         * vip : 1
         * svip : 0
         * is_volunteer : 0
         * realname : 1
         * is_admin : 1
         */

        private String vip;
        private String svip;
        private String is_volunteer;
        private String realname;
        private String is_admin;
        private String match_state;
        private String bkvip;
        private String blvip;

        //新添加字段
        private String bk_vip_role; // 顾问 具体权限
        private String bl_vip_role; // 蓝钻 具体权限



        public String getBkvip() {
            return bkvip;
        }

        public void setBkvip(String bkvip) {
            this.bkvip = bkvip;
        }

        public String getBlvip() {
            return blvip;
        }

        public void setBlvip(String blvip) {
            this.blvip = blvip;
        }

        public String getMatch_state() {
            return match_state;
        }

        public void setMatch_state(String match_state) {
            this.match_state = match_state;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getSvip() {
            return svip;
        }

        public void setSvip(String svip) {
            this.svip = svip;
        }

        public String getIs_volunteer() {
            return is_volunteer;
        }

        public void setIs_volunteer(String is_volunteer) {
            this.is_volunteer = is_volunteer;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }


        public String getBk_vip_role() {
            if (null==bk_vip_role){
                return "";
            }else{
                return bk_vip_role;
            }

        }

        public void setBk_vip_role(String bk_vip_role) {
            this.bk_vip_role = bk_vip_role;
        }

        public String getBl_vip_role() {
            if (null==bl_vip_role){
                return "";
            }else{
                return bl_vip_role;
            }
        }

        public void setBl_vip_role(String bl_vip_role) {
            this.bl_vip_role = bl_vip_role;
        }
    }
}

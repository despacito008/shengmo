package com.aiwujie.shengmo.bean;

import java.util.List;

public class InviteRewardBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : [{"info":"xifit用户通过您邀请注册领取5天SVIP","uid":"680179","invite_uid":"680179","get_svip_time":"1970-01-01 08:00"},{"info":"通过圣魔安卓工程师用户邀请注册领取5天SVIP","uid":"680179","invite_uid":"407950","get_svip_time":"1970-01-01 08:00"}]
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
         * info : xifit用户通过您邀请注册领取5天SVIP
         * uid : 680179
         * invite_uid : 680179
         * get_svip_time : 1970-01-01 08:00
         */

        private String info;
        private String uid;
        private String invite_uid;
        private String get_svip_time;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getInvite_uid() {
            return invite_uid;
        }

        public void setInvite_uid(String invite_uid) {
            this.invite_uid = invite_uid;
        }

        public String getGet_svip_time() {
            return get_svip_time;
        }

        public void setGet_svip_time(String get_svip_time) {
            this.get_svip_time = get_svip_time;
        }
    }
}

package com.aiwujie.shengmo.bean;

public class InviteStateBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"407950","invite_code":"00ACWJ","invite_url":"http://image.aiwujie.com.cn/invite.png?x-oss-process=image/watermark,text_MCAwIEEgQyBXIEo=,color_DB57F3,size_35,g_se,x_152,y_92","invite_text":"【邀请你加入圣魔大家庭】\r\n专属邀请码是\r\n00ACWJ\r\n\u2014\u2014\r\n圣魔 APP下载地址\r\n【http://shengmo.cn】"}
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
         * uid : 407950
         * invite_code : 00ACWJ
         * invite_url : http://image.aiwujie.com.cn/invite.png?x-oss-process=image/watermark,text_MCAwIEEgQyBXIEo=,color_DB57F3,size_35,g_se,x_152,y_92
         * invite_text : 【邀请你加入圣魔大家庭】
         专属邀请码是
         00ACWJ
         ——
         圣魔 APP下载地址
         【http://shengmo.cn】
         */

        private String uid;
        private String invite_code;
        private String invite_url;
        private String invite_text;
        private String reward_text;
        private String invite_reward_type;
        private String invite_bg_url;
        private String invite_rule;

        public String getInvite_rule() {
            return invite_rule;
        }

        public void setInvite_rule(String invite_rule) {
            this.invite_rule = invite_rule;
        }

        public String getInvite_bg_url() {
            return invite_bg_url;
        }

        public void setInvite_bg_url(String invite_bg_url) {
            this.invite_bg_url = invite_bg_url;
        }

        public String getInvite_reward_type() {
            return invite_reward_type;
        }

        public void setInvite_reward_type(String invite_reward_type) {
            this.invite_reward_type = invite_reward_type;
        }

        public String getReward_text() {
            return reward_text;
        }

        public void setReward_text(String reward_text) {
            this.reward_text = reward_text;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getInvite_url() {
            return invite_url;
        }

        public void setInvite_url(String invite_url) {
            this.invite_url = invite_url;
        }

        public String getInvite_text() {
            return invite_text;
        }

        public void setInvite_text(String invite_text) {
            this.invite_text = invite_text;
        }
    }
}

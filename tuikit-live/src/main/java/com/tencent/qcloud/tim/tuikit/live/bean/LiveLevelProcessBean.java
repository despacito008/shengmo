package com.tencent.qcloud.tim.tuikit.live.bean;

public class LiveLevelProcessBean {

    /**
     * retcode : 2000
     * msg : success
     * data : {"nickname":"圣魔圣魔圣","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg","vip":"1","vipannual":"0","is_admin":0,"svip":"1","svipannual":"1","diff":49824,"level":10,"diff_tips":"距离升级还需要49824魔豆","progress":33,"level_role":6,"img_url":"http://image.aiwujie.com.cn/level.png"}
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
         * nickname : 圣魔圣魔圣
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg
         * vip : 1
         * vipannual : 0
         * is_admin : 0
         * svip : 1
         * svipannual : 1
         * diff : 49824
         * level : 10
         * diff_tips : 距离升级还需要49824魔豆
         * progress : 33
         * level_role : 6
         * img_url : http://image.aiwujie.com.cn/level.png
         */

        private String nickname;
        private String head_pic;
        private String vip;
        private String vipannual;
        private int is_admin;
        private String svip;
        private String svipannual;
        private int diff;
        private int level;
        private String diff_tips;
        private int progress;
        private int level_role;
        private String img_url;

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

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getVipannual() {
            return vipannual;
        }

        public void setVipannual(String vipannual) {
            this.vipannual = vipannual;
        }

        public int getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(int is_admin) {
            this.is_admin = is_admin;
        }

        public String getSvip() {
            return svip;
        }

        public void setSvip(String svip) {
            this.svip = svip;
        }

        public String getSvipannual() {
            return svipannual;
        }

        public void setSvipannual(String svipannual) {
            this.svipannual = svipannual;
        }

        public int getDiff() {
            return diff;
        }

        public void setDiff(int diff) {
            this.diff = diff;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getDiff_tips() {
            return diff_tips;
        }

        public void setDiff_tips(String diff_tips) {
            this.diff_tips = diff_tips;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getLevel_role() {
            return level_role;
        }

        public void setLevel_role(int level_role) {
            this.level_role = level_role;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}

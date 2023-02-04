package com.aiwujie.shengmo.bean;

public class LiveLinkChatStateBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"is_anchor":1,"is_live":0,"chat_status":0,"live_chat_status":0,"chat_voice_beans":"20","chat_video_beans":"40"}
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
         * is_anchor : 1
         * is_live : 0
         * chat_status : 0
         * live_chat_status : 0
         * chat_voice_beans : 20
         * chat_video_beans : 40
         */

        private int is_anchor;
        private int is_live;
        private int chat_status;
        private int live_chat_status;
        private String chat_voice_beans;
        private String chat_video_beans;
        private String wallet;

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public int getIs_anchor() {
            return is_anchor;
        }

        public void setIs_anchor(int is_anchor) {
            this.is_anchor = is_anchor;
        }

        public int getIs_live() {
            return is_live;
        }

        public void setIs_live(int is_live) {
            this.is_live = is_live;
        }

        public int getChat_status() {
            return chat_status;
        }

        public void setChat_status(int chat_status) {
            this.chat_status = chat_status;
        }

        public int getLive_chat_status() {
            return live_chat_status;
        }

        public void setLive_chat_status(int live_chat_status) {
            this.live_chat_status = live_chat_status;
        }

        public String getChat_voice_beans() {
            return chat_voice_beans;
        }

        public void setChat_voice_beans(String chat_voice_beans) {
            this.chat_voice_beans = chat_voice_beans;
        }

        public String getChat_video_beans() {
            return chat_video_beans;
        }

        public void setChat_video_beans(String chat_video_beans) {
            this.chat_video_beans = chat_video_beans;
        }
    }
}

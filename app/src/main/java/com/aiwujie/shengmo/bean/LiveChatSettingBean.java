package com.aiwujie.shengmo.bean;

public class LiveChatSettingBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"uid":"407921","chat_status":"1","chat_voice_beans":"50","chat_video_beans":"250","is_anchor":0}
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
         * uid : 407921
         * chat_status : 1
         * chat_voice_beans : 50
         * chat_video_beans : 250
         * is_anchor : 0
         */

        private String uid;
        private String chat_status;
        private String chat_voice_beans;
        private String chat_video_beans;
        private int is_anchor;
        private int video_beans_min = 0;
        private int video_beans_max = 0;
        private int voice_beans_min = 0;
        private int voice_beans_max = 0;

        public int getVideo_beans_min() {
            return video_beans_min;
        }

        public void setVideo_beans_min(int video_beans_min) {
            this.video_beans_min = video_beans_min;
        }

        public int getVideo_beans_max() {
            return video_beans_max;
        }

        public void setVideo_beans_max(int video_beans_max) {
            this.video_beans_max = video_beans_max;
        }

        public int getVoice_beans_min() {
            return voice_beans_min;
        }

        public void setVoice_beans_min(int voice_beans_min) {
            this.voice_beans_min = voice_beans_min;
        }

        public int getVoice_beans_max() {
            return voice_beans_max;
        }

        public void setVoice_beans_max(int voice_beans_max) {
            this.voice_beans_max = voice_beans_max;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getChat_status() {
            return chat_status;
        }

        public void setChat_status(String chat_status) {
            this.chat_status = chat_status;
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

        public int getIs_anchor() {
            return is_anchor;
        }

        public void setIs_anchor(int is_anchor) {
            this.is_anchor = is_anchor;
        }
    }
}

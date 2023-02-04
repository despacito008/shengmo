package com.aiwujie.shengmo.bean;

public class NewAnchorAuthBean {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"room_id":"472153","is_live":"1","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-02/20211102095731235.jpg","live_title":"标题1","idcardStatus":"0","bindingMobileStatus":"0","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-14/20211014203329204.jpg"}
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
         * room_id : 472153
         * is_live : 1
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-02/20211102095731235.jpg
         * live_title : 标题1
         * idcardStatus : 0
         * bindingMobileStatus : 0
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-14/20211014203329204.jpg
         */

        private String room_id;
        private String is_live;
        private String live_poster;
        private String live_title;
        private String idcardStatus;
        private String bindingMobileStatus;
        private String head_pic;
        private String applicationAnchorStatus;

        public String getApplicationAnchorStatus() {
            return applicationAnchorStatus;
        }

        public void setApplicationAnchorStatus(String applicationAnchorStatus) {
            this.applicationAnchorStatus = applicationAnchorStatus;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getIs_live() {
            return is_live;
        }

        public void setIs_live(String is_live) {
            this.is_live = is_live;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getIdcardStatus() {
            return idcardStatus;
        }

        public void setIdcardStatus(String idcardStatus) {
            this.idcardStatus = idcardStatus;
        }

        public String getBindingMobileStatus() {
            return bindingMobileStatus;
        }

        public void setBindingMobileStatus(String bindingMobileStatus) {
            this.bindingMobileStatus = bindingMobileStatus;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }
    }
}

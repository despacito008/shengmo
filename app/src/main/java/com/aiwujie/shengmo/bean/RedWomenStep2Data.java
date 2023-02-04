package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/12/18.
 */

public class RedWomenStep2Data {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"12","match_introduce":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试","match_photo":["http://hao.shengmo.org:888/Uploads/Picture/2017-04-07/20170407175934104.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg"],"match_photo_lock":"0","match_state":"2"}
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
         * match_introduce : 测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试
         * match_photo : ["http://hao.shengmo.org:888/Uploads/Picture/2017-04-07/20170407175934104.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-10-23/20171023002654427.jpg"]
         * match_photo_lock : 0
         * match_state : 2
         */

        private String uid;
        private String match_introduce;
        private String match_photo_lock;
        private String match_state;
        private List<String> match_photo;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMatch_introduce() {
            return match_introduce;
        }

        public void setMatch_introduce(String match_introduce) {
            this.match_introduce = match_introduce;
        }

        public String getMatch_photo_lock() {
            return match_photo_lock;
        }

        public void setMatch_photo_lock(String match_photo_lock) {
            this.match_photo_lock = match_photo_lock;
        }

        public String getMatch_state() {
            return match_state;
        }

        public void setMatch_state(String match_state) {
            this.match_state = match_state;
        }

        public List<String> getMatch_photo() {
            return match_photo;
        }

        public void setMatch_photo(List<String> match_photo) {
            this.match_photo = match_photo;
        }
    }
}

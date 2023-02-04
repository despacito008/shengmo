package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2020/6/4.
 */

public class FollowLiveData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"440256","nickname":"Ouyowc","live_title":"摩羯座","live_poster":"http://cs.shengmo.org/Uploads/refuse.jpg","watchsum":"111","live_time":"66","pullUrl":""},{"uid":"440255","nickname":"这是测试服务器","live_title":"摩羯座","live_poster":"http://aiwujie.com.cn/Uploads/Picture/2020-03-16/20200316151644101.jpg","watchsum":"120","live_time":"60","pullUrl":""},{"uid":"440254","nickname":"测试001","live_title":"摩羯座","live_poster":"http://cs.shengmo.org/Uploads/refuse.jpg","watchsum":"26","live_time":"60","pullUrl":""},{"uid":"440253","nickname":"写意的呀","live_title":"巨蟹座","live_poster":"http://cs.shengmo.org/Uploads/refuse.jpg","watchsum":"25","live_time":"72","pullUrl":""},{"uid":"440252","nickname":"黄叔","live_title":"双鱼座","live_poster":"http://thirdqq.qlogo.cn/g?b=oidb&k=N1U2guZs8nc1dicwJmJWvWA&s=100&t=1563240715","watchsum":"21","live_time":"60","pullUrl":""},{"uid":"440251","nickname":"迷称","live_title":"双子座","live_poster":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-28/20190728100801300.jpg","watchsum":"25","live_time":"90","pullUrl":""},{"uid":"440250","nickname":"leessangai","live_title":"白羊座","live_poster":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoicBiaYQhWgjI1yibR3IAybmWxcJc3OjdtaRDVI257ZuvO4M9EGtukBAUYpYQ4nhMmwzKibFkPBnhN0Q/132","watchsum":"29","live_time":"78","pullUrl":""},{"uid":"440249","nickname":"an\n\nan","live_title":"金牛座","live_poster":"http://thirdqq.qlogo.cn/g?b=oidb&k=0K06ulQhAoORk3K345icvYg&s=100","watchsum":"20","live_time":"67","pullUrl":""},{"uid":"440248","nickname":"happay","live_title":"射手座","live_poster":"https://thirdwx.qlogo.cn/mmopen/vi_32/Q3auHgzwzM6xemiaOA6JBX6rgpTNyZujPxmk2l8nnfYFRfRpmMfegur0BluvbUnuA1vbKNLKKvMb4Slyl6EheKw/132","watchsum":"32","live_time":"75","pullUrl":""},{"uid":"440247","nickname":"我的baby在哪里","live_title":"双鱼座","live_poster":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-28/2019072809583239.jpg","watchsum":"22","live_time":"53","pullUrl":""}]
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
         * uid : 440256
         * nickname : Ouyowc
         * live_title : 摩羯座
         * live_poster : http://cs.shengmo.org/Uploads/refuse.jpg
         * watchsum : 111
         * live_time : 66
         * pullUrl :
         */

        private String uid;
        private String nickname;
        private String live_title;
        private String live_poster;
        private String watchsum;
        private String live_time;
        private String pullUrl;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getWatchsum() {
            return watchsum;
        }

        public void setWatchsum(String watchsum) {
            this.watchsum = watchsum;
        }

        public String getLive_time() {
            return live_time;
        }

        public void setLive_time(String live_time) {
            this.live_time = live_time;
        }

        public String getPullUrl() {
            return pullUrl;
        }

        public void setPullUrl(String pullUrl) {
            this.pullUrl = pullUrl;
        }
    }
}

package com.aiwujie.shengmo.timlive.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/10/24.
 */

public class BannerLiveData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * "data":{
     * "live_banner":null,
     * "live_label":[
     * {
     * "name":"热门",
     * "tid":"1"
     * },
     * {
     * "name":"附近",
     * "tid":"2"
     * },
     * {
     * "name":"健身",
     * "tid":"3"
     * },
     * {
     * "name":"唱歌",
     * "tid":"4"
     * },
     * {
     * "name":"跳舞",
     * "tid":"5"
     * }
     * ]
     * }
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
        List<LiveBanner> live_banner;
        List<LiveLabel> live_label;

        public List<LiveBanner> getLive_banner() {
            return live_banner;
        }

        public void setLive_banner(List<LiveBanner> live_banner) {
            this.live_banner = live_banner;
        }

        public List<LiveLabel> getLive_label() {
            return live_label;
        }

        public void setLive_label(List<LiveLabel> live_label) {
            this.live_label = live_label;
        }
    }

    //标签
    class LiveLabel{
        private String name;
        private String tid;

        @Override
        public String toString() {
            return "LiveLabel{" +
                    "name='" + name + '\'' +
                    ", tid='" + tid + '\'' +
                    '}';
        }
    }
    public static class LiveBanner {
        /**
         * path : http://hao.shengmo.org:888/Uploads/Picture/2017-10-14/59e17fb1b09f5.jpg
         * url : http://hao.shengmo.org:888/Home/Info/Shengmosimu/id/3
         * title : 冲刺点评榜,天天获打赏
         * link_type : 0
         * link_id : 0
         */

        private String path;
        private String url;
        private String title;
        private String link_type;
        private String link_id;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getLink_id() {
            return link_id;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }
    }
}

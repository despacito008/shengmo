package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2020/6/2.
 */


public class LiveBannerData {

    private int retcode;
    private String msg;
    private Data data;
    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }
    public int getRetcode() {
        return retcode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }



    /**
     * Auto-generated: 2020-06-02 15:44:52
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Data {

        private List<Live_banner> live_banner;
        private List<Live_label> live_label;
        public void setLive_banner(List<Live_banner> live_banner) {
            this.live_banner = live_banner;
        }
        public List<Live_banner> getLive_banner() {
            return live_banner;
        }

        public void setLive_label(List<Live_label> live_label) {
            this.live_label = live_label;
        }
        public List<Live_label> getLive_label() {
            return live_label;
        }

    }

    /**
     * Auto-generated: 2020-06-02 15:44:52
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Live_banner {

        private String path;
        private String url;
        private String title;
        private String link_type;
        private String link_id;
        public void setPath(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }
        public String getLink_type() {
            return link_type;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }
        public String getLink_id() {
            return link_id;
        }

    }



    /**
     * Auto-generated: 2020-06-02 15:44:52
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Live_label {

        private String name;
        private String tid;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
        public String getTid() {
            return tid;
        }

    }

}

package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2016/12/24.
 */
public class BannerData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"path":"http://hao.shengmo.org:888/Uploads/Picture/2017-08-17/599536be5d0fc.jpg","url":"http://hao.shengmo.org:888/Home/Info/greensm","title":"违规处理实时数据公示"}
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
         * path : http://hao.shengmo.org:888/Uploads/Picture/2017-08-17/599536be5d0fc.jpg
         * url : http://hao.shengmo.org:888/Home/Info/greensm
         * title : 违规处理实时数据公示
         */

        private String path;
        private String url;
        private String title;

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
    }
}

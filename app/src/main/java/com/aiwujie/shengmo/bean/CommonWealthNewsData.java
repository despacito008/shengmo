package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/10/20.
 */

public class CommonWealthNewsData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"1","title":"公告测试1","url":"","pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-10-19/59e860540602f.png"},{"id":"2","title":"公告测试2","url":"","pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-10-19/59e860540602f.png"},{"id":"3","title":"公告测试3","url":"","pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-10-19/59e860540602f.png"}]
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
         * id : 1
         * title : 公告测试1
         * url :
         * pic : http://hao.shengmo.org:888/Uploads/Picture/2017-10-19/59e860540602f.png
         */

        private String id;
        private String title;
        private String url;
        private String pic;
        private String url_type;

        public String getUrl_type() {
            return url_type;
        }

        public void setUrl_type(String url_type) {
            this.url_type = url_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}

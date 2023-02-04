package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/10/20.
 */

public class WealthQuestionData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"4","title":"常见问题1","url":""},{"id":"5","title":"常见问题2","url":""},{"id":"6","title":"常见问题3","url":""},{"id":"7","title":"常见问题4","url":""},{"id":"8","title":"常见问题5","url":""},{"id":"9","title":"常见问题6","url":""}]
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
         * id : 4
         * title : 常见问题1
         * url :
         */

        private String id;
        private String title;
        private String url;
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
    }
}

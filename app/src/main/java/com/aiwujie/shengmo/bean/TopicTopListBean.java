package com.aiwujie.shengmo.bean;

import java.util.List;

public class TopicTopListBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"7","title":"555555555555","tid":"2905","link_id":"2904","type":"5","url":"2904","sort":"5","is_hidden":"0","addtime":"1627717347"},{"id":"6","title":"444444444444","tid":"2905","link_id":"10004859","type":"4","url":"10004859","sort":"4","is_hidden":"0","addtime":"1627717287"},{"id":"4","title":"无聊，有玩的吗","tid":"2905","link_id":"51512","type":"3","url":"51512","sort":"3","is_hidden":"0","addtime":"1627717011"},{"id":"3","title":"2222222222","tid":"2905","link_id":"2563168","type":"2","url":"2563168","sort":"2","is_hidden":"0","addtime":"1627716990"},{"id":"2","title":"111111111111","tid":"2905","link_id":"0","type":"1","url":"http://www.shengmo.cn","sort":"1","is_hidden":"0","addtime":"1627716938"}]
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
         * id : 7
         * title : 555555555555
         * tid : 2905
         * link_id : 2904
         * type : 5
         * url : 2904
         * sort : 5
         * is_hidden : 0
         * addtime : 1627717347
         */

        private String id;
        private String title;
        private String tid;
        private String link_id;
        private String type;
        private String url;
        private String sort;
        private String is_hidden;
        private String addtime;

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

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getLink_id() {
            return link_id;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getIs_hidden() {
            return is_hidden;
        }

        public void setIs_hidden(String is_hidden) {
            this.is_hidden = is_hidden;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}

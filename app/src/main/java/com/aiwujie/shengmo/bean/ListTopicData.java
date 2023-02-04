package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/7/19.
 */

public class ListTopicData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"tid":"1","title":"456","introduce":"456","partaketimes":"0","dynamicnum":"0","pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-01-30/2017013000011996.jpg"},{"tid":"6","title":"麻辣鸡丝","introduce":"麻辣鸡丝麻辣鸡丝麻辣鸡麻辣鸡麻辣鸡丝麻辣鸡丝麻辣鸡麻辣鸡丝麻辣鸡","partaketimes":"0","dynamicnum":"0","pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-07-18/20170718173205436.jpg"}]
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
         * tid : 1
         * title : 456
         * introduce : 456
         * partaketimes : 0
         * dynamicnum : 0
         * pic : http://hao.shengmo.org:888/Uploads/Picture/2017-01-30/2017013000011996.jpg
         */

        private String tid;
        private String title;
        private String introduce;
        private String partaketimes;
        private String dynamicnum;
        private String pic;
        private String is_admin;

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getPartaketimes() {
            return partaketimes;
        }

        public void setPartaketimes(String partaketimes) {
            this.partaketimes = partaketimes;
        }

        public String getDynamicnum() {
            return dynamicnum;
        }

        public void setDynamicnum(String dynamicnum) {
            this.dynamicnum = dynamicnum;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}

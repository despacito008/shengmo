package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/9/28.
 */

public class DynamicAndTopicCountData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"j_topic":[{"title":"旅行晒照"},{"title":"漂流瓶"}],"c_topic":[{"title":"官方通知"},{"title":"第四爱"}],"dynamiccount":{"dynamicnum":164,"comnum":6586,"laudnum":99332,"recommend":74}}
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
         * j_topic : [{"title":"旅行晒照"},{"title":"漂流瓶"}]
         * c_topic : [{"title":"官方通知"},{"title":"第四爱"}]
         * dynamiccount : {"dynamicnum":164,"comnum":6586,"laudnum":99332,"recommend":74}
         */

        private DynamiccountBean dynamiccount;
        private List<JTopicBean> j_topic;
        private List<CTopicBean> c_topic;

        public DynamiccountBean getDynamiccount() {
            return dynamiccount;
        }

        public void setDynamiccount(DynamiccountBean dynamiccount) {
            this.dynamiccount = dynamiccount;
        }

        public List<JTopicBean> getJ_topic() {
            return j_topic;
        }

        public void setJ_topic(List<JTopicBean> j_topic) {
            this.j_topic = j_topic;
        }

        public List<CTopicBean> getC_topic() {
            return c_topic;
        }

        public void setC_topic(List<CTopicBean> c_topic) {
            this.c_topic = c_topic;
        }

        public static class DynamiccountBean {
            /**
             * dynamicnum : 164
             * comnum : 6586
             * laudnum : 99332
             * recommend : 74
             */

            private int dynamicnum;
            private int comnum;
            private int laudnum;
            private int recommend;
            private int topnum;

            public int getTopnum() {
                return topnum;
            }

            public void setTopnum(int topnum) {
                this.topnum = topnum;
            }

            public int getDynamicnum() {
                return dynamicnum;
            }

            public void setDynamicnum(int dynamicnum) {
                this.dynamicnum = dynamicnum;
            }

            public int getComnum() {
                return comnum;
            }

            public void setComnum(int comnum) {
                this.comnum = comnum;
            }

            public int getLaudnum() {
                return laudnum;
            }

            public void setLaudnum(int laudnum) {
                this.laudnum = laudnum;
            }

            public int getRecommend() {
                return recommend;
            }

            public void setRecommend(int recommend) {
                this.recommend = recommend;
            }
        }

        public static class JTopicBean {
            /**
             * title : 旅行晒照
             */

            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class CTopicBean {
            /**
             * title : 官方通知
             */

            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}

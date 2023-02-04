package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/7/10.
 */

public class RedbagshouBean {

    /**
     * retcode : 2001
     * msg : 红包已被领取过！
     * data : {"u_head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","f_head_pic":"http://thirdqq.qlogo.cn/g?b=oidb&k=Qmne5ME73byibgWzOYfSiaww&s=100","gettime":"1562763980","beans":"1","u_nickname":"呀yyyyyy","f_nickname":"kk呀","content":""}
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
         * u_head_pic : http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg
         * f_head_pic : http://thirdqq.qlogo.cn/g?b=oidb&k=Qmne5ME73byibgWzOYfSiaww&s=100
         * gettime : 1562763980
         * beans : 1
         * u_nickname : 呀yyyyyy
         * f_nickname : kk呀
         * content :
         */

        private String u_head_pic;
        private String f_head_pic;
        private String gettime;
        private String beans;
        private String u_nickname;
        private String f_nickname;
        private String content;

        public String getU_head_pic() {
            return u_head_pic;
        }

        public void setU_head_pic(String u_head_pic) {
            this.u_head_pic = u_head_pic;
        }

        public String getF_head_pic() {
            return f_head_pic;
        }

        public void setF_head_pic(String f_head_pic) {
            this.f_head_pic = f_head_pic;
        }

        public String getGettime() {
            return gettime;
        }

        public void setGettime(String gettime) {
            this.gettime = gettime;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getU_nickname() {
            return u_nickname;
        }

        public void setU_nickname(String u_nickname) {
            this.u_nickname = u_nickname;
        }

        public String getF_nickname() {
            return f_nickname;
        }

        public void setF_nickname(String f_nickname) {
            this.f_nickname = f_nickname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

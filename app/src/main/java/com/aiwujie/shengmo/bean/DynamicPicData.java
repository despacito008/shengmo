package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/4/13.
 */

public class DynamicPicData {

    /**
     * retcode : 2000
     * msg : 上传成功
     * data : {"slimg":"Uploads/Picture/2017-04-13/20170413175412775sl.jpeg","syimg":"Uploads/Picture/2017-04-13/20170413175412775sy.jpeg"}
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
         * slimg : Uploads/Picture/2017-04-13/20170413175412775sl.jpeg
         * syimg : Uploads/Picture/2017-04-13/20170413175412775sy.jpeg
         */

        private String slimg;
        private String syimg;

        public String getSlimg() {
            return slimg;
        }

        public void setSlimg(String slimg) {
            this.slimg = slimg;
        }

        public String getSyimg() {
            return syimg;
        }

        public void setSyimg(String syimg) {
            this.syimg = syimg;
        }
    }
}

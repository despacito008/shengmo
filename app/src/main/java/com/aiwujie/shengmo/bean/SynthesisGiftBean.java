package com.aiwujie.shengmo.bean;

import java.util.List;

public class SynthesisGiftBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"list":[{"gift_id":"10","gift_name":"棒棒糖","gift_beans":"2","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png","num":"85"},{"gift_id":"13","gift_name":"黄瓜","gift_beans":"38","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew04.png","num":"89"},{"gift_id":"15","gift_name":"香蕉","gift_beans":"88","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew06.png","num":"76"},{"gift_id":"14","gift_name":"心心相印","gift_beans":"99","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew05.png","num":"91"},{"gift_id":"19","gift_name":"眼罩","gift_beans":"520","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew10.png","num":"64"},{"gift_id":"20","gift_name":"心灵束缚","gift_beans":"666","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew11.png","num":"103"},{"gift_id":"22","gift_name":"拍之印","gift_beans":"777","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew13.png","num":"86"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * gift_id : 10
             * gift_name : 棒棒糖
             * gift_beans : 2
             * gift_type : 0
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png
             * num : 85
             */

            private String gift_id;
            private String gift_name;
            private String gift_beans;
            private String gift_type;
            private String gift_image;
            private String num;

            public String getGift_id() {
                return gift_id;
            }

            public void setGift_id(String gift_id) {
                this.gift_id = gift_id;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_type() {
                return gift_type;
            }

            public void setGift_type(String gift_type) {
                this.gift_type = gift_type;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}

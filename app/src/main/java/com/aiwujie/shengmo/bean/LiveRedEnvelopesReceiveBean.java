package com.aiwujie.shengmo.bean;

import java.util.List;

public class LiveRedEnvelopesReceiveBean {


    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"text":"共2个/5魔豆，已领取1个/2魔豆","list":[{"nickname":"圣魔圣魔圣","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg","num":"2"}]}
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
         * text : 共2个/5魔豆，已领取1个/2魔豆
         * list : [{"nickname":"圣魔圣魔圣","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg","num":"2"}]
         */

        private String text;
        private List<ListBean> list;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * nickname : 圣魔圣魔圣
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg
             * num : 2
             */

            private String nickname;
            private String head_pic;
            private String num;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
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

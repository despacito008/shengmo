package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/7/12.
 */

public class Sfzrz_bean {

    /**
     * retcode : 2000
     * msg : 已认证
     * data : {"id":"1","uid":"402624","real_name":"1","ids":"2","card_z":"Uploads/Picture/2019-07-12/20190712212732260.jpg","card_f":"Uploads/Picture/2019-07-12/2019071221273838.jpg","card_hand":"Uploads/Picture/2019-07-12/2019071221280222.jpg","addtime":"1562938083","returntime":"0","state":"1","status":"0"}
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
         * id : 1
         * uid : 402624
         * real_name : 1
         * ids : 2
         * card_z : Uploads/Picture/2019-07-12/20190712212732260.jpg
         * card_f : Uploads/Picture/2019-07-12/2019071221273838.jpg
         * card_hand : Uploads/Picture/2019-07-12/2019071221280222.jpg
         * addtime : 1562938083
         * returntime : 0
         * state : 1
         * status : 0
         */

        private String id;
        private String uid;
        private String real_name;
        private String ids;
        private String card_z;
        private String card_f;
        private String card_hand;
        private String addtime;
        private String returntime;
        private String state;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        public String getCard_z() {
            return card_z;
        }

        public void setCard_z(String card_z) {
            this.card_z = card_z;
        }

        public String getCard_f() {
            return card_f;
        }

        public void setCard_f(String card_f) {
            this.card_f = card_f;
        }

        public String getCard_hand() {
            return card_hand;
        }

        public void setCard_hand(String card_hand) {
            this.card_hand = card_hand;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getReturntime() {
            return returntime;
        }

        public void setReturntime(String returntime) {
            this.returntime = returntime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

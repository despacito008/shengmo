package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/9/3.
 */

public class FriendGroupListBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"7","uid":"402011","fgname":"，，"},{"id":"6","uid":"402011","fgname":"，"}]
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
         * uid : 402011
         * fgname : ，，
         */

        private String id;
        private String uid;
        public String fgname;
        private String is_select;
        private String num;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getIs_select() {
            return is_select;
        }

        public void setIs_select(String is_select) {
            this.is_select = is_select;
        }

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

        public String getFgname() {
            return fgname;
        }

        public void setFgname(String fgname) {
            this.fgname = fgname;
        }
    }
}

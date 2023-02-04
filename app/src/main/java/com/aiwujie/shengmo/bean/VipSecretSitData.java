package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/6/13.
 */

public class VipSecretSitData {
    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"char_rule":"0"}
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
         * char_rule : 0
         */

        private String char_rule;

        public String getChar_rule() {
            return char_rule;
        }

        public void setChar_rule(String char_rule) {
            this.char_rule = char_rule;
        }
    }
}

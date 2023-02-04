package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2016/12/25.
 */
public class SetMarkNameData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"vip":"1","svip":"0","is_volunteer":"0","realname":"1","is_admin":"1"}
     */

    private int retcode;
    private String msg;

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

    public static class DataBean {

    }
}

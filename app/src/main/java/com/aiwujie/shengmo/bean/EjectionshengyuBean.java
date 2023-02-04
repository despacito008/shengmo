package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/6/18.
 */

public class EjectionshengyuBean {

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

    public class DataBean{
        String   wallet_topcard;

        public String getWallet_topcard() {
            if (null==wallet_topcard){
                return "";
            }else{
                return wallet_topcard;
            }

        }

        public void setWallet_topcard(String wallet_topcard) {
            this.wallet_topcard = wallet_topcard;
        }
    }
}

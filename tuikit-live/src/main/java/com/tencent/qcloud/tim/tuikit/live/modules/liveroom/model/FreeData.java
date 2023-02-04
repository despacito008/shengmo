package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model;

public class FreeData {

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

    public int retcode ;
    public String msg ;
    public DataBean data ;

    public class  DataBean  {
        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public  String  min ;
    }
}

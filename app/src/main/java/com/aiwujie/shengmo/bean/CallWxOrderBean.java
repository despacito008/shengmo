package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: CallWxOrderBean
 * @Author: xmf
 * @CreateDate: 2022/5/27 20:35
 * @Description:
 */
public class CallWxOrderBean {
    private int retcode;
    private String msg;
    private WeChatOrderBean data;

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

    public WeChatOrderBean getData() {
        return data;
    }

    public void setData(WeChatOrderBean data) {
        this.data = data;
    }
}

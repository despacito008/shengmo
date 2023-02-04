package com.tencent.qcloud.tim.tuikit.live.bean;

import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;

import java.util.List;

/**
 * @author: xmf
 * @date: 2022/6/15 17:00
 * @desc:
 */
public class LiveHistoryMessageBean {
    private int retcode;
    private String msg;
    private DataBean data;

    public static class DataBean {
        private List<ChatEntity> list;

        public List<ChatEntity> getList() {
            return list;
        }

        public void setList(List<ChatEntity> list) {
            this.list = list;
        }
    }

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
}

package com.aiwujie.shengmo.bean;

import java.util.List;

public class LotteryDrawMarqueeBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"msgData":["恭喜圣魔圣魔圣抽中666魔豆心灵束缚","恭喜魔徒抽中520魔豆眼罩","恭喜魔徒抽中520魔豆眼罩","恭喜魔徒抽中520魔豆眼罩","恭喜魔徒抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中666魔豆心灵束缚","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中666魔豆心灵束缚","恭喜魔徒抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中520魔豆眼罩","恭喜圣魔圣魔圣抽中666魔豆心灵束缚"]}
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
        private List<String> msgData;

        public List<String> getMsgData() {
            return msgData;
        }

        public void setMsgData(List<String> msgData) {
            this.msgData = msgData;
        }
    }
}

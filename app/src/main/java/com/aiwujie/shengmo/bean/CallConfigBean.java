package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: CallConfigBean
 * @Author: xmf
 * @CreateDate: 2022/5/26 15:16
 * @Description:
 */
public class CallConfigBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"call_text1":"60分钟内10倍曝光\r\n开启后，让更多的人看到你\r\n快速获得聊天，遇见对的他","call_text2":"快速交友利器！\r\n开启呼唤，60分钟内10倍曝光\r\n让更多人看到你，获得更多互动与关注","call_text3":"此文案暂无用"}
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
         * call_text1 : 60分钟内10倍曝光
         开启后，让更多的人看到你
         快速获得聊天，遇见对的他
         * call_text2 : 快速交友利器！
         开启呼唤，60分钟内10倍曝光
         让更多人看到你，获得更多互动与关注
         * call_text3 : 此文案暂无用
         */

        private String call_text1;
        private String call_text2;
        private String call_text3;

        public String getCall_text1() {
            return call_text1;
        }

        public void setCall_text1(String call_text1) {
            this.call_text1 = call_text1;
        }

        public String getCall_text2() {
            return call_text2;
        }

        public void setCall_text2(String call_text2) {
            this.call_text2 = call_text2;
        }

        public String getCall_text3() {
            return call_text3;
        }

        public void setCall_text3(String call_text3) {
            this.call_text3 = call_text3;
        }
    }
}

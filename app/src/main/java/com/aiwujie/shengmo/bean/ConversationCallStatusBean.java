package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: ConversationCallStatusBean
 * @Author: xmf
 * @CreateDate: 2022/5/25 15:46
 * @Description:
 */
public class ConversationCallStatusBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"status":"0","call_times":"0"}
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
         * status : 0
         * call_times : 0
         */

        private String status;
        private String call_times;
        private String remaining_time;
        private String toast_tip;

        public String getToast_tip() {
            return toast_tip;
        }

        public void setToast_tip(String toast_tip) {
            this.toast_tip = toast_tip;
        }

        public String getRemaining_time() {
            return remaining_time;
        }

        public void setRemaining_time(String remaining_time) {
            this.remaining_time = remaining_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCall_times() {
            return call_times;
        }

        public void setCall_times(String call_times) {
            this.call_times = call_times;
        }
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: ExtensionDetailBean
 * @Author: xmf
 * @CreateDate: 2022/6/7 9:25
 * @Description:
 */
public class ExtensionDetailBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"history_id":"40","history_status":"1","history_remark":"","history_times":"2","time_duration":"30","add_time":"1654502720","add_time_string":"2022-06-06 16:05:20","call_pv":"3","call_uv":"1","call_click":"1","click_user_data":[{"nickname":"notalone","head_pic":"http://ploads/refuse.jpg","uid":"703916"}]}
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
         * history_id : 40
         * history_status : 1
         * history_remark :
         * history_times : 2
         * time_duration : 30
         * add_time : 1654502720
         * add_time_string : 2022-06-06 16:05:20
         * call_pv : 3
         * call_uv : 1
         * call_click : 1
         * click_user_data : [{"nickname":"notalone","head_pic":"http://ploads/refuse.jpg","uid":"703916"}]
         */

        private String history_id;
        private String history_status;
        private String history_remark;
        private String history_times;
        private String time_duration;
        private String add_time;
        private String add_time_string;
        private String call_pv;
        private String call_uv;
        private String call_click;
        private List<ClickUserDataBean> click_user_data;

        public String getHistory_id() {
            return history_id;
        }

        public void setHistory_id(String history_id) {
            this.history_id = history_id;
        }

        public String getHistory_status() {
            return history_status;
        }

        public void setHistory_status(String history_status) {
            this.history_status = history_status;
        }

        public String getHistory_remark() {
            return history_remark;
        }

        public void setHistory_remark(String history_remark) {
            this.history_remark = history_remark;
        }

        public String getHistory_times() {
            return history_times;
        }

        public void setHistory_times(String history_times) {
            this.history_times = history_times;
        }

        public String getTime_duration() {
            return time_duration;
        }

        public void setTime_duration(String time_duration) {
            this.time_duration = time_duration;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getAdd_time_string() {
            return add_time_string;
        }

        public void setAdd_time_string(String add_time_string) {
            this.add_time_string = add_time_string;
        }

        public String getCall_pv() {
            return call_pv;
        }

        public void setCall_pv(String call_pv) {
            this.call_pv = call_pv;
        }

        public String getCall_uv() {
            return call_uv;
        }

        public void setCall_uv(String call_uv) {
            this.call_uv = call_uv;
        }

        public String getCall_click() {
            return call_click;
        }

        public void setCall_click(String call_click) {
            this.call_click = call_click;
        }

        public List<ClickUserDataBean> getClick_user_data() {
            return click_user_data;
        }

        public void setClick_user_data(List<ClickUserDataBean> click_user_data) {
            this.click_user_data = click_user_data;
        }

        public static class ClickUserDataBean {
            /**
             * nickname : notalone
             * head_pic : http://ploads/refuse.jpg
             * uid : 703916
             */

            private String nickname;
            private String head_pic;
            private String uid;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}

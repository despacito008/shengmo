package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: CallUseHistoryBean
 * @Author: xmf
 * @CreateDate: 2022/5/26 15:57
 * @Description: 呼唤使用记录
 */
public class CallUseHistoryBean {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"history_id":"38","history_status":"1","history_remark":null,"history_times":"1","time_duration":"30","add_time":"1654498989","add_time_string":"2022-06-06 15:03:09","history_desc":"使用了1次呼唤","call_pv":"0","call_uv":"0","call_click":"0","img_url":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg","click_user_data":[]},{"history_id":"31","history_status":"1","history_remark":null,"history_times":"1","time_duration":"30","add_time":"1654489342","add_time_string":"2022-06-06 12:22:22","history_desc":"使用了1次呼唤","call_pv":"0","call_uv":"0","call_click":"0","img_url":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg","click_user_data":[]},{"history_id":"25","history_status":"1","history_remark":null,"history_times":"1","time_duration":"30","add_time":"1654171858","add_time_string":"2022-06-02 20:10:58","history_desc":"使用了1次呼唤","call_pv":"0","call_uv":"0","call_click":"0","img_url":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg","click_user_data":[]},{"history_id":"21","history_status":"1","history_remark":null,"history_times":"1","time_duration":"60","add_time":"1654150159","add_time_string":"2022-06-02 14:09:19","history_desc":"使用了1次呼唤","call_pv":"0","call_uv":"0","call_click":"0","img_url":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg","click_user_data":[]},{"history_id":"19","history_status":"1","history_remark":null,"history_times":"1","time_duration":"60","add_time":"1654142978","add_time_string":"2022-06-02 12:09:38","history_desc":"使用了1次呼唤","call_pv":"0","call_uv":"0","call_click":"0","img_url":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg","click_user_data":[]}]
     */

    private int retcode;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * history_id : 38
         * history_status : 1
         * history_remark : null
         * history_times : 1
         * time_duration : 30
         * add_time : 1654498989
         * add_time_string : 2022-06-06 15:03:09
         * history_desc : 使用了1次呼唤
         * call_pv : 0
         * call_uv : 0
         * call_click : 0
         * img_url : http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg
         * click_user_data : []
         */

        private String history_id;
        private String history_status;
        private String history_remark;
        private String history_times;
        private String time_duration;
        private String add_time;
        private String add_time_string;
        private String history_desc;
        private String call_pv;
        private String call_uv;
        private String call_click;
        private String img_url;
        private List<String> click_user_data;

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

        public Object getHistory_remark() {
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

        public String getHistory_desc() {
            return history_desc;
        }

        public void setHistory_desc(String history_desc) {
            this.history_desc = history_desc;
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

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public List<String> getClick_user_data() {
            return click_user_data;
        }

        public void setClick_user_data(List<String> click_user_data) {
            this.click_user_data = click_user_data;
        }
    }
}

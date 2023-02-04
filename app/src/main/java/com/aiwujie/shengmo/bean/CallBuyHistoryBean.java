package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: CallBuyHistoryBean
 * @Author: xmf
 * @CreateDate: 2022/5/27 11:38
 * @Description:
 */
public class CallBuyHistoryBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"4","history_desc":"使用45.00购买","bag_times_desc":" 15次呼唤","add_time":"2022-05-26 20:01:17"},{"id":"3","history_desc":"使用45.00购买","bag_times_desc":" 15次呼唤","add_time":"2022-05-26 20:01:17"},{"id":"2","history_desc":"使用50魔豆兑换","bag_times_desc":" 10次呼唤","add_time":"2022-05-26 20:01:17"},{"id":"1","history_desc":"使用50魔豆兑换","bag_times_desc":" 15次呼唤","add_time":"2022-05-26 20:01:17"}]
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
         * id : 4
         * history_desc : 使用45.00购买
         * bag_times_desc :  15次呼唤
         * add_time : 2022-05-26 20:01:17
         */

        private String id;
        private String history_desc;
        private String bag_times_desc;
        private String add_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHistory_desc() {
            return history_desc;
        }

        public void setHistory_desc(String history_desc) {
            this.history_desc = history_desc;
        }

        public String getBag_times_desc() {
            return bag_times_desc;
        }

        public void setBag_times_desc(String bag_times_desc) {
            this.bag_times_desc = bag_times_desc;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}

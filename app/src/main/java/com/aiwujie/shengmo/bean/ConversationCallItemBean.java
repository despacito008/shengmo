package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: ConversatoinCallItemBean
 * @Author: xmf
 * @CreateDate: 2022/5/25 11:31
 * @Description:
 */
public class ConversationCallItemBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : [{"bag_id":"1","bag_times":"5","bag_jg":"45.00","bag_jg_desc":"￥9/次","discount_desc":"最受欢迎"}]
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
         * bag_id : 1
         * bag_times : 5
         * bag_jg : 45.00
         * bag_jg_desc : ￥9/次
         * discount_desc : 最受欢迎
         */

        private String bag_id;
        private String bag_times;
        private String bag_jg;
        private String bag_jg_desc;
        private String discount_desc;

        public String getBag_id() {
            return bag_id;
        }

        public void setBag_id(String bag_id) {
            this.bag_id = bag_id;
        }

        public String getBag_times() {
            return bag_times;
        }

        public void setBag_times(String bag_times) {
            this.bag_times = bag_times;
        }

        public String getBag_jg() {
            return bag_jg;
        }

        public void setBag_jg(String bag_jg) {
            this.bag_jg = bag_jg;
        }

        public String getBag_jg_desc() {
            return bag_jg_desc;
        }

        public void setBag_jg_desc(String bag_jg_desc) {
            this.bag_jg_desc = bag_jg_desc;
        }

        public String getDiscount_desc() {
            return discount_desc;
        }

        public void setDiscount_desc(String discount_desc) {
            this.discount_desc = discount_desc;
        }
    }
}

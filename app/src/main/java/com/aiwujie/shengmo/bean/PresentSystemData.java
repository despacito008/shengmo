package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/7/4.
 */

public class PresentSystemData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"type":"38","num":"1","addtime":"1499152250","addtime_format":"2017-07-04 15:10:50","week":"星期二"},{"type":"37","num":"1","addtime":"1499151893","addtime_format":"2017-07-04 15:04:53","week":"星期二"}]
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
         * type : 38
         * num : 1
         * addtime : 1499152250
         * addtime_format : 2017-07-04 15:10:50
         * week : 星期二
         */

        private String type;
        private String num;
        private String addtime;
        private String addtime_format;
        private String week;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAddtime_format() {
            return addtime_format;
        }

        public void setAddtime_format(String addtime_format) {
            this.addtime_format = addtime_format;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}

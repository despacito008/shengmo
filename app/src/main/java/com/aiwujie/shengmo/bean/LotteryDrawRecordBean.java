package com.aiwujie.shengmo.bean;

import java.util.List;

public class LotteryDrawRecordBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : [{"id":"180","uid":"402583","addtime":"1647519534","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 20:18:54","week":"星期四"},{"id":"176","uid":"402583","addtime":"1647518906","beans":"2000","num":"100","nickname":"圣魔圣魔圣","text":"[抽奖x100次]中[拍之印x13][心灵束缚x10][眼罩x14][心心相印x12][香蕉x10][黄瓜x13][狗粮x13][棒棒糖x15]","addtime_format":"2022-03-17 20:08:26","week":"星期四"},{"id":"174","uid":"402583","addtime":"1647518886","beans":"200","num":"10","nickname":"圣魔圣魔圣","text":"[抽奖x10次]中[心灵束缚x2][眼罩x2][香蕉x1][黄瓜x1][狗粮x1][棒棒糖x3]","addtime_format":"2022-03-17 20:08:06","week":"星期四"},{"id":"173","uid":"402583","addtime":"1647518877","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[心灵束缚x1]","addtime_format":"2022-03-17 20:07:57","week":"星期四"},{"id":"172","uid":"402583","addtime":"1647518398","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[狗粮x1]","addtime_format":"2022-03-17 19:59:58","week":"星期四"},{"id":"171","uid":"402583","addtime":"1647518130","beans":"200","num":"10","nickname":"圣魔圣魔圣","text":"[抽奖x10次]中[拍之印x2][心灵束缚x1][眼罩x2][心心相印x1][香蕉x2][棒棒糖x2]","addtime_format":"2022-03-17 19:55:30","week":"星期四"},{"id":"170","uid":"402583","addtime":"1647518114","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[心心相印x1]","addtime_format":"2022-03-17 19:55:14","week":"星期四"},{"id":"169","uid":"402583","addtime":"1647518108","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 19:55:08","week":"星期四"},{"id":"161","uid":"402583","addtime":"1647515029","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[狗粮x1]","addtime_format":"2022-03-17 19:03:49","week":"星期四"},{"id":"160","uid":"402583","addtime":"1647515021","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[心心相印x1]","addtime_format":"2022-03-17 19:03:41","week":"星期四"},{"id":"159","uid":"402583","addtime":"1647515015","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 19:03:35","week":"星期四"},{"id":"157","uid":"402583","addtime":"1647514738","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[心灵束缚x1]","addtime_format":"2022-03-17 18:58:58","week":"星期四"},{"id":"156","uid":"402583","addtime":"1647514726","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[狗粮x1]","addtime_format":"2022-03-17 18:58:46","week":"星期四"},{"id":"155","uid":"402583","addtime":"1647514694","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 18:58:14","week":"星期四"},{"id":"154","uid":"402583","addtime":"1647514689","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":" 2022-03-17 20:42:35.569 17298-17520/com.aiwujie.shengmo D/OkHttp: 2022-03-17 18:58:09","week":"星期四"},{"id":"153","uid":"402583","addtime":"1647514682","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[棒棒糖x1]","addtime_format":"2022-03-17 18:58:02","week":"星期四"},{"id":"152","uid":"402583","addtime":"1647514676","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 18:57:56","week":"星期四"},{"id":"151","uid":"402583","addtime":"1647514669","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 18:57:49","week":"星期四"},{"id":"150","uid":"402583","addtime":"1647514663","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[香蕉x1]","addtime_format":"2022-03-17 18:57:43","week":"星期四"},{"id":"149","uid":"402583","addtime":"1647514657","beans":"20","num":"1","nickname":"圣魔圣魔圣","text":"[抽奖x1次]中[眼罩x1]","addtime_format":"2022-03-17 18:57:37","week":"星期四"}]
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
         * id : 180
         * uid : 402583
         * addtime : 1647519534
         * beans : 20
         * num : 1
         * nickname : 圣魔圣魔圣
         * text : [抽奖x1次]中[眼罩x1]
         * addtime_format : 2022-03-17 20:18:54
         * week : 星期四
         */

        private String id;
        private String uid;
        private String addtime;
        private String beans;
        private String num;
        private String nickname;
        private String text;
        private String addtime_format;
        private String week;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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

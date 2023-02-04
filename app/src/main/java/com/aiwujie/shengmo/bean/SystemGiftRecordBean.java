package com.aiwujie.shengmo.bean;

import java.util.List;

public class SystemGiftRecordBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"list":[{"addtime_format":"2021-11-27 12:25:32","week":"星期六","text":"每日签到赠[套套]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 12:16:56","week":"星期六","text":"每日签到赠[内内]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 12:16:35","week":"星期六","text":"每日签到赠[糖果]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 12:00:30","week":"星期六","text":"每日签到赠[幸运草]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 10:09:34","week":"星期六","text":"每日签到赠[糖果]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-26 18:24:17","week":"星期五","text":"每日签到赠[幸运草]x1","gift_type":"系统礼物"}],"has_more":"0"}
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
         * list : [{"addtime_format":"2021-11-27 12:25:32","week":"星期六","text":"每日签到赠[套套]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 12:16:56","week":"星期六","text":"每日签到赠[内内]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 12:16:35","week":"星期六","text":"每日签到赠[糖果]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 12:00:30","week":"星期六","text":"每日签到赠[幸运草]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-27 10:09:34","week":"星期六","text":"每日签到赠[糖果]x1","gift_type":"系统礼物"},{"addtime_format":"2021-11-26 18:24:17","week":"星期五","text":"每日签到赠[幸运草]x1","gift_type":"系统礼物"}]
         * has_more : 0
         */

        private String has_more;
        private List<ListBean> list;

        public String getHas_more() {
            return has_more;
        }

        public void setHas_more(String has_more) {
            this.has_more = has_more;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * addtime_format : 2021-11-27 12:25:32
             * week : 星期六
             * text : 每日签到赠[套套]x1
             * gift_type : 系统礼物
             */

            private String addtime_format;
            private String week;
            private String text;
            private String gift_type;

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

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getGift_type() {
                return gift_type;
            }

            public void setGift_type(String gift_type) {
                this.gift_type = gift_type;
            }
        }
    }
}

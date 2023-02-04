package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

public class AnchorLiveCardBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"label":[{"tid":"1","name":"热门","weight":"1","is_default":"0","is_lock":"0"},{"tid":"2","name":"附近","weight":"0","is_default":"0","is_lock":"0"},{"tid":"3","name":"健身","weight":"0","is_default":"0","is_lock":"0"},{"tid":"4","name":"唱歌","weight":"0","is_default":"0","is_lock":"0"},{"tid":"5","name":"跳舞","weight":"0","is_default":"0","is_lock":"0"},{"tid":"6","name":"打假","weight":"0","is_default":"0","is_lock":"0"},{"tid":"7","name":"才艺","weight":"0","is_default":"0","is_lock":"0"},{"tid":"8","name":"玩儿","weight":"0","is_default":"0","is_lock":"0"},{"tid":"9","name":"男的","weight":"0","is_default":"0","is_lock":"0"},{"tid":"10","name":"女的","weight":"0","is_default":"0","is_lock":"0"}],"max_label_num":3}
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
         * label : [{"tid":"1","name":"热门","weight":"1","is_default":"0","is_lock":"0"},{"tid":"2","name":"附近","weight":"0","is_default":"0","is_lock":"0"},{"tid":"3","name":"健身","weight":"0","is_default":"0","is_lock":"0"},{"tid":"4","name":"唱歌","weight":"0","is_default":"0","is_lock":"0"},{"tid":"5","name":"跳舞","weight":"0","is_default":"0","is_lock":"0"},{"tid":"6","name":"打假","weight":"0","is_default":"0","is_lock":"0"},{"tid":"7","name":"才艺","weight":"0","is_default":"0","is_lock":"0"},{"tid":"8","name":"玩儿","weight":"0","is_default":"0","is_lock":"0"},{"tid":"9","name":"男的","weight":"0","is_default":"0","is_lock":"0"},{"tid":"10","name":"女的","weight":"0","is_default":"0","is_lock":"0"}]
         * max_label_num : 3
         */

        private int max_label_num;
        private List<LabelBean> label;
        private String live_title;
        private String live_poster;
        private String is_interaction;
        private String interaction_tips;
        private String is_ticket;
        private String ticket_beans;
        private String anchor_status;
        private String ticket_tips;
        private String is_record;
        private String is_pwd;
        private String room_pwd;
        private String is_apply_pwd;

        public String getRoom_pwd() {
            return room_pwd;
        }

        public void setRoom_pwd(String room_pwd) {
            this.room_pwd = room_pwd;
        }

        public String getIs_apply_pwd() {
            return is_apply_pwd;
        }

        public void setIs_apply_pwd(String is_apply_pwd) {
            this.is_apply_pwd = is_apply_pwd;
        }

        public String getIs_pwd() {
            return is_pwd;
        }

        public void setIs_pwd(String is_pwd) {
            this.is_pwd = is_pwd;
        }

        public String getIs_record() {
            return is_record;
        }

        public void setIs_record(String is_record) {
            this.is_record = is_record;
        }

        public String getTicket_tips() {
            return ticket_tips;
        }

        public void setTicket_tips(String ticket_tips) {
            this.ticket_tips = ticket_tips;
        }

        public String getAnchor_status() {
            return anchor_status;
        }

        public void setAnchor_status(String anchor_status) {
            this.anchor_status = anchor_status;
        }

        public String getIs_ticket() {
            return is_ticket;
        }

        public void setIs_ticket(String is_ticket) {
            this.is_ticket = is_ticket;
        }

        public String getTicketBeans() {
            return ticket_beans;
        }

        public void setTicketBeans(String ticket_beans) {
            this.ticket_beans = ticket_beans;
        }

        public String getInteraction_tips() {
            return interaction_tips;
        }

        public void setInteraction_tips(String interaction_tips) {
            this.interaction_tips = interaction_tips;
        }

        public String getIs_interaction() {
            return is_interaction;
        }

        public void setIs_interaction(String is_interaction) {
            this.is_interaction = is_interaction;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public int getMax_label_num() {
            return max_label_num;
        }

        public void setMax_label_num(int max_label_num) {
            this.max_label_num = max_label_num;
        }

        public List<LabelBean> getLabel() {
            return label;
        }

        public void setLabel(List<LabelBean> label) {
            this.label = label;
        }

        public static class LabelBean {
            /**
             * tid : 1
             * name : 热门
             * weight : 1
             * is_default : 0
             * is_lock : 0
             */

            private String tid;
            private String name;
            private String weight;
            private String is_default;
            private String is_lock;

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }

            public String getIs_lock() {
                return is_lock;
            }

            public void setIs_lock(String is_lock) {
                this.is_lock = is_lock;
            }
        }
    }
}

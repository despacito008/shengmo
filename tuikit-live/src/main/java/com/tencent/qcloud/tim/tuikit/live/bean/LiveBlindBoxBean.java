package com.tencent.qcloud.tim.tuikit.live.bean;

import java.io.Serializable;

public class LiveBlindBoxBean implements Serializable {
        /**
         * retcode : 2000
         * msg : 获取成功！
         * data : [{"uid":"564","nickname":"深浅。","age":"21","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-27/20170527113901611.jpg","sex":"2","role":"M","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505179994","distance":"0.00","province":"北京市","city":"北京市","lat":"39.871319","lng":"116.689484","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"12","nickname":"小清新","age":"25","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","last_login_time":"1505178353","distance":"0.01","province":"北京市","city":"北京市","lat":"39.871338","lng":"116.689354","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"8","wealth_val":"0","onlinestate":1},{"uid":"175","nickname":"深藏功与名.3","age":"24","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg","sex":"1","role":"~","vip":"1","vipannual":"1","realname":"1","last_login_time":"1505178509","distance":"0.02","province":"北京市","city":"北京市","lat":"39.871426","lng":"116.689240","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"6","wealth_val":"0","onlinestate":1},{"uid":"2604","nickname":"CD主","age":"28","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-25/20170425180808736.jpg","sex":"3","role":"S","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505113831","distance":"0.02","province":"北京市","city":"北京市","lat":"39.871422","lng":"116.689339","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"13","nickname":"斯慕群组管理员","age":"29","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-06/20170606230045882.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","last_login_time":"1504923658","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871391","lng":"116.689156","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"25882","nickname":"爱好者157","age":"25","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519210756572.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505176125","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871410","lng":"116.689140","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"35240","nickname":"斯慕客服小魔","age":"27","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-28/20170628122832627.jpg","sex":"2","role":"~","vip":"1","vipannual":"1","realname":"1","last_login_time":"1505121294","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871437","lng":"116.689163","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"3","wealth_val":"42","onlinestate":1}]
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

        public static class DataBean implements Serializable {
            private String id;
            private String url;
            private String name;
            private String price;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public DataBean(String id, String url, String name, String price) {
                this.id = id;
                this.url = url;
                this.name = name;
                this.price = price;
            }
        }
}
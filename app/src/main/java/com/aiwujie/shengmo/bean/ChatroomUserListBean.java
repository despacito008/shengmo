package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/8/1.
 */

public class ChatroomUserListBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"total":2,"users":[{"uid":"402279","nickname":"kiss呀","age":"23","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg","sex":"1","role":"~","vip":"1","vipannual":"0","svip":"1","svipannual":"0","realname":"0"},{"uid":"394585","nickname":"warda","age":"29","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-20/20190720165327900.jpg","sex":"1","role":"SM","vip":"1","vipannual":"0","svip":"1","svipannual":"0","realname":"1"}]}
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
         * total : 2
         * users : [{"uid":"402279","nickname":"kiss呀","age":"23","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg","sex":"1","role":"~","vip":"1","vipannual":"0","svip":"1","svipannual":"0","realname":"0"},{"uid":"394585","nickname":"warda","age":"29","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-20/20190720165327900.jpg","sex":"1","role":"SM","vip":"1","vipannual":"0","svip":"1","svipannual":"0","realname":"1"}]
         */

        private int total;
        private List<UsersBean> users;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public static class UsersBean {
            /**
             * uid : 402279
             * nickname : kiss呀
             * age : 23
             * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg
             * sex : 1
             * role : ~
             * vip : 1
             * vipannual : 0
             * svip : 1
             * svipannual : 0
             * realname : 0
             */

            private String uid;
            private String nickname;
            private String age;
            private String head_pic;
            private String sex;
            private String role;
            private String vip;
            private String vipannual;
            private String svip;
            private String svipannual;
            private String realname;
            private String rule;

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getVip() {
                return vip;
            }

            public void setVip(String vip) {
                this.vip = vip;
            }

            public String getVipannual() {
                return vipannual;
            }

            public void setVipannual(String vipannual) {
                this.vipannual = vipannual;
            }

            public String getSvip() {
                return svip;
            }

            public void setSvip(String svip) {
                this.svip = svip;
            }

            public String getSvipannual() {
                return svipannual;
            }

            public void setSvipannual(String svipannual) {
                this.svipannual = svipannual;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }
        }
    }
}

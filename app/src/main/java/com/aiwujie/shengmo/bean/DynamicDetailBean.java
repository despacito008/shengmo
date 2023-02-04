package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/20.
 */

public class DynamicDetailBean {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"addtime":"1561017425","uid":"402279","nickname":"推顶卡","age":"119","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg","sex":"1","role":"~","vip":"1","vipannual":"0","realname":"0","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","did":"879398","content":"\n近期新增加的一些功能，大家都用上了么？😊\n有好的建议可以发动态或评论~\n\n正在开发的功能：\n●拉黑后，互相看不到对方的个人主页、以及所有列表中各自的动态","pic":""},{"addtime":"1561016305","uid":"18","nickname":"强子123","age":"35","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-05-30/20190530123048820.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"1","did":"879398","content":"\n近期新增加的一些功能，大家都用上了么？😊\n有好的建议可以发动态或评论~\n\n正在开发的功能：\n●拉黑后，互相看不到对方的个人主页、以及所有列表中各自的动态","pic":""},{"addtime":"1560961295","uid":"18","nickname":"强子123","age":"35","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-05-30/20190530123048820.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"1","did":"879398","content":"\n近期新增加的一些功能，大家都用上了么？😊\n有好的建议可以发动态或评论~\n\n正在开发的功能：\n●拉黑后，互相看不到对方的个人主页、以及所有列表中各自的动态","pic":""},{"addtime":"1560957613","uid":"402279","nickname":"推顶卡","age":"119","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg","sex":"1","role":"~","vip":"1","vipannual":"0","realname":"0","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","did":"879398","content":"\n近期新增加的一些功能，大家都用上了么？😊\n有好的建议可以发动态或评论~\n\n正在开发的功能：\n●拉黑后，互相看不到对方的个人主页、以及所有列表中各自的动态","pic":""},{"addtime":"1560957511","uid":"402279","nickname":"推顶卡","age":"119","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg","sex":"1","role":"~","vip":"1","vipannual":"0","realname":"0","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","did":"879398","content":"\n近期新增加的一些功能，大家都用上了么？😊\n有好的建议可以发动态或评论~\n\n正在开发的功能：\n●拉黑后，互相看不到对方的个人主页、以及所有列表中各自的动态","pic":""}]
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
         * addtime : 1561017425
         * uid : 402279
         * nickname : 推顶卡
         * age : 119
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2019-06-20/201906201310496.jpg
         * sex : 1
         * role : ~
         * vip : 1
         * vipannual : 0
         * realname : 0
         * is_admin : 0
         * is_hand : 0
         * is_volunteer : 0
         * svip : 1
         * svipannual : 0
         * did : 879398
         * content :
         近期新增加的一些功能，大家都用上了么？😊
         有好的建议可以发动态或评论~

         正在开发的功能：
         ●拉黑后，互相看不到对方的个人主页、以及所有列表中各自的动态
         * pic :
         */

        private String addtime;
        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String realname;
        private String is_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String did;
        private String content;
        private String pic;
        private int state;
        private int onlinestate;
        private String use_sum;
        private String sum;
        private String  interval;
        private String bkvip;
        private String blvip;

        public String getBkvip() {
            return bkvip;
        }

        public void setBkvip(String bkvip) {
            this.bkvip = bkvip;
        }

        public String getBlvip() {
            return blvip;
        }

        public void setBlvip(String blvip) {
            this.blvip = blvip;
        }

        public String getUse_sum() {
            return use_sum;
        }

        public void setUse_sum(String use_sum) {
            this.use_sum = use_sum;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getIs_hand() {
            return is_hand;
        }

        public void setIs_hand(String is_hand) {
            this.is_hand = is_hand;
        }

        public String getIs_volunteer() {
            return is_volunteer;
        }

        public void setIs_volunteer(String is_volunteer) {
            this.is_volunteer = is_volunteer;
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

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

    }
}

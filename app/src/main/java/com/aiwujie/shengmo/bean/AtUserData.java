package com.aiwujie.shengmo.bean;

import java.util.List;

public class AtUserData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"4","atuid":"623754","did":"2173309","addtime":"13分钟前","cmid":"6077522","uid":"623762","nickname":"小子","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-05-29/20210529090750521.jpg","pic":"","is_admin":"1","is_hand":"1","bkvip":"0","blvip":"1","content":"one night","info":"小子别跑在该条动态的评论中有提到你","markname":"小子别跑"}]
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
         * atuid : 623754
         * did : 2173309
         * addtime : 13分钟前
         * cmid : 6077522
         * uid : 623762
         * nickname : 小子
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-05-29/20210529090750521.jpg
         * pic :
         * is_admin : 1
         * is_hand : 1
         * bkvip : 0
         * blvip : 1
         * content : one night
         * info : 小子别跑在该条动态的评论中有提到你
         * markname : 小子别跑
         */

        private String id;
        private String atuid;
        private String did;
        private String addtime;
        private String cmid;
        private String uid;
        private String nickname;
        private String head_pic;
        private String pic;
        private String is_admin;
        private String is_hand;
        private String bkvip;
        private String blvip;
        private String content;
        private String info;
        private String markname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAtuid() {
            return atuid;
        }

        public void setAtuid(String atuid) {
            this.atuid = atuid;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getCmid() {
            return cmid;
        }

        public void setCmid(String cmid) {
            this.cmid = cmid;
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

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getMarkname() {
            return markname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }
    }
}

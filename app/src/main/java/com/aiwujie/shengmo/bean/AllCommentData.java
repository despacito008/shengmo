package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/24.
 */
public class AllCommentData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"cmid":"9","uid":"11","addtime":"1485188201","otheruid":"0","ccontent":"哈哈","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":""},{"cmid":"10","uid":"11","addtime":"1485188214","otheruid":"0","ccontent":"啥玩意","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":""},{"cmid":"15","uid":"11","addtime":"1485228027","otheruid":"0","ccontent":"是","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":""},{"cmid":"17","uid":"11","addtime":"1485233917","otheruid":"0","ccontent":"嗯嗯","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":""},{"cmid":"18","uid":"11","addtime":"1485235115","otheruid":"0","ccontent":"在","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"6","content":"老卢傻屌","pic":"Uploads/Picture/2017-01-21/20170121194645141.jpg,Uploads/Picture/2017-01-21/20170121194649636.jpg,Uploads/Picture/2017-01-21/20170121194654725.jpg,Uploads/Picture/2017-01-21/20170121194655732.jpg,Uploads/Picture/2017-01-21/20170121194655519.jpg,Uploads/Picture/2017-01-21/20170121194656187.jpg,Uploads/Picture/2017-01-21/20170121194656398.jpg,Uploads/Picture/2017-01-21/20170121194657330.jpg,Uploads/Picture/2017-01-21/20170121194657941.jpg","othernickname":""},{"cmid":"19","uid":"11","addtime":"1485240574","otheruid":"0","ccontent":"嗯嗯","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":""},{"cmid":"21","uid":"11","addtime":"1485248678","otheruid":"0","ccontent":"是","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":""},{"cmid":"22","uid":"11","addtime":"1485253529","otheruid":"0","ccontent":"哈哈","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","did":"8","content":"明明有","pic":"Uploads/Picture/2017-01-21/20170121200150444.jpg","othernickname":""},{"cmid":"23","uid":"12","addtime":"1485259062","otheruid":"11","ccontent":"ffff","nickname":"我","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":"","othernickname":"哈哈哈是吧"}]
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
         * cmid : 9
         * uid : 11
         * addtime : 1485188201
         * otheruid : 0
         * ccontent : 哈哈
         * nickname : 哈哈哈是吧
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg
         * did : 12
         * content : 是我是我是我是我我going明敏hope热热热热热热see破旅途
         * pic :
         * othernickname :
         */

        private String cmid;
        private String uid;
        private String addtime;
        private String otheruid;
        private String ccontent;
        private String nickname;
        private String head_pic;
        private String did;
        private String content;
        private String pic;
        private String othernickname;
        private String duid;
        private int reportstate;
        private String coverUrl;

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getDuid() {
            return duid;
        }

        public void setDuid(String duid) {
            this.duid = duid;
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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getOtheruid() {
            return otheruid;
        }

        public void setOtheruid(String otheruid) {
            this.otheruid = otheruid;
        }

        public String getCcontent() {
            return ccontent;
        }

        public void setCcontent(String ccontent) {
            this.ccontent = ccontent;
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

        public String getOthernickname() {
            return othernickname;
        }

        public void setOthernickname(String othernickname) {
            this.othernickname = othernickname;
        }

        public int getReportState() {
            return reportstate;
        }

        public void setReportState(int reportState) {
            this.reportstate = reportState;
        }
    }
}

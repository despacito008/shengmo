package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/24.
 */
public class AllZanData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"lid":"80","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"42分钟前","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""},{"lid":"78","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"3小时前","did":"2","content":"明minsXP我去去去up","pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121180509641.jpg"},{"lid":"77","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"3小时前","did":"8","content":"明明有","pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121200150444.jpg"},{"lid":"73","uid":"12","nickname":"屌不屌","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","addtime":"11小时前","did":"6","content":"老卢傻屌","pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121194645141.jpg"},{"lid":"60","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1天前","did":"6","content":"老卢傻屌","pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121194645141.jpg"},{"lid":"13","uid":"12","nickname":"屌不屌","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","addtime":"2天前","did":"2","content":"明minsXP我去去去up","pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121180509641.jpg"},{"lid":"11","uid":"12","nickname":"屌不屌","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","addtime":"2天前","did":"8","content":"明明有","pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121200150444.jpg"}]
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
         * lid : 80
         * uid : 11
         * nickname : 哈哈哈是吧
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg
         * addtime : 42分钟前
         * did : 12
         * content : 是我是我是我是我我going明敏hope热热热热热热see破旅途
         * pic :
         */

        private String lid;
        private String uid;
        private String nickname;
        private String head_pic;
        private String addtime;
        private String did;
        private String content;
        private String pic;

        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

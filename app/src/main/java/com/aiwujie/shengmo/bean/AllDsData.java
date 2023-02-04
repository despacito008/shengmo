package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/24.
 */
public class AllDsData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"rwid":"13","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1485248683","amount":"7","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""},{"rwid":"12","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1485235392","amount":"7","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""},{"rwid":"11","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1485235195","amount":"7","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""},{"rwid":"10","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1485235040","amount":"7","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""},{"rwid":"9","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1485233934","amount":"1","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""},{"rwid":"8","uid":"11","nickname":"哈哈哈是吧","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","addtime":"1485230214","amount":"1","did":"12","content":"是我是我是我是我我going明敏hope热热热热热热see破旅途","pic":""}]
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
         * rwid : 13
         * uid : 11
         * nickname : 哈哈哈是吧
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg
         * addtime : 1485248683
         * amount : 7
         * did : 12
         * content : 是我是我是我是我我going明敏hope热热热热热热see破旅途
         * pic :
         */

        private String rwid;
        private String uid;
        private String nickname;
        private String head_pic;
        private String addtime;
        private String amount;
        private String did;
        private String content;
        private String pic;
        private String psid;
        private String num;
        private String gift_name;
        private String text;

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPsid() {
            return psid;
        }

        public void setPsid(String psid) {
            this.psid = psid;
        }

        public String getRwid() {
            return rwid;
        }

        public void setRwid(String rwid) {
            this.rwid = rwid;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

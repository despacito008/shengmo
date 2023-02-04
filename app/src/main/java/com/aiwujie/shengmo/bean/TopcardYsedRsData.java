package com.aiwujie.shengmo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class TopcardYsedRsData {



    private int retcode;
    private String msg;
    private List<DataBean> data;
    @SerializedName("data")
    private List<DataBean> dataX;

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

    public List<DataBean> getDataX() {
        return dataX;
    }

    public void setDataX(List<DataBean> dataX) {
        this.dataX = dataX;
    }

    public static class DataBean {




        /**
         * addtime : 1561004390
         * nickname : 推顶卡
         * head_pic :
         * did : 884469
         * content : 天上白玉京 十二楼五层 仙人抚我顶 结发受长生 我堂堂青龙会龙首白玉京 在线卑微 骗四叶草🍀🍀🍀
         * pic : Uploads/Picture/2019-06-20/20190620121811191sl.jpeg
         * is_admin : 0
         * is_hand : 0
         * duid : 335960
         */

        private String addtime;
        private String nickname;
        private String head_pic;
        private String did;
        private String content;
        private String pic;
        private String is_admin;
        private String is_hand;
        private String duid;



        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
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

        public String getDuid() {
            return duid;
        }

        public void setDuid(String duid) {
            this.duid = duid;
        }
    }




}

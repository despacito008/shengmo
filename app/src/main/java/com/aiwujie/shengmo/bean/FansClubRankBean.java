package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: FansClubRankBean
 * @Author: xmf
 * @CreateDate: 2022/6/1 14:34
 * @Description:
 */
public class FansClubRankBean {

    /**
     * retcode : 2000
     * msg : 操作成功
     * data : [{"uid":"407921","nickname":"测试ok棒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427183102410.jpg","fanclub_card":"okb","member":"5","fanclub_name":"测试ok棒的粉丝团"},{"uid":"250385","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","fanclub_card":"","member":"1","fanclub_name":"鸿雁传书12的粉丝团"}]
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
         * uid : 407921
         * nickname : 测试ok棒
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427183102410.jpg
         * fanclub_card : okb
         * member : 5
         * fanclub_name : 测试ok棒的粉丝团
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String fanclub_card;
        private String member;
        private String fanclub_name;

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

        public String getFanclub_card() {
            return fanclub_card;
        }

        public void setFanclub_card(String fanclub_card) {
            this.fanclub_card = fanclub_card;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getFanclub_name() {
            return fanclub_name;
        }

        public void setFanclub_name(String fanclub_name) {
            this.fanclub_name = fanclub_name;
        }
    }
}

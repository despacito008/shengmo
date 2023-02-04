package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: ClubMemberListBean
 * @Author: xmf
 * @CreateDate: 2022/5/31 17:26
 * @Description:
 */
public class ClubMemberListBean {

    /**
     * retcode : 2000
     * msg : 操作成功
     * data : [{"uid":"402583","nickname":"圣魔圣魔圣魔","sex":"1","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg","fanclub_level":5},{"uid":"402624","nickname":"魔徒","sex":"2","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","fanclub_level":1},{"uid":"703915","nickname":"圣魔-微尘-测试2","sex":"1","head_pic":"http://thirdqq.qlogo.cn/g?b=oidb&k=bt3VxkV2P1AqqVhz9WPvdA&s=100&t=1565419609","fanclub_level":3},{"uid":"407921","nickname":"测试ok棒","sex":"3","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427183102410.jpg","fanclub_level":14}]
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
         * uid : 402583
         * nickname : 圣魔圣魔圣魔
         * sex : 1
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427194206327.jpg
         * fanclub_level : 5
         */

        private String uid;
        private String nickname;
        private String sex;
        private String head_pic;
        private int fanclub_level;
        private String fanclub_card;

        public String getFanclub_card() {
            return fanclub_card;
        }

        public void setFanclub_card(String fanclub_card) {
            this.fanclub_card = fanclub_card;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public int getFanclub_level() {
            return fanclub_level;
        }

        public void setFanclub_level(int fanclub_level) {
            this.fanclub_level = fanclub_level;
        }
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/4/26.
 */

public class AtGroupMemberData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"4654","nickname":" 。","head_pic":"http://q.qlogo.cn/qqapp/1105968084/94ED5A552FACB46F8A9D205025F69325/100"},{"uid":"10282","nickname":" 暖主","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-08/20170408071159286.jpg"},{"uid":"10322","nickname":"%＆爱￥3","head_pic":"http://q.qlogo.cn/qqapp/1105968084/363CF1BBF9066347B4A51DFD9E42D2A4/100"},{"uid":"12248","nickname":"(๑\u2022́ωก̀๑)喵","head_pic":"http://q.qlogo.cn/qqapp/1105968084/4A3FC540E125B5D0EE47CDB8C6287E5F/100"},{"uid":"10367","nickname":"(Bobo.)","head_pic":"http://q.qlogo.cn/qqapp/1105968084/84D4D90AB0F1261575A306117E0E2480/100"},{"uid":"8733","nickname":"(T＿T)","head_pic":"http://q.qlogo.cn/qqapp/1105968084/DCB4F5FA3CC56BB52D16BD02CC8998EE/100"},{"uid":"8953","nickname":"(^_^)","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-04/20170404150712924.jpg"},{"uid":"11559","nickname":"(⊙o⊙)","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-13/20170413174211816.jpg"},{"uid":"8722","nickname":"(≧▽≦)/","head_pic":"http://q.qlogo.cn/qqapp/1105968084/1A569F667B02993AE15B6313B1B1CC77/100"},{"uid":"8164","nickname":"(→o←)","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-03/20170403085323773.jpg"},{"uid":"8608","nickname":"(り）￣敷衍的小情話","head_pic":"http://q.qlogo.cn/qqapp/1105968084/A60BAE229D2CCCECBB263A831C768C90/100"},{"uid":"10193","nickname":" 天倥詪黯ξ","head_pic":"http://q.qlogo.cn/qqapp/1105968084/D7409FE923468F65C70E66F98395792F/100"},{"uid":"11024","nickname":"-jjh","head_pic":"http://q.qlogo.cn/qqapp/1105968084/13FE63289E0ECC89BA00D8B22DA30DB5/100"},{"uid":"9678","nickname":"-plumbum-","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-06/20170406142157469.jpg"},{"uid":"10039","nickname":"-亡命天涯不如早点回","head_pic":"http://q.qlogo.cn/qqapp/1105968084/C51148644EF4A89ACD83E9AEAFCC44D1/100"},{"uid":"11940","nickname":"-衍夏成歌","head_pic":"http://q.qlogo.cn/qqapp/1105968084/47F9EEBAFDCE173BD2851CD6E2F5B6E1/100"},{"uid":"10121","nickname":"-有一只大熊Ss","head_pic":"http://wx.qlogo.cn/mmopen/eRSz3naW62yI4BvkITickhI0A1gahXzcZiasia2RDBc95rl4F9O4x0ObjQUVRe3FJQ8S8ibfM1SXsAjVO2Rjzfd8KA/0"},{"uid":"11583","nickname":"-栀璃鸢年","head_pic":"http://q.qlogo.cn/qqapp/1105968084/ADF006DE34F69E606A5891F227757454/100"},{"uid":"13083","nickname":"..","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-20/20170420131535229.jpg"},{"uid":"11046","nickname":".丨.","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-11/20170411094454201.jpg"}]
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
         * uid : 4654
         * nickname :  。
         * head_pic : http://q.qlogo.cn/qqapp/1105968084/94ED5A552FACB46F8A9D205025F69325/100
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String cardname;

        public String getCardname() {
            return cardname;
        }

        public void setCardname(String cardname) {
            this.cardname = cardname;
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
    }
}

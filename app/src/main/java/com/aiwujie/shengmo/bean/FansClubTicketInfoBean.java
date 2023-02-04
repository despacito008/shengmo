package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: FansClubTicketInfoBean
 * @Author: xmf
 * @CreateDate: 2022/5/30 12:07
 * @Description:
 */
public class FansClubTicketInfoBean {

    /**
     * retcode : 2000
     * msg : 操作成功
     * data : {"num":"","gift_id":"80","gift_name":"粉丝券","gift_beans":"10","gift_type":"3","gift_index":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/gift/image/dcdc.png","gift_status":"1","gift_lottieurl":"","gift_svgaurl":"","gift_lottie_status":"0","gift_source":"","is_new":"0","source_type":"0","groupname":"","group_pic":"","introduce":"","anchor_uid":0,"tip":"欢迎加入粉丝群"}
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
         * num :
         * gift_id : 80
         * gift_name : 粉丝券
         * gift_beans : 10
         * gift_type : 3
         * gift_index : 0
         * gift_image : http://image.aiwujie.com.cn/Uploads/gift/image/dcdc.png
         * gift_status : 1
         * gift_lottieurl :
         * gift_svgaurl :
         * gift_lottie_status : 0
         * gift_source :
         * is_new : 0
         * source_type : 0
         * groupname :
         * group_pic :
         * introduce :
         * anchor_uid : 0
         * tip : 欢迎加入粉丝群
         */

        private String num;
        private String gift_id;
        private String gift_name;
        private String gift_beans;
        private String gift_type;
        private String gift_index;
        private String gift_image;
        private String gift_status;
        private String gift_lottieurl;
        private String gift_svgaurl;
        private String gift_lottie_status;
        private String gift_source;
        private String is_new;
        private String source_type;
        private String groupname;
        private String group_pic;
        private String introduce;
        private String anchor_uid;
        private String tip;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getGift_id() {
            return gift_id;
        }

        public void setGift_id(String gift_id) {
            this.gift_id = gift_id;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public String getGift_beans() {
            return gift_beans;
        }

        public void setGift_beans(String gift_beans) {
            this.gift_beans = gift_beans;
        }

        public String getGift_type() {
            return gift_type;
        }

        public void setGift_type(String gift_type) {
            this.gift_type = gift_type;
        }

        public String getGift_index() {
            return gift_index;
        }

        public void setGift_index(String gift_index) {
            this.gift_index = gift_index;
        }

        public String getGift_image() {
            return gift_image;
        }

        public void setGift_image(String gift_image) {
            this.gift_image = gift_image;
        }

        public String getGift_status() {
            return gift_status;
        }

        public void setGift_status(String gift_status) {
            this.gift_status = gift_status;
        }

        public String getGift_lottieurl() {
            return gift_lottieurl;
        }

        public void setGift_lottieurl(String gift_lottieurl) {
            this.gift_lottieurl = gift_lottieurl;
        }

        public String getGift_svgaurl() {
            return gift_svgaurl;
        }

        public void setGift_svgaurl(String gift_svgaurl) {
            this.gift_svgaurl = gift_svgaurl;
        }

        public String getGift_lottie_status() {
            return gift_lottie_status;
        }

        public void setGift_lottie_status(String gift_lottie_status) {
            this.gift_lottie_status = gift_lottie_status;
        }

        public String getGift_source() {
            return gift_source;
        }

        public void setGift_source(String gift_source) {
            this.gift_source = gift_source;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getSource_type() {
            return source_type;
        }

        public void setSource_type(String source_type) {
            this.source_type = source_type;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroup_pic() {
            return group_pic;
        }

        public void setGroup_pic(String group_pic) {
            this.group_pic = group_pic;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getAnchor_uid() {
            return anchor_uid;
        }

        public void setAnchor_uid(String anchor_uid) {
            this.anchor_uid = anchor_uid;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }
    }
}

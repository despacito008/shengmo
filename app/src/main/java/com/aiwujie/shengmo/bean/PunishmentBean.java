package com.aiwujie.shengmo.bean;

import java.io.Serializable;
import java.util.List;

public class PunishmentBean {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"devicestatus":"1","status":"1","chatstatus":"1","infostatus":"1","dynamicstatus":"1","uid":"402624","nickname":"大家都很烦你小shi","age":"19","head_pic":"http://aiwujie.com.cn/Uploads/refuse.jpg","sex":"1","role":"M","vip":"1","vipannual":"0","realname":"1","last_login_time":"33分钟前","province":"","city":"北京市","lat":"40.072430","lng":"116.353661","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","bkvip":"0","blvip":"0","type":"3","addtime":"1616575039","deblockingtime":"1616709600","blockreason":"1wwo","image":["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-06/20210306085355118sy.jpeg"],"blockingalong":"1","is_show":"1","pid":"572793","wealth_val_new":"10","charm_val_new":"29732","onlinestate":1},{"devicestatus":"0","status":"0","chatstatus":"1","infostatus":"1","dynamicstatus":"1","uid":"627967","nickname":"魅舞娘i","age":"26","head_pic":"http://aiwujie.com.cn/Uploads/refuse.jpg","sex":"2","role":"S","vip":"0","vipannual":"0","realname":"0","last_login_time":"1周前","province":"","city":"","lat":"0.000000","lng":"0.000000","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","bkvip":"0","blvip":"0","type":"5","addtime":"1615974921","deblockingtime":"2147483647","blockreason":"借调教名义拍视频进行威胁敲诈勒索","image":["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-06/20210306085355118sy.jpeg"],"blockingalong":"0","is_show":"1","pid":"556723","wealth_val_new":"0","charm_val_new":"0","onlinestate":0},{"devicestatus":"0","status":"0","chatstatus":"1","infostatus":"1","dynamicstatus":"1","uid":"635698","nickname":"阎折仙","age":"29","head_pic":"http://aiwujie.com.cn/Uploads/refuse.jpg","sex":"2","role":"S","vip":"0","vipannual":"0","realname":"0","last_login_time":"2周前","province":"上海市","city":"上海市","lat":"31.243319","lng":"121.484650","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","bkvip":"0","blvip":"0","type":"5","addtime":"1615209270","deblockingtime":"2147483647","blockreason":"先引诱受害者下载不明软件获取通讯录，然后裸聊时截取不雅图片或视频，最后实施敲诈。","image":["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-06/20210306085355118sy.jpeg"],"blockingalong":"0","is_show":"1","pid":"535789","wealth_val_new":"0","charm_val_new":"21","onlinestate":0},{"devicestatus":"1","status":"1","chatstatus":"1","infostatus":"1","dynamicstatus":"1","uid":"619305","nickname":"绿葡萄涂了药","age":"121","head_pic":"https://thirdwx.qlogo.cn/mmopen/vi_32/454EibFlHB6VucdjFQiatiau3PB9kekCxOXlvNCuem7OjnXQkwc6V94fWmc5huUAxS55re2bUKia59w65oloUVz8vg/132","sex":"1","role":"M","vip":"0","vipannual":"0","realname":"0","last_login_time":"13小时前","province":"甘肃省","city":"临夏回族自治州","lat":"35.707077","lng":"102.905960","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","bkvip":"0","blvip":"0","type":"2","addtime":"1614992036","deblockingtime":"1616277600","blockreason":"动态评论发大量外部链接，封禁动态两周。","image":["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-06/20210306085355118sy.jpeg"],"blockingalong":"14","is_show":"1","pid":"530154","wealth_val_new":"0","charm_val_new":"0","onlinestate":1}]
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

    public static class DataBean implements Serializable {
        /**
         * devicestatus : 1
         * status : 1
         * chatstatus : 1
         * infostatus : 1
         * dynamicstatus : 1
         * uid : 402624
         * nickname : 大家都很烦你小shi
         * age : 19
         * head_pic : http://aiwujie.com.cn/Uploads/refuse.jpg
         * sex : 1
         * role : M
         * vip : 1
         * vipannual : 0
         * realname : 1
         * last_login_time : 33分钟前
         * province :
         * city : 北京市
         * lat : 40.072430
         * lng : 116.353661
         * is_hand : 0
         * is_volunteer : 0
         * svip : 1
         * svipannual : 0
         * bkvip : 0
         * blvip : 0
         * type : 3
         * addtime : 1616575039
         * deblockingtime : 1616709600
         * blockreason : 1wwo
         * image : ["http://image.aiwujie.com.cn/Uploads/Picture/2021-03-06/20210306085355118sy.jpeg"]
         * blockingalong : 1
         * is_show : 1
         * pid : 572793
         * wealth_val_new : 10
         * charm_val_new : 29732
         * onlinestate : 1
         */

        private String devicestatus;
        private String status;
        private String chatstatus;
        private String infostatus;
        private String dynamicstatus;
        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String realname;
        private String last_login_time;
        private String province;
        private String city;
        private String lat;
        private String lng;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String bkvip;
        private String blvip;
        private String type;
        private String addtime;
        private String deblockingtime;
        private String blockreason;
        private String blockingalong;
        private String is_show;
        private String pid;
        private String wealth_val_new;
        private String charm_val_new;
        private int onlinestate;
        private List<String> image;
        private String did;
        private String comnum;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getComnum() {
            return comnum;
        }

        public void setComnum(String comnum) {
            this.comnum = comnum;
        }

        public String getDevicestatus() {
            return devicestatus;
        }

        public void setDevicestatus(String devicestatus) {
            this.devicestatus = devicestatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getChatstatus() {
            return chatstatus;
        }

        public void setChatstatus(String chatstatus) {
            this.chatstatus = chatstatus;
        }

        public String getInfostatus() {
            return infostatus;
        }

        public void setInfostatus(String infostatus) {
            this.infostatus = infostatus;
        }

        public String getDynamicstatus() {
            return dynamicstatus;
        }

        public void setDynamicstatus(String dynamicstatus) {
            this.dynamicstatus = dynamicstatus;
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

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getDeblockingtime() {
            return deblockingtime;
        }

        public void setDeblockingtime(String deblockingtime) {
            this.deblockingtime = deblockingtime;
        }

        public String getBlockreason() {
            return blockreason;
        }

        public void setBlockreason(String blockreason) {
            this.blockreason = blockreason;
        }

        public String getBlockingalong() {
            return blockingalong;
        }

        public void setBlockingalong(String blockingalong) {
            this.blockingalong = blockingalong;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getWealth_val_new() {
            return wealth_val_new;
        }

        public void setWealth_val_new(String wealth_val_new) {
            this.wealth_val_new = wealth_val_new;
        }

        public String getCharm_val_new() {
            return charm_val_new;
        }

        public void setCharm_val_new(String charm_val_new) {
            this.charm_val_new = charm_val_new;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }
    }
}

package com.aiwujie.shengmo.timlive.bean;

import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;

import java.util.List;

public class HotRedBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"sensationAnchor":null,"hotAnchor":[{"uid":"623093","vip":"0","vipannual":"0","is_admin":"0","svip":"0","svipannual":"0","watchsum":"1","live_title":"复活甲","live_poster":"http://image.aiwujie.com.cn/null","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-02-03/20210203165612678.jpg","is_live":"0","room_id":"10015","age":"61","role":"~","sex":"1","nickname":"-略略略~"}]}
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
         * sensationAnchor : null
         * hotAnchor : [{"uid":"623093","vip":"0","vipannual":"0","is_admin":"0","svip":"0","svipannual":"0","watchsum":"1","live_title":"复活甲","live_poster":"http://image.aiwujie.com.cn/null","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-02-03/20210203165612678.jpg","is_live":"0","room_id":"10015","age":"61","role":"~","sex":"1","nickname":"-略略略~"}]
         */

        private List<ScenesRoomInfoBean> hotAnchor;
        private List<ScenesRoomInfoBean> sensationAnchor;

        public List<ScenesRoomInfoBean> getSensationAnchor() {
            return sensationAnchor;
        }

        public void setSensationAnchor(List<ScenesRoomInfoBean> sensationAnchor) {
            this.sensationAnchor = sensationAnchor;
        }

        public List<ScenesRoomInfoBean> getHotAnchor() {
            return hotAnchor;
        }

        public void setHotAnchor(List<ScenesRoomInfoBean> hotAnchor) {
            this.hotAnchor = hotAnchor;
        }

    }
}

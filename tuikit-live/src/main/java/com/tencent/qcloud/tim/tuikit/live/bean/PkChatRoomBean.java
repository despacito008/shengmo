package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

public class PkChatRoomBean {

    /**
     * customMassageType : liveRoomPKAvChatRoom
     * actionType : 0
     * pkInfo : {"current":{"uid":"250385","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","room_id":"333227","follow_state":3,"pk_score":8,"level_role":"7","pk_top":[{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"},{"uid":"250385","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg"}],"pk_start_time":"1639041057","is_win":1},"other":{"uid":"407943","nickname":"测试一下吧","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-19/20211119173223394.jpg","room_id":"407943","follow_state":3,"pk_score":2,"level_role":"7","pk_top":[{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"}],"pk_start_time":"1639041057","is_win":0}}
     */

    private String customMassageType;
    private String actionType;
    private String is_reset;
    private PkInfoBean pkInfo;

    public String getIs_reset() {
        return is_reset;
    }

    public void setIs_reset(String is_reset) {
        this.is_reset = is_reset;
    }

    public String getCustomMassageType() {
        return customMassageType;
    }

    public void setCustomMassageType(String customMassageType) {
        this.customMassageType = customMassageType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public PkInfoBean getPkInfo() {
        return pkInfo;
    }

    public void setPkInfo(PkInfoBean pkInfo) {
        this.pkInfo = pkInfo;
    }


}

package com.tencent.qcloud.tim.tuikit.live.bean;

import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.v2.V2TIMMessage;

public class LiveMessageBean {
    V2TIMMessage message; //Message拓展字段
    String CustomElemData;  //IM自定义扩展字段相关
    String CloudCustomData; //message相关的

    public V2TIMMessage getMessage() {
        return message;
    }

    public void setMessage(V2TIMMessage message) {
        this.message = message;
    }

    public String getCustomElemData() {
        return CustomElemData;
    }

    public void setCustomElemData(String customElemData) {
        CustomElemData = customElemData;
    }

    public String getCloudCustomData() {
        return CloudCustomData;
    }

    public void setCloudCustomData(String cloudCustomData) {
        CloudCustomData = cloudCustomData;
    }
}

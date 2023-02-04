package com.aiwujie.shengmo.bean;

import com.tencent.imsdk.message.Message;

public class LiveMessageBean {
    Message message;
    String CustomElemData;
    String CloudCustomData;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
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

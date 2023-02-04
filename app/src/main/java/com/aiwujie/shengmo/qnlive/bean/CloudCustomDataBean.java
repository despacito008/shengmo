package com.aiwujie.shengmo.qnlive.bean;

public class CloudCustomDataBean {

    /**
     * costomMassageType : changeInfoAvChatRoom
     * actionType : 5
     * link_mic_num : 1
     * showType : 1
     */

    private String costomMassageType;
    private String showType;
    private String touid;
    private String actionType;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }
}

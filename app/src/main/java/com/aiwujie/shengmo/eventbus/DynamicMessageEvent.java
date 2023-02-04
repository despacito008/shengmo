package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/7/11.
 */

public class DynamicMessageEvent {
    /**
     * 0为隐藏 1为显示
     */
    private int isShow;
    private int isShowcount;

    public DynamicMessageEvent(int isShow,int isShowcount) {
        this.isShow = isShow;
        this.isShowcount=isShowcount;
    }

    public int getIsShowcount() {
        return isShowcount;
    }

    public void setIsShowcount(int isShowcount) {
        this.isShowcount = isShowcount;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}

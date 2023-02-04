package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/8/28.
 * 关注通知
 */

public class FollowEvent {
    private String followFlag;
    private String followState;
    private int followStateNew;
    private int position;
    public FollowEvent(String followFlag, String followState) {
        this.followFlag = followFlag;
        this.followState = followState;
    }

    /**
     *广场-视频中点击进入到短视频页面
     * @param followStateNew 关注状态
     * @param position 当前位置是
     */
    public FollowEvent(int followStateNew,int position){
        this.followStateNew = followStateNew;
        this.position=position;
    }

    public String getFollowFlag() {
        return followFlag;
    }

    public void setFollowFlag(String followFlag) {
        this.followFlag = followFlag;
    }

    public String getFollowState() {
        return followState;
    }

    public void setFollowState(String followState) {
        this.followState = followState;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getFollowStateNew() {
        return followStateNew;
    }

    public void setFollowStateNew(int followStateNew) {
        this.followStateNew = followStateNew;
    }
}

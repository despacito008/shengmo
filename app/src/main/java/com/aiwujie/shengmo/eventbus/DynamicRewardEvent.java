package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/1/22.
 */
public class DynamicRewardEvent {
    int position;
    int rewardcount;

    public DynamicRewardEvent(int position, int rewardcount) {
        this.position = position;
        this.rewardcount = rewardcount;
    }

    public int getRewardcount() {
        return rewardcount;
    }

    public void setRewardcount(int rewardcount) {
        this.rewardcount = rewardcount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}

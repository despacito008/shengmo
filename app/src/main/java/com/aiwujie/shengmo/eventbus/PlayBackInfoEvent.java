package com.aiwujie.shengmo.eventbus;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.eventbus
 * @ClassName: PlayBackInfoEvent
 * @Author: xmf
 * @CreateDate: 2022/5/4 15:41
 * @Description:
 */
public class PlayBackInfoEvent {
    String state = "";
    public PlayBackInfoEvent(String state) {
        this.state = state;
    }
}

package com.aiwujie.shengmo.eventbus;

/**
 * @author：zq 2021/5/13 10:45
 * 邮箱：80776234@qq.com
 */
public class DynamicPageTurnEvent {
  private   int position;

    public DynamicPageTurnEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }


}

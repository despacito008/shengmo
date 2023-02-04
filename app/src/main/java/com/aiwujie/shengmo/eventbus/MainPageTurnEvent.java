package com.aiwujie.shengmo.eventbus;

/**
 * @author：zq 2021/5/12 18:37
 * 邮箱：80776234@qq.com
 */
public class MainPageTurnEvent {
   /**
   跳转到main页面的第几个item 以0开始
    */
    private  int position;
    /**
     * 备选字段 跳转到 position的第几个页面 可以自己定义
     */
    private int type;

    public MainPageTurnEvent(int position, int type) {
        this.position = position;
        this.type = type;
    }

    public int getPosition() {
        return position;
    }



    public int getType() {
        return type;
    }

}

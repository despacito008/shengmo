package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/1/22.
 */
public class DynamicCommentEvent {
    int position;
    int commentcount;

    public DynamicCommentEvent(int position, int commentcount) {
        this.position = position;
        this.commentcount = commentcount;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}

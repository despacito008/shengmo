package com.aiwujie.shengmo.eventbus;

/**
 * 评论后调用事件
 */
public class CommentEvent {
    int position;
    int commentcount;

    public CommentEvent(int position, int commentcount) {
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

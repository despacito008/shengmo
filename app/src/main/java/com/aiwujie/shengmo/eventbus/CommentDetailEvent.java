package com.aiwujie.shengmo.eventbus;

public class CommentDetailEvent {
    int position;
    int type;

    public CommentDetailEvent(int position, int type) {
        this.position = position;
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

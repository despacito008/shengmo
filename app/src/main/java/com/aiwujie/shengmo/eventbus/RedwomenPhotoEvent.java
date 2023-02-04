package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2018/1/25.
 */

public class RedwomenPhotoEvent {
    private int match_photo_lock;

    public RedwomenPhotoEvent(int match_photo_lock) {
        this.match_photo_lock = match_photo_lock;
    }

    public int getMatch_photo_lock() {
        return match_photo_lock;
    }

    public void setMatch_photo_lock(int match_photo_lock) {
        this.match_photo_lock = match_photo_lock;
    }
}

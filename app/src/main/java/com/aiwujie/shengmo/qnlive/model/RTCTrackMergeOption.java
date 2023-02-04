package com.aiwujie.shengmo.qnlive.model;

import com.aiwujie.shengmo.qnlive.utils.Config;
import com.qiniu.droid.rtc.QNTrackInfo;
import com.qiniu.droid.rtc.QNTrackKind;
import com.qiniu.droid.rtc.model.QNMergeTrackOption;
import com.qiniu.droid.rtc.model.QNStretchMode;

public class RTCTrackMergeOption {

    private final String mTrackId;
    private final QNTrackInfo mQNTrackInfo;

    private boolean mTrackInclude = true;
    private final QNMergeTrackOption mQNMergeTrackOption;

    public RTCTrackMergeOption(QNTrackInfo trackInfo) {
        mQNTrackInfo = trackInfo;
        mTrackId = mQNTrackInfo.getTrackId();

        mQNMergeTrackOption = new QNMergeTrackOption();
        if (trackInfo.getTrackKind().equals(QNTrackKind.VIDEO)) {
            mQNMergeTrackOption.setWidth(Config.T_STREAMING_WIDTH);
            mQNMergeTrackOption.setHeight(Config.T_STREAMING_HEIGHT);
            mQNMergeTrackOption.setStretchMode(QNStretchMode.ASPECT_FILL);
        }
        mQNMergeTrackOption.setTrackId(mTrackId);
    }

    public String getTrackId() {
        return mTrackId;
    }

    public QNTrackInfo getQNTrackInfo() {
        return mQNTrackInfo;
    }

    public QNMergeTrackOption getQNMergeTrackOption() {
        return mQNMergeTrackOption;
    }

    public boolean isTrackInclude() {
        return mTrackInclude;
    }

    public void setTrackInclude(boolean trackInclude) {
        mTrackInclude = trackInclude;
    }

    public void updateQNMergeTrackOption(QNMergeTrackOption option) {
        if (option == null) {
            return;
        }
        mQNMergeTrackOption.setX(option.getX());
        mQNMergeTrackOption.setY(option.getY());
        mQNMergeTrackOption.setZ(option.getZ());
        mQNMergeTrackOption.setWidth(option.getWidth());
        mQNMergeTrackOption.setHeight(option.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RTCTrackMergeOption) {
            return mQNTrackInfo.equals(((RTCTrackMergeOption) obj).mQNTrackInfo);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return mQNTrackInfo.hashCode();
    }
}

package com.aliyun.apsara.alivclittlevideo.net.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.aliyun.apsara.alivclittlevideo.constants.LittleVideoParamConfig;
import com.aliyun.apsara.alivclittlevideo.sts.StsInfoManager;
import com.aliyun.apsara.alivclittlevideo.sts.StsTokenInfo;
import com.aliyun.apsara.alivclittlevideo.view.video.BaseVideoSourceModel;
import com.aliyun.apsara.alivclittlevideo.view.video.videolist.VideoSourceType;
import com.aliyun.player.source.VidSts;

import java.util.List;

/**
 * 视频信息 javaBean
 *
 */
public class LittleMineVideoInfo implements Parcelable {

    private int total;
    private List<VideoListBean> videoList;

    protected LittleMineVideoInfo(Parcel in) {
        total = in.readInt();
        videoList = in.createTypedArrayList(VideoListBean.CREATOR);
    }

    public static final Creator<LittleMineVideoInfo> CREATOR = new Creator<LittleMineVideoInfo>() {
        @Override
        public LittleMineVideoInfo createFromParcel(Parcel in) {
            return new LittleMineVideoInfo(in);
        }

        @Override
        public LittleMineVideoInfo[] newArray(int size) {
            return new LittleMineVideoInfo[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<VideoListBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoListBean> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeTypedList(videoList);
    }

    public static class VideoListBean extends BaseVideoSourceModel implements Parcelable {
        /**
         * 视频审核状态，审核中
         */
        public static final String STATUS_CENSOR_ON = "onCensor";
        /**
         * 视频审核状态，待审核
         */
        public static final String STATUS_CENSOR_WAIT = "check";
        /**
         * 视频审核状态，审核通过
         */
        public static final String STATUS_CENSOR_SUCCESS = "success";
        /**
         * 视频审核状态，审核不通过
         */
        public static final String STATUS_CENSOR_FAIL = "fail";


        private String videoId;

        private int duration;
        private String transcodeStatus;
        private String snapshotStatus;
        private String censorStatus;
        private String narrowTranscodeStatus;

        @Override
        public VideoSourceType getSourceType() {
            if (STATUS_CENSOR_SUCCESS.equals(censorStatus)) {
                return VideoSourceType.TYPE_STS;
            } else {
                return VideoSourceType.TYPE_ERROR_NOT_SHOW;
            }
        }

        @Override
        public VidSts getVidStsSource() {
            StsTokenInfo tokenInfo = StsInfoManager.getInstance().getStsToken();
            //生成播放需要信息
            VidSts vidSource = new VidSts();
            if (tokenInfo != null) {
                vidSource.setAccessKeyId(tokenInfo.getAccessKeyId());
                vidSource.setAccessKeySecret(tokenInfo.getAccessKeySecret());
                vidSource.setSecurityToken(tokenInfo.getSecurityToken());
                vidSource.setRegion(LittleVideoParamConfig.Player.REGION);

            }
            vidSource.setVid(videoId);
            vidSource.setTitle(getTitle());
            return vidSource;
        }

        @Override
        public String getUUID() {
            return videoId;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getTranscodeStatus() {
            return transcodeStatus;
        }

        public void setTranscodeStatus(String transcodeStatus) {
            this.transcodeStatus = transcodeStatus;
        }

        public String getSnapshotStatus() {
            return snapshotStatus;
        }

        public void setSnapshotStatus(String snapshotStatus) {
            this.snapshotStatus = snapshotStatus;
        }

        public String getCensorStatus() {
            return censorStatus;
        }

        public void setCensorStatus(String censorStatus) {
            this.censorStatus = censorStatus;
        }

        public String getNarrowTranscodeStatus() {
            return narrowTranscodeStatus;
        }

        public void setNarrowTranscodeStatus(String narrowTranscodeStatus) {
            this.narrowTranscodeStatus = narrowTranscodeStatus;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.videoId);
            dest.writeInt(this.duration);
            dest.writeString(this.transcodeStatus);
            dest.writeString(this.snapshotStatus);
            dest.writeString(this.censorStatus);
            dest.writeString(this.narrowTranscodeStatus);
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.description);
            dest.writeString(this.coverUrl);
            dest.writeString(this.creationTime);
            dest.writeString(this.status);
            dest.writeString(this.firstFrameUrl);
            dest.writeInt(this.size);
            dest.writeInt(this.cateId);
            dest.writeString(this.cateName);
            dest.writeString(this.tags);
            dest.writeString(this.shareUrl);
            dest.writeParcelable(this.user, flags);
        }

        protected VideoListBean(Parcel in) {
            this.videoId = in.readString();
            this.duration = in.readInt();
            this.transcodeStatus = in.readString();
            this.snapshotStatus = in.readString();
            this.censorStatus = in.readString();
            this.narrowTranscodeStatus = in.readString();
            this.id = in.readString();
            this.title = in.readString();
            this.description = in.readString();
            this.coverUrl = in.readString();
            this.creationTime = in.readString();
            this.status = in.readString();
            this.firstFrameUrl = in.readString();
            this.size = in.readInt();
            this.cateId = in.readInt();
            this.cateName = in.readString();
            this.tags = in.readString();
            this.shareUrl = in.readString();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
        }

        public static final Creator<VideoListBean> CREATOR = new Creator<VideoListBean>() {
            @Override
            public VideoListBean createFromParcel(Parcel source) {
                return new VideoListBean(source);
            }

            @Override
            public VideoListBean[] newArray(int size) {
                return new VideoListBean[size];
            }
        };
    }
}

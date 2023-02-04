package com.aliyun.apsara.alivclittlevideo.view.video;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.aliyun.apsara.alivclittlevideo.view.video.videolist.IVideoSourceModel;
import com.aliyun.player.source.UrlSource;
import com.aliyun.player.source.VidSts;

/**
 * 视频信息基类
 */
public abstract class BaseVideoSourceModel implements IVideoSourceModel {

    @Override
    public UrlSource getUrlSource() {
        return null;
    }

    @Override
    public VidSts getVidStsSource() {
        return null;
    }
    @Override
    public String getFirstFrame() {
        if (!TextUtils.isEmpty(firstFrameUrl)) {
            return firstFrameUrl;
        } else {
            return coverUrl;
        }
    }
    /**
     * id
     */
    protected String id = "";

    /**
     * 标题
     */
    protected String title = "";
    /**
     * 描述
     */
    protected String description = "";

    /**
     * 封面url
     */
    protected String coverUrl = "";
    /**
     * 创建时间
     */
    protected String creationTime = "";
    /**
     * 状态
     */
    protected String status = "";
    /**
     * 首帧地址
     */
    protected String firstFrameUrl = "";
    /**
     * 大小
     */
    protected int size = 0;
    /**
     * 分类id
     */
    protected int cateId = 0;
    /**
     * 分类名称
     */
    protected String cateName = "";
    /**
     * 标签
     */
    protected String tags = "";

    /**
     * 分享链接
     */
    protected String shareUrl = "";

    /**
     * 视频作者
     */

    protected UserBean user;

    public int getId() {
        int i = 0;
        try {
            i = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;

    }

    public void setId(int id) {
        this.id = id+"";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstFrameUrl() {
        return firstFrameUrl;
    }

    public void setFirstFrameUrl(String firstFrameUrl) {
        this.firstFrameUrl = firstFrameUrl;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    public static class UserBean implements Parcelable {

        private String userId;
        private String userName;
        private String avatarUrl;

        public UserBean(String userId, String nickName, String avatarUrl) {
            this.userId = userId;
            this.userName = nickName;
            this.avatarUrl = avatarUrl;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userId);
            dest.writeString(this.userName);
            dest.writeString(this.avatarUrl);
        }

        public UserBean() {}

        protected UserBean(Parcel in) {
            this.userId = in.readString();
            this.userName = in.readString();
            this.avatarUrl = in.readString();
        }

        public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel source) {return new UserBean(source);}

            @Override
            public UserBean[] newArray(int size) {return new UserBean[size];}
        };
    }
}

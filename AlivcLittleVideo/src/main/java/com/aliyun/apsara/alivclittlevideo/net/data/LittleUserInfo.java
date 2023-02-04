package com.aliyun.apsara.alivclittlevideo.net.data;

/**
 * 用户信息 javaBean
 *
 * @author xlx
 */
public class LittleUserInfo {

    private String id;
    private String userId;
    private String nickName;
    private String avatarUrl;
    private String gmtCreate;
    private String gmtModified;
    private String token;

    public String getId() { return id;}

    public void setId(String id) { this.id = id;}

    public String getUserId() { return userId;}

    public void setUserId(String userId) { this.userId = userId;}

    public String getNickName() { return nickName;}

    public void setNickName(String nickName) { this.nickName = nickName;}

    public String getAvatarUrl() { return avatarUrl;}

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl;}

    public String getGmtCreate() { return gmtCreate;}

    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate;}

    public String getGmtModified() { return gmtModified;}

    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified;}

    public String getToken() { return token;}

    public void setToken(String token) { this.token = token;}

    @Override
    public String toString() {
        return "LittleUserInfo{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            ", nickName='" + nickName + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", gmtCreate='" + gmtCreate + '\'' +
            ", gmtModified='" + gmtModified + '\'' +
            ", token='" + token + '\'' +
            '}';
    }
}

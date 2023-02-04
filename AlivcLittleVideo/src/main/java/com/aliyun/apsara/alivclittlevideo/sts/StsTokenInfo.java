package com.aliyun.apsara.alivclittlevideo.sts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Akira on 2018/6/2.
 */

public class StsTokenInfo implements Parcelable {
    private String accessKeySecret;
    private String securityToken;
    private String expiration;
    private String accessKeyId;

    protected StsTokenInfo(Parcel in) {
        accessKeySecret = in.readString();
        securityToken = in.readString();
        expiration = in.readString();
        accessKeyId = in.readString();
    }
    public StsTokenInfo(){

    }
    public StsTokenInfo(String accessKeySecret, String securityToken, String expiration, String accessKeyId) {
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.expiration = expiration;
        this.accessKeyId = accessKeyId;
    }

    public static final Creator<StsTokenInfo> CREATOR = new Creator<StsTokenInfo>() {
        @Override
        public StsTokenInfo createFromParcel(Parcel in) {
            return new StsTokenInfo(in);
        }

        @Override
        public StsTokenInfo[] newArray(int size) {
            return new StsTokenInfo[size];
        }
    };

    @Override
    public String toString() {
        return "StsTokenInfo{" +
                "AccessKeySecret='" + accessKeySecret + '\'' +
                ", SecurityToken='" + securityToken + '\'' +
                ", Expiration='" + expiration + '\'' +
                ", AccessKeyId='" + accessKeyId + '\'' +
                '}';
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        expiration = expiration;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessKeySecret);
        dest.writeString(securityToken);
        dest.writeString(expiration);
        dest.writeString(accessKeyId);
    }
}

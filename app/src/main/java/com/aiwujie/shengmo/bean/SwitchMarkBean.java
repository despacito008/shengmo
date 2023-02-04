package com.aiwujie.shengmo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2019/7/5.
 */

@Entity
public class SwitchMarkBean {

    @Id
    long uid;
    String user_id;
    String user_name;
    String password;
    String headimage;
    String t_sign;

    String sex;
    String sexual;
    String url_token;

    public String getUrl_token() {
        return url_token;
    }

    public void setUrl_token(String url_token) {
        this.url_token = url_token;
    }

    public String getSexual() {
        return this.sexual;
    }
    public void setSexual(String sexual) {
        this.sexual = sexual;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getT_sign() {
        return t_sign;
    }

    public void setT_sign(String t_sign) {
        this.t_sign = t_sign;
    }

    public String getHeadimage() {
        return this.headimage;
    }
    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public long getUid() {
        return this.uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
    }
    @Generated(hash = 449156752)
    public SwitchMarkBean(long uid, String user_id, String user_name,
            String password, String headimage, String t_sign, String sex,
            String sexual, String url_token) {
        this.uid = uid;
        this.user_id = user_id;
        this.user_name = user_name;
        this.password = password;
        this.headimage = headimage;
        this.t_sign = t_sign;
        this.sex = sex;
        this.sexual = sexual;
        this.url_token = url_token;
    }

    @Generated(hash = 34391472)
    public SwitchMarkBean() {
    }



}

package com.aiwujie.shengmo.eventbus;

/**
 * Created by Administrator on 2019/6/15.
 * 地图页筛选   -----model
 */

@Deprecated
public class mapEvent {
    private String onlinestate;
    private String realname;
    private String age;
    private String sex;
    private String sexual;
    private String role;
    private String culture;
    private String monthly;

    public mapEvent(String onlinestate, String realname, String age, String sex, String sexual, String role, String culture, String monthly) {
        this.onlinestate = onlinestate;
        this.realname = realname;
        this.age = age;
        this.sex = sex;
        this.sexual = sexual;
        this.role = role;
        this.culture = culture;
        this.monthly = monthly;
    }

    public String getOnlinestate() {
        return onlinestate;
    }

    public void setOnlinestate(String onlinestate) {
        this.onlinestate = onlinestate;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexual() {
        return sexual;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }
}

package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2016/12/25.
 */
public class FilterData {
    private String onlinestate;
    private String realname;
    private String age;
    private String sex;
    private String sexual;
    private String role;
    private String culture;
    private String income;
    private String upxzya;

    public FilterData(String onlinestate, String realname, String age, String sex, String sexual, String role, String culture, String income,String upxzya) {
        this.onlinestate = onlinestate;
        this.realname = realname;
        this.age = age;
        this.sex = sex;
        this.sexual = sexual;
        this.role = role;
        this.culture = culture;
        this.income = income;
        this.upxzya=upxzya;
    }

    public String getUpxzya() {
        return upxzya;
    }

    public void setUpxzya(String upxzya) {
        this.upxzya = upxzya;
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

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}

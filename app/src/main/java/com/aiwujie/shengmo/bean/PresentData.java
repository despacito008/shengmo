package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/6/20.
 */

public class PresentData {
    private String name;
    private String money;
    private int iconRes;
    private int psid;

    public PresentData(String name, String money, int iconRes,int psid) {
        this.name = name;
        this.money = money;
        this.iconRes = iconRes;
        this.psid=psid;
    }

    public int getPsid() {
        return psid;
    }

    public void setPsid(int psid) {
        this.psid = psid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}

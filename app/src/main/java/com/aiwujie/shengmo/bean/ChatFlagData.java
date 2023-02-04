package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2018/1/23.
 */

public class ChatFlagData {
    private String filiation;
    private String vip;
    private String vipannual;
    private String svip;
    private String svipannual;
    private String volunteer;
    private String admin;
    private String bkvip;
    private String blvip;

    public ChatFlagData(String filiation, String vip, String vipannual, String svip, String svipannual, String volunteer, String admin,String bkvip,String blvip) {
        this.filiation = filiation;
        this.vip = vip;
        this.vipannual = vipannual;
        this.svip = svip;
        this.svipannual = svipannual;
        this.volunteer = volunteer;
        this.admin = admin;
        this.bkvip=bkvip;
        this.blvip=blvip;
    }

    public String getBkvip() {
        return bkvip;
    }

    public void setBkvip(String bkvip) {
        this.bkvip = bkvip;
    }

    public String getBlvip() {
        return blvip;
    }

    public void setBlvip(String blvip) {
        this.blvip = blvip;
    }

    public String getFiliation() {
        return filiation;
    }

    public void setFiliation(String filiation) {
        this.filiation = filiation;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getVipannual() {
        return vipannual;
    }

    public void setVipannual(String vipannual) {
        this.vipannual = vipannual;
    }

    public String getSvip() {
        return svip;
    }

    public void setSvip(String svip) {
        this.svip = svip;
    }

    public String getSvipannual() {
        return svipannual;
    }

    public void setSvipannual(String svipannual) {
        this.svipannual = svipannual;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}

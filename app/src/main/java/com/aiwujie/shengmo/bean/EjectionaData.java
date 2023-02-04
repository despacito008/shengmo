package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/1/9.
 */
public class EjectionaData {
    private String num;
    private String money;
    private String modou;
    private String yuanmodou;
    private String jinyuan;

    public EjectionaData(String num, String money, String modou, String yuanmodou, String jinyuan) {
        this.num = num;
        this.money = money;
        this.modou = modou;
        this.yuanmodou = yuanmodou;
        this.jinyuan = jinyuan;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getJinyuan() {
        return jinyuan;
    }

    public void setJinyuan(String jinyuan) {
        this.jinyuan = jinyuan;
    }

    public EjectionaData( ) {

    }

    public String getYuanmodou() {
        return yuanmodou;
    }

    public void setYuanmodou(String yuanmodou) {
        this.yuanmodou = yuanmodou;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getModou() {
        return modou;
    }

    public void setModou(String modou) {
        this.modou = modou;
    }
}

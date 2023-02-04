package com.aiwujie.shengmo.bean;

public class TimDynamicMessageBean {
    public static final String DYNAMIC = "dlaud";
    public static final String REWORD = "drew";
    public static final String PUSH = "dtopcard";
    public static final String AT = "dcat";
    public static final String COMMENT = "dcom";

    /**
     * costomMassageType : dlaud
     * num : 1
     * allnum : 1
     */

    private String costomMassageType;
    private int num;
    private int allnum;

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAllnum() {
        return allnum;
    }

    public void setAllnum(int allnum) {
        this.allnum = allnum;
    }
}

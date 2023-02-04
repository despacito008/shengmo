package com.aiwujie.shengmo.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class HighAuthDataBean   implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String name;
    public String desc;
    public String img;

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
    }

    public ArrayList<String> imgList =new ArrayList<>();


}

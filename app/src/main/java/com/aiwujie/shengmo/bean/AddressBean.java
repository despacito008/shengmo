package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2016/12/22.
 */
public class AddressBean {
    private double longitude;//经度
    private double latitude;//纬度
    private String title;//信息标题
    private String text;//信息内容
    private String province;//省
    private String city;//市
    public AddressBean(double lon, double lat, String title, String text,String province,String city){
        this.longitude = lon;
        this.latitude = lat;
        this.title = title;
        this.text = text;
        this.province=province;
        this.city=city;
    }



    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public String getTitle() {
        return title;
    }
    public String getText(){
        return text;
    }
}

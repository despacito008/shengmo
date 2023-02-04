package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/4/7.
 */

public class LocationEvent {
    String lat;
    String lng;
    String city;
    String address;

    public LocationEvent(String lat, String lng, String city, String address) {
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

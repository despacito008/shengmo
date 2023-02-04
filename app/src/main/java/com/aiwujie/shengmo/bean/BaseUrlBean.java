package com.aiwujie.shengmo.bean;

public class BaseUrlBean {

    /**
     * api_host : http://api.aiwujie.com.cn:8082/
     * image_host : http://image.aiwujie.com.cn/
     * test_api_host : http://testapi.aiwujie.net:8001/
     */

    private String api_host;
    private String image_host;
    private String test_api_host;
    private String pull_host;

    public String getPull_host() {
        return pull_host;
    }

    public void setPull_host(String pull_host) {
        this.pull_host = pull_host;
    }

    public String getApi_host() {
        return api_host;
    }

    public void setApi_host(String api_host) {
        this.api_host = api_host;
    }

    public String getImage_host() {
        return image_host;
    }

    public void setImage_host(String image_host) {
        this.image_host = image_host;
    }

    public String getTest_api_host() {
        return test_api_host;
    }

    public void setTest_api_host(String test_api_host) {
        this.test_api_host = test_api_host;
    }
}

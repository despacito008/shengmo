package com.aiwujie.shengmo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: WeChatOrderBean
 * @Author: xmf
 * @CreateDate: 2022/5/27 20:34
 * @Description:
 */
public class WeChatOrderBean {



    /**
     * appid : wx0392b14b6a6f023c
     * partnerid : 1438399502
     * prepayid : wx15165244494808a94447edd3b1cb4a0000
     * package : Sign=WXPay
     * noncestr : JxEwrVIVv1vShCGCokPQGGGe743AiZi5
     * timestamp : 1650012765
     * sign : F40D4610D82843258A2015680290DA47
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

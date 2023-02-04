package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/16.
 */

public class GroupIdsData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : ["175","181","18","5649","7033","8025","3456","3706","3064","9393","7709","8987","9434","10543","3168","11075","10748","12","7598","690","14571","4872","118","5342","40","1745","15279","91","15840","13311","1152","2357","16706","16753","8509","17390","17517","17338","18195","16775","18548","16631","18923","19609","11","19997","18670","18512","17577","20325","19216","22207","23287","22903","3370","12021","22982","24950","25442","14283","24038","25768","26187","26272","22079","26681","25771","26979","7559","27503","27559","28004","154","25441","27505","28681","28700","29294","29672","29869","28436","28575","30261","30184","31140","30047","31957","28357","32313","9950","8831","33444","32147","32066","34034","34481","1054","34939","35373","15241","36497","36692","36711","36982","36346","6472","37978","29167","37747","32654","37284","35240","40146","40173"]
     */

    private int retcode;
    private String msg;
    private List<String> data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}

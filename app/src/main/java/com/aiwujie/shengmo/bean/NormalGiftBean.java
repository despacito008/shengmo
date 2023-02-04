package com.aiwujie.shengmo.bean;

import java.util.List;

public class NormalGiftBean {


    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"goods":[{"gift_id":"10","gift_name":"棒棒糖","gift_beans":"2","gift_type":"0","gift_index":"10","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-31/20210831172609366.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191915444.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"11","gift_name":"狗粮","gift_beans":"6","gift_type":"0","gift_index":"11","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew02.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191955260.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"12","gift_name":"雪糕","gift_beans":"10","gift_type":"0","gift_index":"12","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew03.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192111747.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"13","gift_name":"黄瓜","gift_beans":"38","gift_type":"0","gift_index":"13","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew04.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192200874.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"15","gift_name":"香蕉","gift_beans":"88","gift_type":"0","gift_index":"15","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew06.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192258342.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"14","gift_name":"心心相印","gift_beans":"99","gift_type":"0","gift_index":"14","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew05.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192851833.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"16","gift_name":"口红","gift_beans":"123","gift_type":"0","gift_index":"16","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew07.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192417827.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"17","gift_name":"亲一个","gift_beans":"166","gift_type":"0","gift_index":"17","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew08.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930183720496.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"18","gift_name":"玫瑰花","gift_beans":"199","gift_type":"0","gift_index":"18","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew09.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192613891.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"21","gift_name":"黄金","gift_beans":"250","gift_type":"0","gift_index":"21","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew12.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192512413.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"19","gift_name":"眼罩","gift_beans":"520","gift_type":"0","gift_index":"19","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew10.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191757185.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"20","gift_name":"心灵束缚","gift_beans":"666","gift_type":"0","gift_index":"20","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew11.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191613555.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"28","gift_name":"666","gift_beans":"666","gift_type":"0","gift_index":"28","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew19.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191446248.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"22","gift_name":"拍之印","gift_beans":"777","gift_type":"0","gift_index":"22","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew13.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191348142.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"23","gift_name":"鞭之痕","gift_beans":"888","gift_type":"0","gift_index":"23","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew14.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191221757.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"24","gift_name":"一飞冲天","gift_beans":"999","gift_type":"0","gift_index":"24","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew15.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191128903.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"29","gift_name":"红酒","gift_beans":"999","gift_type":"0","gift_index":"29","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew20.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191005151.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"25","gift_name":"一生一世","gift_beans":"1314","gift_type":"0","gift_index":"25","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew16.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190854531.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"26","gift_name":"水晶高跟","gift_beans":"1666","gift_type":"0","gift_index":"26","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew17.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190659930.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"30","gift_name":"蛋糕","gift_beans":"1888","gift_type":"0","gift_index":"30","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew21.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190253588.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"27","gift_name":"恒之光","gift_beans":"1999","gift_type":"0","gift_index":"27","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew18.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190055939.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"31","gift_name":"钻戒","gift_beans":"2899","gift_type":"0","gift_index":"31","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew22.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930185837567.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"32","gift_name":"皇冠","gift_beans":"3899","gift_type":"0","gift_index":"32","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew23.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930185514980.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"33","gift_name":"跑车","gift_beans":"6888","gift_type":"0","gift_index":"33","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew24.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192725576.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"34","gift_name":"直升机","gift_beans":"9888","gift_type":"0","gift_index":"34","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew25.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930184316176.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"35","gift_name":"游轮","gift_beans":"52000","gift_type":"0","gift_index":"35","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew26.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930185226184.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"36","gift_name":"城堡","gift_beans":"99999","gift_type":"0","gift_index":"36","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew27.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190422409.json","gift_lottie_status":"1","gift_source":""}],"auspicious":[{"num":1,"name":"一心一意","gift_lottieurl":""},{"num":99,"name":"长长久久","gift_lottieurl":""},{"num":520,"name":"永远爱你","gift_lottieurl":""},{"num":888,"name":"恭喜发财","gift_lottieurl":""}],"auspicious_max_beans":50,"rich_beans":"193","knapsack":[{"num":"23","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew28.png","gift_id":"37","gift_lottieurl":"","gift_name":"幸运草","gift_beans":"0","gift_source":"签到礼物"},{"num":"25","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew29.png","gift_id":"38","gift_lottieurl":"","gift_name":"糖果","gift_beans":"0","gift_source":"签到礼物"},{"num":"24","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew30.png","gift_id":"39","gift_lottieurl":"","gift_name":"玩具狗","gift_beans":"0","gift_source":"签到礼物"},{"num":"6","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew31.png","gift_id":"40","gift_lottieurl":"","gift_name":"内内","gift_beans":"0","gift_source":"签到礼物"},{"num":"3","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew32.png","gift_id":"41","gift_lottieurl":"","gift_name":"TT","gift_beans":"0","gift_source":"签到礼物"}]}
     */

    private int retcode;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * goods : [{"gift_id":"10","gift_name":"棒棒糖","gift_beans":"2","gift_type":"0","gift_index":"10","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-31/20210831172609366.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191915444.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"11","gift_name":"狗粮","gift_beans":"6","gift_type":"0","gift_index":"11","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew02.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191955260.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"12","gift_name":"雪糕","gift_beans":"10","gift_type":"0","gift_index":"12","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew03.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192111747.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"13","gift_name":"黄瓜","gift_beans":"38","gift_type":"0","gift_index":"13","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew04.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192200874.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"15","gift_name":"香蕉","gift_beans":"88","gift_type":"0","gift_index":"15","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew06.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192258342.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"14","gift_name":"心心相印","gift_beans":"99","gift_type":"0","gift_index":"14","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew05.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192851833.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"16","gift_name":"口红","gift_beans":"123","gift_type":"0","gift_index":"16","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew07.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192417827.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"17","gift_name":"亲一个","gift_beans":"166","gift_type":"0","gift_index":"17","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew08.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930183720496.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"18","gift_name":"玫瑰花","gift_beans":"199","gift_type":"0","gift_index":"18","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew09.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192613891.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"21","gift_name":"黄金","gift_beans":"250","gift_type":"0","gift_index":"21","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew12.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192512413.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"19","gift_name":"眼罩","gift_beans":"520","gift_type":"0","gift_index":"19","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew10.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191757185.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"20","gift_name":"心灵束缚","gift_beans":"666","gift_type":"0","gift_index":"20","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew11.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191613555.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"28","gift_name":"666","gift_beans":"666","gift_type":"0","gift_index":"28","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew19.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191446248.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"22","gift_name":"拍之印","gift_beans":"777","gift_type":"0","gift_index":"22","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew13.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191348142.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"23","gift_name":"鞭之痕","gift_beans":"888","gift_type":"0","gift_index":"23","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew14.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191221757.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"24","gift_name":"一飞冲天","gift_beans":"999","gift_type":"0","gift_index":"24","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew15.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191128903.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"29","gift_name":"红酒","gift_beans":"999","gift_type":"0","gift_index":"29","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew20.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191005151.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"25","gift_name":"一生一世","gift_beans":"1314","gift_type":"0","gift_index":"25","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew16.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190854531.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"26","gift_name":"水晶高跟","gift_beans":"1666","gift_type":"0","gift_index":"26","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew17.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190659930.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"30","gift_name":"蛋糕","gift_beans":"1888","gift_type":"0","gift_index":"30","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew21.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190253588.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"27","gift_name":"恒之光","gift_beans":"1999","gift_type":"0","gift_index":"27","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew18.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190055939.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"31","gift_name":"钻戒","gift_beans":"2899","gift_type":"0","gift_index":"31","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew22.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930185837567.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"32","gift_name":"皇冠","gift_beans":"3899","gift_type":"0","gift_index":"32","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew23.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930185514980.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"33","gift_name":"跑车","gift_beans":"6888","gift_type":"0","gift_index":"33","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew24.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930192725576.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"34","gift_name":"直升机","gift_beans":"9888","gift_type":"0","gift_index":"34","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew25.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930184316176.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"35","gift_name":"游轮","gift_beans":"52000","gift_type":"0","gift_index":"35","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew26.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930185226184.json","gift_lottie_status":"1","gift_source":""},{"gift_id":"36","gift_name":"城堡","gift_beans":"99999","gift_type":"0","gift_index":"36","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew27.png","gift_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930190422409.json","gift_lottie_status":"1","gift_source":""}]
         * auspicious : [{"num":1,"name":"一心一意","gift_lottieurl":""},{"num":99,"name":"长长久久","gift_lottieurl":""},{"num":520,"name":"永远爱你","gift_lottieurl":""},{"num":888,"name":"恭喜发财","gift_lottieurl":""}]
         * auspicious_max_beans : 50
         * rich_beans : 193
         * knapsack : [{"num":"23","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew28.png","gift_id":"37","gift_lottieurl":"","gift_name":"幸运草","gift_beans":"0","gift_source":"签到礼物"},{"num":"25","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew29.png","gift_id":"38","gift_lottieurl":"","gift_name":"糖果","gift_beans":"0","gift_source":"签到礼物"},{"num":"24","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew30.png","gift_id":"39","gift_lottieurl":"","gift_name":"玩具狗","gift_beans":"0","gift_source":"签到礼物"},{"num":"6","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew31.png","gift_id":"40","gift_lottieurl":"","gift_name":"内内","gift_beans":"0","gift_source":"签到礼物"},{"num":"3","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew32.png","gift_id":"41","gift_lottieurl":"","gift_name":"TT","gift_beans":"0","gift_source":"签到礼物"}]
         */

        private int auspicious_max_beans;
        private String rich_beans;
        private List<GoodsBean> goods;
        private List<AuspiciousBean> auspicious;
        private List<KnapsackBean> knapsack;
        private List<BlindBoxBean> blindbox;
        private List<GoodsBean> fanclub_gift;

        public List<GoodsBean> getFanclub_gift() {
            return fanclub_gift;
        }

        public void setFanclub_gift(List<GoodsBean> fanclub_gift) {
            this.fanclub_gift = fanclub_gift;
        }

        public List<BlindBoxBean> getBlindbox() {
            return blindbox;
        }

        public void setBlindbox(List<BlindBoxBean> blindbox) {
            this.blindbox = blindbox;
        }

        public int getAuspicious_max_beans() {
            return auspicious_max_beans;
        }

        public void setAuspicious_max_beans(int auspicious_max_beans) {
            this.auspicious_max_beans = auspicious_max_beans;
        }

        public String getRich_beans() {
            return rich_beans;
        }

        public void setRich_beans(String rich_beans) {
            this.rich_beans = rich_beans;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public List<AuspiciousBean> getAuspicious() {
            return auspicious;
        }

        public void setAuspicious(List<AuspiciousBean> auspicious) {
            this.auspicious = auspicious;
        }

        public List<KnapsackBean> getKnapsack() {
            return knapsack;
        }

        public void setKnapsack(List<KnapsackBean> knapsack) {
            this.knapsack = knapsack;
        }

        public static class GoodsBean {
            /**
             * gift_id : 10
             * gift_name : 棒棒糖
             * gift_beans : 2
             * gift_type : 0
             * gift_index : 10
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/2021-08-31/20210831172609366.png
             * gift_status : 1
             * gift_lottieurl : http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-30/20210930191915444.json
             * gift_lottie_status : 1
             * gift_source :
             */

            private String gift_id;
            private String gift_name;
            private String gift_beans;
            private String gift_type;
            private String gift_index;
            private String gift_image;
            private String gift_status;
            private String gift_lottieurl;
            private String gift_lottie_status;
            private String gift_source;
            //  刷新背包礼物数量
            private String num;
            private String gift_svgaurl;

            public String getGift_svgaurl() {
                return gift_svgaurl;
            }

            public void setGift_svgaurl(String gift_svgaurl) {
                this.gift_svgaurl = gift_svgaurl;
            }

            public String getGift_id() {
                return gift_id;
            }

            public void setGift_id(String gift_id) {
                this.gift_id = gift_id;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_type() {
                return gift_type;
            }

            public void setGift_type(String gift_type) {
                this.gift_type = gift_type;
            }

            public String getGift_index() {
                return gift_index;
            }

            public void setGift_index(String gift_index) {
                this.gift_index = gift_index;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getGift_status() {
                return gift_status;
            }

            public void setGift_status(String gift_status) {
                this.gift_status = gift_status;
            }

            public String getGift_lottieurl() {
                return gift_lottieurl;
            }

            public void setGift_lottieurl(String gift_lottieurl) {
                this.gift_lottieurl = gift_lottieurl;
            }

            public String getGift_lottie_status() {
                return gift_lottie_status;
            }

            public void setGift_lottie_status(String gift_lottie_status) {
                this.gift_lottie_status = gift_lottie_status;
            }

            public String getGift_source() {
                return gift_source;
            }

            public void setGift_source(String gift_source) {
                this.gift_source = gift_source;
            }


            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }

        public static class AuspiciousBean {
            /**
             * num : 1
             * name : 一心一意
             * gift_lottieurl :
             */

            private int num;
            private String name;
            private String gift_lottieurl;

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getGift_lottieurl() {
                return gift_lottieurl;
            }

            public void setGift_lottieurl(String gift_lottieurl) {
                this.gift_lottieurl = gift_lottieurl;
            }
        }

        public static class KnapsackBean {
            /**
             * num : 23
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew28.png
             * gift_id : 37
             * gift_lottieurl :
             * gift_name : 幸运草
             * gift_beans : 0
             * gift_source : 签到礼物
             */

            private String num;
            private String gift_image;
            private String gift_id;
            private String gift_lottieurl;
            private String gift_name;
            private String gift_beans;
            private String gift_source;
            private String gift_svgaurl;

            public String getGift_svgaurl() {
                return gift_svgaurl;
            }

            public void setGift_svgaurl(String gift_svgaurl) {
                this.gift_svgaurl = gift_svgaurl;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getGift_id() {
                return gift_id;
            }

            public void setGift_id(String gift_id) {
                this.gift_id = gift_id;
            }

            public String getGift_lottieurl() {
                return gift_lottieurl;
            }

            public void setGift_lottieurl(String gift_lottieurl) {
                this.gift_lottieurl = gift_lottieurl;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_source() {
                return gift_source;
            }

            public void setGift_source(String gift_source) {
                this.gift_source = gift_source;
            }
        }

        public static class BlindBoxBean {
            private String id;
            private String blindbox_image;
            private String beans;
            private String blindbox_name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBlindbox_image() {
                return blindbox_image;
            }

            public void setBlindbox_image(String blindbox_image) {
                this.blindbox_image = blindbox_image;
            }

            public String getBeans() {
                return beans;
            }

            public void setBeans(String beans) {
                this.beans = beans;
            }

            public String getBlindbox_name() {
                return blindbox_name;
            }

            public void setBlindbox_name(String blindbox_name) {
                this.blindbox_name = blindbox_name;
            }
        }
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: DefaultGroupBean
 * @Author: xmf
 * @CreateDate: 2022/6/7 14:35
 * @Description:
 */
public class DefaultGroupBean {
    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"gid":"10000021","groupname":"最美群屌爆","group_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-17/20170117203859655.jpg","introduce":"你mins你明明倪敏明宫ing哦尼过敏","member":"3","distance":"0.00","state":"3"},{"gid":"10000025","groupname":"嘻嘻哈哈","group_pic":"http://59.110.28.150:888/","introduce":"啥玩意","member":"2","distance":"0.10","state":-1},{"gid":"10000026","groupname":"了了了了了了了了了了了了了了了","group_pic":"http://59.110.28.150:888/","introduce":"了了了了了了了了了了了","member":"1","distance":"685.52","state":-1},{"gid":"10000024","groupname":"还是挺好的自建卢建奎","group_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-17/20170117193215873.jpg","introduce":"123老累了无图回家路途天童年卢建奎了哦哦哦卢建奎快乐摩羯旅途兔兔卢建奎了卢建奎卢建奎卢建奎卢建奎卢建奎卢建奎卢卡咯多头木木木OK","member":"2","distance":2323,"state":"1"}]
     */

    private int retcode;
    private String msg;
    private List<NormalGroupBean> data;

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

    public List<NormalGroupBean> getData() {
        return data;
    }

    public void setData(List<NormalGroupBean> data) {
        this.data = data;
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: CreateGroupBean
 * @Author: xmf
 * @CreateDate: 2022/6/7 12:20
 * @Description:
 */
public class CreateGroupBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"fangroup":[{"uid":"407921","gid":"10004887","new_group_uid":"407921","new_group_nickname":"测试ok棒","new_group_head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-27/20220427183102410.jpg","group_num":"501742","groupname":"测试ok棒的粉丝团","group_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-06-07/20220607103604904.jpg","introduce":"欢迎加入测试ok棒的粉丝团交流群1","member":"2","distance":12230,"admin_last_login_time":"0","new_change_group_uid_time":"1970-01-01","new_change_group_uid_time_order":"0","is_claim":0}],"group":[]}
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
        private List<NormalGroupBean> fangroup;
        private List<NormalGroupBean> group;

        public List<NormalGroupBean> getFangroup() {
            return fangroup;
        }

        public void setFangroup(List<NormalGroupBean> fangroup) {
            this.fangroup = fangroup;
        }

        public List<NormalGroupBean> getGroup() {
            return group;
        }

        public void setGroup(List<NormalGroupBean> group) {
            this.group = group;
        }


    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/24.
 */

public class Atbean {


    List<DataBean> dataBean;

    public List<DataBean> getDataBean() {
        return dataBean;
    }

    public void setDataBean(List<DataBean> dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean{
        String uid;
        String nickname;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}

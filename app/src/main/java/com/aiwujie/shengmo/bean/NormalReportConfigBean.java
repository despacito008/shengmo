package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @author: xmf
 * @date: 2022/6/14 15:59
 * @desc:
 */
public class NormalReportConfigBean {

    /**
     * retcode : 2000
     * msg : 成功！
     * data : {"option":["广告骚扰","政治敏感","发布色情内容","涉毒","其他原因"],"image":"0"}
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
         * option : ["广告骚扰","政治敏感","发布色情内容","涉毒","其他原因"]
         * image : 0
         */

        private String image;
        private List<String> option;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<String> getOption() {
            return option;
        }

        public void setOption(List<String> option) {
            this.option = option;
        }
    }
}

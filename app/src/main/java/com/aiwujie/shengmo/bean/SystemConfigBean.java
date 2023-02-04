package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: SystemCofigBean
 * @Author: xmf
 * @CreateDate: 2022/5/9 9:34
 * @Description:
 */
public class SystemConfigBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"liveWarningConfig":["请勿穿着暴露","请不要有不雅动作","请文明用语","请注意聊天内容","请勿推销","请勿抽烟","警告1次","警告2次","警告3次"]}
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
        private List<String> liveWarningConfig;

        public List<String> getLiveWarningConfig() {
            return liveWarningConfig;
        }

        public void setLiveWarningConfig(List<String> liveWarningConfig) {
            this.liveWarningConfig = liveWarningConfig;
        }
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: signAnchorInfoBean
 * @Author: xmf
 * @CreateDate: 2022/5/13 16:42
 * @Description:
 */
public class SignAnchorInfoBean {

    /**
     * retcode : 2000
     * msg : 操作成功！
     * data : {"wallet_receive":"4054234","invite_ratio":40,"beans_val":"162169.36","id_name":"13589658886","key_text":"总资产：4054234魔豆\n提点：   40%   提现金额：162169.36元\n支付宝：13589658886","key_words":["4054234","40%","162169.36","13589658886"]}
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
         * wallet_receive : 4054234
         * invite_ratio : 40
         * beans_val : 162169.36
         * id_name : 13589658886
         * key_text : 总资产：4054234魔豆
         提点：   40%   提现金额：162169.36元
         支付宝：13589658886
         * key_words : ["4054234","40%","162169.36","13589658886"]
         */

        private String wallet_receive;
        private int invite_ratio;
        private String beans_val;
        private String id_name;
        private String key_text;
        private List<String> key_words;

        public String getWallet_receive() {
            return wallet_receive;
        }

        public void setWallet_receive(String wallet_receive) {
            this.wallet_receive = wallet_receive;
        }

        public int getInvite_ratio() {
            return invite_ratio;
        }

        public void setInvite_ratio(int invite_ratio) {
            this.invite_ratio = invite_ratio;
        }

        public String getBeans_val() {
            return beans_val;
        }

        public void setBeans_val(String beans_val) {
            this.beans_val = beans_val;
        }

        public String getId_name() {
            return id_name;
        }

        public void setId_name(String id_name) {
            this.id_name = id_name;
        }

        public String getKey_text() {
            return key_text;
        }

        public void setKey_text(String key_text) {
            this.key_text = key_text;
        }

        public List<String> getKey_words() {
            return key_words;
        }

        public void setKey_words(List<String> key_words) {
            this.key_words = key_words;
        }
    }
}

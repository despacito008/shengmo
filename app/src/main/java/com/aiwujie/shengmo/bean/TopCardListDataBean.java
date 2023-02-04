package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/26.
 */

public class TopCardListDataBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"addtime":"58分钟前","nickname":"李等等\"","fnickname":"李等等\"","uid":"85712","fuid":"85712"},{"addtime":"58分钟前","nickname":"渣男阁网骗","fnickname":"渣男阁网骗","uid":"321864","fuid":"321864"},{"addtime":"1小时前","nickname":"渣男阁网骗","fnickname":"渣男阁网骗","uid":"321864","fuid":"321864"},{"addtime":"1小时前","nickname":"渣男阁网骗","fnickname":"渣男阁网骗","uid":"321864","fuid":"321864"},{"addtime":"6小时前","nickname":"科马嘉尔","fnickname":"科马嘉尔","uid":"410155","fuid":"410155"},{"addtime":"7小时前","nickname":"绳知遇伐(殿下)","fnickname":"绳知遇伐(殿下)","uid":"73626","fuid":"73626"},{"addtime":"7小时前","nickname":"绳知遇伐(殿下)","fnickname":"绳知遇伐(殿下)","uid":"73626","fuid":"73626"},{"addtime":"7小时前","nickname":"吾辈","fnickname":"吾辈","uid":"405423","fuid":"405423"},{"addtime":"8小时前","nickname":"吾辈","fnickname":"吾辈","uid":"405423","fuid":"405423"},{"addtime":"8小时前","nickname":"绳知遇伐(殿下)","fnickname":"绳知遇伐(殿下)","uid":"73626","fuid":"73626"},{"addtime":"9小时前","nickname":"缘_罪","fnickname":"❤丝怡❤","uid":"96058","fuid":"28700"},{"addtime":"9小时前","nickname":"科马嘉尔","fnickname":"科马嘉尔","uid":"410155","fuid":"410155"},{"addtime":"12小时前","nickname":"缘_罪","fnickname":"❤丝怡❤","uid":"96058","fuid":"28700"},{"addtime":"12小时前","nickname":"圣魔红娘服务","fnickname":"未栀","uid":"35240","fuid":"298515"},{"addtime":"12小时前","nickname":"圣魔红娘服务","fnickname":"纵横七海","uid":"35240","fuid":"340513"},{"addtime":"12小时前","nickname":"强子","fnickname":"强子","uid":"18","fuid":"18"},{"addtime":"13小时前","nickname":"客服小姐姐","fnickname":"司令ZOEYCHIU","uid":"51512","fuid":"304518"},{"addtime":"14小时前","nickname":"NC终结者","fnickname":"深渊徘徊者","uid":"351847","fuid":"122125"},{"addtime":"15小时前","nickname":"圣魔红娘服务","fnickname":"GuardianAngel","uid":"35240","fuid":"351813"},{"addtime":"15小时前","nickname":"客服小姐姐","fnickname":"忆＆小天","uid":"51512","fuid":"412211"}]
     */

    private int retcode;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addtime : 58分钟前
         * nickname : 李等等"
         * fnickname : 李等等"
         * uid : 85712
         * fuid : 85712
         */

        private String addtime;
        private String nickname;
        private String fnickname;
        private String uid;
        private String fuid;
        private String head_pic;
        private String fhead_pic;

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getFhead_pic() {
            return fhead_pic;
        }

        public void setFhead_pic(String fhead_pic) {
            this.fhead_pic = fhead_pic;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getFnickname() {
            return fnickname;
        }

        public void setFnickname(String fnickname) {
            this.fnickname = fnickname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }
    }
}

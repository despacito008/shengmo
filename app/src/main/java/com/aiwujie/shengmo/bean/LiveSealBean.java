package com.aiwujie.shengmo.bean;

import java.util.List;

public class LiveSealBean {

    /**
     * retcode : 2000
     * msg : 查询成功
     * data : {"has_more":"0","list":[{"id":"109","uid":"703474","type":"montior_warning","reasontext":"请勿穿着暴露","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-25 20:44:48","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"108","uid":"703474","type":"montior_warning","reasontext":"请文明用语","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-23 14:06:35","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"82","uid":"703474","type":"montior_warning","reasontext":"请勿穿着暴露","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:19:04","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"78","uid":"703474","type":"montior_warning","reasontext":"请勿抽烟:语音","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:16:56","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"75","uid":"703474","type":"montior_warning","reasontext":"请勿推销:预约一人","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:12:06","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"74","uid":"703474","type":"montior_warning","reasontext":"请文明用语","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:11:42","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"63","uid":"703474","type":"montior_warning","reasontext":"请不要有不雅动作:语音","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:05:09","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"58","uid":"703474","type":"montior_warning","reasontext":"请勿推销","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:02:51","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"54","uid":"703474","type":"montior_warning","reasontext":"请不要有不雅动作","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 09:58:39","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"}]}
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
         * has_more : 0
         * list : [{"id":"109","uid":"703474","type":"montior_warning","reasontext":"请勿穿着暴露","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-25 20:44:48","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"108","uid":"703474","type":"montior_warning","reasontext":"请文明用语","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-23 14:06:35","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"82","uid":"703474","type":"montior_warning","reasontext":"请勿穿着暴露","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:19:04","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"78","uid":"703474","type":"montior_warning","reasontext":"请勿抽烟:语音","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:16:56","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"75","uid":"703474","type":"montior_warning","reasontext":"请勿推销:预约一人","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:12:06","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"74","uid":"703474","type":"montior_warning","reasontext":"请文明用语","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:11:42","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"63","uid":"703474","type":"montior_warning","reasontext":"请不要有不雅动作:语音","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:05:09","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"58","uid":"703474","type":"montior_warning","reasontext":"请勿推销","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 10:02:51","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"},{"id":"54","uid":"703474","type":"montior_warning","reasontext":"请不要有不雅动作","blockingalong":"0","prohibition_endtime":"0","cuid":"402279","username":"圣魔iOS～工程","addtime":"2021-11-16 09:58:39","nickname":"圣魔iOS～工程","tonickname":"测试哈哈哈哈哈哈哈"}]
         */

        private String has_more;
        private List<ListBean> list;

        public String getHas_more() {
            return has_more;
        }

        public void setHas_more(String has_more) {
            this.has_more = has_more;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 109
             * uid : 703474
             * type : montior_warning
             * reasontext : 请勿穿着暴露
             * blockingalong : 0
             * prohibition_endtime : 0
             * cuid : 402279
             * username : 圣魔iOS～工程
             * addtime : 2021-11-25 20:44:48
             * nickname : 圣魔iOS～工程
             * tonickname : 测试哈哈哈哈哈哈哈
             */

            private String id;
            private String uid;
            private String type;
            private String reasontext;
            private String blockingalong;
            private String prohibition_endtime;
            private String cuid;
            private String username;
            private String addtime;
            private String nickname;
            private String tonickname;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getReasontext() {
                return reasontext;
            }

            public void setReasontext(String reasontext) {
                this.reasontext = reasontext;
            }

            public String getBlockingalong() {
                return blockingalong;
            }

            public void setBlockingalong(String blockingalong) {
                this.blockingalong = blockingalong;
            }

            public String getProhibition_endtime() {
                return prohibition_endtime;
            }

            public void setProhibition_endtime(String prohibition_endtime) {
                this.prohibition_endtime = prohibition_endtime;
            }

            public String getCuid() {
                return cuid;
            }

            public void setCuid(String cuid) {
                this.cuid = cuid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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

            public String getTonickname() {
                return tonickname;
            }

            public void setTonickname(String tonickname) {
                this.tonickname = tonickname;
            }
        }
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/11.
 */

public class Group_liwulistbean {


    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"num":"1","datas":[{"uid":"402624","fuid":"402624","nickname":"呀yyyyyy","fnickname":"呀yyyyyy","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","type":"12","num":"1","addtime":"1562843351","state":"1","gettime":"1562843360"},{"uid":"402624","fuid":"394585","nickname":"呀yyyyyy","fnickname":"wardadd","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-11/20190711183911525.jpg","type":"12","num":"1","addtime":"1562843351","state":"1","gettime":"1562843492"}]}
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
         * num : 1
         * datas : [{"uid":"402624","fuid":"402624","nickname":"呀yyyyyy","fnickname":"呀yyyyyy","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","type":"12","num":"1","addtime":"1562843351","state":"1","gettime":"1562843360"},{"uid":"402624","fuid":"394585","nickname":"呀yyyyyy","fnickname":"wardadd","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg","fhead_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-11/20190711183911525.jpg","type":"12","num":"1","addtime":"1562843351","state":"1","gettime":"1562843492"}]
         */

        private String num;
        private List<DatasBean> datas;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean {
            /**
             * uid : 402624
             * fuid : 402624
             * nickname : 呀yyyyyy
             * fnickname : 呀yyyyyy
             * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg
             * fhead_pic : http://hao.shengmo.org:888/Uploads/Picture/2019-06-24/2019062410365665.jpg
             * type : 12
             * num : 1
             * addtime : 1562843351
             * state : 1
             * gettime : 1562843360
             */

            private String uid;
            private String fuid;
            private String nickname;
            private String fnickname;
            private String head_pic;
            private String fhead_pic;
            private String type;
            private String num;
            private String addtime;
            private String state;
            private String gettime;


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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getGettime() {
                return gettime;
            }

            public void setGettime(String gettime) {
                this.gettime = gettime;
            }
        }
    }
}

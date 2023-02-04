package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/10.
 */

public class RedbagshouGroupBean {


    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"back":null,"num":"0","datas":[{"uid":"402624","fuid":"402624","nickname":"官方测试号","fnickname":"官方测试号","head_pic":"http://59.110.28.150:888/Uploads/refuse.jpg","fhead_pic":"http://59.110.28.150:888/Uploads/refuse.jpg","num":"10","addtime":"1563883850","state":"0","gettime":"1563883854"}]}
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
         * back : null
         * num : 0
         * datas : [{"uid":"402624","fuid":"402624","nickname":"官方测试号","fnickname":"官方测试号","head_pic":"http://59.110.28.150:888/Uploads/refuse.jpg","fhead_pic":"http://59.110.28.150:888/Uploads/refuse.jpg","num":"10","addtime":"1563883850","state":"0","gettime":"1563883854"}]
         */

        private Object back;
        private String num;
        private String nums;
        private List<DatasBean> datas;
        private String tnum;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTnum() {
            return tnum;
        }

        public void setTnum(String tnum) {
            this.tnum = tnum;
        }

        public String getNums() {
            return nums;
        }

        public void setNums(String nums) {
            this.nums = nums;
        }

        public Object getBack() {
            return back;
        }

        public void setBack(Object back) {
            this.back = back;
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
             * nickname : 官方测试号
             * fnickname : 官方测试号
             * head_pic : http://59.110.28.150:888/Uploads/refuse.jpg
             * fhead_pic : http://59.110.28.150:888/Uploads/refuse.jpg
             * num : 10
             * addtime : 1563883850
             * state : 0
             * gettime : 1563883854
             */

            private String uid;
            private String fuid;
            private String nickname;
            private String fnickname;
            private String head_pic;
            private String fhead_pic;
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

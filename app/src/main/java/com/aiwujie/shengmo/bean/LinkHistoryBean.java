package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: LinkHistoryBean
 * @Author: xmf
 * @CreateDate: 2022/5/11 15:00
 * @Description:
 */
public class LinkHistoryBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"chattimes":"8","all_time_length":"11分","all_beans":"1170","list":[{"id":"88","uid":"703929","anchor_uid":"407943","type":"1","beans_current_count":"0","time_length":"6","status":"2","nickname":"uuuooo","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg","add_time":"2022-03-30 11:42:23","time_length_str":"6秒"},{"id":"87","uid":"703929","anchor_uid":"407943","type":"1","beans_current_count":"0","time_length":"6","status":"2","nickname":"uuuooo","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg","add_time":"2022-03-30 11:41:36","time_length_str":"6秒"},{"id":"86","uid":"703929","anchor_uid":"407943","type":"1","beans_current_count":"20","time_length":"285","status":"2","nickname":"uuuooo","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg","add_time":"2022-03-30 11:31:26","time_length_str":"4分45秒"},{"id":"81","uid":"250385","anchor_uid":"407943","type":"1","beans_current_count":"200","time_length":"18","status":"2","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","add_time":"2022-03-30 10:22:09","time_length_str":"18秒"},{"id":"68","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"200","time_length":"64","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:33:34","time_length_str":"1分4秒"},{"id":"67","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"350","time_length":"97","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:16:04","time_length_str":"1分37秒"},{"id":"66","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"0","time_length":"114","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:11:49","time_length_str":"1分54秒"},{"id":"64","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"400","time_length":"120","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:00:25","time_length_str":"2分"}]}
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
         * chattimes : 8
         * all_time_length : 11分
         * all_beans : 1170
         * list : [{"id":"88","uid":"703929","anchor_uid":"407943","type":"1","beans_current_count":"0","time_length":"6","status":"2","nickname":"uuuooo","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg","add_time":"2022-03-30 11:42:23","time_length_str":"6秒"},{"id":"87","uid":"703929","anchor_uid":"407943","type":"1","beans_current_count":"0","time_length":"6","status":"2","nickname":"uuuooo","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg","add_time":"2022-03-30 11:41:36","time_length_str":"6秒"},{"id":"86","uid":"703929","anchor_uid":"407943","type":"1","beans_current_count":"20","time_length":"285","status":"2","nickname":"uuuooo","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg","add_time":"2022-03-30 11:31:26","time_length_str":"4分45秒"},{"id":"81","uid":"250385","anchor_uid":"407943","type":"1","beans_current_count":"200","time_length":"18","status":"2","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","add_time":"2022-03-30 10:22:09","time_length_str":"18秒"},{"id":"68","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"200","time_length":"64","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:33:34","time_length_str":"1分4秒"},{"id":"67","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"350","time_length":"97","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:16:04","time_length_str":"1分37秒"},{"id":"66","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"0","time_length":"114","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:11:49","time_length_str":"1分54秒"},{"id":"64","uid":"402624","anchor_uid":"407943","type":"1","beans_current_count":"400","time_length":"120","status":"2","nickname":"魔徒","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg","add_time":"2022-03-11 17:00:25","time_length_str":"2分"}]
         */

        private String chattimes;
        private String all_time_length;
        private String all_beans;
        private List<ListBean> list;

        public String getChattimes() {
            return chattimes;
        }

        public void setChattimes(String chattimes) {
            this.chattimes = chattimes;
        }

        public String getAll_time_length() {
            return all_time_length;
        }

        public void setAll_time_length(String all_time_length) {
            this.all_time_length = all_time_length;
        }

        public String getAll_beans() {
            return all_beans;
        }

        public void setAll_beans(String all_beans) {
            this.all_beans = all_beans;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 88
             * uid : 703929
             * anchor_uid : 407943
             * type : 1
             * beans_current_count : 0
             * time_length : 6
             * status : 2
             * nickname : uuuooo
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2022-03-30/20220330112427459.jpg
             * add_time : 2022-03-30 11:42:23
             * time_length_str : 6秒
             */

            private String id;
            private String uid;
            private String anchor_uid;
            private String type;
            private String beans_current_count;
            private String time_length;
            private String status;
            private String nickname;
            private String head_pic;
            private String add_time;
            private String time_length_str;
            private String live_beans;

            public String getLive_beans() {
                return live_beans;
            }

            public void setLive_beans(String live_beans) {
                this.live_beans = live_beans;
            }

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

            public String getAnchor_uid() {
                return anchor_uid;
            }

            public void setAnchor_uid(String anchor_uid) {
                this.anchor_uid = anchor_uid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getBeans_current_count() {
                return beans_current_count;
            }

            public void setBeans_current_count(String beans_current_count) {
                this.beans_current_count = beans_current_count;
            }

            public String getTime_length() {
                return time_length;
            }

            public void setTime_length(String time_length) {
                this.time_length = time_length;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getTime_length_str() {
                return time_length_str;
            }

            public void setTime_length_str(String time_length_str) {
                this.time_length_str = time_length_str;
            }
        }
    }
}

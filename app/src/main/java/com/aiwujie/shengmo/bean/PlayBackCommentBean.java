package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: PlayBackCommentBean
 * @Author: xmf
 * @CreateDate: 2022/6/13 10:14
 * @Description:
 */
public class PlayBackCommentBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"comment_total":"1","comment_list":[{"user":{"uid":"703930","nickname":"黑色掩饰","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-01/20220401104347926.jpg","vip":"1","vipannual":"1","svip":"1","svipannual":"1","bkvip":"1","blvip":"0"},"comment":{"comment_id":"2","live_log_id":"4465","comment_uid":"703930","comment_content":"我是第一个评论的","comment_time":"2022-06-10 16:21:41"}}]}
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
         * comment_total : 1
         * comment_list : [{"user":{"uid":"703930","nickname":"黑色掩饰","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-01/20220401104347926.jpg","vip":"1","vipannual":"1","svip":"1","svipannual":"1","bkvip":"1","blvip":"0"},"comment":{"comment_id":"2","live_log_id":"4465","comment_uid":"703930","comment_content":"我是第一个评论的","comment_time":"2022-06-10 16:21:41"}}]
         */

        private String comment_total;
        private List<CommentListBean> comment_list;

        public String getComment_total() {
            return comment_total;
        }

        public void setComment_total(String comment_total) {
            this.comment_total = comment_total;
        }

        public List<CommentListBean> getComment_list() {
            return comment_list;
        }

        public void setComment_list(List<CommentListBean> comment_list) {
            this.comment_list = comment_list;
        }

        public static class CommentListBean {
            /**
             * user : {"uid":"703930","nickname":"黑色掩饰","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-01/20220401104347926.jpg","vip":"1","vipannual":"1","svip":"1","svipannual":"1","bkvip":"1","blvip":"0"}
             * comment : {"comment_id":"2","live_log_id":"4465","comment_uid":"703930","comment_content":"我是第一个评论的","comment_time":"2022-06-10 16:21:41"}
             */

            private UserBean user;
            private CommentBean comment;

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public CommentBean getComment() {
                return comment;
            }

            public void setComment(CommentBean comment) {
                this.comment = comment;
            }

            public static class UserBean {
                /**
                 * uid : 703930
                 * nickname : 黑色掩饰
                 * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2022-04-01/20220401104347926.jpg
                 * vip : 1
                 * vipannual : 1
                 * svip : 1
                 * svipannual : 1
                 * bkvip : 1
                 * blvip : 0
                 */

                private String uid;
                private String nickname;
                private String head_pic;
                private String vip;
                private String vipannual;
                private String svip;
                private String svipannual;
                private String bkvip;
                private String blvip;
                private String vip_type;

                public String getVip_type() {
                    return vip_type;
                }

                public void setVip_type(String vip_type) {
                    this.vip_type = vip_type;
                }

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

                public String getHead_pic() {
                    return head_pic;
                }

                public void setHead_pic(String head_pic) {
                    this.head_pic = head_pic;
                }

                public String getVip() {
                    return vip;
                }

                public void setVip(String vip) {
                    this.vip = vip;
                }

                public String getVipannual() {
                    return vipannual;
                }

                public void setVipannual(String vipannual) {
                    this.vipannual = vipannual;
                }

                public String getSvip() {
                    return svip;
                }

                public void setSvip(String svip) {
                    this.svip = svip;
                }

                public String getSvipannual() {
                    return svipannual;
                }

                public void setSvipannual(String svipannual) {
                    this.svipannual = svipannual;
                }

                public String getBkvip() {
                    return bkvip;
                }

                public void setBkvip(String bkvip) {
                    this.bkvip = bkvip;
                }

                public String getBlvip() {
                    return blvip;
                }

                public void setBlvip(String blvip) {
                    this.blvip = blvip;
                }
            }

            public static class CommentBean {
                /**
                 * comment_id : 2
                 * live_log_id : 4465
                 * comment_uid : 703930
                 * comment_content : 我是第一个评论的
                 * comment_time : 2022-06-10 16:21:41
                 */

                private String comment_id;
                private String live_log_id;
                private String comment_uid;
                private String comment_content;
                private String comment_time;

                public String getComment_id() {
                    return comment_id;
                }

                public void setComment_id(String comment_id) {
                    this.comment_id = comment_id;
                }

                public String getLive_log_id() {
                    return live_log_id;
                }

                public void setLive_log_id(String live_log_id) {
                    this.live_log_id = live_log_id;
                }

                public String getComment_uid() {
                    return comment_uid;
                }

                public void setComment_uid(String comment_uid) {
                    this.comment_uid = comment_uid;
                }

                public String getComment_content() {
                    return comment_content;
                }

                public void setComment_content(String comment_content) {
                    this.comment_content = comment_content;
                }

                public String getComment_time() {
                    return comment_time;
                }

                public void setComment_time(String comment_time) {
                    this.comment_time = comment_time;
                }
            }
        }
    }
}

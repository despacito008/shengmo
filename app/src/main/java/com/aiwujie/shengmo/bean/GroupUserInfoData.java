package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/20.
 */
public class GroupUserInfoData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"groupname":"群组：今生今世","group_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-20/2017012019271530.jpg","userarr":[{"id":"76","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-12/2017011220502321.jpg","nickname":"屌不屌"}]}
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
         * groupname : 群组：今生今世
         * group_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-20/2017012019271530.jpg
         * userarr : [{"id":"76","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-12/2017011220502321.jpg","nickname":"屌不屌"}]
         */

        private String groupname;
        private String group_pic;
        private List<UserarrBean> userarr;

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroup_pic() {
            return group_pic;
        }

        public void setGroup_pic(String group_pic) {
            this.group_pic = group_pic;
        }

        public List<UserarrBean> getUserarr() {
            return userarr;
        }

        public void setUserarr(List<UserarrBean> userarr) {
            this.userarr = userarr;
        }

        public static class UserarrBean {
            /**
             * id : 76
             * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-12/2017011220502321.jpg
             * nickname : 屌不屌
             */

            private String id;
            private String head_pic;
            private String nickname;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}

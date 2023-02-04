package com.aiwujie.shengmo.bean;

/**
 * @program: newshengmo
 * @description: 创建群组信息
 * @author: whl
 * @create: 2022-06-09 12:00
 **/
public class GroupChatInfoModel {

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

    public int retcode;
    public String msg;
    public DataBean data;

    public class DataBean {

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getImg_gid() {
            return img_gid;
        }

        public void setImg_gid(String img_gid) {
            this.img_gid = img_gid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String gid;
        public String img_gid;
        public String groupname;
    }

}

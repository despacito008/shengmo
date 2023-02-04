package com.aiwujie.shengmo.bean;

public class CheckUserTopBean  {

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
    public String  msg;
    public DataBean  data;


   public class  DataBean {
        public String getIs_top_user() {
            return is_top_user;
        }

        public void setIs_top_user(String is_top_user) {
            this.is_top_user = is_top_user;
        }

        public String getTop_status() {
            return top_status;
        }

        public void setTop_status(String top_status) {
            this.top_status = top_status;
        }

        public String getTop_id() {
            return top_id;
        }

        public void setTop_id(String top_id) {
            this.top_id = top_id;
        }

        public String is_top_user;
        public String top_status;
        public String top_id;

       public String getTop_overtime() {
           return top_overtime;
       }

       public void setTop_overtime(String top_overtime) {
           this.top_overtime = top_overtime;
       }

       public String top_overtime;
    }
}

package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @author：zq 2021/4/12 17:15
 * 邮箱：80776234@qq.com
 */

public class AccountDeviceData {

    private Integer retcode;
    private String msg;
    private Data data;

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String uid;
        private String mobile;
        private String email;
        private String sex;
        private String new_device_brand;
        private String new_device_version;
        private String new_device_token;
        private String new_device_appversion;
        private String openid;
        private String channel;
        private String new_login_time;
        private LocationInfo location_info;
        private List<DeviceInfo> device_info;
        private String accout_number;
        private String invite_nickname;
        private String invite_uid;
        private String invite_count;

        public String getInvite_count() {
            return invite_count;
        }

        public void setInvite_count(String invite_count) {
            this.invite_count = invite_count;
        }

        public String getInvite_nickname() {
            return invite_nickname;
        }

        public void setInvite_nickname(String invite_nickname) {
            this.invite_nickname = invite_nickname;
        }

        public String getInvite_uid() {
            return invite_uid;
        }

        public void setInvite_uid(String invite_uid) {
            this.invite_uid = invite_uid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNew_device_brand() {
            return new_device_brand;
        }

        public void setNew_device_brand(String new_device_brand) {
            this.new_device_brand = new_device_brand;
        }

        public String getNew_device_version() {
            return new_device_version;
        }

        public void setNew_device_version(String new_device_version) {
            this.new_device_version = new_device_version;
        }

        public String getNew_device_token() {
            return new_device_token;
        }

        public void setNew_device_token(String new_device_token) {
            this.new_device_token = new_device_token;
        }

        public String getNew_device_appversion() {
            return new_device_appversion;
        }

        public void setNew_device_appversion(String new_device_appversion) {
            this.new_device_appversion = new_device_appversion;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getNew_login_time() {
            return new_login_time;
        }

        public void setNew_login_time(String new_login_time) {
            this.new_login_time = new_login_time;
        }

        public LocationInfo getLocation_info() {
            return location_info;
        }

        public void setLocation_info(LocationInfo location_info) {
            this.location_info = location_info;
        }

        public List<DeviceInfo> getDevice_info() {
            return device_info;
        }

        public void setDevice_info(List<DeviceInfo> device_info) {
            this.device_info = device_info;
        }

        public String getAccout_number() {
            return accout_number;
        }

        public void setAccout_number(String accout_number) {
            this.accout_number = accout_number;
        }

        public  class DeviceInfo{
            private String device;
            private String status;
            private List<DeviceUser> device_user;

            public String getDevice() {
                return device;
            }

            public void setDevice(String device) {
                this.device = device;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public List<DeviceUser> getDevice_user() {
                return device_user;
            }

            public void setDevice_user(List<DeviceUser> device_user) {
                this.device_user = device_user;
            }

            public  class DeviceUser {
                private String uid;
                private String sex;
                private String nickname;
                private String reg_time;
                private String new_login_time;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getReg_time() {
                    return reg_time;
                }

                public void setReg_time(String reg_time) {
                    this.reg_time = reg_time;
                }

                public String getNew_login_time() {
                    return new_login_time;
                }

                public void setNew_login_time(String new_login_time) {
                    this.new_login_time = new_login_time;
                }
            }
        }
    }
}


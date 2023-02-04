package com.aiwujie.shengmo.bean;

import java.util.List;

public class LotteryDrawGiftBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"config":{"beans":20,"button_left":{"type":1,"name":"抽1次","tip":"20魔豆","num":1,"beans":20},"button_right":{"type":2,"name":"抽10次","tip":"200魔豆","num":10,"beans":200}},"list":[{"gift_id":"10","gift_name":"棒棒糖","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png","gift_beans":"2","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2022-01-17/20220117100130553.json"}]}
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
         * config : {"beans":20,"button_left":{"type":1,"name":"抽1次","tip":"20魔豆","num":1,"beans":20},"button_right":{"type":2,"name":"抽10次","tip":"200魔豆","num":10,"beans":200}}
         * list : [{"gift_id":"10","gift_name":"棒棒糖","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png","gift_beans":"2","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2022-01-17/20220117100130553.json"}]
         */

        private ConfigBean config;
        private List<ListBean> list;

        public ConfigBean getConfig() {
            return config;
        }

        public void setConfig(ConfigBean config) {
            this.config = config;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ConfigBean {
            /**
             * beans : 20
             * button_left : {"type":1,"name":"抽1次","tip":"20魔豆","num":1,"beans":20}
             * button_right : {"type":2,"name":"抽10次","tip":"200魔豆","num":10,"beans":200}
             */

            private int beans;
            private String tip;
            private ButtonLeftBean button_left;
            private ButtonRightBean button_right;
            private String wallet;
            private String rule_text;

            public String getRule_text() {
                return rule_text;
            }

            public void setRule_text(String rule_text) {
                this.rule_text = rule_text;
            }

            public String getWallet() {
                return wallet;
            }

            public void setWallet(String wallet) {
                this.wallet = wallet;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }

            public int getBeans() {
                return beans;
            }

            public void setBeans(int beans) {
                this.beans = beans;
            }

            public ButtonLeftBean getButton_left() {
                return button_left;
            }

            public void setButton_left(ButtonLeftBean button_left) {
                this.button_left = button_left;
            }

            public ButtonRightBean getButton_right() {
                return button_right;
            }

            public void setButton_right(ButtonRightBean button_right) {
                this.button_right = button_right;
            }

            public static class ButtonLeftBean {
                /**
                 * type : 1
                 * name : 抽1次
                 * tip : 20魔豆
                 * num : 1
                 * beans : 20
                 */

                private int type;
                private String name;
                private String tip;
                private int num;
                private int beans;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTip() {
                    return tip;
                }

                public void setTip(String tip) {
                    this.tip = tip;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public int getBeans() {
                    return beans;
                }

                public void setBeans(int beans) {
                    this.beans = beans;
                }
            }

            public static class ButtonRightBean {
                /**
                 * type : 2
                 * name : 抽10次
                 * tip : 200魔豆
                 * num : 10
                 * beans : 200
                 */

                private int type;
                private String name;
                private String tip;
                private int num;
                private int beans;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTip() {
                    return tip;
                }

                public void setTip(String tip) {
                    this.tip = tip;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public int getBeans() {
                    return beans;
                }

                public void setBeans(int beans) {
                    this.beans = beans;
                }
            }
        }

        public static class ListBean {
            /**
             * gift_id : 10
             * gift_name : 棒棒糖
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png
             * gift_beans : 2
             * gift_lottieurl : http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2022-01-17/20220117100130553.json
             */

            private String gift_id;
            private String gift_name;
            private String gift_image;
            private String gift_beans;
            private String gift_lottieurl;

            public String getGift_id() {
                return gift_id;
            }

            public void setGift_id(String gift_id) {
                this.gift_id = gift_id;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_lottieurl() {
                return gift_lottieurl;
            }

            public void setGift_lottieurl(String gift_lottieurl) {
                this.gift_lottieurl = gift_lottieurl;
            }
        }
    }
}

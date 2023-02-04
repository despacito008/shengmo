package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @program: newshengmo
 * @description: 座驾详情
 * @author: whl
 * @create: 2022-05-25 11:18
 **/
public class CarInfoModel {


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



    public int retcode;
    public String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public DataBean data;


    public  class DataBean  {


        public String getAnimation_id() {
            return animation_id;
        }

        public void setAnimation_id(String animation_id) {
            this.animation_id = animation_id;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
        public String getJg() {
            return jg;
        }

        public void setJg(String jg) {
            this.jg = jg;
        }

        public String getAnimation() {
            return animation;
        }

        public void setAnimation(String animation) {
            this.animation = animation;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<DataInfoBean> getProduct() {
            return product;
        }

        public void setProduct(List<DataInfoBean> product) {
            this.product = product;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public   String animation_id;
        public   String cover;
        public   String name;
        public   String price;
        public   String animation;
        public   String title;
        public   List<DataInfoBean> product;
        public   String content;

        public String getCommonProblemUrl() {
            return commonProblemUrl;
        }

        public void setCommonProblemUrl(String commonProblemUrl) {
            this.commonProblemUrl = commonProblemUrl;
        }

        public   String commonProblemUrl;


        public   String jg;


        public  class DataInfoBean {
            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getJg() {
                return jg;
            }

            public void setJg(String jg) {
                this.jg = jg;
            }

            private String product_id;
            private String month;
            private String price;
            private String jg;

            public String getWallet() {
                return wallet;
            }

            public void setWallet(String wallet) {
                this.wallet = wallet;
            }

            private String wallet;

            public String getDiscount_desc() {
                return discount_desc;
            }

            public void setDiscount_desc(String discount_desc) {
                this.discount_desc = discount_desc;
            }

            private String discount_desc;
        }
    }
}

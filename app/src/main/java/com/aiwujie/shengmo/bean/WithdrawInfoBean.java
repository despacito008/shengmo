package com.aiwujie.shengmo.bean;

public class WithdrawInfoBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"profit":6,"proportion":0.4,"min":2000}
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
         * profit : 6
         * proportion : 0.4
         * min : 2000
         */

        private int profit;
        private double proportion;
        private int min;
        private String tips;
        private String cash_out;

        public String getCash_out() {
            return cash_out;
        }

        public void setCash_out(String cash_out) {
            this.cash_out = cash_out;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public int getProfit() {
            return profit;
        }

        public void setProfit(int profit) {
            this.profit = profit;
        }

        public double getProportion() {
            return proportion;
        }

        public void setProportion(double proportion) {
            this.proportion = proportion;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }
    }
}

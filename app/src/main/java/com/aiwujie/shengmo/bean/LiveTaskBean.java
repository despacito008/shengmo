package com.aiwujie.shengmo.bean;

import java.util.List;

public class LiveTaskBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"taskList":[{"msg":"在直播间发言20次 7/20","complete_rate":"7/20","status":"未完成","reward":"奖 2金魔豆＋幸运草(1魔豆)"},{"msg":"在直播间发言40次 7/40","complete_rate":"7/40","status":"未完成","reward":"奖 4金魔豆＋糖果(3魔豆)"},{"msg":"在直播间发言60次 7/60","complete_rate":"7/60","status":"未完成","reward":"奖 6金魔豆＋玩具狗(5魔豆)"},{"msg":"在直播间发言80次 7/80","complete_rate":"7/80","status":"未完成","reward":"奖 8金魔豆＋套套(8魔豆)"},{"msg":"在直播间发言100次 7/100","complete_rate":"7/100","status":"未完成","reward":"奖 10金魔豆＋内内(10魔豆)"}],"taskTips":"(请勿频繁发布无意义的垃圾信息，否则将被取消奖励，严重者还会被禁言)"}
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
         * taskList : [{"msg":"在直播间发言20次 7/20","complete_rate":"7/20","status":"未完成","reward":"奖 2金魔豆＋幸运草(1魔豆)"},{"msg":"在直播间发言40次 7/40","complete_rate":"7/40","status":"未完成","reward":"奖 4金魔豆＋糖果(3魔豆)"},{"msg":"在直播间发言60次 7/60","complete_rate":"7/60","status":"未完成","reward":"奖 6金魔豆＋玩具狗(5魔豆)"},{"msg":"在直播间发言80次 7/80","complete_rate":"7/80","status":"未完成","reward":"奖 8金魔豆＋套套(8魔豆)"},{"msg":"在直播间发言100次 7/100","complete_rate":"7/100","status":"未完成","reward":"奖 10金魔豆＋内内(10魔豆)"}]
         * taskTips : (请勿频繁发布无意义的垃圾信息，否则将被取消奖励，严重者还会被禁言)
         */

        private String taskTips;
        private List<TaskListBean> taskList;

        public String getTaskTips() {
            return taskTips;
        }

        public void setTaskTips(String taskTips) {
            this.taskTips = taskTips;
        }

        public List<TaskListBean> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<TaskListBean> taskList) {
            this.taskList = taskList;
        }

        public static class TaskListBean {
            /**
             * msg : 在直播间发言20次 7/20
             * complete_rate : 7/20
             * status : 未完成
             * reward : 奖 2金魔豆＋幸运草(1魔豆)
             */

            private String msg;
            private String complete_rate;
            private String status;
            private String reward;
            private String task_icon;
            private String status_text;

            public String getStatus_text() {
                return status_text;
            }

            public void setStatus_text(String status_text) {
                this.status_text = status_text;
            }

            public String getTask_icon() {
                return task_icon;
            }

            public void setTask_icon(String task_icon) {
                this.task_icon = task_icon;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getComplete_rate() {
                return complete_rate;
            }

            public void setComplete_rate(String complete_rate) {
                this.complete_rate = complete_rate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getReward() {
                return reward;
            }

            public void setReward(String reward) {
                this.reward = reward;
            }
        }
    }
}

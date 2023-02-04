/**
 * Copyright 2020 bejson.com
 */
package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Auto-generated: 2020-06-02 17:1:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class LiveAnchorData {

    private int retcode;
    private String msg;
    private Data data;
    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }
    public int getRetcode() {
        return retcode;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }


    /**
     * Auto-generated: 2020-06-02 17:1:54
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class RecommendAnchor {

        private String uid;
        private String nickname;
        private String live_title;
        private String live_poster;
        private String watchsum;
        private String live_time;
        private String pullUrl;
        public void setUid(String uid) {
            this.uid = uid;
        }
        public String getUid() {
            return uid;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getNickname() {
            return nickname;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }
        public String getLive_title() {
            return live_title;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }
        public String getLive_poster() {
            return live_poster;
        }

        public void setWatchsum(String watchsum) {
            this.watchsum = watchsum;
        }
        public String getWatchsum() {
            return watchsum;
        }

        public void setLive_time(String live_time) {
            this.live_time = live_time;
        }
        public String getLive_time() {
            return live_time;
        }

        public void setPullUrl(String pullUrl) {
            this.pullUrl = pullUrl;
        }
        public String getPullUrl() {
            return pullUrl;
        }

    }


    /**
     * Auto-generated: 2020-06-02 17:1:54
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class HotAnchor {

        private String uid;
        private String nickname;
        private String live_title;
        private String live_poster;
        private String watchsum;
        private String live_time;
        private String pullUrl;
        public void setUid(String uid) {
            this.uid = uid;
        }
        public String getUid() {
            return uid;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getNickname() {
            return nickname;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }
        public String getLive_title() {
            return live_title;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }
        public String getLive_poster() {
            return live_poster;
        }

        public void setWatchsum(String watchsum) {
            this.watchsum = watchsum;
        }
        public String getWatchsum() {
            return watchsum;
        }

        public void setLive_time(String live_time) {
            this.live_time = live_time;
        }
        public String getLive_time() {
            return live_time;
        }

        public void setPullUrl(String pullUrl) {
            this.pullUrl = pullUrl;
        }
        public String getPullUrl() {
            return pullUrl;
        }

    }

    /**
     * Auto-generated: 2020-06-02 17:1:54
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Data {

        private List<RecommendAnchor> recommendAnchor;
        private List<HotAnchor> hotAnchor;
        public void setRecommendAnchor(List<RecommendAnchor> recommendAnchor) {
            this.recommendAnchor = recommendAnchor;
        }
        public List<RecommendAnchor> getRecommendAnchor() {
            return recommendAnchor;
        }

        public void setHotAnchor(List<HotAnchor> hotAnchor) {
            this.hotAnchor = hotAnchor;
        }
        public List<HotAnchor> getHotAnchor() {
            return hotAnchor;
        }

    }
}
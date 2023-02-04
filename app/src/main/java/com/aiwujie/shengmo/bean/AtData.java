package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/5/3.
 */

public class AtData {

    /**
     * uid : 175
     * nickname : 深藏功与名.
     * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-04-26/20170426135919645.jpg
     * did : 6818
     * content : Uploads/Picture/2017-05-03/20170503144736534sl.jpeg
     * pic : Uploads/Picture/2017-05-03/20170503144736534sl.jpeg
     */

    private String uid;
    private String nickname;
    private String head_pic;
    private String did;
    private String content;
    private String pic;
    private String addtime;

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
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

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

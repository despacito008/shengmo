package com.aiwujie.shengmo.net;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LiveHttpHelper {
    private LiveHttpHelper HttpHelper;

    public static LiveHttpHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final LiveHttpHelper INSTANCE = new LiveHttpHelper();
    }
    //是否有开播权限  提现权限
    public void isAuth(HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.getLiveAuth,map,httpListener);
    }

    //确认申请
    public void applyAnchor(HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.applyAnchor,map,httpListener);
    }

    //确认申请
    public void applyAnchorNew(HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.applyAnchorNew,map,httpListener);
    }

    public void beginToShow(String live_poster,String live_title,String tids,String is_interact,String is_ticket,String ticketBeans,
                            String isRecord,String isPwd,String pwd,HttpListener httpListener){
        Map<String, String> map = new HashMap<>();
        //map.put("uid", MyApp.uid);
        map.put("live_poster",live_poster);
        map.put("live_title",live_title);
        map.put("tids",tids);
        map.put("is_interaction",is_interact);
        map.put("is_ticket",is_ticket);
        map.put("ticket_beans",ticketBeans);
        map.put("is_record",isRecord);
        map.put("is_pwd",isPwd);
        map.put("room_pwd",pwd);
        basePost(HttpUrl.beginToShow,map,httpListener);
    }

    public void closeLive(String roomId,HttpListener httpListener){
        Map<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        basePost(HttpUrl.closeToShow,map,httpListener);
    }

    public void getPullAddress(String anchorId,HttpListener httpListener){
        Map<String, String> map = new HashMap<>();
        map.put("uid", anchorId);
        basePost(HttpUrl.getPullAddress,map,httpListener);
    }

    public void followAnchor(String fuid, HttpListener httpListener){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        basePost(HttpUrl.FollowOneBox,map,httpListener);
    }
    public void unFollowAnchor(String fuid, HttpListener httpListener){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        basePost(HttpUrl.OverFollow,map,httpListener);
    }
    public void uploadLivePoster(String fileName,HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("image", fileName);
        basePost(HttpUrl.uploadLivePoster,map,httpListener);
    }
    public void getFollowAnchor(int page,HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.getFollowAnchor,map,httpListener);
    }
    //红人榜
    public void celebrityList(HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.celebrityList,map,httpListener);
    }

    //热门主播
    public void hotAnchorList(int page, HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        basePost(HttpUrl.hotAnchorList,map,httpListener);
    }

    //红人榜/热门榜
    public void onairlist(int page, HttpListener httpListener){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        basePost(HttpUrl.RedHotList,map,httpListener);
    }

    //最新
    public void newAnchorList(int page, HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        //map.put("uid", MyApp.uid);
        map.put("page", page + "");
        basePost(HttpUrl.newAnchorList,map,httpListener);
    }

    //进入直播间
    public void joinLiveRoom(String anchor_uid, HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.joinLiveRoom,map,httpListener);
    }


    //退出直播间
    public void quitAvChatRoom(String anchor_uid, HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.quitAvChatRoom,map,httpListener);
    }


    //获取主播信息
    public void getAnchorInfo(String anchor_uid, HttpListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.getAnchorInfo,map,httpListener);
    }
    //送礼物
    public void sendGift(String anchor_uid, String gift_id,String gift_count,String gift_type, HttpCodeListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        map.put("gift_id", gift_id);
        map.put("gift_count", gift_count);
        map.put("gift_type",gift_type);
        basePost(HttpUrl.sendGift,map,httpListener);
    }

    //直播间礼物
    public String liveGiftList(String url) {
        return url.startsWith("https://") ? url : url.equals(com.aiwujie.shengmo.http.HttpUrl.GetBaseUrl) ? ("http://cs.aiwujie.net/" + url) : (com.aiwujie.shengmo.http.HttpUrl.NetPic() + url);
    }
    //获取免费礼物
    public void getMySystemPresent(HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("uid",MyApp.uid);
        IRequestManager manager=RequestFactory.getRequestManager();
        basePost(HttpUrl.GetMyBasicGift, map, httpListener);
    }

    //设置/取消场控
    public void setChatRoomAdmin(String type,String touid,String anchor_uid,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("type",type);
        map.put("touid",touid);
        map.put("anchor_id",anchor_uid);
        basePost(HttpUrl.setChatRoomAdmin, map, httpListener);
    }
    //场控列表
    public void managerList(String anchorId,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        //map.put("anchor_id",anchorId);
        basePost(HttpUrl.groupAdminList, map, httpListener);
    }

    //本场/永久/取消禁言
    public void setNoTalking(String type,String touid,String anchor_uid,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("type",type);
        map.put("touid",touid);
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.noTalking, map, httpListener);
    }

    //获取卡片信息
    public void getCardInfo(String touid, String anchor_uid,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("touid",touid);
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.card, map, httpListener);
    }
    //本场/月榜
    public void getGiftRank(String anchor_uid,String type,int page,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        map.put("type",type);
        map.put("page" ,page + "");
        basePost(HttpUrl.giftRank, map, httpListener);
    }
    //热度榜主播礼物排行
    public void getHourRank(String anchor_uid,String type,int page,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        map.put("type",type);
        map.put("page" ,page + "");
        basePost(HttpUrl.hourRank, map, httpListener);
    }
    //触屏点赞
    public void likeanchor(String anchor_uid,HttpListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.likeAnchor, map, httpListener);
    }

    //弹幕功能
    public void sendBarrage(String anchor_uid,String barrage,HttpListener httpListener){
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        map.put("barrage",barrage);
        basePost(HttpUrl.sendBarrage, map, httpListener);
    }

    void basePost(String url, Map map, final HttpListener listener) {
        RequestFactory.getRequestManager().post(url, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj.getInt("retcode") == 2000 || obj.getInt("retcode") == 2001) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFail(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFail(throwable.getMessage());
            }
        });
    }

    void basePost(String url, Map map, final HttpCodeListener listener) {
        RequestFactory.getRequestManager().post(url, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj.getInt("retcode") == 2000 || obj.getInt("retcode") == 2001) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFail(obj.getInt("retcode"),obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFail(1000,throwable.getMessage());
            }
        });
    }
}

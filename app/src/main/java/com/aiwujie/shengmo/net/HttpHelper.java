package com.aiwujie.shengmo.net;

import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomePageUserScreen;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.SystemUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.VersionUtils;


import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;


public class HttpHelper {

    private HttpHelper HttpHelper;

    public static HttpHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpHelper INSTANCE = new HttpHelper();
    }

    //点赞评论
    public void thumbUpComment(String cmid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("cmid", cmid);
        basePost(HttpUrl.ThumnUpComment, map, listener);
    }

    //删除评论
    public void deleteComment(String did, String cmid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("cmid", cmid);
        basePost(HttpUrl.DelComment, map, listener);
    }


    public void sendChildComment(String did, String pid, String content, String ouid, final HttpListener listener) {
        String platuid;
        String platuname;
        List<String> atuidlist = new ArrayList<>();
        List<String> atunamelist = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("content", content);
        map.put("otheruid", ouid);
        if (!TextUtil.isEmpty(pid)) {
            map.put("pid", pid);
        }
        String substring = "";
        String substring2 = "";
        platuid = "";
        platuname = "";
        for (int i = 0; i < atuidlist.size(); i++) {
            platuid += atuidlist.get(i) + ",";
            platuname += atunamelist.get(i) + ",";
        }
        if (atuidlist.size() > 0) {
            substring = platuid.substring(0, platuid.length() - 1);
            substring2 = platuname.substring(0, platuname.length() - 1);
        }
        map.put("atuid", substring);
        map.put("atuname", substring2);
        RequestFactory.getRequestManager().post(HttpUrl.SendCommentNewred, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj.getInt("retcode") == 2000) {
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


    //获取两个人所有红包的记录
    public void getAllRedBagStatus(String other, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("otherid", other);
        basePost(HttpUrl.getAllRedBagStatus, map, listener);
    }

    //获取用户详细信息
    public void getUserInfo(String uid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.GetNewUserInfo, map, listener);
    }

    //获取用户收到的礼物
    public void getUserPresent(String uid, HttpListener listener) {
        getUserPresent2(uid, "", listener);
    }

    public void getUserPresent2(String uid, String redBag, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        if (!TextUtil.isEmpty(redBag)) {
            map.put("redbag", redBag);
        }
        basePost(HttpUrl.GetMyPresent, map, listener);
    }

    // 悄悄 关注/取关
    public void quietFollow(String uid, boolean followType, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        map.put("type", followType ? "0" : "1");
        basePost(HttpUrl.followQuiet, map, listener);
    }

    // 拉黑/取消拉黑
    public void blockUser(String uid, boolean blockType, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        map.put("type", blockType ? "0" : "1");
        basePost(HttpUrl.blockUser, map, listener);
    }

    //获取自己的vip等状态
    public void getMyOwnInfo(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetUserPowerInfo, map, listener);
    }

    // 可疑/取消可疑
    public void editLikeLiar(String uid, int type, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("method", type == 1 ? "setone" : "removeone");
        basePost(HttpUrl.OperateOneLiker, map, listener);
    }

    //设置用户违规
    public void delIllegallyUserInfo(String uid, int type, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("type", String.valueOf(type));
        basePost(HttpUrl.delIllegallyUserInfo, map, listener);
    }

    public void getBanUserStatus(String uid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.GetAllUserStatus, map, listener);
    }


    //封禁各种状态
    public void banUserStatus(String uid, int method, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("method", String.valueOf(method));
        basePost(HttpUrl.ChangeAllUserStatus, map, listener);
    }

    //解除封禁各种状态
    public void removeBanUserStatus(String uid, String method, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("method", String.valueOf(method));
        if (String.valueOf(method).equals("resume")) {
            basePost(HttpUrl.ChangeDeviceStatus, map, listener);
        } else {
            basePost(HttpUrl.RecoverAllUserStatus, map, listener);
        }
    }

    //封禁各种状态
    public void banUserStatus(String uid, String method, String day, String content, String pic, boolean isPublic, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("method", method);
        map.put("content", content);
        map.put("pic", pic);
        map.put("sypic", pic);
        map.put("login_uid", MyApp.uid);
        map.put("blockingalong", day);
        map.put("is_show", isPublic ? "1" : "0");
        if (method.equals("forbid")) {
            basePost(HttpUrl.ChangeDeviceStatus, map, listener);
        } else {
            basePost(HttpUrl.ChangeAllUserStatus, map, listener);
        }
    }


    //admin获取用户详细信息
    public void getAdminDeviceInfo(String uid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        basePost(HttpUrl.detailMobile, map, listener);
    }

    //富豪榜  日/周/总
    public void getRewardWealthRankingList(String type, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetWealthRankingList, map, listener);

    }

    //魅力榜  日/周/总
    public void getRewardCharmRankingList(String type, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetCharmRankingList, map, listener);

    }

    public void getPopularityRankingList(String popType, String timeType, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", timeType);
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        if ("1".equals(popType)) {
            basePost(HttpUrl.GetBeCommentedRankingList, map, listener);
        } else if ("2".equals(popType)) {
            basePost(HttpUrl.GetBeLaudedRankingList, map, listener);
        } else if ("3".equals(popType)) {
            map.put("recommand", "1");
            basePost(HttpUrl.GetSendDynamicRandkingList, map, listener);
        }
    }

    public void getActiveRankingList(String popType, String timeType, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", timeType);
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        if ("1".equals(popType)) {
            basePost(HttpUrl.GetCommentRankingList, map, listener);
        } else if ("2".equals(popType)) {
            basePost(HttpUrl.GetLaudRankingList, map, listener);
        } else if ("3".equals(popType)) {
            map.put("recommand", "0");
            basePost(HttpUrl.GetSendDynamicRandkingList, map, listener);
        }
    }

    public void getVipOverTime(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetVipOverTimeNew, map, listener);
    }

    public void getVipGoodsList(String type, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        basePost(HttpUrl.GetVipGoods, map, listener);
    }

    public void getDynamicPushList(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        basePost(HttpUrl.GetPushGoods, map, listener);
    }

    public void getStampList(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        basePost(HttpUrl.GetStampGoods, map, listener);
    }

    public void getBeansList(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        basePost(HttpUrl.GetBeansGoods, map, listener);
    }

    /**
     * @param type    商品类型  1 -  vip   2 - svip
     * @param subject 支付id
     * @param uid
     * @param isOpen
     * @param payType 支付类型  1 - ali   2 - wechat
     */
    public void getOrderIdBeforePay(String payType, String type, String subject, String uid, String isOpen, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        if (MyApp.uid.equals(uid)) {
            map.put("uid", MyApp.uid);
        } else {
            map.put("uid", uid);
            map.put("guid", MyApp.uid);
            if ("2".equals(type)) { // svip可选
                map.put("dalaba", isOpen);
            }
        }
        map.put("subject", subject);
        if ("1".equals(payType)) {
            String json = GsonUtil.getInstance().toJson(map);
            if ("1".equals(type)) {
                basePost(HttpUrl.ALIPAYvipcharge, json, listener);
            } else {
                basePost(HttpUrl.ALIPAYsvipcharge, json, listener);
            }
        } else {
            map.put("appName", "shengmo");
            String json = GsonUtil.getInstance().toJson(map);
            if ("1".equals(type)) {
                basePost(HttpUrl.WXvipcharge, json, listener);
            } else {
                basePost(HttpUrl.WXsvipcharge, json, listener);
            }
        }
    }

    // 购买推顶卡 - 支付
    public void getOrderIdBeforePay(String payType, String num, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("num", num);
        map.put("uid", MyApp.uid);
        if ("1".equals(payType)) {
            String json = GsonUtil.getInstance().toJson(map);
            basePost(HttpUrl.ALIPAYtopcardcharge, json, listener);
        } else {
            map.put("appName", "圣魔");
            String json = GsonUtil.getInstance().toJson(map);
            basePost(HttpUrl.WXCtopcardcharge, json, listener);
        }
    }


    public void payByBeans(String type, String subject, String user_id, String isOpen, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid", MyApp.uid);
        if ("1".equals(type)) {
            map.put("uid", user_id);
            map.put("viptype", subject);
            map.put("beanstype", "0");
            String json = GsonUtil.getInstance().toJson(map);
            basePost(HttpUrl.VipBeans, json, listener);
        } else {
            map.put("vuid", user_id);
            map.put("channel", "1");
            map.put("subject", subject);
            map.put("dalaba", isOpen);
            String json = GsonUtil.getInstance().toJson(map);
            basePost(HttpUrl.SvipBeans, json, listener);
        }
    }

    public void stampByBeans(String num, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("num", num);
        map.put("channel", "0");
        String json = GsonUtil.getInstance().toJson(map);
        basePost(HttpUrl.topcard_baans, json, listener);
    }

    public void getCommentList(String did, int sortType, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("order", sortType == 1 ? "time" : "hot");
        basePost(HttpUrl.GetNewCommentList, map, listener);
    }

    public void getDynamicDetail(String did, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        basePost(HttpUrl.GetDynamicdetailFive, map, listener);
    }

    public void getDynamicComment(String did, int page, int sortType, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("order", sortType == 1 ? "time" : "hot");
        basePost(HttpUrl.GetCommentListNew, map, listener);
    }

    public void getDynamicThumb(String did, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        basePost(HttpUrl.GetLaudListNew, map, listener);
    }

    public void getDynamicThumb(String did, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        basePost(HttpUrl.GetLaudListNew, map, listener);
    }


    public void getDynamicReward(String did, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        basePost(HttpUrl.GetRewardListNew, map, listener);
    }

    public void getDynamicPush(String did, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("did", did);
        map.put("uid", MyApp.uid);
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.getTopcardUsedRs, map, listener);
    }

    public void getAtUsData(int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        basePost(HttpUrl.AtUsData, map, listener);
    }

    public void sendComment(String content, String did, String cmid, String otheruid, String atuid, String atuname, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("content", content);
        if (!TextUtil.isEmpty(cmid)) {
            map.put("pid", cmid);
            map.put("otheruid", otheruid);
        }
        if (!TextUtil.isEmpty(atuid)) {
            map.put("atuid", atuid);
            map.put("atuname", atuname);
        }
        basePost(HttpUrl.SendCommentNewred, map, listener);
    }

    public void thumbUpDynamic(String did, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        basePost(HttpUrl.LaudDynamicNewrd, map, listener);
    }

    public void pushTopDynamic(String did, boolean pushTop, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        if (pushTop) {
            basePost(HttpUrl.SetStickDynamic, map, listener);
        } else {
            basePost(HttpUrl.SetUnStickDynamic, map, listener);
        }
    }

    public void collectDynamic(String did, boolean collect, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        if (collect) {
            basePost(HttpUrl.CollectDynamic, map, listener);
        } else {
            basePost(HttpUrl.CancelCollectDynamic, map, listener);
        }
    }

    public void getUserSendDynamicPermission(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.JudgeDynamicNewrd, map, listener);
    }

    public void recommendDynamic(String did, boolean recommend, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("method", "0");
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        if (recommend) {
            basePost(HttpUrl.SetDynamicRerommend, map, listener);
        } else {
            basePost(HttpUrl.CancelDynamicRerommend, map, listener);
        }
    }

    public void recommendAllDynamic(String did, boolean recommend,int time, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("method", "1");
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        map.put("topping_time",String.valueOf(time));
        if (recommend) {
            basePost(HttpUrl.SetDynamicRerommend, map, listener);
        } else {
            basePost(HttpUrl.CancelDynamicRerommend, map, listener);
        }
    }

    public void hideDynamic(String did, boolean hide, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid", MyApp.uid);
        map.put("did", did);
        if (hide) {
            basePost(HttpUrl.DynamicForbid, map, listener);
        } else {
            basePost(HttpUrl.DynamicResume, map, listener);
        }
    }

    public void deleteDynamic(String did, boolean isSelf, boolean isAdmin, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        if (isSelf || !isAdmin) {
            basePost(HttpUrl.DelDynamic, map, listener);
        } else {
            basePost(HttpUrl.AdminDelDynamic, map, listener);
        }
    }


    public void getTopicDynamicList(String type, String sex, String sexual, String topicId, String lastId, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastId);
        map.put("tid", topicId);
        basePost(HttpUrl.GetDynamicListNewFive, map, listener);
    }

    public void getFollowDynamicList(int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", "2");
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("lastid", "");
        basePost(HttpUrl.GetDynamicListNewFive, map, listener);
    }


    public void getDynamicList(String type, String sex, String sexual, String order, String lastId, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastId);
        if (!TextUtil.isEmpty(order)) {
            map.put("order", order);
        }
        basePost(HttpUrl.GetDynamicListNewFive, map, listener);
    }

    public void getDynamicList(String type, String sex, String sexual, String order, String lastId, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastId);
        if (!TextUtil.isEmpty(order)) {
            map.put("order", order);
        }
        basePost(HttpUrl.GetDynamicListNewFive, map, listener);
    }

    public void getTopicDetail(String tid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("tid", tid);
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetTopicDetail, map, listener);
    }

    public void getCallState(String toUid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("touid", toUid);
        basePost(HttpUrl.callingState, map, listener);
    }

    public void getCallStateNew(String toUid, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("touid", toUid);
        basePost(HttpUrl.callingStateNew, map, listener);
    }

    public void getTimUserSign(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.getUserSign, map, listener);
    }

    public void applyQuitGroup(String groupID, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", groupID);
        basePost(HttpUrl.applyQuitGroup, map, listener);
    }

    public void getAppUpdate(HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.appUpdate, map, listener);
    }

    public void addDynamicViewCount(String did, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        basePost(HttpUrl.addViewCount, map, listener);
    }

    public void getMyGroupList(String type, int page, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", type);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.GetGroupListFilter, map, listener);
    }

    public void revokeGroupMessage(String gid, long seq, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("msgseq", String.valueOf(seq));
        basePost(HttpUrl.revokeMessage, map, listener);
    }

    public void setGroupAutoCheck(String gid, String isAutoCheck, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("is_auto_check", isAutoCheck);
        basePost(HttpUrl.editGroupAutoCheck, map, listener);
    }

    public void setGroupAutoCheck(String gid, String isAutoCheck, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("is_auto_check", isAutoCheck);
        basePost(HttpUrl.editGroupAutoCheck, map, listener);
    }

    public void sendVerCode(String mobile, String code, String session, final HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("verify", code);
        String url = (HttpUrl.ForPswVercode);
        Set<Map.Entry<String, String>> entries = map.entrySet();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getValue() != null) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .addHeader("cookie", session)
                .url(url.startsWith("https://") ? url : com.aiwujie.shengmo.http.HttpUrl.NetPic() + url)
                .post(body)
                .build();
        Call call = OkHttpRequestManager.getInstance().getHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                OkHttpRequestManager.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                final String json = response.body().string();
                                if (TextUtil.isEmpty(json)) {
                                    listener.onFail("empty");
                                }
                                if (json.contains("callback")) {
                                    listener.onFail("callback");
                                } else {
                                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(json);
                                    if (jsonObject == null) {
                                        listener.onFail("json is null");
                                        return;
                                    }
                                    Integer retcode = jsonObject.getInteger("retcode");

                                    switch (retcode) {
                                        case 50001:
                                        case 50002:
                                            EventBus.getDefault().post(new TokenFailureEvent());
                                            break;
                                        case 2000:
                                            listener.onSuccess(json);
                                            break;
                                        default:
                                            listener.onFail(json);
                                            break;
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            listener.onFail("请求失败");
                        }
                    }
                });
            }
        });

    }

    public void sendVerEmail(String email, String code, HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("verify", code);
        basePost(HttpUrl.SendMailCode_forget, map, listener);
    }

    public void getVerImg(HttpByteListener listener) {
        baseImgGet(HttpUrl.GetPicSession, listener);
    }

    public void getMessageState(int type,String orderId,HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        map.put("orderid", orderId);
        basePost(HttpUrl.getMessageState, map, listener);
    }

    public void banGroup(String gid,int isBan,HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("is_ban", String.valueOf(isBan));
        basePost(HttpUrl.banGroup, map, listener);
    }

    public void getClaimState(String gid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.getClaimState, map, listener);
    }

    public void addClaimGroup(String gid,String reason,HttpListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("reason", reason);
        basePost(HttpUrl.addClaimGroup, map, listener);
    }

    public void addClaimGroup(String gid,String reason,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("reason", reason);
        basePost(HttpUrl.addClaimGroup, map, listener);
    }

    public void pushTopTopic(String type,String did,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("did", did);
        map.put("type", type);
        basePost(HttpUrl.pushTopTopic, map, listener);
    }

    public void getTopicTopList(String tid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("tid", tid);
        basePost(HttpUrl.getTopicTopList, map, listener);
    }

    public void getUserGroupList(int page,HttpCodeListener listener) {
        getGroupList(page,"5",listener);
    }

    public void getGroupList(int page,String type,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", type);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.GetGroupListFilter, map, listener);
    }

    //获取用户加入的群列表
    public void getUserJoinGroupList(String uid,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("type", "2");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.GetGroupListFilter, map, listener);
    }


    public void getInviteState(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.getInviteVIPState, map, listener);
    }

    public void checkInviteCode(String invite_code,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("invite_code",invite_code);
        basePost(HttpUrl.checkInviteCode, map, listener);
    }

    public void getNewUserInviteState(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.getNewUserInviteState, map, listener);
    }

    public void getUserInviteRewardState(int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.getUserInviteRewordInfo, map, listener);
    }

    public void getUserAuthPhoto(String uid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.Getidstate, map, listener);
    }

    public void getUserAuthPhoto(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.Getidstate, map, listener);
    }

    public void changeGroupLocation(String groupId,String province,String city,String lat,String lng,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid",groupId);
        map.put("province",province);
        map.put("city",city);
        map.put("lat",lat);
        map.put("lng",lng);
        basePost(HttpUrl.changeGroupLocation, map, listener);
    }

    public void changeGroupAdmin(String groupId,String uid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid",groupId);
        map.put("change_group_uid",uid);
        basePost(HttpUrl.changeGroupAdmin, map, listener);
    }

    public void searchLiveUser(String key,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", key);
        map.put("page", page + "");
        basePost(HttpUrl.searchAnchor,map,listener);
    }

    public void getGiftList(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.liveGiftList,map,listener);
    }

    public void sendGiftToDynamic(boolean isKnapsack,String did,String giftId,int num,int totalPrice,boolean isOpen,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("amount", String.valueOf(totalPrice));
        map.put("psid", giftId);
        map.put("num", String.valueOf(num));
        map.put("dalaba", isOpen?"1":"2");
        String json = GsonUtil.getInstance().toJson(map);
        if (isKnapsack) {
            basePost(HttpUrl.RewardBasicGDynamic, json, listener);
        } else {
            basePost(HttpUrl.RewardDynamicNew, json, listener);
        }
    }

    public void sendGiftToPerson(boolean isKnapsack,String uid,String giftId,int num,int totalPrice,boolean isOpen,String sendType,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        map.put("beans", String.valueOf(totalPrice));
        map.put("num", String.valueOf(num));
        map.put("type", giftId);
        map.put("dalaba",isOpen?"1":"2");
        map.put("is_home", sendType);
        String json = GsonUtil.getInstance().toJson(map);
        if (isKnapsack) {
            basePost(HttpUrl.RewardOneBasicG, json, listener);
        } else {
            basePost(HttpUrl.RewardOne, json, listener);
        }
    }

    public void sendGiftToGroup(boolean isKnapsack,String gid,String giftId,int num,int totalPrice,boolean isOpen,String orderid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", gid);
        map.put("beans", String.valueOf(totalPrice));
        map.put("num", String.valueOf(num));
        map.put("type", giftId);
        map.put("dalaba", isOpen ? "1" : "2");
        map.put("orderid", orderid);
        String json = GsonUtil.getInstance().toJson(map);
        if (isKnapsack) {
            basePost(HttpUrl.RewardOneBasicG, json, listener);
        } else {
            basePost(HttpUrl.RewardOne, json, listener);
        }
    }

    public void getMyWallet(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.Getmywallet, map, listener);
    }

    public void getGroupMsgOperateList(int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.GetGroupMsg,map,listener);
    }

    public void refuseJoinGroup(String uid,String mid,String reason,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", uid);
        map.put("mid", mid);
        map.put("reason",reason);
        basePost(HttpUrl.RefuseOneInto,map,listener);
    }

    public void agreeJoinGroup(String uid,String mid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", uid);
        map.put("mid", mid);
        basePost(HttpUrl.AgreeOneInto,map,listener);
    }

    public void clearGroupOperateMsg(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.DelGroupMsg,map,listener);
    }


    public void getWithdrawInfo(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.balanceInfo,map,listener);
    }

    public void getWithdrawLog(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.withdrawLog,map,listener);
    }

    //提现
    public void withdrawMoney(String bid,int profit,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("bid", bid);
        map.put("profit", String.valueOf(profit));
        basePost(HttpUrl.withdrawMoney,map,listener);
    }

    //首页 最近开播
    public void getRecommendLiveList(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.recommendLive,map,listener);
    }

    public void followAnchor(String fuid,boolean isFollow,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type",isFollow?"0":"1");
        map.put("uid", MyApp.uid);
        map.put("fuid",fuid);
        basePost(HttpUrl.followAnchor,map,listener);
    }

    public void adminOperateLive(int type,String uid,String reason,int day,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        if (type == 1) {
            map.put("type","montior_warning");
        } else if (type == 2){
            map.put("type","montior_seal");
        } else if (type == 3) {
            map.put("type","cancel_montior_seal");
        } else if (type == 4) {
            map.put("type","montior_kick");
        } else if (type == 5) {
            map.put("type","signed_anchor");
        } else if (type == 6) {
            map.put("type","signed_no_anchor");
        }
        map.put("uid",uid);
        map.put("reasontext",reason);
        map.put("blocksupalong",String.valueOf(day == 99?"forever":day));
        basePost(HttpUrl.liveAdminOperate,map,listener);
    }

    public void getLiveOnLineUser(String roomId,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("anchor_uid",roomId);
        basePost(HttpUrl.liveOnlineUser,map,listener);
    }

    public void getLiveManageUser(String anchorId,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_id",anchorId);
        basePost(HttpUrl.groupAdminList, map, listener);
    }

    public void getMuteForeverUser(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.getMuteForeverUser,map,listener);
    }

    public void reportHeartBeat(String anchorId) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid",MyApp.uid);
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.reportHeartBeat, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    public void reportAnchorHeartBeat() {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.reportAnchorHeartBeat, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {
                reportAnchorHeartBeatRetry();
                reportAnchorHeartBeatRetry();
                reportAnchorHeartBeatRetry();
            }
        });
    }

    public void reportAnchorHeartBeatRetry() {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.reportAnchorHeartBeat, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {
            }
        });
    }

    public void getNewLiveAuth(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.liveAuthNew,map,listener);
    }

    public void getWithdrawAuth(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.withdrawAuth,map,listener);
    }

    public void getAnchorCard(String anchorId,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.anchorLiveCard,map,listener);
    }

    public void editAnchorCard(String anchor_uid,String title,String poster,String tid,
                               String is_ticket,String ticket_beans,
                               String is_pwd,String room_pwd,
                               HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid",anchor_uid);
        if (title != null) {
            map.put("live_title",title);
        }
        if (poster != null) {
            map.put("live_poster",poster);
        }
        if (tid != null) {
            map.put("tids",tid);
        }
        if (is_ticket != null) {
            map.put("is_ticket",is_ticket);
        }
        if (ticket_beans != null) {
            map.put("ticket_beans",ticket_beans);
        }

        if (is_pwd != null) {
            map.put("is_pwd",is_pwd);
        }

        if (room_pwd != null) {
            map.put("room_pwd",room_pwd);
        }

        basePost(HttpUrl.changeAnchorLiveCard,map,listener);
    }

    public void getHomePageLiveLabel(HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.homePageLiveLabel,map,listener);
    }

    public void getLabelAnchorList(String tid,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("tid",tid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.labelAnchorList,map,listener);
    }

    public void updateLiveLinkMicList(String uidStr) {
        Map<String, String> map = new HashMap<>();
        map.put("uids",uidStr);
        basePost(HttpUrl.updateLinkMicList, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    public void updateManagerLinkMicList(String uidStr) {
        Map<String, String> map = new HashMap<>();
        map.put("uids",uidStr);
        basePost(HttpUrl.updateLinkMangerList, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    public void getLiveLinkMicList(String anchorId,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.linkMicList, map, listener);
    }


    public void getLiveAudienceRankList(int type,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.liveRankingList, map, listener);
    }

    public void getLiveAnchorRankList(int type,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.liveCharmRankingList, map, listener);
    }

    public void getLivePlayBackRankList(int type,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.livePlayBackRankingList, map, listener);
    }


    public void getLiveAnchorState(String anchorId,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_id",anchorId);
        basePost(HttpUrl.liveAnchorState, map, listener);
    }

    public void recommendOrCancel(String anchorId,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_id",anchorId);
        map.put("weight","1");
        basePost(HttpUrl.recommendRedAnchor, map, listener);
    }

    public void getUserLiveHistory(String anchorId,int page,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_id",anchorId);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.liveHistory, map, listener);
    }

    public void reportLiveMessage(String anchorId,String roomId,String content,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        map.put("roomId",roomId);
        map.put("content",content);
        basePost(HttpUrl.reportChatMessage, map, listener);
    }

    public void reportBeautyPermission(HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.consumptionState,map,listener);
    }

    public void getLabelIntroduce(String tid,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("tid",tid);
        basePost(HttpUrl.labelIntroduce,map,listener);
    }

    public void updateAnchorCameraState(boolean isOpen) {
        Map<String,String> map = new HashMap<>();
        map.put("status",isOpen?"0":"1");
        basePost(HttpUrl.updateCameraStatus, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }


    public void getLiveTaskList(HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.liveTaskList,map,listener);
    }

    public void closeLive(String roomId) {
        Map<String,String> map = new HashMap<>();
        map.put("room_id",roomId);
        basePost(HttpUrl.closeLive, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    public void getSystemGiftDetail(int page,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.systemGiftRecord,map,listener);
    }

    public  void  getMapUser(int page,String lat,String lng,String onlinestate,String realname,String age,String upxzya,
                             String sex,String sexual,String role,String culture,String monthly,HttpCodeMsgListener listener){
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("layout", "1");
        map.put("type", "1");
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("onlinestate", onlinestate);
        map.put("realname", realname);
        map.put("age", age);
        map.put("want", upxzya);
        map.put("sex", sex);
        map.put("sexual", sexual);

        map.put("role", role);
        map.put("culture", culture);
        map.put("monthly", monthly);
        map.put("loginid", MyApp.uid);
        basePost(HttpUrl.UserListNewth,map,listener);
    }



    void baseImgGet(String url, final HttpByteListener listener) {
        Request request = new Request.Builder()
                .url(url.startsWith("https://") ? url : HttpUrl.NetPic()+ HttpUrl.GetPicSession)
                .build();

        Call call = OkHttpRequestManager.getInstance().getHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                OkHttpRequestManager.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] byte_image = response.body().bytes();
                            //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                            Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");
                            String session = cookies.get(0);
                            String s = session.substring(0, session.indexOf(";"));
                            listener.onSuccess(s, byte_image);
                        } catch (Exception e) {
                            listener.onFail("加载图片验证码失败");
                        }
                    }
                });

            }
        });
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
                        listener.onFail(obj.optInt("retcode"),obj.optString("msg"));
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


    void basePost(String url, Map map, final HttpCodeMsgListener listener) {
        Log.v("basePostUrl",url+"");
        RequestFactory.getRequestManager().post(url, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    if (obj.getInt("retcode") == 2000 || obj.getInt("retcode") == 2001) {
                        listener.onSuccess(response,obj.optString("msg"));
                    } else {
                        listener.onFail(obj.optInt("retcode"),obj.optString("msg"));
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

    void basePost(String url, String json, final HttpListener listener) {
        RequestFactory.getRequestManager().post(url, json, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFail(throwable.getMessage());
            }
        });
    }

    void basePost(String url, String json, final HttpCodeListener listener) {
        RequestFactory.getRequestManager().post(url, json, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                //listener.onSuccess(response);
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

    public void getLiveMuteList(String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.mutePeopleList,map,listener);
    }

    public void getWatchLivePower(String andchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",andchorId);
        basePost(HttpUrl.joinLive,map,listener);
    }

    public void buyTicket(String androidId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",androidId);
        basePost(HttpUrl.buyTicket,map,listener);
    }

    public void getTicketInfo(String androidId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",androidId);
        basePost(HttpUrl.liveTicketInfo,map,listener);
    }

    public void getBanner(int type,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("type",String.valueOf(type));
        basePost(HttpUrl.GetSlideMore,map,listener);
    }

    public void getChatRanking(HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.chatRanking,map,listener);
    }

    public void getLiveSeal(String anchorId,String type,int page,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_id",anchorId);
        map.put("type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.liveSeal,map,listener);
    }

    public void getPkAnchorList(HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.onlinePkAnchorList,map,listener);
    }

    public void getPkAnchorInfo(String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.pkAnchorInfo,map,listener);
    }

    public void startPk(String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("invite_anchor_uid",anchorId);
        basePost(HttpUrl.LivePkStart,map,listener);
    }

    public void completePk(String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("invite_anchor_uid",anchorId);
        basePost(HttpUrl.LivePkComplete,map,listener);
    }

    public void stopPk(String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("invite_anchor_uid",anchorId);
        basePost(HttpUrl.LivePkEnd,map,listener);
    }

    public void getPkTopData(String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.PkTopData,map,listener);
    }

    public void getMyLiveLevel(String type,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        basePost(HttpUrl.myLiveLevel,map,listener);
    }

    public void getMyBankCard(HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        basePost(HttpUrl.Getbankcard,map,listener);
    }

    public void setCardDefault(String bid,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("bid",bid);
        basePost(HttpUrl.setCardDefault,map,listener);
    }

    public void getLivePlayBackDetail(String id,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("live_log_id",id);
        basePost(HttpUrl.livePlayBackDetail,map,listener);
    }

    public void buyPlayBack(String playbackId,String anchorId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("live_log_id",playbackId);
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.buyPlayBack,map,listener);
    }

    public void setPlayBackTicket(String videoId,String isFree,String ticketBean,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("live_log_id",videoId);
        map.put("is_free",isFree);
        map.put("live_beans",ticketBean);
        basePost(HttpUrl.setPlayBackPrice,map,listener);
    }

    public void getPlayBackTicketInfo(String videoId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("live_log_id",videoId);
        basePost(HttpUrl.playBackTicketInfo,map,listener);
    }

    public void beginToShow(String live_poster,String live_title,String tids,String is_interact,String is_ticket,String ticketBeans,
                            String isRecord,String isPwd,String pwd,
                            HttpCodeListener httpListener){
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

    //弹幕功能
    public void sendBarrage(String anchor_uid,String barrage,HttpCodeListener httpListener){
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        map.put("barrage",barrage);
        basePost(HttpUrl.sendBarrage, map, httpListener);
    }

    //获取主播信息
    public void getAnchorInfo(String anchor_uid, HttpCodeListener httpListener) {
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

    //触屏点赞
    public void likeanchor(String anchor_uid) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        basePost(HttpUrl.likeAnchor, map, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }


    public void kickOutUser(String uid,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("uid", uid);
        if (listener == null) {
            listener = new HttpCodeListener() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onFail(int code, String msg) {

                }
            };
        }
        basePost(HttpUrl.kickOutUser, map,listener);
    }

    public void delPlayBack(String liveLogId,String anchorId,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("live_log_id", liveLogId);
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.delPlayBack,map,listener);
    }

    public void hidePlayBack(String liveLogId,String anchorId,String type,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("type",type);
        map.put("live_log_id", liveLogId);
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.hidePlayBack,map,listener);
    }

    //新身份证认证
    public void authNewIdCard(String name,String idCard,String cardPic,HttpCodeMsgListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("real_name", name);
        map.put("ids", idCard);
        map.put("card_pic", cardPic);
        basePost(HttpUrl.idCardAuth,map,listener);
    }

    public void kickUser(String uid,String anchorId,String day,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchorId);
        map.put("touid", uid);
        map.put("along", day);
        basePost(HttpUrl.KickUser,map,listener);
    }

    public void getKickUserList(String anchorId,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchorId);
        basePost(HttpUrl.kickUserList,map,listener);
    }

    public void muteUser(String uid,String anchorId,String day,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchorId);
        map.put("touid", uid);
        map.put("along", day);
        basePost(HttpUrl.MuteUser,map,listener);
    }

    public void getMuteUserList(String anchorId,HttpCodeListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchorId);
        basePost(HttpUrl.MuteUserList,map,listener);
    }

    public void invitePKAgain(String invite_anchor_uid,HttpCodeListener listener ) {
        Map<String,String> map = new HashMap<>();
        map.put("invite_anchor_uid",invite_anchor_uid);
        basePost(HttpUrl.invitePkAgain,map,listener);
    }

    public void AgreeOrRejectPk(String invite_anchor_uid,String type,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("invite_anchor_uid",invite_anchor_uid);
        basePost(HttpUrl.pkAgreeOrRefuse,map,listener);
    }

    public void editPlayBackInfo(String logId,String title,String poster,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("live_log_id",logId);
        map.put("live_title",title);
        map.put("live_poster",poster);
        basePost(HttpUrl.changePlayBackInfo,map,listener);
    }

    public void verifyLivePassword(String anchorId,String pwd,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        map.put("room_pwd",pwd);
        basePost(HttpUrl.verifyLivePwd,map,listener);
    }

    public void lockLiveLog(String anchorId,String logId,String type,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        map.put("live_log_id",logId);
        map.put("type",type);
        basePost(HttpUrl.lockLiveLog,map,listener);
    }

    public void setRewardType(String type, HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        basePost(HttpUrl.setRewardType,map,listener);
    }

    public void getUserInviteList(String uid,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.inviteList,map,listener);
    }

    //直播间发红包
    public void sendLiveRedEnvelopes(String num,String money,String roomId,boolean isOpen,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("num",num);
        map.put("money",money);
        map.put("room_id",roomId);
        map.put("dalaba",isOpen?"1":"2");
        basePost(HttpUrl.liveSendRedBag,map,listener);
    }

    public void getLiveRedBagList(String roomId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("room_id",roomId);
        basePost(HttpUrl.getLiveRedBagList,map,listener);
    }

    public void receiveRedBag(String orderId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("orderid",orderId);
        basePost(HttpUrl.liveReceiveRedBag,map,listener);
    }

    public void getRedBagHistoryList(String orderId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("orderid",orderId);
        basePost(HttpUrl.getLiveRedBagHistoryList,map,listener);
    }

    public void getRedBagHistoryListNew(String orderId,HttpCodeListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("orderid",orderId);
        basePost(HttpUrl.getLiveRedBagHistoryListNew,map,listener);
    }

    public void giveSvip(String uid,String day,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("givealong",day);
        basePost(HttpUrl.giveSvip,map,listener);
    }

    public void reportManagerLink(String anchorId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        basePost(HttpUrl.reportManagerLink,map,listener);
    }

    public void getReceiveGiftDetail(int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetReceivePresent,map,listener);
    }

    public void getRechargeRecordDetail(int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetWalletRecord,map,listener);
    }

    public void getGivePsRecord(int type,int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        map.put("type",String.valueOf(type));
        basePost(HttpUrl.GetGivePsRerond,map,listener);
    }

    //获取收藏动态列表
    public void getCollectDynamic(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        map.put("loginuid", MyApp.uid);
        basePost(HttpUrl.GetCollectDynamicList,map,listener);
    }

    public void searchUser(String keyWord,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        map.put("search", keyWord);
        basePost(HttpUrl. SearchUserNewth,map,listener);
    }

    public void searchGroup(int type,String keyWord,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        map.put("search", keyWord);
        map.put("type", String.valueOf(type));
        basePost(HttpUrl.SearchGroup,map,listener);
    }

    public void getNoticeData(int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page", String.valueOf(page));
        map.put("type", String.valueOf(type));
        basePost(HttpUrl.GetPresentMsg,map,listener);
    }

    public void getNoticeRedBagData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.redBagMsg,map,listener);
    }

    public void getNoticeTopCardData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid",MyApp.uid);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.getTopcardUsedLb,map,listener);
    }

    public void retryPk(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("invite_anchor_uid",uid);
        basePost(HttpUrl.retryPk,map,listener);
    }

    public void getPushTopUsData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("fuid", MyApp.uid);
        map.put("page", String.valueOf(page));
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.getTopcardUsedRs,map,listener);
    }

    public void getPushTopUseData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", String.valueOf(page));
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.getTopcardUsedRs,map,listener);
    }

    public void getThumbUpUsData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetLaudedList,map,listener);
    }

    public void responsePkAgain(int type,String mPkAnchorId,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        map.put("invite_anchor_uid", mPkAnchorId);
        basePost(HttpUrl.responsePk,map,listener);
    }

    public void getAtUsData(int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.AtUsData, map, listener);
    }

    public void getStampStatistical(int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", String.valueOf(page));
        switch (type) {
            case 1:
                basePost(HttpUrl.GetStampPaymentRs, map, listener);
                break;
            case 2:
                basePost(HttpUrl.GetBasicStampGiveRs, map, listener);
                break;
            case 3:
                basePost(HttpUrl.GetStampUsedRs, map, listener);
                break;
        }
    }

    public void getPushTopBuyData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.getTopcardPaymentRs, map, listener);
    }

    /**
     * 获取群列表
     * @param type 0-推荐 1-附近 2-我加入的 3-我创建的  6-可认领 7-已认领
     * @param whatSexual
     * @param page
     * @param listener
     */
    public void getNormalGroupData(int type,String whatSexual,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", String.valueOf(page));
        map.put("type", String.valueOf(type));
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("filterabnormal", whatSexual);
        basePost(HttpUrl.GetGroupListFilter, map, listener);
    }

    public void getGroupMemberList(String gid,String keyWord,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", gid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        map.put("search", keyWord);
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.GetGroupMember, map, listener);
    }

    public void getGroupMemberBaseList(String gid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", gid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("pagetype", "1");
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.GetGroupMember, map, listener);
    }

    public void getGroupMemberList(String gid,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", gid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        map.put("login_uid", MyApp.uid);
        basePost(HttpUrl.GetGroupMember, map, listener);
    }

    /**
     * 修改群名片
     * @param gid
     * @param uid
     * @param nickname
     * @param listener
     */
    public void editGroupUserNickName(String gid,String uid,String nickname,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("gid", gid);
        map.put("cardname", nickname);
        basePost(HttpUrl.editGroupCardName, map, listener);
    }

    public void muteGroupUser(String uid,long time,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("ugid",uid);
        map.put("blockingalong", String.valueOf(time));
        basePost(HttpUrl.BanSomeOne, map, listener);
    }

    public void cancelMuteGroupUser(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("ugid",uid);
        basePost(HttpUrl.RmoveGagSomeOne, map, listener);
    }

    public void kickOutGroupUser(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("ugid",uid);
        basePost(HttpUrl.TiGroup, map, listener);
    }

    public void setManagerGroupUser(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("ugid",uid);
        basePost(HttpUrl.SetManager, map, listener);
    }

    public void cancelManagerGroupUser(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("ugid",uid);
        basePost(HttpUrl.CancelManager, map, listener);
    }

    public void getNearUser(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("loginid",MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", String.valueOf(page));
        map.put("type","1");
        map.put("layout", "1");
        basePost(HttpUrl.UserListNewth, map, listener);
    }

    public void getSocialUser(String uid,int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid",MyApp.uid);
        map.put("uid",uid);
        map.put("page",String.valueOf(page));
        map.put("type",String.valueOf(type));
        basePost(HttpUrl.GetFollewingList, map, listener);
    }

    public void sendGroupInvite(String gid,String toUids,String toGids,HttpCodeMsgListener listener) {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid",gid);
        map.put("uidstr",toUids);
        map.put("gidstr",toGids);
        basePost(HttpUrl.InviteOneIntoGroupNew, map, listener);
    }

    public void getVipBuyList(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.vipbuylist, map, listener);
    }

    public void getVipReceiveList(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.vipgiftlist, map, listener);
    }

    public void getExchangeRecord(int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetExchangeRecord, map, listener);
    }

    //关注 取关 用户
    public void followUser(boolean isFollow,String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fuid",uid);
        if (isFollow) {
            basePost(HttpUrl.FollowOneBox, map, listener);
        } else {
            basePost(HttpUrl.OverFollow, map, listener);
        }
    }

    public void getSocialUser(String uid,String keyWord,int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid",MyApp.uid);
        map.put("uid",uid);
        map.put("name",keyWord);
        map.put("page",String.valueOf(page));
        map.put("type",String.valueOf(type));
        basePost(HttpUrl.GetFollewingList, map, listener);
    }

    public void getQuietFollowUser(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.getFollowListQueit, map, listener);
    }

    public void getComplaintRecordList(String type,String uid,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("type",type);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.RecordList, map, listener);
    }


    public void getFollowAnchor(int page,HttpCodeMsgListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        basePost(HttpUrl.getFollowAnchor,map,httpListener);
    }

    public void getHomepageUser(int type, int page, HomePageUserScreen userScreen,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("loginid",MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page",String.valueOf(page));
        map.put("type",String.valueOf(type));
        map.put("layout", "1");
        if (userScreen != null) {
            if (!TextUtil.isEmpty(userScreen.getSex())) {
                map.put("sex", userScreen.getSex());
            }
            if (!TextUtil.isEmpty(userScreen.getRole())) {
                map.put("role", userScreen.getRole());
            }
            if (!TextUtil.isEmpty(userScreen.getSexual())) {
                map.put("sexual", userScreen.getSexual());
            }
            if (!TextUtil.isEmpty(userScreen.getAge())) {
                map.put("age", userScreen.getAge());
            }
            if (!TextUtil.isEmpty(userScreen.getEdu())) {
                map.put("culture", userScreen.getEdu());
            }
            if (!TextUtil.isEmpty(userScreen.getSalary())) {
                map.put("monthly", userScreen.getSalary());
            }
            if (!TextUtil.isEmpty(userScreen.getWant())) {
                map.put("want", userScreen.getWant());
            }
            if (!TextUtil.isEmpty(userScreen.getAuth())) {
                map.put("realname", userScreen.getAuth());
            }
            if (!TextUtil.isEmpty(userScreen.getOnline())) {
                map.put("onlinestate", userScreen.getOnline());
            }
        }
        basePost(HttpUrl.UserListNewth, map, listener);
    }

    public void getHomepageTopicList(int type, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page", String.valueOf(page));
        if (type == 0) {
            basePost(HttpUrl.GetFollowTopicList,map,listener);
        } else {
            if (type <= 7) {
                map.put("type", "0");
                map.put("pid", String.valueOf(type));
            } else {
                map.put("type", String.valueOf(type - 6));
            }
            basePost(HttpUrl.GetTopicList, map, listener);
        }
    }

    public void getJoinTopicList(String uid, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("page", String.valueOf(page));
        map.put("type", "3");
        basePost(HttpUrl.GetTopicList, map, listener);
    }

    public void getAttentionTopic(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetFollowTopicList,map,listener);
    }

    public void getShortVideoList(String mode,String sex, String sexual,String lastid,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", "7");
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastid);
        map.put("mode",mode);
        basePost(HttpUrl.DynamicShortVideoList,map,listener);

    }

    public void sendC2CRedBag(String toUid,String beans,String content,String orderId,boolean isOpen,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid",toUid);
        map.put("num",beans);
        map.put("orderid",orderId);
        map.put("content",content);
        map.put("dalaba",isOpen?"1":"2");
        basePost(HttpUrl.giveRedbag,map,listener);
    }

    public void sendGroupRedBag(String toUid,String num,String beans,String content,String orderId,boolean isOpen,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("groupid",toUid);
        map.put("num",beans);
        map.put("nums",num);
        map.put("orderid",orderId);
        map.put("content",content);
        map.put("dalaba",isOpen ? "1":"2");
        basePost(HttpUrl.qunGiveRedbag,map,listener);
    }

    public void getLiveRedBagRankList(int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.liveRedBagRank, map, listener);
    }

    public void getFollowMessage(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetFollowMessage, map, listener);
    }

    public void deleteFollowMessage(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        basePost(HttpUrl.DelFollowMessage, map, listener);
    }

    public void getUnReadMessage(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("type","0");
        basePost(HttpUrl.GetUnreadNum, map, listener);
    }

    public void getUnreadComment(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetCommentedList, map, listener);
    }

    public void getPlayBackList(int type,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("sort_type",String.valueOf(type));
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.getLiveLogList, map, listener);
    }

    public void getPlayBackListByTid(String tid,int page,int  type ,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("tid",tid);
        map.put("page",String.valueOf(page));
        map.put("sort_type",type+"");
        basePost(HttpUrl.getLiveLogList, map, listener);
    }

    public void newAnchorList(int page, HttpCodeMsgListener httpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        basePost(HttpUrl.newAnchorList,map,httpListener);
    }

    public void getLiveChat(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.getLiveChatSetting,map,listener);
    }

    public void setLiveChat(String state,String audioPrice,String videoPrice,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtil.isEmpty(state)) {
            map.put("chat_status", state);
        }
        if (!TextUtil.isEmpty(audioPrice)) {
            map.put("chat_voice_beans", audioPrice);
        }
        if (!TextUtil.isEmpty(videoPrice)) {
            map.put("chat_video_beans", videoPrice);
        }
        basePost(HttpUrl.setLiveChat,map,listener);
    }

    public void getRewardUsData(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.GetRewardedList,map,listener);
    }

    public void getAnchorChatAuth(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        basePost(HttpUrl.userChatState,map,listener);
    }

    public void startLinkUser(String uid,int type,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", uid);
        map.put("type",String.valueOf(type));
        basePost(HttpUrl.startLinkUser,map,listener);
    }

    public void getChatAnchorList(String sex,String sortType,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("sex",sex);
        map.put("sort_type",sortType);
        basePost(HttpUrl.linkChatList,map,listener);
    }

    public void getBuyLiveLogList(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.buyLiveLogList,map,listener);
    }


    public void getGroupingFriendList(String gid,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("fgid",gid);
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.getgfuserslist,map,listener);
    }

    //获取 分组
    public void getFriendGrouping(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        basePost(HttpUrl.friendgrouplist,map,listener);
    }

    //获取 分组
    public void getChatFriendGrouping(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("check_svip","1");
        basePost(HttpUrl.friendgrouplist,map,listener);
    }

    //新建 分组
    public void addFriendGrouping(String name,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fgname",name);
        basePost(HttpUrl.addfriendgroup,map,listener);
    }

    //排序 分组
    public void sortFriendGrouping(String sort,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("sort",sort);
        basePost(HttpUrl.friendgroupsort,map,listener);
    }

    //删除 分组
    public void delFriendGrouping(String id,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fgid",id);
        basePost(HttpUrl.delfriendgroup,map,listener);
    }

    public void addGroupingMember(String fgid,String ids,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fgid",fgid);
        map.put("fuid",ids);
        basePost(HttpUrl.setfgusers,map,listener);
    }

    public void updateGroupingName(String fgid,String name,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fgid",fgid);
        map.put("fgname",name);
        basePost(HttpUrl.updfriendgroup,map,listener);
    }

    public void EditPassWord(String oldpwd,String newpwd,String code,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("oldpwd",oldpwd);
        map.put("newpwd",newpwd);
        map.put("code",code);
        basePost(HttpUrl.EditPwd,map,listener);
    }

    public void feedBack(String suggest,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("suggest",suggest);
        basePost(HttpUrl.Suggest,map,listener);
    }

    //本场/月榜
    public void getGiftRank(String anchor_uid,String type,int page,HttpCodeMsgListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("anchor_uid", anchor_uid);
        map.put("type",type);
        map.put("page" ,page + "");
        basePost(HttpUrl.giftRank, map, httpListener);
    }

    //删除直播间标题封面
    public void deleteLiveTitleOrCover(String anchorId,String deleteType,HttpCodeMsgListener httpListener) {
        Map<String,String> map=new HashMap<>();
        map.put("token", MyApp.token);
        map.put("anchor_uid",anchorId);
        map.put("delete_type" ,deleteType);
        basePost(HttpUrl.delete_live_info, map, httpListener);
    }

    public void delGroupingMember(String fgid,String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fgid",fgid);
        map.put("fuid",uid);
        basePost(HttpUrl.delfgusers,map,listener);
    }

    public void getLotteryDrawGiftList(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.lotteryDrawGiftList,map,listener);
    }

    public void startLotteryDraw(int type,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type",String.valueOf(type));
        basePost(HttpUrl.startLotteryDraw,map,listener);
    }

    public void getLotteryDrawRecord(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.lotteryDrawRecord,map,listener);
    }

    public void getLotteryDrawMarquee(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.lotteryDrawMarquee,map,listener);
    }

    public void getTopicPage(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.topicPageList,map,listener);
    }

    public void getPidTopicList(String pid,int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("pid",pid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.pidTopicList,map,listener);
    }

    public void getSynthesisGiftList(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.synthesisGiftList,map,listener);
    }

    public void getMergeGiftInfo(String giftId,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gift_id",giftId);
        basePost(HttpUrl.mergeGiftInfo,map,listener);
    }

    public void synthesisGift(String giftId,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gift_id",giftId);
        basePost(HttpUrl.doSynthesisGift,map,listener);
    }

    public void getSynthesisRecord(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.synthesisRecord,map,listener);
    }


    public void getPunishment(int page,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.GetPunishmentList,map,listener);
    }

    public void getWarnUserData(int page, String type,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", page + "");
        basePost(HttpUrl.GetOutOfLineList,map,listener);
    }

    public void  addUserToGroup(String fuid,String fgid,HttpCodeMsgListener listener){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        map.put("fgid", fgid);
        basePost(HttpUrl.setfgsusers,map,listener);
    }
    //获取 分组
    public void getFriendGrouping(String fuid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("fuid",fuid);
        basePost(HttpUrl.friendgrouplist,map,listener);
    }
    public void uploadImage(String path,HttpCodeMsgListener listener) {
        OkHttpRequestManager.getInstance().upload(path, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess(response,"上传成功");
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFail(4000,throwable.getMessage());
            }
        });
    }

    public void uploadImageWithWaterMark(String path,HttpCodeMsgListener listener) {
        OkHttpRequestManager.getInstance().uploadWithWaterMark(path, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                listener.onSuccess(response,"上传成功");
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFail(4000,throwable.getMessage());
            }
        });
    }

    public void setManagerMark(String fuid,String content,String pic,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("loginid", MyApp.uid);
        map.put("uid", fuid);
        map.put("content", content);
        map.put("sypic", pic);
        basePost(HttpUrl.editAdminmrak,map,listener);
    }

    public void setUserMark(String fuid,String content,String pic,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        map.put("lmarkname", content);
        map.put("sypic", pic);
        basePost(HttpUrl.lmarkName,map,listener);
    }

    public void editPunishment(String pid,String content,String images,boolean isOpen,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("pid", pid);
        map.put("blockreason", content);
        map.put("image", images);
        map.put("is_show", isOpen ? "1" : "0");
        basePost(HttpUrl.EditPunishment,map,listener);
    }

    public void getLargeGift(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.largeGift,map,listener);
    }


    public void updateFreeLink(String type,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type",type);
        basePost(HttpUrl.updateFreeLink,map,listener);
    }

    public void dailySign(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.dailySign,map,listener);
    }

    public void getSignState(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.signState,map,listener);
    }

    public void updateLocationAndLastLoginTime() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("lat",MyApp.lat);
        map.put("lng",MyApp.lng);
        map.put("city",MyApp.city);
        map.put("province",MyApp.province);
        map.put("addr",MyApp.address);
        basePost(HttpUrl.LogintimeAndLocation, map, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) { }

            @Override
            public void onFail(int code, String msg) { }
        });
    }

    public void getNoticeUnreadMessage(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.noticeMessage,map,listener);
    }

    public void exchangeBeans(int beans,boolean isAdd,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("beans_receive", String.valueOf(beans * 2));
        map.put("beans", String.valueOf(beans));
        map.put("caifu", isAdd ?"0":"1");
        basePost(HttpUrl.exBeans,map,listener);
    }

    public void getIdCardAuthState(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.getrealidstate,map,listener);
    }

    public void getSelfieState(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.selfieState,map,listener);
    }

    public void setSelfieOpenState(boolean isOpen,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("status", isOpen ?"vip":"nobody");
        basePost(HttpUrl.SetRealnameState,map,listener);
    }

    public void commitSelfieAuth(String pic,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("card_face",pic);
        basePost(HttpUrl.selfieAuth,map,listener);
    }

    public void getGroupInfo(String gid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid",gid);
        map.put("uid",MyApp.uid);
        basePost(HttpUrl.GetGroupinfo,map,listener);
    }

    public void joinGroup(String gid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid",gid);
        map.put("uid",MyApp.uid);
        basePost(HttpUrl.AgreeIntoGroupOne,map,listener);
    }

    public void clearGroupMember(String gid,String day,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("day",day);
        basePost(HttpUrl.shotOffmore,map,listener);
    }

    public void dissolutionGroup(String gid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("gid",gid);
        basePost(HttpUrl.DelGroup,map,listener);
    }

    public void getEditPersonInfo(String uid,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",  uid == null ? MyApp.uid : uid);
        basePost(HttpUrl.GetMineinfodetailNew,map,listener);
    }

    public void editPersonInfo(String uid,String introduce,String photo_charge_time,String nickname,
                               String birthday,String head_pic,String tall,String weight,String role,
                               String sex,String sexual,String want,String level,String along,String experience,
                               String culture,String monthly,String attribute,String photo_rule,String picString,
                               HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",   uid);
        map.put("login_uid",  MyApp.uid);
        map.put("introduce", introduce );
        map.put("photo_charge_time", "0" );
        map.put("nickname", nickname );
        map.put("birthday", birthday );
        map.put("head_pic", head_pic );
        map.put("tall", tall );
        map.put("weight", weight );
        map.put("role", role );
        map.put("sex", sex );
        map.put("sexual", sexual );
        map.put("want", want );
        map.put("level", level );
        if (!TextUtil.isEmpty(along)){
            map.put("along", along );
        }
        if (!TextUtil.isEmpty(experience)){
            map.put("experience", experience );
        }
        map.put("culture", culture );
        map.put("monthly", monthly );
        map.put("attribute", attribute );
        map.put("photo_rule", photo_rule );
        map.put("photo", picString );
        basePost(HttpUrl.EditInfo,map,listener);
    }


    public void getStampByShare(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.GetShareStamp,map,listener);
    }

    public void reportScreenCapture(boolean isScreenCapture) {
        Map<String, String> map = new HashMap<>();
        map.put("status",isScreenCapture?"1":"0");
        basePost(HttpUrl.screenCapture, map, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    public void login(String loginJson,HttpCodeListener listener) {
        basePost(HttpUrl.Login,loginJson,listener);
    }

    //第三方登录
    public void thirdPartyLogin(int type,String unionId,String openId,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtil.isEmpty(unionId)) {
            map.put("unionid",unionId);
        }
        if (!TextUtil.isEmpty(openId)) {
            map.put("openid", openId);
        }
        map.put("channel",String.valueOf(type));
        map.put("device_token", GetDeviceIdUtils.getSN(MyApp.getInstance()));
        map.put("new_device_brand", SystemUtil.getDeviceBrand()+"-"+SystemUtil.getSystemModel());
        map.put("new_device_version", SystemUtil.getSystemVersion());
        try {
            map.put("new_device_appversion", VersionUtils.getVersion(MyApp.getInstance()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        basePost(HttpUrl.ChargeOpenid,map,listener);
    }


    public void  getHighList(String sex,String page ,String  limit,HttpCodeMsgListener listener){
        Map<String, String> map = new HashMap<>();
        map.put("sex",sex);
        map.put("page",page);
        map.put("limit",limit);
        basePost(HttpUrl.getHighList,map,listener);
    }

    public  void  getHighUserDetail (String topId,String  isEditPage,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("top_id",topId);
        map.put("is_edit_page",isEditPage);
        basePost(HttpUrl.HighUserDetail,map,listener);
    }

    public  void  updatePushHighPhoto(String topId,String top_photo_auth ,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("top_id",topId);
        map.put("top_photo_auth",top_photo_auth);
        basePost(HttpUrl.updateHighPhoto,map,listener);
    }


    public  void  getHighRule (HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        basePost(HttpUrl.getHighRule,map,listener);
    }

    public  void  submitHighInfo (String topId,String topPhoto,String photoStates,String  topDesc,String topRedDesc,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("top_id",topId);
        map.put("top_photo",topPhoto);
        map.put("top_photo_auth",photoStates);
        map.put("top_desc",topDesc);
        map.put("top_red_desc",topRedDesc);
        basePost(HttpUrl.submitHighInfo,map,listener);
    }

    public  void  submitHighAuthInfo(String audit_type, String audit_id, String name, String desc, String img,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("audit_type",audit_type);
        map.put("audit_id",audit_id);
        map.put("name",name);
        map.put("desc",desc);
        map.put("img",img);
        basePost(HttpUrl.submitHighAuthInfo,map,listener);
    }
    public  void  submitHighAuthInfo(HashMap<String,String>  map,HttpCodeMsgListener listener){
        basePost(HttpUrl.submitHighAuthInfo,map,listener);
    }

    public  void  checkSelefTop (HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        basePost(HttpUrl.checkSelfTop,map,listener);
    }

    public  void  getHighAuthInfo (String audit_type,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("audit_type",audit_type);
        basePost(HttpUrl.highAuthInfo,map,listener);
    }


    public  void  getRedMenList (HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        basePost(HttpUrl.NewGetMatchmakerInfo,map,listener);
    }

    public  void  deleteHighAuth (String authId,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("audit_id",authId);
        basePost(HttpUrl.deleteHighAuth,map,listener);
    }

    public  void  openHighWechat (String bag_id,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("bag_id",bag_id);
        basePost(HttpUrl.openHighWechat,map,listener);
    }

    public  void  openHighAlipay (String bag_id,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("bag_id",bag_id);
        basePost(HttpUrl.openHighAliPay,map,listener);
    }

    public  void  getTopConfig (HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        basePost(HttpUrl.getTopConfig,map,listener);
    }

    public  void  getMatchList (String topid,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("top_id",topid);
        basePost(HttpUrl.getHighMatchList,map,listener);
    }

    public void getOnAirListNew(HttpCodeMsgListener listener) {
        HashMap<String,String>  map= new HashMap<>();
        basePost(HttpUrl.onAirListNew,map,listener);
    }

    public void getHotAnchorLiveLog(HttpCodeMsgListener listener) {
        HashMap<String,String>  map= new HashMap<>();
        basePost(HttpUrl.hotAnchorLiveLog,map,listener);
    }

    public void getUserLiveLogList(String uid,int page, HttpCodeMsgListener listener) {
        HashMap<String,String>  map= new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.userLiveLogList,map,listener);
    }

    public  void  isChatWithHigh (String topid,HttpCodeMsgListener listener){
        HashMap<String,String>  map= new HashMap<>();
        map.put("top_id",topid);
        basePost(HttpUrl.getHighIsChat,map,listener);
    }

    public  void  getLiveBanner(String type ,HttpCodeMsgListener listener){
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("uid", MyApp.uid);
        basePost(HttpUrl.getliveBanner,map,listener);

    }


    /**
     * 统一赠送礼物
     * @param type 打赏方式  1-动态 2-个人主页 3-回放 4-私聊 5-群聊
     * @param id   打赏id 根据type不同 代表的id也不同
     * @param sendType  打赏礼物类型 0-普通礼物 1-背包礼物 2-盲盒
     * @param giftId    礼物id
     * @param num   礼物数量
     * @param isOpen    是否上大喇叭
     * @param listener
     */
    public void sendGift(int type,String id,int sendType,String giftId,String num,boolean isOpen,HttpCodeMsgListener listener) {
        HashMap<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("id_type",String.valueOf(type));
        map.put("send_type",String.valueOf(sendType));
        map.put("gift_id",giftId);
        map.put("num",num);
        map.put("dalaba",isOpen ? "1" : "2");
        basePost(HttpUrl.normalSendGift,map,listener);
    }

    public void getSystemConfig(HttpCodeMsgListener listener) {
        HashMap<String,String> map = new HashMap<>();
        basePost(HttpUrl.systemConfig,map,listener);
    }

    public void editRedMenRemarks(String loginId,String id,String remarks,HttpCodeMsgListener listener) {
        HashMap<String,String> map = new HashMap<>();
        map.put("login_uid",loginId);
        map.put("id",id);
        map.put("remarks",remarks);
        basePost(HttpUrl.EditRemarks,map,listener);
    }

    /**
     * 高端用户搜索
     *
     * @param keyWord
     * @param page
     * @param listener
     */
    public void searchHighUser(String keyWord, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("keywords", keyWord);
        basePost(HttpUrl.getHighList, map, listener);
    }

    /**
     * 获取用户连线历史明细
     *
     * @param uid
     * @param page
     * @param listener
     */
    public void getUserLinkHistory(String uid, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("anchor_uid", uid);
        basePost(HttpUrl.linkHistory, map, listener);
    }

    public void getBlockingList(String uid, String type, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        map.put("type", type);
        basePost(HttpUrl.BlockingList, map, listener);
    }

    public void getSignedAnchorInfo(String uid, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", uid);
        basePost(HttpUrl.signedAnchorInfo, map, listener);
    }


    public void getHighDetail(String type, int page, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("history_type", type);
        map.put("page", page + "");
        basePost(HttpUrl.getHighDetail, map, listener);
    }

    public void adminMatchPair(String topId, String targetUid, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", topId);
        map.put("uid", targetUid);
        basePost(HttpUrl.adminMatchPair, map, listener);
    }

    public void adminOpenHigh(String uid, String openType, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("open_type", openType);
        basePost(HttpUrl.adminOpenHigh, map, listener);
    }

    public void adminCloseHigh(String top_id, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        basePost(HttpUrl.adminCloseHigh, map, listener);
    }

    public void adminOpenRedService(String uid, String openType, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("open_type", openType);
        basePost(HttpUrl.adminOpenRedService, map, listener);
    }

    public void adminCloseRedService(String top_id, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        basePost(HttpUrl.adminCloseRedService, map, listener);
    }

    public void adminMangerTopDesc(String top_id, String field, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        map.put("field", field);
        basePost(HttpUrl.adminMangerTopDesc, map, listener);
    }

    public void adminCancleChatAndInfo(String top_id, String disable_type, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        map.put("disable_type", disable_type);
        basePost(HttpUrl.adminCancleChatAndInfo, map, listener);
    }
    public void adminBannedCahtAndInfo(String top_id, String disable_type,String time_type,String content,String is_show,String pic, HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        map.put("disable_type", disable_type);
        map.put("time_type", time_type);
        map.put("content", content);
        map.put("is_show", is_show);
        map.put("pic", pic);
        basePost(HttpUrl.adminBannedChatAndInfo, map, listener);
    }
    public void adminCancleAudit(String top_id,String cancel_audit_desc,  HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        map.put("cancel_audit_desc", cancel_audit_desc);
        basePost(HttpUrl.adminCancleAudit, map, listener);
    }
    public void adminHideUser(String top_id,String show_hidden_desc,  HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("top_id", top_id);
        map.put("show_hidden_desc", show_hidden_desc);
        basePost(HttpUrl.adminHideUser, map, listener);
    }

    //获取主播回放列表
    public void getAnchorRecordList(String uid,int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_id",uid);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.anchorRecordList, map, listener);
    }

    //呼唤套餐
    public void getCallItem(HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.callItem, map, listener);
    }

    //获取呼唤状态
    public void getConversationCallStatus(HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.callStatus, map, listener);
    }

    //魔豆购买呼唤
    public void buyCallByBeans(String bagId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("bag_id",bagId);
        basePost(HttpUrl.buyCallByBeans, map, listener);
    }

    //获取呼唤成员列表
    public void getCallList(int page,String sortType,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("lng", MyApp.lng);
        map.put("lat", MyApp.lat);
        map.put("sort_type",sortType);
        basePost(HttpUrl.callList, map, listener);
    }

    //开启呼唤
    public void openCall(int num,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("times",String.valueOf(num));
        basePost(HttpUrl.openCall, map, listener);
    }

    //获取用户是否可以编辑资料
    public void getEditPermission(String uid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.isEditPermission, map, listener);
    }

    //获取呼唤配置信息
    public void getCallConfig(HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.callConfig, map, listener);
    }

    //获取呼唤使用记录
    public void getCallUseHistory(int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.callUseHistory, map, listener);
    }

    //获取呼唤使用记录
    public void getCallBuyHistory(int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.callBuyHistory, map, listener);
    }


    public void getCarList( HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.getCarlist, map, listener);
    }

    public void getCarInfo( String id,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("animation_id",id);
        basePost(HttpUrl.getCarInfo, map, listener);
    }

    public void exchangeCar( String goodsId,String  buyId ,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("animation_id",goodsId);
        map.put("product_id",buyId);
        String json = GsonUtil.getInstance().toJson(map);
        basePost(HttpUrl.exchangeCar, json, listener);
    }

    public void getMyCar(HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        basePost(HttpUrl.myCarList, map, listener);
    }

    public void switchCar(String carId,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("record_id",carId);
        basePost(HttpUrl.switchCar, map, listener);
    }

    public void carRecord(int page ,HttpCodeMsgListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("page",page+"");
        basePost(HttpUrl.carRecord, map, listener);
    }

    //呼唤 - 微信支付
    public void getCallWeChatOrder(String bagId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("bag_id",bagId);
        basePost(HttpUrl.callWeChatOrder,map,listener);
    }

    //呼唤 - 支付宝支付
    public void getCallAliPayOrder(String bagId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("bag_id",bagId);
        basePost(HttpUrl.callAliPayOrder,map,listener);
    }

    //获取 - 粉丝团入团券信息
    public void getFansClubGiftInfo(String gid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("gid",gid);
        basePost(HttpUrl.fanClubGiftInfo,map,listener);
    }

    //购买粉丝券
    public void buyFansClubTicket(String gid,String anchorId,String giftId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("gid",gid);
        map.put("anchor_uid",anchorId);
        map.put("gift_id",giftId);
        basePost(HttpUrl.buyFanClubGift,map,listener);
    }
    //开通粉丝团
    public void openFansClub(String fanclub_card,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("fanclub_card",fanclub_card);
        basePost(HttpUrl.openFanClub,map,listener);
    }

    //获取粉丝团详情
    public void getFanClubInfo(String uid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.fanClubInfo,map,listener);
    }

    //获取我的粉丝团列表
    public void getFanClubList(int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.fanClubList,map,listener);
    }

    //获取粉丝团成员列表
    public void getFanClubMemberList(String uid,int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("sort","1");
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.fanClubMemberList,map,listener);
    }

    //显示或者隐藏粉丝牌
    public void showOrHideFansClub(String gid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("gid",gid);
        basePost(HttpUrl.hideOrShowFanClub,map,listener);
    }

    //获取 粉丝团排行
    public void getFansClubRankList(int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.fanClubRank,map,listener);
    }

    //修改勋章名称
    public void changeFansClubName(String name,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("fanclub_card",String.valueOf(name));
        basePost(HttpUrl.setAnchorClubCard,map,listener);
    }

    //开通粉丝团信息
    public void getOpenClubInfo(HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.openClubInfo,map,listener);
    }

    //分组是否在聊天显示
    public void getGroupingConfig(HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.groupingConfig,map,listener);
    }

    //设置分组是否在聊天显示
    public void setGroupingConfig(String status,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("status",status);
        basePost(HttpUrl.setGroupingConfig,map,listener);
    }

    //获取推广详情
    public void getCallDetail(String hid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        if (TextUtil.isEmpty(hid)) {
            map.put("history_id", hid);
        }
        basePost(HttpUrl.callDetails,map,listener);
    }

    //获取创建的群组
    public void getCreateGroupList(String uid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        map.put("lat",MyApp.lat);
        map.put("lng",MyApp.lng);
        basePost(HttpUrl.createGroupList,map,listener);
    }

    //获取高端id
    public void getHighIdByUid(String uid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.getHighIdByUid,map,listener);
    }

    //高端聊天
    public void startHighGroup(String topId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("top_id",topId);
        basePost(HttpUrl.startHighGroup,map,listener);
    }


    //删除高端聊天
    public void deleteMsgRecord(String deleteType,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("delete_type",deleteType);
        basePost(HttpUrl.deleteMsgRecord,map,listener);
    }
    //高端列表聊天
    public void startHighGroupChat(String im_gid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("im_gid",im_gid);
        basePost(HttpUrl.startHighGroupChat,map,listener);
    }

    //回放评论列表
    public void getPlayBackCommentList(String logId,int page,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("live_log_id",logId);
        map.put("page",String.valueOf(page));
        basePost(HttpUrl.livePlayBackCommentList,map,listener);
    }

    //评论回放
    public void addPlayBackComment(String logId,String content,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("live_log_id",logId);
        map.put("content",content);
        basePost(HttpUrl.livePlayBackAddComment,map,listener);
    }

    //删除回放评论
    public void deletePlayBackComment(String commentId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("comment_id",commentId);
        basePost(HttpUrl.livePlayBackDeleteComment,map,listener);
    }

    //举报配置
    public void getReportConfig(int type,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("type_id",String.valueOf(type));
        basePost(HttpUrl.livePlayBackReportConfig,map,listener);
    }

    //举报回放评论
    public void reportPlayBackComment(String id,String content,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("comment_id",id);
        map.put("reason",content);
        basePost(HttpUrl.livePlayBackReport,map,listener);
    }

    //高端列表聊天
    public void setHighUserFollow(String topId,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("top_id",topId);
        basePost(HttpUrl.setHighUserFollow,map,listener);
    }

    //高端关注列表
    public void highFollowList(String page,String keywords,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",page);
        map.put("keywords",keywords);
        basePost(HttpUrl.highFollowList,map,listener);
    }

    //高端粉丝列表
    public void highFansList(String page,String keywords,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("page",page);
        map.put("keywords",keywords);
        basePost(HttpUrl.highFansList,map,listener);
    }
    //  黑V查看身份证
    public void getIdCardPhoto(String uid,HttpCodeListener listener) {
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        basePost(HttpUrl.getIDCardPhoto, map, listener);
    }

    //关闭连麦者
    public void kickMicUser(String anchorId,String uid,HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        map.put("anchor_uid",anchorId);
        map.put("uid",uid);
        basePost(HttpUrl.liveKickMicUser,map,listener);
    }


    //连线状态
    public void getLinkSettingStatus(HttpCodeMsgListener listener) {
        Map<String,String> map = new HashMap<>();
        basePost(HttpUrl.getLinkSettingStatus,map,listener);
    }
}



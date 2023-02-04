package com.aiwujie.shengmo.qnlive.utils;

import android.text.TextUtils;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.net.HttpHelper;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.IMProtocol;

import org.json.JSONException;
import org.json.JSONObject;

public class LiveMessageHelper {
    private LiveMessageHelper liveMessageHelper;

    public static LiveMessageHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final LiveMessageHelper INSTANCE = new LiveMessageHelper();
    }

    public static final int IMCMD_PRAISE              = 4;   // 点赞消息
    public static final int IMCMD_DANMU               = 5;   // 弹幕消息
    public static final int IMCMD_GIFT                = 6;   // 礼物消息
    //直播间发送的统一cmd
    public static final int IMCMD_CUSTOM              = 233; // 自定义消息

    public static final int IMCMD_ANNOUNCEMENT        = 100; // 直播间通告消息
    public static final int IMCMD_DANMU_NEW           = 101; // 直播间新弹幕消息



    String isGroupAdmin;
    public void sendLiveMessage(String roomId,String message) {
        //判断当前发送用户的Vip角色
        int vipRole = TUIKitLive.getLoginUserInfo().getRole();
        String customMessage = getCusMessage(message,getGroupRole(isGroupAdmin, MyApp.uid),vipRole);
        String data = IMProtocol.getCusMsgJsonStr(String.valueOf(IMCMD_CUSTOM), customMessage);
        V2TIMManager.getInstance().sendGroupCustomMessage(data.getBytes(), roomId, V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //发送直播间消息
    public void sendLiveMessage(String roomId,ChatEntity entity,OnMessageCallback onMessageCallback) {
        //判断当前发送用户的Vip角色
        String customMessage = getCusMessage(entity);
        String data = IMProtocol.getCusMsgJsonStr(String.valueOf(IMCMD_CUSTOM), customMessage);
        V2TIMManager.getInstance().sendGroupCustomMessage(data.getBytes(), roomId, V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                if (onMessageCallback != null) {
                    onMessageCallback.onSendSuc();
                }
            }

            @Override
            public void onError(int i, String s) {
                if (onMessageCallback != null) {
                    onMessageCallback.onSendFail(i + s);
                }
            }
        });

    }


    public void sendPraiseMessage(String roomId) {
        String data = IMProtocol.getCusMsgJsonStr(String.valueOf(IMCMD_PRAISE), "");
        V2TIMManager.getInstance().sendGroupCustomMessage(data.getBytes(), roomId, V2TIMMessage.V2TIM_PRIORITY_LOW, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
//                if (onMessageCallback != null) {
//                    onMessageCallback.onSendSuc();
//                }
            }

            @Override
            public void onError(int i, String s) {
//                if (onMessageCallback != null) {
//                    onMessageCallback.onSendFail(i + s);
//                }
            }
        });
    }

    /**
     * 判断当前发送用户的角色
     * (场控/主播/普通标识)
     * 0-普通  1-场控  2-主播
     * @param is_group_admin
     * @param uid
     * @return
     */
    public  String getGroupRole(String is_group_admin,String uid){
        String live_group_role = "";
        if ("1".equals(is_group_admin)){
            live_group_role = "1";
        } else {
            String selfUserId = ProfileManager.getInstance().getUserModel().userId;
            if(uid.equals(selfUserId)){
                live_group_role = "2";
            }else{
                live_group_role = "0";
            }
        }
        return live_group_role;
    }

    public  String getCusMessage(String text,String live_group_role,int live_user_role) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", text);
            jsonObject.put("live_group_role", live_group_role);
            jsonObject.put("live_user_role",live_user_role);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getCusMessage(ChatEntity entity) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text", entity.getContent());
            jsonObject.put("live_group_role", entity.getLive_group_role());
            jsonObject.put("live_user_role",entity.getLive_user_role());
            jsonObject.put("wealth_val",entity.getWealth_val());
            jsonObject.put("wealth_val_switch",entity.getWealth_val_switch());
            jsonObject.put("name_color",entity.getName_color());
            jsonObject.put("user_level",entity.getUser_level());
            jsonObject.put("anchor_level",entity.getAnchor_level());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public static ChatEntity buildNormalEntity(String userId,String nickname,String text) {
        ChatEntity entity = new ChatEntity();
        entity.setLevel(-1);
        entity.setSenderName(!TextUtils.isEmpty(nickname) ? nickname : userId);
        entity.setContent(!TextUtils.isEmpty(text) ? text : "");
        entity.setType(Constants.TEXT_TYPE);
        entity.setUid(userId);
        return entity;
    }

    public interface OnMessageCallback {
        void onSendSuc();
        void onSendFail(String msg);
    }
}

package com.tencent.qcloud.tim.tuikit.live.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveJoinAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMessageSignBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.IMProtocol;

/**
 * 消息类型
 */
public class IMUtils {
    private static final String TAG = IMUtils.class.getSimpleName();

    /**
     * 判断当前发送用户的角色
     * (场控/主播/普通标识)
     * 0-普通  1-场控  2-主播
     * @param is_group_admin
     * @param uid
     * @return
     */
    public static String getGroupRole(String is_group_admin,String uid){
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

    /**
     * 发送自定义文本消息
     * @param text
     * @param is_group_admin  //备注：是否场控1是0不是
     * @param uid 当前发送者id
     * String customMsg 备注 json
     * live_group_role 备注 0:普通 1：场控  2：主播
     */
    public static void sendText(TRTCLiveRoom mLiveRoom, String text, String is_group_admin, String uid, final OnTextSendListener onTextSendListener) {
        //判断当前发送用户的角色
        String role =  getGroupRole(is_group_admin,uid);
        //判断当前发送用户的Vip角色
        int vipRole = TUIKitLive.getLoginUserInfo().getRole();
        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_CUSTOM), IMProtocol.getCusMessage(text,role,vipRole), new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                } else {
                    //消息发送成功
                    if (onTextSendListener != null) {
                        onTextSendListener.sendSuc();
                    }
                }
            }
        });
    }

    public static void sendText(TRTCLiveRoom mLiveRoom,ChatEntity entity,final OnTextSendListener onTextSendListener) {
        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_CUSTOM), IMProtocol.getCusMessage(entity), new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                } else {
                    //消息发送成功
                    if (onTextSendListener != null) {
                        onTextSendListener.sendSuc();
                    }
                }
            }
        });
    }


    /**
     * 处理文本消息显示
     * @param userInfo
     * @param text
     * @return
     */
    public static ChatEntity handleTextMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        ChatEntity entity = new ChatEntity();
        entity.setLevel(-1);
        entity.setSenderName(!TextUtils.isEmpty(userInfo.userName) ? userInfo.userName : userInfo.userId);
        entity.setContent(!TextUtils.isEmpty(text) ? text : "");
        entity.setType(Constants.TEXT_TYPE);
        entity.setUid(userInfo.userId);
        return entity;
    }

    /**
     * 处理自定义文本消息显示
     * @param customMsg
     */
    public static ChatEntity handleCustomTextMsg(String sendName,String userId,CustomMessage customMsg) {
        ChatEntity entity = new ChatEntity();
        entity.setLevel(-1);
        entity.setSenderName(!TextUtils.isEmpty(sendName) ? sendName : userId);
        entity.setContent(!TextUtils.isEmpty(customMsg.text) ? customMsg.text : "");
        entity.setLive_group_role(!TextUtils.isEmpty(customMsg.live_group_role) ? customMsg.live_group_role : "");
        entity.setLive_user_role(!TextUtils.isEmpty(customMsg.live_user_role) ? customMsg.live_user_role : "");
        entity.setContentType(customMsg.content_type);
        entity.setContent_text_color(customMsg.content_text_color);
        entity.setWealth_val(customMsg.getWealth_val());
        entity.setName_color(customMsg.getName_color());
        entity.setWealth_val_switch(customMsg.getWealth_val_switch());
        entity.setType(Constants.IMCMD_CUSTOM);
        entity.setUid(userId);
        entity.setUser_level(customMsg.getUser_level());
        entity.setAnchor_level(customMsg.getAnchor_level());
        entity.setBackground_color(customMsg.getBackground_color());
        entity.setFanclub_card(customMsg.getFanclub_card());
        entity.setFanclub_level(customMsg.getFanclub_level());
        entity.setFanclub_status(customMsg.getFanclub_status());
        return entity;
    }

    /**
     * 更新用户信息消息
     * @param userInfo
     * @return
     */
    public static ChatEntity handleUserData(TimCustomMessage userInfo) {
        ChatEntity entity = new ChatEntity();
        entity.setContent(userInfo.content);
        entity.setSenderName(!TextUtils.isEmpty(userInfo.nickname) ? userInfo.nickname : userInfo.uid);
        entity.setLevel(-1);//不显示等级图标
        entity.setIs_group_admin(userInfo.is_group_admin);
        return entity;
    }

    /**
     * 更新公告消息
     * @return
     */
    public static ChatEntity handlePrompt(CustomMessage timCustomRoom, V2TIMMessage v2Message) {
        ChatEntity entity = new ChatEntity();
        entity.setContent(timCustomRoom.text);
        entity.setSenderName(v2Message.getNickName());
        entity.setUid(v2Message.getSender());
        entity.setLevel(-1);//不显示等级图标
        entity.setLive_user_role(timCustomRoom.getLive_user_role());
        entity.setLive_group_role(timCustomRoom.getLive_group_role());
        entity.setUser_level(timCustomRoom.getUser_level());
        entity.setAnchor_level(timCustomRoom.getAnchor_level());
        entity.setName_color(timCustomRoom.getName_color());
        entity.setBackground_color(timCustomRoom.getBackground_color());
        entity.setFanclub_status("2");
        return entity;
    }

    /**
     * 加入直播间消息
     * @return
     */
    public static ChatEntity joinRoom(CustomMessage timCustomRoom, LiveJoinAvChatRoom liveJoinAvChatRoom, String nickname, String uid) {
        ChatEntity entity = new ChatEntity();
        entity.setContent(timCustomRoom.text);
        entity.setSenderName(nickname);
        entity.setUid(uid);
        entity.setLive_group_role(!TextUtils.isEmpty(timCustomRoom.live_group_role) ? timCustomRoom.live_group_role : "");
        entity.setLive_user_role(!TextUtils.isEmpty(timCustomRoom.live_user_role) ? timCustomRoom.live_user_role : "");
        entity.setContentType(timCustomRoom.content_type);
        entity.setContent_text_color(timCustomRoom.content_text_color);
        entity.setWealth_val(timCustomRoom.getWealth_val());
        entity.setName_color(timCustomRoom.getName_color());
        entity.setWealth_val_switch(timCustomRoom.getWealth_val_switch());
        entity.setLevel(-1);//不显示等级图标
        entity.setAnchor_level(timCustomRoom.getAnchor_level());
        entity.setUser_level(timCustomRoom.getUser_level());
        entity.setBackground_color(timCustomRoom.getBackground_color());
        entity.setFanclub_status(liveJoinAvChatRoom.getFanclub_status());
        entity.setFanclub_level(liveJoinAvChatRoom.getFanclub_level());
        entity.setFanclub_card(liveJoinAvChatRoom.getFanclub_card());
        return entity;
    }

    public interface OnTextSendListener {
        void sendSuc();
    }

    public OnTextSendListener onTextSendListener;

    public void setOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.onTextSendListener = onTextSendListener;
    }
}

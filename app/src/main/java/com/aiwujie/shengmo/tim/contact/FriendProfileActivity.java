package com.aiwujie.shengmo.tim.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.MainActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.utils.Constants;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

public class FriendProfileActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_friend_profile_activity);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);

        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
        layout.setOnButtonClickListener(new FriendProfileLayout.OnButtonClickListener() {
            @Override
            public void onStartConversationClick(ContactItemBean info) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType( V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(info.getId());
                String chatName = info.getId();
                if (!TextUtils.isEmpty(info.getRemark())) {
                    chatName = info.getRemark();
                } else if (!TextUtils.isEmpty(info.getNickname())) {
                    chatName = info.getNickname();
                }
                chatInfo.setChatName(chatName);
                Intent intent = new Intent(MyApp.getInstance(), ChatActivity.class);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.instance().startActivity(intent);
            }

            @Override
            public void onDeleteFriendClick(String id) {
                //Intent intent = new Intent(MyApp.instance(), MainActivity.class);
                Intent intent = new Intent(MyApp.instance(), HomePageActivity.class);
                startActivity(intent);
            }
        });
    }

}

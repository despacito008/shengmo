package com.aiwujie.shengmo.tim.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;

import java.lang.ref.WeakReference;

public class ChatManagerHelper {
    private ChatManagerKit managerKit;
    private ChatLayout chatLayout;
    private Context context;
    /**
     * 私有化构造方法
     */
    private ChatManagerHelper() {
    }

    /**
     * 静态内部类做单例
     */
    private static class SingletonHolder {
        private static final ChatManagerHelper chatManagerHelper = new ChatManagerHelper();
    }

    /**
     * 获取单例方法
     *
     * @return
     */
    public static final ChatManagerHelper getInstance() {
        return SingletonHolder.chatManagerHelper;
    }

    public void init(Context context, ChatLayout chatLayout) {
        this.chatLayout = chatLayout;
        this.managerKit = chatLayout.getChatManager();
        this.context = new WeakReference<>(context).get();
    }

    public ChatManagerKit getChatMangerKit() {
        return managerKit;
    }

    public ChatLayout getChatLayout() {
        return chatLayout;
    }

    public Context getContext() {
        return context;
    }




}

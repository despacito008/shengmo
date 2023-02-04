package com.aiwujie.shengmo.tim.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.bean.RedEnvelopesMessageBean;
import com.aiwujie.shengmo.tim.bean.SystemTipsMessageBean;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

public class SystemTipsUIController {

    private static final String TAG = SystemTipsUIController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final String data, MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_system_tips, null, false);
        parent.addMessageItemView(view);
        //parent.addMessageContentView(view);
        SystemTipsMessageBean systemTipsMessageBean = GsonUtil.GsonToBean(data,SystemTipsMessageBean.class);
        TextView tvTips = view.findViewById(R.id.tv_item_message_tim_system_tips);
        if(info.isSelf()) {
            tvTips.setText(systemTipsMessageBean.getContentDict().getSelfShowString());
        } else {
            tvTips.setText(systemTipsMessageBean.getContentDict().getOtherShowString());
        }
    }
}

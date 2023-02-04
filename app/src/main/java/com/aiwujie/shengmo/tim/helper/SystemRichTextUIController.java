package com.aiwujie.shengmo.tim.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.bean.SystemRichTextMessageBean;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemRichTextUIController {
    public static void onDraw(final ICustomMessageViewGroup parent, final String data, final MessageInfo info) {
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_rich_text, null, false);
        parent.addMessageContentView(view);
        TextView tvContent = view.findViewById(R.id.msg_body_tv);
       // tvContent.setText(info.getTimMessage().getTextElem().getText());
        final Context context = view.getContext();
        final SystemRichTextMessageBean systemRichTextMessageBean = GsonUtil.GsonToBean(data,SystemRichTextMessageBean.class);
        //tvContent.setText(Html.fromHtml(systemRichTextMessageBean.getContentDict().getContent()));
        //tvContent.setText(systemRichTextMessageBean.getContentDict().getContent());
        tvContent.setText(matcherSearchText(
                TextUtil.isEmpty(systemRichTextMessageBean.getContentDict().getHighlightWhenMatchKeywords().getKeyword_color())?
                        R.color.boyColor:Color.parseColor(systemRichTextMessageBean.getContentDict().getHighlightWhenMatchKeywords().getKeyword_color()),
                systemRichTextMessageBean.getContentDict().getContent(),
                systemRichTextMessageBean.getContentDict().getHighlightWhenMatchKeywords().getKeyword()));
       // tvContent.setText(matcherSearchText(R.color.redBaoOrange,info.getTimMessage().getTextElem().getText(),systemRichTextMessageBean.getContentDict().getHighlightWhenMatchKeywords().getKeyword()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ToastUtil.show(context,systemRichTextMessageBean.getContentDict().getContent());
                switch (systemRichTextMessageBean.getContentDict().getHighlightWhenMatchKeywords().getKeytype()) {
                    case "1" : //个人
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("uid",systemRichTextMessageBean.getContentDict().getHighlightWhenMatchKeywords().getKeyid());
                        context.startActivity(intent);
                        break;

                }
            }
        });
    }

    public static SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString ss = new SpannableString(text);
        String paWord = Pattern.quote(keyword);
        Pattern pattern = Pattern.compile(paWord);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            final int start = matcher.start();
            final int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ss.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(@NonNull View widget) {
//                    ToastUtil.show(MyApp.getInstance(),start + "---" + end);
//                }
//            },start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
}

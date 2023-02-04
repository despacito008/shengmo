package com.aiwujie.shengmo.tim.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.FallingViewActivity;
import com.aiwujie.shengmo.activity.Group_liwu_list;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.LottieAnimActivity;
import com.aiwujie.shengmo.kt.ui.activity.SVGAAnimActivity;
import com.aiwujie.shengmo.kt.ui.activity.SimpleAnimActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.bean.GiftMessageBean;
import com.aiwujie.shengmo.tim.utils.DemoLog;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftBean;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.trtc.TRTCCloud;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class GiftUIController {

    private static final String TAG = GiftUIController.class.getSimpleName();

    private static Context context;
    private static int[] presents = {R.mipmap.presentnew01, R.mipmap.presentnew02, R.mipmap.presentnew03, R.mipmap.presentnew04, R.mipmap.presentnew05,
            R.mipmap.presentnew06, R.mipmap.presentnew07, R.mipmap.presentnew08, R.mipmap.presentnew09,
            R.mipmap.presentnew10, R.mipmap.presentnew11, R.mipmap.presentnew12, R.mipmap.presentnew13,
            R.mipmap.presentnew14, R.mipmap.presentnew15, R.mipmap.presentnew16, R.mipmap.presentnew17,
            R.mipmap.presentnew18, R.mipmap.presentnew19, R.mipmap.presentnew20, R.mipmap.presentnew21,
            R.mipmap.presentnew22, R.mipmap.presentnew23, R.mipmap.presentnew24, R.mipmap.presentnew25,
            R.mipmap.presentnew26, R.mipmap.presentnew27, R.mipmap.presentnew28, R.mipmap.presentnew29, R.mipmap.presentnew30,
            R.mipmap.presentnew31, R.mipmap.presentnew32};

    private int[] presenttos = {R.mipmap.presentnew28, R.mipmap.presentnew29, R.mipmap.presentnew30, R.mipmap.presentnew31, R.mipmap.presentnew32};

    private static String[] titles = {"棒棒糖", "狗粮", "雪糕", "黄瓜", "心心相印", "香蕉", "口红", "亲一个", "玫瑰花", "眼罩",
            "心灵束缚", "黄金", "拍之印", "鞭之痕", "一飞冲天", "一生一世", "水晶高跟", "恒之光", "666", "红酒", "蛋糕", "钻戒", "皇冠",
            "跑车", "直升机", "游轮", "城堡", "幸运草", "糖果", "玩具狗", "内内", "TT"};


    private static ChatInfo mChatInfo;

    public static void onDraw(ICustomMessageViewGroup parent, final String data, final MessageInfo info) {
        int index = -1;
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.ry_chat_phone_layout, null, false);
        parent.addMessageContentView(view);
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.ry_chat_phone_des_tv);
        ImageView imageView = view.findViewById(R.id.iv_liwuya);
        final GiftMessageBean giftMessageBean = GsonUtil.GsonToBean(data, GiftMessageBean.class);
        final String text = MyApp.instance().getString(R.string.no_support_msg);
        //MessageInfoUtil.buildCustomMessage();
        if (giftMessageBean == null) {
            textView.setText(text);
            return;
        }
        if (info.isSelf()) {
            textView.setTextColor(MyApp.getInstance().getResources().getColor(R.color.white));
        } else {
            textView.setTextColor(MyApp.getInstance().getResources().getColor(R.color.black));
        }

        textView.setText(giftMessageBean.getContentDict().getGiftText());
        if (TextUtil.isEmpty(giftMessageBean.getContentDict().getGiftUrl())) { //老版
            for (int j = 0; j < titles.length; j++) {
                if (titles[j].equals(giftMessageBean.getContentDict().getImageName())) {
                    imageView.setImageResource(presents[j]);
                    index = j;
                }
            }
        } else {
            Glide.with(MyApp.getInstance()).load(giftMessageBean.getContentDict().getGiftUrl()).into(imageView);
        }


        view.setClickable(true);
        final int finalIndex = index;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data == null) {
                    DemoLog.e(TAG, "Do what?");
                    ToastUtil.toastShortMessage(text);
                    return;
                }
                if (ChatManagerHelper.getInstance().getChatMangerKit().getCurrentChatInfo().getType() == V2TIMConversation.V2TIM_C2C) {
                    if (!TextUtil.isEmpty(giftMessageBean.getContentDict().getSvgaUrl())) {
                        Intent intent = new Intent(MyApp.getInstance(), SVGAAnimActivity.class);
                        intent.putExtra("url", giftMessageBean.getContentDict().getSvgaUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.getInstance().startActivity(intent);
                    } else if (!TextUtil.isEmpty(giftMessageBean.getContentDict().getLottieUrl())) {
                        Intent intent = new Intent(MyApp.getInstance(), LottieAnimActivity.class);
                        intent.putExtra("url", giftMessageBean.getContentDict().getLottieUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.getInstance().startActivity(intent);
                    } else if (!TextUtil.isEmpty(giftMessageBean.getContentDict().getGiftUrl())) {
                        SimpleAnimActivity.Companion.start(MyApp.getInstance(), giftMessageBean.getContentDict().getGiftUrl());
                    }
                } else if (ChatManagerHelper.getInstance().getChatMangerKit().getCurrentChatInfo().getType() == V2TIMConversation.V2TIM_GROUP) {
                    getgetQunGift(giftMessageBean, finalIndex);
                }
            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (info.isSelf() && info.isGroup()) { //群聊本人发送的礼物可再次转发
                    getMessageState(giftMessageBean.getContentDict());
                }
                return true;
            }
        });
    }

    private static int a;

    static boolean isLoading = false;

    private static void getgetQunGift(final GiftMessageBean giftMessageBean, final int index) {
        if (isLoading) {
            return;
        }
        isLoading = true;
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("orderid", giftMessageBean.getContentDict().getOrderid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getQunGift, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                isLoading = false;
                try {
                    if (response != null) {
                        JSONObject obj = new JSONObject(response);
                        String msg = obj.getString("msg");
                        Integer retcode = obj.getInt("retcode");
                        if (retcode == 2000) {
                            MessageSendHelper.getInstance().insertGroupMessage(ChatManagerHelper.getInstance().getChatMangerKit().getCurrentChatInfo().getId(), "你抢到了礼物：" + giftMessageBean.getContentDict().getImageName());
                            if (!TextUtil.isEmpty(giftMessageBean.getContentDict().getSvgaUrl())) {
                                Intent intent = new Intent(MyApp.getInstance(), SVGAAnimActivity.class);
                                intent.putExtra("url", giftMessageBean.getContentDict().getSvgaUrl());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyApp.getInstance().startActivity(intent);
                            } else if (!TextUtil.isEmpty(giftMessageBean.getContentDict().getLottieUrl())) {
                                Intent intent = new Intent(MyApp.getInstance(), LottieAnimActivity.class);
                                intent.putExtra("url", giftMessageBean.getContentDict().getLottieUrl());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MyApp.getInstance().startActivity(intent);
                            } else if (!TextUtil.isEmpty(giftMessageBean.getContentDict().getGiftUrl())) {
                                SimpleAnimActivity.Companion.start(MyApp.getInstance(), giftMessageBean.getContentDict().getGiftUrl());
                            }
                        } else {
                            ToastUtil.toastShortMessage(msg);
                            Intent intent = new Intent(MyApp.getInstance(), Group_liwu_list.class);
                            intent.putExtra("orderid", giftMessageBean.getContentDict().getOrderid());
                            intent.putExtra("image", presents[index]);
                            intent.putExtra("number", giftMessageBean.getContentDict().getNumber());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApp.getInstance().startActivity(intent);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable throwable) {
                isLoading = false;
                LogUtil.d(throwable.toString());
            }
        });
    }

    static void getMessageState(final GiftMessageBean.ContentDictBean contentDictBean) {
        HttpHelper.getInstance().getMessageState(1, contentDictBean.getOrderid(), new HttpListener() {
            @Override
            public void onSuccess(String data) {
                showRelayPop(contentDictBean);
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.toastShortMessage(msg);
            }
        });
    }

    static void showRelayPop(final GiftMessageBean.ContentDictBean contentDictBean) {
        if (FinishActivityManager.getManager().currentActivity() instanceof Activity) {
            new AlertDialog.Builder(FinishActivityManager.getManager().currentActivity()).setMessage("是否再次转发礼物？")
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MessageSendHelper.getInstance().sendGiftMessage(contentDictBean.getImageName(), contentDictBean.getNumber(), contentDictBean.getOrderid());
                }
            }).create().show();
        }
    }
}

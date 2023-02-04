package com.aiwujie.shengmo.tim.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.RedBaoshouActivity;
import com.aiwujie.shengmo.activity.RedBaoshougroupActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.bean.RedEnvelopesMessageBean;
import com.aiwujie.shengmo.tim.bean.RefreshMessageBean;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RedEnvelopesUIController {

    private static final String TAG = RedEnvelopesUIController.class.getSimpleName();
    //static ChatInfo mChatInfo;

    public static void onDraw(final ICustomMessageViewGroup parent, final String data, final MessageInfo info) {
        //mChatInfo = ChatInfo;
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.ry_bao_red_layout, null, false);
        parent.addMessageContentView(view);
        final Context context = view.getContext();

        RedEnvelopesMessageBean redEnvelopesMessageBean = GsonUtil.GsonToBean(data.toString(), RedEnvelopesMessageBean.class);
        final RedEnvelopesMessageBean.ContentDictBean contentDict = redEnvelopesMessageBean.getContentDict();
        LinearLayout phoneLayout = view.findViewById(R.id.ry_phone_layout);
        TextView desTv = view.findViewById(R.id.ry_chat_phone_des_tv);
        ImageView ivliwuya = view.findViewById(R.id.iv_liwuya);
        final LinearLayout llya = view.findViewById(R.id.llya);
        if (!TextUtil.isEmpty(contentDict.getMessage())) {
            desTv.setText(contentDict.getMessage());
        } else {
            contentDict.setMessage("恭喜发财，大吉大利");
            desTv.setText("恭喜发财，大吉大利");
        }
        String status = "1";
        final boolean isReceived = isReceived(contentDict.getOrderid());
        final String[] sendUserName = new String[1];
        final String[] sendUserUrl = new String[1];
        if(!info.isSelf()) {
            V2TIMManager.getInstance().getUsersInfo(Arrays.asList(info.getFromUser()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                    sendUserName[0] = v2TIMUserFullInfos.get(0).getNickName();
                    sendUserUrl[0] = v2TIMUserFullInfos.get(0).getFaceUrl();
                }
            });
        } else {
            sendUserName[0] = ProfileManager.getInstance().getUserModel().userName;
            sendUserUrl[0] = ProfileManager.getInstance().getUserModel().userAvatar;
        }
        if (isReceived) {
            phoneLayout.setAlpha(0.5f);
        } else {
            phoneLayout.setAlpha(1f);
        }

        view.setOnClickListener(new View.OnClickListener() {
            boolean isReceivedBao;
            @Override
            public void onClick(View v) {
                isReceivedBao = isReceived(contentDict.getOrderid());
                //单聊
                if (ChatManagerHelper.getInstance().getChatMangerKit().getCurrentChatInfo().getType() == V2TIMConversation.V2TIM_C2C) {
                    if (info.getTimMessage().getSender().equals(MyApp.uid) || isReceivedBao) {
                        Intent intent = new Intent(MyApp.getInstance(), RedBaoshouActivity.class);
                        intent.putExtra("orderid", contentDict.getOrderid());
                        intent.putExtra("show", 1);
                        intent.putExtra("url",sendUserUrl[0]);
                        intent.putExtra("name",sendUserName[0]);
                        intent.putExtra("info",contentDict.getMessage());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.instance().startActivity(intent);
                        return;
                    } else {
                        final Dialog dialog = new Dialog(ChatManagerHelper.getInstance().getContext(), R.style.dialog);
                        dialog.setContentView(R.layout.item_open_red_dialog);
                        dialog.show();
                        final ImageView iv_red_kai = dialog.findViewById(R.id.iv_red_kai);
                        View clRedBaoBg = dialog.findViewById(R.id.cl_red_bao_bg);
                        final View viewRedBaoKai = dialog.findViewById(R.id.view_red_bao_kai);
                        TextView mred_miaoshu = dialog.findViewById(R.id.red_miaoshu);
                        ImageView ivIcon = dialog.findViewById(R.id.iv_red_bao_icon);
                        TextView tvName = dialog.findViewById(R.id.tv_red_bao_name);
                        TextView tvInfo = dialog.findViewById(R.id.tv_red_bao_info);
                        Glide.with(MyApp.getInstance()).load(sendUserUrl[0]).transforms(new RoundedCornersTransformation(15,5)).into(ivIcon);
                        tvName.setText(sendUserName[0]);
                        tvInfo.setText("给您发了一个红包");
                        if (TextUtil.isEmpty(contentDict.getMessage())) {
                            mred_miaoshu.setText("恭喜发财，大吉大利");
                        } else {
                            mred_miaoshu.setText(contentDict.getMessage() + "");
                        }
                        clRedBaoBg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        viewRedBaoKai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewRedBaoKai.setEnabled(false);
                                ObjectAnimator ra = ObjectAnimator.ofFloat(iv_red_kai, "rotationY", 0f, 1200f);
                                ra.setDuration(2000);
                                ra.start();
                                ra.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        dialog.dismiss();
                                        Intent intent = new Intent(MyApp.getInstance(), RedBaoshouActivity.class);
                                        intent.putExtra("orderid", contentDict.getOrderid());
                                        intent.putExtra("url",sendUserUrl[0]);
                                        intent.putExtra("name",sendUserName[0]);
                                        intent.putExtra("info",contentDict.getMessage());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MyApp.getInstance().startActivity(intent);
                                        MessageSendHelper.getInstance().sendTipMessage("你领取了ta的红包", "ta领取了你的红包");
                                    }
                                });
                            }
                        });
                    }
                } else { //群聊
                    if (isReceivedBao) {
                        Intent intent = new Intent(MyApp.getInstance(), RedBaoshougroupActivity.class);
                        intent.putExtra("orderid", contentDict.getOrderid());
                        intent.putExtra("url",sendUserUrl[0]);
                        intent.putExtra("name",sendUserName[0]);
                        intent.putExtra("info",contentDict.getMessage());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApp.getInstance().startActivity(intent);
                        return;
                    } else {
                        final Dialog dialog = new Dialog(ChatManagerHelper.getInstance().getContext(), R.style.Dialog_Fullscreen);
                        dialog.setContentView(R.layout.item_open_red_dialog);
                        dialog.show();
                        if(context instanceof Activity){
                            backgroundAlpha(0.5f,(Activity)context);
                        }
                        final ImageView iv_red_kai = dialog.findViewById(R.id.iv_red_kai);
                        View clRedBaoBg = dialog.findViewById(R.id.cl_red_bao_bg);
                        final View viewRedBaoKai = dialog.findViewById(R.id.view_red_bao_kai);
                        ImageView ivIcon = dialog.findViewById(R.id.iv_red_bao_icon);
                        TextView tvName = dialog.findViewById(R.id.tv_red_bao_name);
                        TextView tvInfo = dialog.findViewById(R.id.tv_red_bao_info);
                        Glide.with(MyApp.getInstance()).load(sendUserUrl[0]).transforms(new RoundedCornersTransformation(15,5)).into(ivIcon);
                        tvName.setText(sendUserName[0]);
                        tvInfo.setText("给大家发了一个红包");
                        TextView mred_miaoshu = dialog.findViewById(R.id.red_miaoshu);
                        if (TextUtil.isEmpty(contentDict.getMessage())) {
                            mred_miaoshu.setText("恭喜发财，大吉大利");
                        } else {
                            mred_miaoshu.setText(contentDict.getMessage());
                        }

                        clRedBaoBg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        viewRedBaoKai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewRedBaoKai.setEnabled(false);
                                ObjectAnimator ra = ObjectAnimator.ofFloat(iv_red_kai, "rotationY", 0f, 1200f);
                                ra.setDuration(2000);
                                ra.start();
                                ra.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        dialog.dismiss();
                                        getgroupredbao(contentDict.getOrderid(),sendUserUrl[0],sendUserName[0],contentDict.getMessage());
                                        //MessageSendHelper.getInstance().sendTipMessage("你领取了ta的红包","ta领取了你的红包");
                                    }
                                });
                            }
                        });
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(context instanceof Activity){
                                    backgroundAlpha(1f,(Activity)context);
                                }
                            }
                        });
                    }
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (info.isSelf() && info.isGroup()) {
                    getMessageState(contentDict);
                }
                return true;
            }
        });
    }


    public static void getgroupredbao(final String orderid, final String url, final String name, final String message) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("orderid", orderid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.qunGetRedbag, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            signRedEnvelopes(orderid);
                            MessageSendHelper.getInstance().insertGroupMessage(ChatManagerHelper.getInstance().getChatMangerKit().getCurrentChatInfo().getId(),"你领取了红包");
                            EventBus.getDefault().post(new RefreshMessageBean());
                        case 2001:
                            signRedEnvelopes(orderid);
                        case 3000:
                        case 3001:
                        case 4000:
                        case 4001:
                        case 4002:
                        case 4003:
                        case 4004:
                        case 4005:
                        case 4006:
                        default:
                            Intent intent = new Intent(MyApp.getInstance(), RedBaoshougroupActivity.class);
                            intent.putExtra("orderid", orderid);
                            intent.putExtra("url",url);
                            intent.putExtra("name",name);
                            intent.putExtra("info",message);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApp.getInstance().startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    //判断红包是否领取了
    static boolean isReceived(String orderId) {
        try {
            List<String> redEnvelopesCacheList = SharedPreferencesUtils.getDataList("red_envelopes_" + MyApp.uid);
            if (redEnvelopesCacheList == null) {
                return false;
            } else {
                return redEnvelopesCacheList.contains(orderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static void signRedEnvelopes(String orderId) {
        try {
            List<String> redEnvelopesCacheList = SharedPreferencesUtils.getDataList("red_envelopes_" + MyApp.uid);
            if (redEnvelopesCacheList == null) {
                redEnvelopesCacheList = new ArrayList<>();
            }
            redEnvelopesCacheList.add(orderId);
            SharedPreferencesUtils.addDataList("red_envelopes_" + MyApp.uid, redEnvelopesCacheList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void backgroundAlpha(float bgAlpha, Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    static void getMessageState(final RedEnvelopesMessageBean.ContentDictBean contentDictBean) {
        HttpHelper.getInstance().getMessageState(2, contentDictBean.getOrderid(), new HttpListener() {
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

    static void showRelayPop(final RedEnvelopesMessageBean.ContentDictBean contentDictBean) {
        if (FinishActivityManager.getManager().currentActivity() instanceof Activity) {
            new AlertDialog.Builder(FinishActivityManager.getManager().currentActivity()).setMessage("是否再次转发红包？")
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MessageSendHelper.getInstance().sendRedEnvelopsMessage(contentDictBean.getOrderid(), TextUtil.isEmpty(contentDictBean.getMessage())?"恭喜发财，大吉大利":contentDictBean.getMessage(), new IUIKitCallBack() {
                        @Override
                        public void onSuccess(Object data) {

                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {

                        }
                    });
                }
            }).create().show();
        }
    }
}

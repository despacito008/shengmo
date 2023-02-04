package com.aiwujie.shengmo.tim.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupSquareActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationProvider;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.ILoadConversationCallback;

import java.util.ArrayList;
import java.util.List;

public class ConversationLayoutHelper {

    public static void customizeConversation(final ConversationLayout layout, final Activity context) {

        ConversationListLayout listLayout = (ConversationListLayout) layout.getConversationList();
        layout.getTitleBar().setVisibility(View.GONE);
        listLayout.setItemTopTextSize(16); // 设置adapter item中top文字大小
        listLayout.setItemBottomTextSize(12);// 设置adapter item中bottom文字大小
        listLayout.setItemDateTextSize(10);// 设置adapter item中timeline文字大小
        listLayout.setItemAvatarRadius(500);// 设置adapter item头像圆角大小
        listLayout.disableItemUnreadDot(false);// 设置adapter item是否不显示未读红点，默认显示
        layout.getTitleBar().setBackgroundColor(Color.WHITE);
        layout.getTitleBar().setTitle("消息", ITitleBarLayout.POSITION.MIDDLE);
        layout.getTitleBar().setTitle("群广场", ITitleBarLayout.POSITION.LEFT);
        layout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupSquareActivity.class);
                context.startActivity(intent);
                //ConversationManagerKit.getInstance().screenByUser("消");
            }
        });
        layout.getTitleBar().getRightIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOption(context, layout);
                //ConversationManagerKit.getInstance().screenByUser("");

            }
        });
        layout.getTitleBar().getLeftGroup().setVisibility(View.VISIBLE);
        layout.getTitleBar().getLeftIcon().setVisibility(View.GONE);
        layout.getTitleBar().setRightIcon(R.mipmap.sandian);

    }

    public static void showOption(final Activity context, final ConversationLayout layout) {
        if (layout == null || layout.getTitleBar() == null) {
            return;
        }
        final View contentView = LayoutInflater.from(context).inflate(R.layout.item_message_pop, null);
        LinearLayout ll01 = (LinearLayout) contentView.findViewById(R.id.item_message_pop_ll01);
        LinearLayout ll02 = (LinearLayout) contentView.findViewById(R.id.item_message_pop_ll02);
        LinearLayout ll03 = (LinearLayout) contentView.findViewById(R.id.item_message_pop_ll03);
        TextView tv03 = (TextView) contentView.findViewById(R.id.tv_03);
        final PopupWindow mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f, context);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f, context);
            }
        });
        mPopWindow.showAsDropDown(layout.getTitleBar().getRightGroup());
        mPopWindow.setAnimationStyle(R.style.AnimationPreview);

        ll01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IgnoreConversation(layout);
                mPopWindow.dismiss();
            }
        });
        ll02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setMessage("清空会话列表后，会话记录将无法恢复，是否清空？")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearConversation(layout);
                    }
                }).create().show();
                mPopWindow.dismiss();
            }
        });
        ll03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setMessage("清空会话列表后，会话记录将无法恢复，是否清空？")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showClearMsgTimePop(context);
                    }
                }).create().show();
                mPopWindow.dismiss();
            }
        });
    }

   public static void showClearMsgTimePop(Activity context) {
        List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "7天"));
        normalMenuItemList.add(new NormalMenuItem(0, "15天"));
        normalMenuItemList.add(new NormalMenuItem(0, "30天"));
        NormalMenuPopup kickOutPop = new NormalMenuPopup(context, normalMenuItemList);
        kickOutPop.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (position) {
                    case 0:
                        deleteMsg(context,"1");
                        kickOutPop.dismiss();
                        break;
                    case 1:
                        deleteMsg(context,"2");
                        kickOutPop.dismiss();
                        break;
                    case 2:
                        deleteMsg(context,"3");
                        kickOutPop.dismiss();
                        break;
                    default:
                        kickOutPop.dismiss();
                        break;
                }
            }
        });
        kickOutPop.showPopupWindow();
    }

    private static void deleteMsg(Activity context, String deleteType) {
        HttpHelper.getInstance().deleteMsgRecord(deleteType, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(context,msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context,msg);
            }
        });
    }

    /**
     * 清理群消息 并刷新会话
     *
     * @param layout
     */
    public static void clearConversation(final ConversationLayout layout) {
        V2TIMManager.getConversationManager().getConversationList(0, Integer.MAX_VALUE, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.show(MyApp.getInstance(), "网络错误，请稍后再试");
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                for (V2TIMConversation c : v2TIMConversationResult.getConversationList()) {
                    switch (c.getType()) {
                        case V2TIMConversation.V2TIM_C2C:
                            if (Integer.parseInt(c.getUserID()) > 10 ) {
                                V2TIMManager.getConversationManager().deleteConversation("c2c_" + c.getUserID(), new V2TIMCallback() {
                                    @Override
                                    public void onError(int code, String desc) {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        LogUtil.d("--V2TIM_C2C-");
                                    }
                                });
                            }
                            break;
                        case V2TIMConversation.V2TIM_GROUP:
                            V2TIMManager.getConversationManager().deleteConversation("group_" + c.getGroupID(), new V2TIMCallback() {
                                @Override
                                public void onError(int code, String desc) {

                                }

                                @Override
                                public void onSuccess() {
                                    LogUtil.d("--V2TIM_GROUP-");
                                }
                            });
                            break;
                        default:
                            break;

                    }
                }
            }
        });

        //
        ConversationManagerKit.getInstance().loadConversation(0, new ILoadConversationCallback() {
            @Override
            public void onSuccess(ConversationProvider provider, boolean isFinished, long nextSeq) {
                ((ConversationProvider) provider).getDataSource().clear();
                layout.getConversationList().getAdapter().setDataProvider(provider);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });

    }


    /**
     * 忽略未读消息
     *
     * @param layout
     */
    public static void IgnoreConversation(final ConversationLayout layout) {


        V2TIMManager.getConversationManager().getConversationList(0, Integer.MAX_VALUE, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.show(MyApp.getInstance(), "网络错误，请稍后再试");
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                for (V2TIMConversation c : v2TIMConversationResult.getConversationList()) {
                    switch (c.getType()) {
                        case V2TIMConversation.V2TIM_C2C:
                            V2TIMManager.getMessageManager().markC2CMessageAsRead(c.getUserID(), null);
                            break;
                        case V2TIMConversation.V2TIM_GROUP:
                            V2TIMManager.getMessageManager().markGroupMessageAsRead(c.getGroupID(), null);
                            break;
                        default:
                            break;
                    }
                }
            }
        });

//        ConversationManagerKit.getInstance().loadConversation(new IUIKitCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//
//                ((ConversationProvider) data).getDataSource().clear();
//                layout.getConversationList().getAdapter().setDataProvider((ConversationProvider) data);
//            }
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//
//            }
//        });
    }


    public static void backgroundAlpha(float bgAlpha, Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

}

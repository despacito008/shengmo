package com.aiwujie.shengmo.qnlive.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.qnlive.activity.QNLiveRoomAudienceActivity;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.tencent.liteav.custom.Constents;
import com.tencent.qcloud.tim.tuikit.live.bean.LivePermissionBean;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;

import org.greenrobot.eventbus.EventBus;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class QNRoomManager {
    private QNRoomManager roomManager;

    public static QNRoomManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final QNRoomManager INSTANCE = new QNRoomManager();
    }

    Context context;
    String anchorId;
    String roomId;
    public void gotoLiveRoom(Context context, String anchorId, String roomId) {
        this.context = context;
        this.anchorId = anchorId;
        this.roomId = roomId;
        if (isFloatMode()) { //当前是小窗模式
            if (anchorId.equals(FloatWindowLayout.getInstance().getAnchorId())) { //同一个主播
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.AUDIENCE_RESUME_LIVING, "", ""));
            } else {
                getPermissionBeforeWatch();
            }
        } else {
            getPermissionBeforeWatch();
        }
    }

    public void gotoLiveRoom(Context context, String anchorId, int roomId) {
       gotoLiveRoom(context,anchorId,String.valueOf(roomId));
    }

    private boolean isRecord = false;
    private void getPermissionBeforeWatch() {
        HttpHelper.getInstance().getWatchLivePower(anchorId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LivePermissionBean livePermissionBean = GsonUtil.GsonToBean(data, LivePermissionBean.class);
                if (livePermissionBean != null && livePermissionBean.getData() != null) {
                    LivePermissionBean.DataBean permissionBean = livePermissionBean.getData();
                    isRecord = "1".endsWith(permissionBean.getIs_screenshot());
                    if ("1".equals(permissionBean.getIs_ticket())) {
                        if ("1".equals(permissionBean.getIs_buy())) {
                            gotoQNLiveRoom();
                        } else {
                            if ("0".equals(permissionBean.getTry_num())) {
                                showTicketBuyPop(permissionBean.getTry_msg());
                            } else {
                                gotoQNLiveRoomLimit(Integer.parseInt(permissionBean.getTry_time()));
                            }
                        }
                    } else {
                        gotoQNLiveRoom();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
                //ToastUtil.show(context,msg);
                if (code == 8888) {
                    showInputPwdDialog(context);
                } else {
                    ToastUtil.show(context, msg);
                }
            }
        });
    }

    private void showTicketBuyPop(String tip) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop
                .Builder(context)
                .setTitle("温馨提示")
                .setInfo(tip)
                .setCancelStr("取消")
                .setConfirmStr("支付")
                .build();
        normalTipsPop.showPopupWindow();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
                payTicket();
            }
        });
    }

    private void payTicket() {
        HttpHelper.getInstance().buyTicket(anchorId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(context, "购买成功");
                gotoQNLiveRoom();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    private void gotoQNLiveRoomLimit(int limitTime) {
        if (isFloatMode()) { //当前是小窗模式
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
        Intent intent = new Intent(context, QNLiveRoomAudienceActivity.class);
        intent.putExtra("roomId",roomId);
        intent.putExtra("anchorId",anchorId);
        intent.putExtra("canRecord",isRecord);
        intent.putExtra("limitTime",limitTime);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void gotoQNLiveRoom() {
        if (isFloatMode()) { //当前是小窗模式
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
        Intent intent = new Intent(context, QNLiveRoomAudienceActivity.class);
        intent.putExtra("roomId",roomId);
        intent.putExtra("anchorId",anchorId);
        intent.putExtra("canRecord",isRecord);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private boolean isFloatMode() {
        return Constents.isShowAudienceFloatWindow && FloatWindowLayout.getInstance().getFloatState();
    }

    private void showInputPwdDialog(Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_photo_inpsw_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        final EditText etPsw = (EditText) window.findViewById(R.id.item_photo_inpsw_edittext);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_photo_inpsw_confim);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPsw.getText().toString();
                if (TextUtils.equals(password, "")) {
                    ToastUtil.show(context, "请输入密码");
                } else {
                    //验证密码
                    //chargePhotoPwd(password);
                    checkPassword(context,password);
                    alertDialog.dismiss();
                }
            }
        });
        TextView tvCancel = (TextView) window.findViewById(R.id.item_photo_inpsw_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void checkPassword(Context context,String pwd) {
        HttpHelper.getInstance().verifyLivePassword(anchorId, pwd, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                isRecord = "1".equals(MyApp.isAdmin);
                gotoQNLiveRoom();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context,msg);
            }
        });
    }
}

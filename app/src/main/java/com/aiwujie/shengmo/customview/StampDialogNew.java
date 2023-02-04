package com.aiwujie.shengmo.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.StampActivity;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mac on 16/9/23.
 */
public class StampDialogNew extends AlertDialog implements View.OnClickListener {
    //    private final EditText et;
    private AlertDialog alertDialog;
    private Handler handler = new Handler();
    private Context context;
    private String uid;
    private String nickname;
    private String wallet_stamp;
    private String manStamp;
    private String womenStamp;
    private String cdtsStamp;
    private String sex;
    private String stamapSex;
    private AlertDialog startDialog;
    private TextView tvFollow;


    public StampDialogNew(final Context context, String uid,String nickname,String wallet_stamp,String manStamp,String womenStamp,String cdtsStamp,String sex) {
        super(context);
        this.context = context;
        this.uid = uid;
        this.wallet_stamp=wallet_stamp;
        this.manStamp=manStamp;
        this.womenStamp=womenStamp;
        this.cdtsStamp=cdtsStamp;
        this.sex=sex;
        this.nickname=nickname;
        //初始化数据源
        initDatas(context);
    }

    /**
     * 初始化数据源
     */
    private void initDatas(final Context context) {
        View view = View.inflate(context, R.layout.stamp_dialog_layout, null);
        //View view = View.inflate(context, R.layout.app_pop_stamp, null);
        TextView tvWallet_stamp= (TextView) view.findViewById(R.id.stamp_dialog_commonStamp);
        TextView tvManStamp= (TextView) view.findViewById(R.id.stamp_dialog_manYpCount);
        TextView tvWomenStamp= (TextView) view.findViewById(R.id.stamp_dialog_womanYpCount);
        TextView tvCdtsStamp= (TextView) view.findViewById(R.id.stamp_dialog_cdtsYpCount);
        TextView tvGobuy= (TextView) view.findViewById(R.id.stamp_dialog_goBuy);
        TextView tvGoDo= (TextView) view.findViewById(R.id.stamp_dialog_goDo);
        TextView tvOpenVip= (TextView) view.findViewById(R.id.stamp_dialog_openVip);
        tvFollow= (TextView) view.findViewById(R.id.stamp_dialog_follow);
        ImageView ivCommon= (ImageView) view.findViewById(R.id.stamp_dialog_ivCommon);
        ImageView ivMan= (ImageView) view.findViewById(R.id.stamp_dialog_ivMan);
        ImageView ivWomen= (ImageView) view.findViewById(R.id.stamp_dialog_ivWomen);
        ImageView ivCdts= (ImageView) view.findViewById(R.id.stamp_dialog_ivCdts);
        SpannableStringBuilder builder = new SpannableStringBuilder(wallet_stamp+" 张");
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#DB57F3"));
        builder.setSpan(purSpan, 0, wallet_stamp.length()+0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvWallet_stamp.setText(builder);

        SpannableStringBuilder builder1 = new SpannableStringBuilder(manStamp+" 张");
        ForegroundColorSpan purSpan1 = new ForegroundColorSpan(Color.parseColor("#DB57F3"));
        builder1.setSpan(purSpan1, 0, manStamp.length()+0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvManStamp.setText(builder1);

        SpannableStringBuilder builder2 = new SpannableStringBuilder(womenStamp+" 张");
        ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#DB57F3"));
        builder2.setSpan(purSpan2, 0, womenStamp.length()+0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvWomenStamp.setText(builder2);

        SpannableStringBuilder builder3 = new SpannableStringBuilder(cdtsStamp+" 张");
        ForegroundColorSpan purSpan3 = new ForegroundColorSpan(Color.parseColor("#DB57F3"));
        builder3.setSpan(purSpan3, 0, cdtsStamp.length() + 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCdtsStamp.setText(builder3);

        ivCommon.setOnClickListener(this);
        ivMan.setOnClickListener(this);
        ivWomen.setOnClickListener(this);
        ivCdts.setOnClickListener(this);
        tvGobuy.setOnClickListener(this);
        tvGoDo.setOnClickListener(this);
        tvOpenVip.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        alertDialog = new Builder(context, R.style.MyDialog).create();
        alertDialog.setView(view);
        alertDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 90 / 100;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失

//        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //此句是不允许背景变灰色
       // alertDialog.getWindow().setDimAmount(0);

    }

    private void startChatByStamp(final String stampType) {
        View view = View.inflate(context, R.layout.stamp_use_layout, null);
        startDialog = new Builder(context).create();
        startDialog.setView(view);
        startDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = startDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 85 / 100;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(lp);
        startDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//        startDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        startDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        TextView tvCancel= (TextView) view.findViewById(R.id.stamp_use_cancel);
        TextView tvChat= (TextView) view.findViewById(R.id.stamp_use_send);
        TextView tvStampType= (TextView) view.findViewById(R.id.stamp_use_stampType);
        switch (stampType){
            case "4":
                tvStampType.setText("通用邮票");
                break;
            case "1":
                tvStampType.setText("任务男票");
                break;
            case "2":
                tvStampType.setText("任务女票");
                break;
            case "3":
                tvStampType.setText("任务CDTS票");
                break;
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
            }
        });
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useStampToChat(stampType);
                startDialog.dismiss();
                alertDialog.dismiss();
            }
        });
    }

    private void useStampToChat(String stampType) {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("otheruid",uid);
        map.put("stamptype",stampType);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.UseStampToChatNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object=new JSONObject(response);
                            switch (object.getInt("retcode")){
                                case 2000:
//                                    JSONObject obj=object.getJSONObject("data");
//                                    JSONObject obj1=obj.getJSONObject("info");
//                                    RongOpenConversationUtils.startPrivateChat(context, uid, nickname,
//                                            new ChatFlagData(obj.getString("filiation"),
//                                                    obj1.getString("vip"),obj1.getString("vipannual"),
//                                                    obj1.getString("svip"),obj1.getString("svipannual"),
//                                                    obj1.getString("is_volunteer"),
//                                                    obj1.getString("is_admin"),obj1.getString("bkvip"),obj1.getString("blvip")));
                                    if(onSimpleItemListener != null) {
                                        onSimpleItemListener.onItemListener(0);
                                    }
                                    break;
                                case 4001:
                                case 4002:
                                case 4003:
                                case 3000:
                                    ToastUtil.show(getContext().getApplicationContext(),object.getString("msg"));
                                    break;
                            }
                            alertDialog.dismiss();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void chatByStampType(String stampType) {
        View view = View.inflate(context, R.layout.stamp_use_layout_two, null);
        final AlertDialog startDialog = new Builder(context).create();
        startDialog.setView(view);
        startDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = startDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 85 / 100;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(lp);
        startDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//        startDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        startDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        TextView tvConfirm= (TextView) view.findViewById(R.id.stamp_use_two_cancel);
        TextView tvStampType= (TextView) view.findViewById(R.id.stamp_use_two_stampType);
        switch (stampType){
            case "1":
                tvStampType.setText("通用邮票/任务男票");
                break;
            case "2":
                tvStampType.setText("通用邮票/任务女票");
                break;
            case "3":
                tvStampType.setText("通用邮票/任务CDTS票");
                break;
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
            }
        });

    }
    private void StampNotEmpty() {
        View view = View.inflate(context, R.layout.stamp_use_layout_three, null);
        final AlertDialog startDialog = new Builder(context).create();
        startDialog.setView(view);
        startDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = startDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 85 / 100;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(lp);
        startDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//        startDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        startDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        TextView tvConfirm= (TextView) view.findViewById(R.id.stamp_use_three_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stamp_dialog_ivCommon:
                stamapSex="4";
                if(!wallet_stamp.equals("0")){
                    //带弹出确认框
//                    startChatByStamp(stamapSex);
                    //不带弹出确认框直接聊
                    useStampToChat(stamapSex);
                }else{
                    StampNotEmpty();
                }
                break;
            case R.id.stamp_dialog_ivMan:
                stamapSex="1";
                if(!manStamp.equals("0")){
                    if(sex.equals(stamapSex)){
                       // startChatByStamp(stamapSex);
                        //不带弹出确认框直接聊
                        useStampToChat(stamapSex);
                    }else{
                        //请使用sex的性别或者通用邮票发送消息
                        chatByStampType(sex);
                    }
                }else{
                    StampNotEmpty();
                }
                break;
            case R.id.stamp_dialog_ivWomen:
                stamapSex="2";
                if(!womenStamp.equals("0")){
                    if(sex.equals(stamapSex)){
//                        startChatByStamp(stamapSex);
                        //不带弹出确认框直接聊
                        useStampToChat(stamapSex);
                    }else{
                        //请使用sex的性别或者通用邮票发送消息
                        chatByStampType(sex);
                    }
                }else{
                    StampNotEmpty();
                }
                break;
            case R.id.stamp_dialog_ivCdts:
                stamapSex="3";
                if(!cdtsStamp.equals("0")){
                    if(sex.equals(stamapSex)){
//                        startChatByStamp(stamapSex);
                        //不带弹出确认框直接聊
                        useStampToChat(stamapSex);
                    }else{
                        //请使用sex的性别或者通用邮票发送消息
                        chatByStampType(sex);
                    }
                }else{
                    StampNotEmpty();
                }
                break;
            case R.id.stamp_dialog_goBuy:
            case R.id.stamp_dialog_goDo:
                Intent intent=new Intent(context, StampActivity.class);
                context.startActivity(intent);
//                Intent intent=new Intent(context, MyPurseActivity.class);
//                intent.putExtra("openStampPage",1);
//                context.startActivity(intent);
                alertDialog.dismiss();
                break;
            case R.id.stamp_dialog_follow:
                tvFollow.setEnabled(false);
                follow();
                break;
            case R.id.stamp_dialog_openVip:
                intent = new Intent(context, VipCenterActivity.class);
                intent.putExtra("headpic", (String) SharedPreferencesUtils.getParam(context.getApplicationContext(),"headurl",""));
                intent.putExtra("uid", MyApp.uid);
                context.startActivity(intent);
                break;
        }
    }

    private void follow() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("personmsgfollow", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    String followFlag = obj.getString("data");
                                    if (followFlag.equals("1")) {
                                        EventBus.getDefault().post(new FollowEvent("1","3"));
                                    } else {
                                        EventBus.getDefault().post(new FollowEvent("2","1"));
                                    }
                                    tvFollow.setText("已关注");
                                    ToastUtil.showLong(context.getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4002:
                                case 8881:
                                case 8882:
                                    BindGuanzhuDialog.bindAlertDialog(context,obj.getString("msg"));
                                    tvFollow.setEnabled(true);
                                case 4787:
                                    ToastUtil.show(context.getApplicationContext(), "您已经关注了对方~");
                                    break;
                                case 5000:
                                    ToastUtil.show(context, obj.getString("msg")+"");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    OnSimpleItemListener onSimpleItemListener;

    public void setOnSimpleItemListener(OnSimpleItemListener onSimpleItemListener) {
        this.onSimpleItemListener = onSimpleItemListener;
    }

}


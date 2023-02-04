package com.aiwujie.shengmo.zdyview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.Ejection_Activity;
import com.aiwujie.shengmo.activity.HuojianfeiActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.EjectionBean;
import com.aiwujie.shengmo.bean.EjectionshengyuBean;
import com.aiwujie.shengmo.bean.TopcardyesBean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/9.
 */

public class TopcardDialog{

    static  Handler handler = new Handler();
    private static TextView mStamp_count;
    private static TextView mStamp_buy;
    private static String wallet_topcard ="0";
    private static EditText et_shu;
    static String topnum="";
    static String interval="";
    static String dalaba="1";
    private static LinearLayout ll_up_dalaba;
    private static ImageView iv_up_lb;
    private static int aaa = 0;


    public static void dialogShow(final Context context, final String did, final int pos){

        getusertopcardinfo();
        final Dialog dialog = new Dialog(context, R.style.dialog);
        final View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.tuiding_dialog,null);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.addContentView(dialogView,lp);
        dialog.show();

        final ImageView tuiding_dia = dialogView.findViewById(R.id.tuiding_dia);
        final ImageView tuiding_dia2 = dialogView.findViewById(R.id.tuiding_dia2);
        final ImageView tuiding_dia3 = dialogView.findViewById(R.id.tuiding_dia3);
        final TextView addmei = dialogView.findViewById(R.id.addmei);
        final PercentRelativeLayout tuidingrl1 = dialogView.findViewById(R.id.tuidingrl1);
        final RadioGroup rgya = dialogView.findViewById(R.id.rgya);
        final RadioGroup rgya2 = dialogView.findViewById(R.id.rgya2);
        final RadioButton rb_time1 = dialogView.findViewById(R.id.rb_time1);
        final RadioButton rb_time2 = dialogView.findViewById(R.id.rb_time2);
        final RadioButton rb_time3 = dialogView.findViewById(R.id.rb_time3);
        final RadioButton rb_time4 = dialogView.findViewById(R.id.rb_time4);
        final RadioButton rb_time5 = dialogView.findViewById(R.id.rb_time5);
        final RadioButton rb_time6 = dialogView.findViewById(R.id.rb_time6);
        final RadioButton rb_time7 = dialogView.findViewById(R.id.rb_time7);
        final RadioButton rb_time8 = dialogView.findViewById(R.id.rb_time8);
        final RadioButton rb_time9 = dialogView.findViewById(R.id.rb_time9);
        final RadioButton rb_time10 = dialogView.findViewById(R.id.rb_time10);
        ll_up_dalaba = dialogView.findViewById(R.id.ll_up_dalaba);
        iv_up_lb = dialogView.findViewById(R.id.iv_up_lb);
        ll_up_dalaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dalaba.equals("1")){
                    iv_up_lb.setImageResource(R.mipmap.width_duo_kuang);
                    dalaba="2";
                }else {
                    iv_up_lb.setImageResource(R.mipmap.yuandiantaozi);
                    dalaba="1";
                }
            }
        });
        rb_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time1.setChecked(false);
                    interval="";
                }else {
                    rb_time6.setChecked(false);
                    rb_time7.setChecked(false);
                    rb_time8.setChecked(false);
                    rb_time9.setChecked(false);
                    rb_time10.setChecked(false);
                    interval="1";
                }
            }
        });
        rb_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time2.setChecked(false);
                    interval="";
                }else {
                    rb_time6.setChecked(false);
                    rb_time7.setChecked(false);
                    rb_time8.setChecked(false);
                    rb_time9.setChecked(false);
                    rb_time10.setChecked(false);
                    interval="3";
                }
            }
        });
        rb_time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time3.setChecked(false);
                    interval="";
                }else {
                    rb_time6.setChecked(false);
                    rb_time7.setChecked(false);
                    rb_time8.setChecked(false);
                    rb_time9.setChecked(false);
                    rb_time10.setChecked(false);
                    interval="6";
                }
            }
        });
        rb_time4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time4.setChecked(false);
                    interval="";
                }else {
                    rb_time6.setChecked(false);
                    rb_time7.setChecked(false);
                    rb_time8.setChecked(false);
                    rb_time9.setChecked(false);
                    rb_time10.setChecked(false);
                    interval="12";
                }
            }
        });
        rb_time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time5.setChecked(false);
                    interval="";
                }else {
                    rb_time6.setChecked(false);
                    rb_time7.setChecked(false);
                    rb_time8.setChecked(false);
                    rb_time9.setChecked(false);
                    rb_time10.setChecked(false);
                    interval="24";
                }
            }
        });
        rb_time6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time6.setChecked(false);
                    interval="";
                }else {
                    rb_time1.setChecked(false);
                    rb_time2.setChecked(false);
                    rb_time3.setChecked(false);
                    rb_time4.setChecked(false);
                    rb_time5.setChecked(false);
                    interval=5*60+"";
                }
            }
        });
        rb_time7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time7.setChecked(false);
                    interval="";
                }else {
                    rb_time1.setChecked(false);
                    rb_time2.setChecked(false);
                    rb_time3.setChecked(false);
                    rb_time4.setChecked(false);
                    rb_time5.setChecked(false);
                    interval=10*60+"";
                }
            }
        });
        rb_time8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time8.setChecked(false);
                    interval="";
                }else {
                    rb_time1.setChecked(false);
                    rb_time2.setChecked(false);
                    rb_time3.setChecked(false);
                    rb_time4.setChecked(false);
                    rb_time5.setChecked(false);
                    interval=15*60+"";
                }
            }
        });
        rb_time9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time9.setChecked(false);
                    interval="";
                }else {
                    rb_time1.setChecked(false);
                    rb_time2.setChecked(false);
                    rb_time3.setChecked(false);
                    rb_time4.setChecked(false);
                    rb_time5.setChecked(false);
                    interval=20*60+"";
                }
            }
        });
        rb_time10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aaa==0){
                    ToastUtil.show(context,"2张以上可选");
                    rb_time10.setChecked(false);
                    interval="";
                }else {
                    rb_time1.setChecked(false);
                    rb_time2.setChecked(false);
                    rb_time3.setChecked(false);
                    rb_time4.setChecked(false);
                    rb_time5.setChecked(false);
                    interval=30*60+"";
                }
            }
        });

        rgya2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rb_time6:

                        interval=5*60+"";
                        break;
                    case R.id.rb_time7:
                        interval=10*60+"";
                        break;
                    case R.id.rb_time8:
                        interval=15*60+"";
                        break;
                    case R.id.rb_time9:
                        interval=20*60+"";
                        break;
                    case R.id.rb_time10:
                        interval=30*60+"";
                        break;
                }
            }
        });

        rgya.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rb_time1:
                        interval="1";
                        break;
                    case R.id.rb_time2:
                        interval="3";
                        break;
                    case R.id.rb_time3:
                        interval="6";
                        break;
                    case R.id.rb_time4:
                        interval="12";
                        break;
                    case R.id.rb_time5:
                        interval="24";
                        break;
                }
            }
        });
        SpannableStringBuilder builder = new SpannableStringBuilder("被推者增加 0 魅力值");
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
        builder.setSpan(purSpan, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        addmei.setText(builder);

        tuiding_dia2.setVisibility(View.GONE);
        tuiding_dia3.setVisibility(View.GONE);
        tuidingrl1.setVisibility(View.GONE);
        et_shu = dialogView.findViewById(R.id.et_shu);
        et_shu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = et_shu.getText().toString().trim();

                aaa=0;
                if(!"".equals(trim)&&trim!=null){
                    Integer integer = Integer.valueOf(trim);
                    if(integer==1 ||integer==0){
                        rgya.clearCheck();
                        interval="";
                        tuiding_dia.setVisibility(View.VISIBLE);
                        tuiding_dia2.setVisibility(View.GONE);
                        tuiding_dia3.setVisibility(View.GONE);
                        tuidingrl1.setVisibility(View.GONE);
                    }else if(integer==2){
                        tuiding_dia.setVisibility(View.GONE);
                        tuiding_dia2.setVisibility(View.VISIBLE);
                        tuiding_dia3.setVisibility(View.VISIBLE);
                        tuidingrl1.setVisibility(View.VISIBLE);
                        rgya2.clearCheck();
                        rgya.check(R.id.rb_time1);
                        interval="1";
                        aaa=1;

                    }else {
                        tuidingrl1.setVisibility(View.VISIBLE);
                        tuiding_dia.setVisibility(View.VISIBLE);
                        tuiding_dia2.setVisibility(View.VISIBLE);
                        tuiding_dia3.setVisibility(View.VISIBLE);
                        rgya2.clearCheck();
                        rgya.check(R.id.rb_time1);
                        interval="1";
                        aaa=1;

                    }

                    int meinum = integer*100;
                    String s1 = String.valueOf(meinum);
                    SpannableStringBuilder builder = new SpannableStringBuilder("被推者增加 "+s1+" 魅力值");
                    ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                    builder.setSpan(purSpan, 6, s1.length()+6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    addmei.setText(builder);
                }else {
                    rgya.clearCheck();
                    rgya2.clearCheck();
                    interval="";
                    tuiding_dia.setVisibility(View.VISIBLE);
                    tuiding_dia2.setVisibility(View.GONE);
                    tuiding_dia3.setVisibility(View.GONE);
                    tuidingrl1.setVisibility(View.GONE);
                    SpannableStringBuilder builder = new SpannableStringBuilder("被推者增加 0 魅力值");
                    ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                    builder.setSpan(purSpan, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    addmei.setText(builder);
                }

            }
        });
        mStamp_count = dialogView.findViewById(R.id.mStamp_count);
        mStamp_buy = dialogView.findViewById(R.id.mStamp_buy);

        getusertopcardinfo2();
        if(!wallet_topcard.equals("0")){
            mStamp_buy.setText("确定");
        }else {
            mStamp_buy.setText("去购买");
        }
        mStamp_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wallet_topcard.equals("0")){
                    Intent intent = new Intent(context, Ejection_Activity.class);
                    context.startActivity(intent);
                }else {

                    String trim = et_shu.getText().toString().trim();
                    if ("".equals(trim)||trim==null||"0".equals(trim)){
                        return;
                    }
                    topnum = et_shu.getText().toString().trim();
                    getusertopcard(did,pos,context,Integer.valueOf(trim));
                }
                dialog.dismiss();
            }
        });
    }


    //推顶卡余额
    public static void getusertopcardinfo(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("----", "onSuccess: "+response );
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        EjectionshengyuBean ejectionBean = gson.fromJson(response, EjectionshengyuBean.class);
                        wallet_topcard = ejectionBean.getData().getWallet_topcard();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    //推顶卡余额
    public static void getusertopcardinfo2(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardPageInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("----", "onSuccess: "+response );
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        EjectionshengyuBean ejectionBean = gson.fromJson(response, EjectionshengyuBean.class);
                        wallet_topcard = ejectionBean.getData().getWallet_topcard();
                        SpannableStringBuilder builder = new SpannableStringBuilder("剩余 " + wallet_topcard + " 张推顶卡");
                        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                        builder.setSpan(purSpan, 3, 1 + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mStamp_count.setText(builder);
                        if(!wallet_topcard.equals("0")){
                            mStamp_buy.setText("确定");
                        }else {
                            mStamp_buy.setText("去购买");
                        }
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    //推顶
    public static void getusertopcard(final String did, final int pos, final Context context, final int trim){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("num",topnum );
        map.put("interval",interval);
        map.put("dalaba",dalaba);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.useTopcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("----", "onSuccess: "+response );
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    Gson gson = new Gson();
                                    EjectionBean ejectionBean = gson.fromJson(response, EjectionBean.class);
                                    //Toast.makeText(context, ""+ejectionBean.getMsg(), Toast.LENGTH_SHORT).show();
                                    if(ejectionBean.getRetcode()==2000){
                                        EventBus.getDefault().post(new TopcardyesBean(pos,did));
                                        Intent intent = new Intent(context, HuojianfeiActivity.class);
                                        intent.putExtra("feishu",Integer.valueOf(trim));
                                        context.startActivity(intent);
                                    }
                                    break;
                                case 2001:
                                case 3000:
                                case 3001:
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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



}

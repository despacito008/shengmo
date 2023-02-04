package com.aiwujie.shengmo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PhotoAuthenticationActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.PhotoRzEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/11.
 */

public class Wdrz_zprzfragment extends Fragment implements View.OnClickListener{

    ImageView mPhotorzIcon;
    ImageView mPhotorzVip;
    Button mPhotorzBtn;
    Handler handler=new Handler();
    private int retcode;
    //认证照是否公开状态码（0为不公开，1为vip可查看）
    private String status;
    private String card_face;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.wdrz_zprz_fraag,container,false);

        mPhotorzIcon  = view.findViewById(R.id.mPhotorz_icon);
        mPhotorzVip  = view.findViewById(R.id.mPhotorz_vip);
        mPhotorzBtn  = view.findViewById(R.id.mPhotorz_btn);
        mPhotorzBtn.setOnClickListener(this);
        //X_SystemBarUI.initSystemBar(getActivity(), R.color.title_color);
        EventBus.getDefault().register(this);
        String headpic = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "headurl", "");
        try {
            GlideImgManager.glideLoader(getContext(), headpic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, mPhotorzIcon, 0);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        String vip = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "");
        String Svip = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "svip", "");

        if(Svip.equals("1")){
            mPhotorzVip.setImageResource(R.drawable.user_svip);
        } else if(vip.equals("1")){
            mPhotorzVip.setImageResource(R.drawable.user_vip);
        }else{
            mPhotorzVip.setImageResource(R.drawable.user_normal);
        }
//        retcode=getIntent().getIntExtra("retcode",-1);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPhotorz_btn:
                Intent intent;
                switch (retcode){
                    case 2000:
                        intent=new Intent(getContext(),PhotoAuthenticationActivity.class);
                        intent.putExtra("retcode",retcode);
                        intent.putExtra("status",status);
                        intent.putExtra("card_face",card_face);
                        startActivity(intent);
//                        ToastUtil.show(getApplicationContext(),"已认证通过");
                        break;
                    case 2001:
                        ToastUtil.show(getContext(),"正在认证,请耐心等待");
                        break;
                    case 2002:
                        intent=new Intent(getContext(),PhotoAuthenticationActivity.class);
                        intent.putExtra("retcode",retcode);
                        startActivity(intent);
                        break;
                }
                break;
        }
    }

    private void getIdstate() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getidstate, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            retcode = obj.getInt("retcode");
                            switch (retcode){
                                case 2002:
                                    mPhotorzBtn.setText("立即认证");
                                    mPhotorzBtn.setClickable(true);
                                    JSONObject object1 = obj.getJSONObject("data");
                                    status=object1.getString("status");
                                    card_face=object1.getString("card_face");
                                    break;
                                case 2000:
                                    mPhotorzBtn.setText("重新认证");
                                    mPhotorzBtn.setClickable(true);
                                    JSONObject object2=obj.getJSONObject("data");
                                    status=object2.getString("status");
                                    card_face=object2.getString("card_face");
                                    break;
                                case 2001:
                                    mPhotorzBtn.setText("审核中");
                                    mPhotorzBtn.setClickable(false);
                                    JSONObject object3=obj.getJSONObject("data");
                                    status=object3.getString("status");
                                    card_face=object3.getString("card_face");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(PhotoRzEvent event) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getIdstate();
    }
}

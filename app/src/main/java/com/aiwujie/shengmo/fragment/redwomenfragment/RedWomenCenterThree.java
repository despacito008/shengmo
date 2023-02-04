package com.aiwujie.shengmo.fragment.redwomenfragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RedwomenListAdapter;
import com.aiwujie.shengmo.activity.EditRedWomenJSActivity;
import com.aiwujie.shengmo.activity.EditRedWomenJYActivity;
import com.aiwujie.shengmo.customview.MyListView;
import com.aiwujie.shengmo.eventbus.RedWomenIntroData;
import com.aiwujie.shengmo.eventbus.RedWomenJsEvent;
import com.aiwujie.shengmo.eventbus.RedWomenJyEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/12/15.
 */

public class RedWomenCenterThree extends Fragment {
    @BindView(R.id.mRedwomen_three_none01)
    PercentLinearLayout mRedwomenThreeNone01;
    @BindView(R.id.mRedWomen_hnjy)
    ImageView mRedWomenHnjy;
    @BindView(R.id.mRedwomen_mylistview)
    MyListView mRedwomenMylistview;
    @BindView(R.id.mRedwomen_three_none02)
    PercentLinearLayout mRedwomenThreeNone02;
    @BindView(R.id.mRedwomen_three_intro)
    TextView mRedwomenThreeIntro;
    private String uid;
    private Handler handler = new Handler();
    private RedWomenIntroData redwomendata;
    private RedwomenListAdapter redwomenAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_redwomen_three, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setData();
        getMatchMakerIntroduce();
        getMatchObList();
        return view;
    }


    private void setData() {
        String admin = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "admin", "0");
        uid = getActivity().getIntent().getStringExtra("uid");
        if (admin.equals("1")) {
            mRedWomenHnjy.setVisibility(View.VISIBLE);
        } else {
            mRedWomenHnjy.setVisibility(View.GONE);
        }
        mRedwomenMylistview.setFocusable(false);
    }

    private void getMatchMakerIntroduce() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMatchMakerIntroduce, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("redwomencenterintro", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    mRedwomenThreeNone01.setVisibility(View.GONE);
                                    mRedwomenThreeIntro.setVisibility(View.VISIBLE);
                                    JSONObject jsonObject=obj.getJSONObject("data");
                                    mRedwomenThreeIntro.setText(jsonObject.getString("match_makerintroduce"));
                                    break;
                                case 3000:
                                    mRedwomenThreeNone01.setVisibility(View.VISIBLE);
                                    mRedwomenThreeIntro.setVisibility(View.GONE);
                                    break;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

    private void getMatchObList() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",uid);
        IRequestManager manager=RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMatchObList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("getMactchOblist", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    mRedwomenThreeNone02.setVisibility(View.GONE);
                                    mRedwomenMylistview.setVisibility(View.VISIBLE);
                                    redwomendata=new Gson().fromJson(response,RedWomenIntroData.class);
                                    redwomenAdapter=new RedwomenListAdapter(getActivity(),redwomendata.getData());
                                    mRedwomenMylistview.setAdapter(redwomenAdapter);
                                    redwomenAdapter.setRedwomenClick(new RedwomenListAdapter.OnRedwomenClickListener() {
                                        @Override
                                        public void onRedwomenClick(int position) {
                                            Intent intent=new Intent(getActivity(), EditRedWomenJSActivity.class);
                                            intent.putExtra("position",position+"");
                                            intent.putExtra("content",redwomendata.getData().get(position).getRemarks());
                                            intent.putExtra("redid",redwomendata.getData().get(position).getMid());
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case 3000:
                                    mRedwomenThreeNone02.setVisibility(View.VISIBLE);
                                    mRedwomenMylistview.setVisibility(View.GONE);
                                    break;
                            }
                        } catch (JsonSyntaxException e){
                            e.printStackTrace();
                        } catch (JSONException e){
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


    @OnClick(R.id.mRedWomen_hnjy)
    public void onViewClicked() {
        Intent intent=new Intent(getActivity(), EditRedWomenJYActivity.class);
        intent.putExtra("hnjy",mRedwomenThreeIntro.getText().toString());
        intent.putExtra("uid",uid);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(RedWomenJyEvent event) {
        mRedwomenThreeNone01.setVisibility(View.GONE);
        mRedwomenThreeIntro.setVisibility(View.VISIBLE);
        mRedwomenThreeIntro.setText(event.getRedJy());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(RedWomenJsEvent event) {
        redwomendata.getData().get(event.getPosition()).setRemarks(event.getRedJs());
        redwomenAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

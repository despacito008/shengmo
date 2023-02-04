package com.aiwujie.shengmo.fragment.mypursefragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DuihuanModouActivity;
import com.aiwujie.shengmo.activity.WithdrawalsActivity;
import com.aiwujie.shengmo.adapter.MyPresentGridViewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.customview.BindRenZhengDialog;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.eventbus.BuyStampEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
 * Created by 290243232 on 2017/6/23.
 */

public class FragmentPresent extends Fragment {
    @BindView(R.id.mMypresent_count)
    TextView mMypresentCount;
    @BindView(R.id.mMypresent_Moudou)
    TextView mMypresentMoudou;
    @BindView(R.id.mMypresent_kyMoudou)
    TextView mMypresentkyMoudou;
    @BindView(R.id.mMypresent_mfMoudou)
    TextView mMypresentmfMoudou;
    @BindView(R.id.mMypresent_yyMoudou)
    TextView mMypresent_yyMoudou;
    @BindView(R.id.mMypresent_gridview)
    MyGridview mMypresentGridview;
    @BindView(R.id.mMypresent_tdnum)
    TextView mMypresent_tdnum;
    @BindView(R.id.mMypresent_tdmodou)
    TextView mMypresent_tdmodou;

    @BindView(R.id.mMypresent_tixian)
    ImageView mMypresentTixian;

    private Handler handler = new Handler();
    private double rate;
    private String useableBeans="0";
    String rzzt="0";
    private String usedbeans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_present, null);
        ButterKnife.bind(this, view);

        getIdstate();
        EventBus.getDefault().register(this);
        mMypresentGridview.setFocusable(false);
        return view;
    }

    private void getMyPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMyPresent, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fanhuibili", "run: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("retcode") == 2000) {
                                MyPresentData myPresentData = new Gson().fromJson(response, MyPresentData.class);
                                //提现的比例
                                rate = myPresentData.getData().getPay();
                                //可使用的魔豆数量
                                useableBeans = myPresentData.getData().getUseableBeans();
                                usedbeans = myPresentData.getData().getUsedbeans();
                                SpannableStringBuilder builder = new SpannableStringBuilder("共 " + myPresentData.getData().getAllnum() + " 份礼物，");
                                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder.setSpan(purSpan, 2, myPresentData.getData().getAllnum().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentCount.setText(builder);
                                SpannableStringBuilder builder1 = new SpannableStringBuilder("共 " + myPresentData.getData().getAllamount() + " 魅力值");
                                ForegroundColorSpan purSpan1 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder1.setSpan(purSpan1, 2, myPresentData.getData().getAllamount().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentMoudou.setText(builder1);
                                SpannableStringBuilder builder2 = new SpannableStringBuilder("可用 " + useableBeans + " 银魔豆");
                                ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder2.setSpan(purSpan2, 3, useableBeans.length()+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentkyMoudou.setText(builder2);
                                SpannableStringBuilder builder6 = new SpannableStringBuilder("已用 " + usedbeans + " 银魔豆");
                                ForegroundColorSpan purSpan6 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder6.setSpan(purSpan6, 3, usedbeans.length()+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresent_yyMoudou.setText(builder6);
                                SpannableStringBuilder builder3 = new SpannableStringBuilder("免费礼物 " + myPresentData.getData().getFree() + " 魅力值");
                                ForegroundColorSpan purSpan3 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder3.setSpan(purSpan3, 5,  myPresentData.getData().getFree().length()+5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentmfMoudou.setText(builder3);
                                SpannableStringBuilder builder4 = new SpannableStringBuilder("被推顶 " + myPresentData.getData().getTopcard() + " 次");
                                ForegroundColorSpan purSpan4 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder4.setSpan(purSpan4, 4,   myPresentData.getData().getTopcard().length()+4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresent_tdnum.setText(builder4);
                                int integer = Integer.valueOf(myPresentData.getData().getTopcard())*100;
                                SpannableStringBuilder builder5 = new SpannableStringBuilder("( "+integer+ " 魅力值)");
                                ForegroundColorSpan purSpan5 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder5.setSpan(purSpan5, 2,   String.valueOf(integer).length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresent_tdmodou.setText(builder5);

                                mMypresentGridview.setAdapter(new MyPresentGridViewAdapter(getActivity(), myPresentData.getData().getGiftArr()));
                            } else if (object.getInt("retcode") == 4001) {
                                SpannableStringBuilder builder = new SpannableStringBuilder("共 0 份礼物，");
                                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder.setSpan(purSpan, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentCount.setText(builder);
                                SpannableStringBuilder builder1 = new SpannableStringBuilder("共 0 魅力值");
                                ForegroundColorSpan purSpan1 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder1.setSpan(purSpan1, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentMoudou.setText(builder1);
                                SpannableStringBuilder builder2 = new SpannableStringBuilder("可用 0 银魔豆");
                                ForegroundColorSpan purSpan2 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder2.setSpan(purSpan2, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentkyMoudou.setText(builder2);
                                SpannableStringBuilder builder3 = new SpannableStringBuilder("免费礼物 " + 0 + " 魅力值");
                                ForegroundColorSpan purSpan3 = new ForegroundColorSpan(Color.parseColor("#ff9d00"));
                                builder3.setSpan(purSpan3, 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mMypresentmfMoudou.setText(builder3);
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

    @OnClick({ R.id.mMypresent_luwudui,R.id.mMypresent_tixian})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.mMypresent_luwudui:
                if (Integer.valueOf(useableBeans) < 2) {
                    ToastUtil.show(getActivity(), "可用银魔豆大于等于2可兑换金魔豆...");
                } else {
                    intent = new Intent(getActivity(), DuihuanModouActivity.class);
                    intent.putExtra("liwudou",useableBeans);
                    startActivity(intent);
                }

                break;
            case R.id.mMypresent_tixian:
                if (Integer.valueOf(useableBeans) < 2000) {
                    ToastUtil.show(getActivity(), "可用银魔豆大于等于2000可提现...");
                } else {
                    if(rzzt.equals("1")){
                        intent = new Intent(getActivity(), WithdrawalsActivity.class);
                        intent.putExtra("modou", useableBeans);
                        intent.putExtra("rate", rate);
                        startActivity(intent);
                    }else {
                       new BindRenZhengDialog().bindAlertDialog(getContext(),"认证之后可以提现");
                    }

                }
                break;

        }
    }

    //认证状态
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
                          int  retcode = obj.getInt("retcode");
                            switch (retcode) {
                                case 2000:
                                    rzzt="1";
                                    break;
                                case 2001:

                                    break;
                                case 2002:

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
    public void helloEventBus(BuyStampEvent event) {
        getMyPresent();
    }


    @Override
    public void onResume() {
        super.onResume();
        getMyPresent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }




}

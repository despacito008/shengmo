package com.aiwujie.shengmo.fragment.vipcenterfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.VipEvent;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/11/22.
 */

public class FragmentVip extends Fragment {
    @BindView(R.id.mVipCenter_buy01)
    ImageView mVipCenterBuy01;
    @BindView(R.id.mVipCenter_buy02)
    ImageView mVipCenterBuy02;
    @BindView(R.id.mVipCenter_buy03)
    ImageView mVipCenterBuy03;
    @BindView(R.id.mVipCenter_buy04_tv)
    TextView mVipCenterBuy04Tv;
    @BindView(R.id.mVipCenter_buy04)
    ImageView mVipCenterBuy04;
    @BindView(R.id.mVipCenter_zs01)
    TextView mVipCenterZs01;
    @BindView(R.id.mVipCenter_zs02)
    TextView mVipCenterZs02;
    @BindView(R.id.mVipCenter_zs03)
    TextView mVipCenterZs03;
    @BindView(R.id.mVipCenter_zs04)
    TextView mVipCenterZs04;
    @BindView(R.id.mVipCenter_zs05)
    TextView mVipCenterZs05;
    @BindView(R.id.mVipCenter_zs06)
    TextView mVipCenterZs06;
    @BindView(R.id.mVipCenter_zs07)
    TextView mVipCenterZs07;
    @BindView(R.id.mVipCenter_zs08)
    TextView mVipCenterZs08;
    @BindView(R.id.mVipCenter_zs09)
    TextView mVipCenterZs09;
    @BindView(R.id.mVipCenter_zs10)
    TextView mVipCenterZs10;
    @BindView(R.id.mVipCenter_zs11)
    TextView mVipCenterZs11;
    @BindView(R.id.mVipCenter_zs12)
    TextView mVipCenterZs12;
    @BindView(R.id.mVipCenter_zs13)
    TextView mVipCenterZs13;
    @BindView(R.id.mVipCenter_zs14)
    TextView mVipCenterZs14;
   /* @BindView(R.id.mVipCenter_zs14)
    TextView mVipCenterZs14;
    @BindView(R.id.mVipCenter_zs15)
    TextView mVipCenterZs15;*/
    @BindView(R.id.mVipCenter_buy01_tv_money)
    TextView mVipCenterBuy01TvMoney;
    @BindView(R.id.mVipCenter_buy02_tv_money)
    TextView mVipCenterBuy02TvMoney;
    @BindView(R.id.mVipCenter_buy03_tv_money)
    TextView mVipCenterBuy03TvMoney;
    @BindView(R.id.mVipCenter_buy04_tv_money)
    TextView mVipCenterBuy04TvMoney;
    @BindView(R.id.mVipCenter_ll_buy_01)
    PercentLinearLayout mVipCenterLlBuy01;
    @BindView(R.id.mVipCenter_ll_buy_02)
    PercentLinearLayout mVipCenterLlBuy02;
    @BindView(R.id.mVipCenter_ll_buy_03)
    PercentLinearLayout mVipCenterLlBuy03;
    @BindView(R.id.mVipCenter_ll_buy_04)
    PercentLinearLayout mVipCenterLlBuy04;

    private int buyamount;
    private int buysubject;
    private ArrayList<ImageView> buys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_mypurse_vip, null);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
        SpannableStringBuilder builderMoney01 = new SpannableStringBuilder(mVipCenterBuy01TvMoney.getText().toString());
        SpannableStringBuilder builderMoney02 = new SpannableStringBuilder(mVipCenterBuy02TvMoney.getText().toString());
        SpannableStringBuilder builderMoney03 = new SpannableStringBuilder(mVipCenterBuy03TvMoney.getText().toString());
        SpannableStringBuilder builderMoney04 = new SpannableStringBuilder(mVipCenterBuy04TvMoney.getText().toString());
        SpannableStringBuilder builder01 = new SpannableStringBuilder(mVipCenterZs01.getText().toString());
        SpannableStringBuilder builder02 = new SpannableStringBuilder(mVipCenterZs02.getText().toString());
        SpannableStringBuilder builder03 = new SpannableStringBuilder(mVipCenterZs03.getText().toString());
        SpannableStringBuilder builder04 = new SpannableStringBuilder(mVipCenterZs04.getText().toString());
        SpannableStringBuilder builder05 = new SpannableStringBuilder(mVipCenterZs05.getText().toString());
        SpannableStringBuilder builder06 = new SpannableStringBuilder(mVipCenterZs06.getText().toString());
        SpannableStringBuilder builder07 = new SpannableStringBuilder(mVipCenterZs07.getText().toString());
        SpannableStringBuilder builder08 = new SpannableStringBuilder(mVipCenterZs08.getText().toString());
        SpannableStringBuilder builder09 = new SpannableStringBuilder(mVipCenterZs09.getText().toString());
        SpannableStringBuilder builder10 = new SpannableStringBuilder(mVipCenterZs10.getText().toString());
        SpannableStringBuilder builder11 = new SpannableStringBuilder(mVipCenterZs11.getText().toString());
        SpannableStringBuilder builder12 = new SpannableStringBuilder(mVipCenterZs12.getText().toString());
        SpannableStringBuilder builder13 = new SpannableStringBuilder(mVipCenterZs13.getText().toString());
        SpannableStringBuilder builder14 = new SpannableStringBuilder(mVipCenterZs14.getText().toString());
        //SpannableStringBuilder builder14 = new SpannableStringBuilder(mVipCenterZs14.getText().toString());
        //SpannableStringBuilder builder15 = new SpannableStringBuilder(mVipCenterZs15.getText().toString());
//        SpannableStringBuilder builderBuy = new SpannableStringBuilder(mVipCenterBuy04Tv.getText().toString());
        builderMoney01.setSpan(new ForegroundColorSpan(Color.RED), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney01.setSpan(new ForegroundColorSpan(Color.RED), 12, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new ForegroundColorSpan(Color.RED), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new ForegroundColorSpan(Color.RED), 11, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")), 19, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new StrikethroughSpan(), 19, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new ForegroundColorSpan(Color.RED), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new ForegroundColorSpan(Color.RED), 13, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")), 21, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new StrikethroughSpan(), 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new ForegroundColorSpan(Color.RED), 8, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new ForegroundColorSpan(Color.RED), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")), 22, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new StrikethroughSpan(), 22, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder01.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder02.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder03.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder04.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder05.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder06.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder07.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder08.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder09.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder10.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder11.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder12.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder13.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder14.setSpan(purSpan, 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //builder14.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //builder15.setSpan(purSpan, 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builderBuy.setSpan(purSpan, 19, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mVipCenterBuy01TvMoney.setText(builderMoney01);
        mVipCenterBuy02TvMoney.setText(builderMoney02);
        mVipCenterBuy03TvMoney.setText(builderMoney03);
        mVipCenterBuy04TvMoney.setText(builderMoney04);
        mVipCenterZs01.setText(builder01);
       // builder02.setSpan(new ForegroundColorSpan(Color.RED), 16, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       // builder02.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 16, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mVipCenterZs02.setText(builder02);
        mVipCenterZs03.setText(builder03);
        mVipCenterZs04.setText(builder04);
        mVipCenterZs05.setText(builder05);
        mVipCenterZs06.setText(builder06);
        mVipCenterZs07.setText(builder07);
        mVipCenterZs08.setText(builder08);
        mVipCenterZs09.setText(builder09);
        mVipCenterZs10.setText(builder10);
        mVipCenterZs11.setText(builder11);
        mVipCenterZs12.setText(builder12);
        mVipCenterZs13.setText(builder13);
      //  builder14.setSpan(new ForegroundColorSpan(Color.RED), 21, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  builder14.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 21, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mVipCenterZs14.setText(builder14);
       /* mVipCenterZs14.setText(builder14);
        mVipCenterZs15.setText(builder15);*/
//        mVipCenterBuy04Tv.setText(builderBuy);
        buys = new ArrayList<>();
        buys.add(mVipCenterBuy01);
        buys.add(mVipCenterBuy02);
        buys.add(mVipCenterBuy03);
        buys.add(mVipCenterBuy04);

    }


    @OnClick({R.id.mVipCenter_ll_buy_01,R.id.mVipCenter_ll_buy_02,R.id.mVipCenter_ll_buy_03,R.id.mVipCenter_ll_buy_04})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mVipCenter_ll_buy_01:
                buyamount = 30;
                buysubject = 1;
                selectSomeOne(0);
                break;
            case R.id.mVipCenter_ll_buy_02:
                buyamount = 88;
                buysubject = 2;
                selectSomeOne(1);
                break;
            case R.id.mVipCenter_ll_buy_03:
                buyamount = 168;
                buysubject = 3;
                selectSomeOne(2);
                break;
            case R.id.mVipCenter_ll_buy_04:
                buyamount = 298;
                buysubject = 4;
                selectSomeOne(3);
                break;
        }
        EventBus.getDefault().post(new VipEvent(1,buyamount,buysubject));
    }

    public void selectSomeOne(int index) {
        for (int i = 0; i < buys.size(); i++) {
            buys.get(i).setImageResource(R.mipmap.pursewxz);
        }
        buys.get(index).setImageResource(R.mipmap.pursexz);
    }

}

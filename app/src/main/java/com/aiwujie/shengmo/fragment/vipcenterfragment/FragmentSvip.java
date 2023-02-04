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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/11/22.
 */

public class FragmentSvip extends Fragment {
    @BindView(R.id.mSvipCenter_buy01_tv_money)
    TextView mSvipCenterBuy01TvMoney;
    @BindView(R.id.mSvipCenter_buy01)
    ImageView mSvipCenterBuy01;
    @BindView(R.id.mSvipCenter_ll_buy_01)
    PercentLinearLayout mSvipCenterLlBuy01;
    @BindView(R.id.mSvipCenter_buy02_tv_money)
    TextView mSvipCenterBuy02TvMoney;
    @BindView(R.id.mSvipCenter_buy02)
    ImageView mSvipCenterBuy02;
    @BindView(R.id.mSvipCenter_ll_buy_02)
    PercentLinearLayout mSvipCenterLlBuy02;
    @BindView(R.id.mSvipCenter_buy03_tv_money)
    TextView mSvipCenterBuy03TvMoney;
    @BindView(R.id.mSvipCenter_buy03)
    ImageView mSvipCenterBuy03;
    @BindView(R.id.mSvipCenter_ll_buy_03)
    PercentLinearLayout mSvipCenterLlBuy03;
    @BindView(R.id.mSvipCenter_buy04_tv_money)
    TextView mSvipCenterBuy04TvMoney;
    @BindView(R.id.mSvipCenter_buy04)
    ImageView mSvipCenterBuy04;
    @BindView(R.id.mSvipCenter_ll_buy_04)
    PercentLinearLayout mSvipCenterLlBuy04;
    @BindView(R.id.mSvipCenter_bottomTv)
    TextView mSvipCenterBottomTv;
    @BindView(R.id.mSvipCenter_zs01)
    TextView mSvipCenterZs01;
    @BindView(R.id.mSvipCenter_zs02)
    TextView mSvipCenterZs02;
    @BindView(R.id.mSvipCenter_zs03)
    TextView mSvipCenterZs03;
    @BindView(R.id.mSvipCenter_zs04)
    TextView mSvipCenterZs04;
    @BindView(R.id.mSvipCenter_zs05)
    TextView mSvipCenterZs05;
    @BindView(R.id.mSvipCenter_zs06)
    TextView mSvipCenterZs06;
    @BindView(R.id.mSvipCenter_zs07)
    TextView mSvipCenterZs07;
    @BindView(R.id.mSvipCenter_zs08)
    TextView mSvipCenterZs08;
    @BindView(R.id.mSvipCenter_zs09)
    TextView mSvipCenterZs09;
    @BindView(R.id.mSvipCenter_zs10)
    TextView mSvipCenterZs10;
    @BindView(R.id.mSvipCenter_zs12)
    TextView mSvipCenterZs12;
    @BindView(R.id.mSvipCenter_zs13)
    TextView mSvipCenterZs13;
    @BindView(R.id.mSvipCenter_zs14)
    TextView mSvipCenterZs14;
    @BindView(R.id.mSvipCenter_zs15)
    TextView mSvipCenterZs15;

    @BindView(R.id.mSvipCenter_zs11)
    TextView mSvipCenterZs11;
    private int buyamount = 128;
    private int buysubject = 1;
    private ArrayList<ImageView> buys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_mypurse_svip, null);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {



        List<TextView> textViewList = new ArrayList<>();
        textViewList.add(mSvipCenterZs01);
        textViewList.add(mSvipCenterZs02);
        textViewList.add(mSvipCenterZs03);
        textViewList.add(mSvipCenterZs04);
        textViewList.add(mSvipCenterZs05);
        textViewList.add(mSvipCenterZs15);
        textViewList.add(mSvipCenterZs12);
        textViewList.add(mSvipCenterZs06);
        textViewList.add(mSvipCenterZs13);
        textViewList.add(mSvipCenterZs07);
        textViewList.add(mSvipCenterZs08);
        textViewList.add(mSvipCenterZs09);
        textViewList.add(mSvipCenterZs14);
        textViewList.add(mSvipCenterZs10);
        textViewList.add(mSvipCenterZs11);
        buys = new ArrayList<>();
        buys.add(mSvipCenterBuy01);
        buys.add(mSvipCenterBuy02);
        buys.add(mSvipCenterBuy03);
        buys.add(mSvipCenterBuy04);
        ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
        SpannableStringBuilder builderMoney01 = new SpannableStringBuilder(mSvipCenterBuy01TvMoney.getText().toString());
        SpannableStringBuilder builderMoney02 = new SpannableStringBuilder(mSvipCenterBuy02TvMoney.getText().toString());
        SpannableStringBuilder builderMoney03 = new SpannableStringBuilder(mSvipCenterBuy03TvMoney.getText().toString());
        SpannableStringBuilder builderMoney04 = new SpannableStringBuilder(mSvipCenterBuy04TvMoney.getText().toString());
        SpannableStringBuilder builderBottom = new SpannableStringBuilder(mSvipCenterBottomTv.getText().toString());
        builderMoney01.setSpan(new ForegroundColorSpan(Color.RED), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney01.setSpan(new ForegroundColorSpan(Color.RED), 12, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new ForegroundColorSpan(Color.RED), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new ForegroundColorSpan(Color.RED), 12, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")), 21, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney02.setSpan(new StrikethroughSpan(), 21, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new ForegroundColorSpan(Color.RED), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new ForegroundColorSpan(Color.RED), 12, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")), 21, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney03.setSpan(new StrikethroughSpan(), 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new ForegroundColorSpan(Color.RED), 8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new ForegroundColorSpan(Color.RED), 14, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")), 24, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney04.setSpan(new StrikethroughSpan(), 24, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderBottom.setSpan(purSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSvipCenterBuy01TvMoney.setText(builderMoney01);
        mSvipCenterBuy02TvMoney.setText(builderMoney02);
        mSvipCenterBuy03TvMoney.setText(builderMoney03);
        mSvipCenterBuy04TvMoney.setText(builderMoney04);
        mSvipCenterBottomTv.setText(builderBottom);
//        for (int i = 0; i < textViewList.size(); i++) {
//            SpannableStringBuilder builder = new SpannableStringBuilder(textViewList.get(i).getText().toString());
//            if (i >= 9) {
//                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#b73acb")), 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                if(i==12){
//                    builder.setSpan(new ForegroundColorSpan(Color.RED), 17, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 17, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//            } else {
//                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#b73acb")), 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                if(i==3){
//                    builder.setSpan(new ForegroundColorSpan(Color.RED), 25, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 25, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }else if(i==4){
//                    builder.setSpan(new ForegroundColorSpan(Color.RED), 18, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 18, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }else if(i==5){
//                    builder.setSpan(new ForegroundColorSpan(Color.RED), 19, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 19, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }else if(i==6){
//                    builder.setSpan(new ForegroundColorSpan(Color.RED), 21, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 21, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }else if(i==8){
//                    builder.setSpan(new ForegroundColorSpan(Color.RED), 22, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 22, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//            }
//            textViewList.get(i).setText(builder);
//        }

    }


    @OnClick({R.id.mSvipCenter_ll_buy_01, R.id.mSvipCenter_ll_buy_02, R.id.mSvipCenter_ll_buy_03, R.id.mSvipCenter_ll_buy_04})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSvipCenter_ll_buy_01:
                buyamount = 128;
                buysubject = 1;
                selectSomeOne(0);
                break;
            case R.id.mSvipCenter_ll_buy_02:
                buyamount = 348;
                buysubject = 2;
                selectSomeOne(1);
                break;
            case R.id.mSvipCenter_ll_buy_03:
                buyamount = 898;
                buysubject = 3;
                selectSomeOne(2);
                break;
            case R.id.mSvipCenter_ll_buy_04:
                buyamount = 1298;
                buysubject = 4;
                selectSomeOne(3);
                break;
        }
        EventBus.getDefault().post(new VipEvent(2, buyamount, buysubject));
    }

    public void selectSomeOne(int index) {
        for (int i = 0; i < buys.size(); i++) {
            buys.get(i).setImageResource(R.mipmap.pursewxz);
        }
        buys.get(index).setImageResource(R.mipmap.pursexz);
    }

}

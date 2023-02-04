package com.aiwujie.shengmo.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DuihuanModouActivity;
import com.aiwujie.shengmo.activity.WithdrawalsActivity;
import com.aiwujie.shengmo.adapter.GiftItemRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.AnchorAuthActivity;
import com.aiwujie.shengmo.kt.ui.activity.ExchangeBeansActivity;
import com.aiwujie.shengmo.kt.ui.activity.WithdrawActivity;
import com.aiwujie.shengmo.kt.ui.activity.WithdrawAuthActivity;
import com.aiwujie.shengmo.kt.ui.activity.WithdrawDetailActivity;
import com.aiwujie.shengmo.kt.ui.fragment.LazyFragment;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.kt.util.NormalConstant;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.timlive.bean.LiveAuthInfo;
import com.aiwujie.shengmo.timlive.ui.LiveRoomRegisterAuthActivity;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aiwujie.shengmo.timlive.net.RoomManager.createRoom;

public class GiftFragment extends LazyFragment {
    @BindView(R.id.guide_line_1)
    Guideline guideLine1;
    @BindView(R.id.guide_line_2)
    Guideline guideLine2;
    @BindView(R.id.tv_fragment_gift_num)
    TextView tvFragmentGiftNum;
    @BindView(R.id.tv_fragment_gift_bean_balance)
    TextView tvFragmentGiftBeanBalance;
    @BindView(R.id.tv_fragment_gift_free)
    TextView tvFragmentGiftFree;
    @BindView(R.id.tv_fragment_gift_charm)
    TextView tvFragmentGiftCharm;
    @BindView(R.id.tv_fragment_gift_bean_used)
    TextView tvFragmentGiftBeanUsed;
    @BindView(R.id.tv_fragment_gift_pushed)
    TextView tvFragmentGiftPushed;
    @BindView(R.id.rv_fragment_gift)
    RecyclerView rvFragmentGift;
    @BindView(R.id.tv_fragment_gift_exchange)
    TextView tvFragmentGiftExchange;
    Unbinder unbinder;
    @BindView(R.id.tv_fragment_gift_withdraw)
    TextView tvFragmentGiftWithdraw;
    private String useableBeans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_gift, container, false);
        unbinder = ButterKnife.bind(this, view);
        setListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getMyPresent() {
        HttpHelper.getInstance().getUserPresent(MyApp.uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                MyPresentData myPresentData = GsonUtil.GsonToBean(data, MyPresentData.class);
                if (myPresentData != null) {
                    MyPresentData.DataBean presentData = myPresentData.getData();
                    useableBeans = presentData.getUseableBeans();
                    tvFragmentGiftNum.setText(presentData.getAllnum());
                    tvFragmentGiftCharm.setText(presentData.getAllamount());
                    tvFragmentGiftBeanBalance.setText(presentData.getUseableBeans());
                    tvFragmentGiftBeanUsed.setText(String.valueOf(Math.abs(Integer.parseInt(presentData.getUsedbeans()))));
                    tvFragmentGiftFree.setText(presentData.getFree());
                    tvFragmentGiftPushed.setText(presentData.getTopcard() + "/" + presentData.getTopcard() + "00");
                    GiftItemRecyclerAdapter giftItemRecyclerAdapter = new GiftItemRecyclerAdapter(getActivity(), presentData.getGiftArr());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    rvFragmentGift.setAdapter(giftItemRecyclerAdapter);
                    rvFragmentGift.setLayoutManager(gridLayoutManager);
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    void setListener() {
        tvFragmentGiftExchange.setOnClickListener(v -> {
            if (TextUtil.isEmpty(useableBeans)) {
                return;
            }
            Intent intent = new Intent(getActivity(), ExchangeBeansActivity.class);
            intent.putExtra(IntentKey.BALANCE,useableBeans);
            startActivityForResult(intent, NormalConstant.REQUEST_CODE_1);
        });

        tvFragmentGiftWithdraw.setVisibility(View.VISIBLE);
        tvFragmentGiftWithdraw.setOnClickListener(v -> {
            if (TextUtil.isEmpty(useableBeans)) {
                ToastUtil.show(getActivity(),"未获取到礼物");
                return;
            }
            getWithdrawAuth();
        });
    }


    private void getWithdrawAuth() {
        HttpHelper.getInstance().getWithdrawAuth(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveAuthInfo liveAuthInfo = GsonUtil.GsonToBean(data, LiveAuthInfo.class);
                if(liveAuthInfo != null && liveAuthInfo.getData() != null){
                    LiveAuthInfo.DataBean authInfo = liveAuthInfo.getData();
                    if ("1".equals(authInfo.getIs_withdrawal())) {
                        Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), WithdrawAuthActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(getActivity(),msg);
            }
        });
    }

    @Override
    public void loadData() {
        getMyPresent();
    }

    @Override
    public int getContentViewId() {
        return R.layout.app_fragment_gift;
    }

    @Override
    public void initView(@NotNull View rootView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NormalConstant.REQUEST_CODE_1 && resultCode == NormalConstant.RESULT_CODE_OK) {
            if (data.getStringExtra(IntentKey.BALANCE) != null) {
                useableBeans = data.getStringExtra(IntentKey.BALANCE);
                tvFragmentGiftBeanBalance.setText(useableBeans);
            }
        }
    }
}

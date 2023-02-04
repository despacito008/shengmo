package com.aiwujie.shengmo.timlive.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.SetBankcardActivity;
import com.aiwujie.shengmo.activity.SfzrzActivity;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.timlive.bean.LiveAuthInfo;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主播申请权限页面
 */
public class LiveRoomRegisterAuthActivity extends AppCompatActivity {
    private Context mContext;
    @BindView(R.id.btn_start_auth)
    Button btnStartAuth;
    @BindView(R.id.iv_normal_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivTitleMore;
    @BindView(R.id.ll_bind_phone)
    ConstraintLayout llBindPhone;
    @BindView(R.id.ll_bind_bank_card)
    ConstraintLayout llBindBankCard;
    @BindView(R.id.ll_bind_ic_card)
    ConstraintLayout llBindIcCard;
    @BindView(R.id.tv_bind_phone_start)
    TextView tvBindPhone;
    @BindView(R.id.tv_bind_bank_card_start)
    TextView tvBindBankCard;
    @BindView(R.id.tv_auth_start)
    TextView tvIsAuth;
    private boolean isAuthIdCard = false; //是否认证身份证卡
    private boolean isAuthMobile = false; //是否认证手机号
    private boolean isAuthBankCard = false; //是否认证银行卡
    private String icCardstate="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_room_auth_register);
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);
        initView();
        mContext = this;
    }

    private void initView() {
        tvTitle.setText(getResources().getString(R.string.live_require_anchor));
        ivTitleMore.setVisibility(View.GONE);
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.ll_bind_phone,R.id.ll_bind_bank_card,R.id.ll_bind_ic_card,R.id.btn_start_auth})
    public void onViewClicked(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.ll_bind_phone:
                if (!isAuthMobile) {
                    intent = new Intent(this, BindingMobileActivity.class);
                    intent.putExtra("neworchange", "new");
                    startActivity(intent);
                }
                break;
            case R.id.ll_bind_bank_card:
                if(!isAuthBankCard){
                    intent=new Intent(this, SetBankcardActivity.class);
                    intent.putExtra("operation","add");
                    startActivity(intent);
                }
                break;
            case R.id.ll_bind_ic_card:
                if (!isAuthIdCard){
                    intent = new Intent(this, SfzrzActivity.class);
                    intent.putExtra("state",icCardstate.equals("0") ? "0" : "3");
                    startActivity(intent);
                }
                break;
            case R.id.btn_start_auth:
                applyAnchor();
                break;
        }
    }

    //申请主播
    private void applyAnchor() {
        LiveHttpHelper.getInstance().applyAnchor(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                try{
                    JSONObject jsonObject = new JSONObject(data);
                    ToastUtil.show(mContext,jsonObject.getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(mContext,msg);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //获取绑定状态
        anchorAutnStatus();
    }

    //刷新按钮状态
    private void anchorAutnStatus() {
        LiveHttpHelper.getInstance().isAuth(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                Log.d("onSuccess",data);
                LiveAuthInfo liveAuthInfo = GsonUtil.GsonToBean(data, LiveAuthInfo.class);
                if(liveAuthInfo != null && liveAuthInfo.getData() != null) {
                    if(!TextUtils.isEmpty(liveAuthInfo.getData().getIdcardStatus())){
                        icCardstate = liveAuthInfo.getData().getIdcardStatus();
                    }
                    boolean isAuthIdCard = isAuthIdCard(liveAuthInfo.getData().getRealidcardStatus());
                    boolean isAuthMobile = isAuthMobile(liveAuthInfo.getData().getBindingMobileStatus());
                    boolean isAuthBankCard = isAuthBankCard(liveAuthInfo.getData().getBankCardStatus());
                    if(isAuthIdCard && isAuthMobile && isAuthBankCard){
                        btnStartAuth.setClickable(true);
                        if(!TextUtils.isEmpty(liveAuthInfo.getData().getApplicationAnchorStatus())){
                            String anchorApplyStatus = liveAuthInfo.getData().getApplicationAnchorStatus();
                            //备注：是否申请过主播认证 0待审核 1 审核通过 2 审核不通过 3 未申请
                            if ("2".equals(anchorApplyStatus)){
                                btnStartAuth.setText("审核不通过");
                                btnStartAuth.setClickable(false);
                            } else if ("3".equals(anchorApplyStatus)){
                                btnStartAuth.setText("确认申请");
                                btnStartAuth.setClickable(true);
                            } else if ("0".equals(anchorApplyStatus)){
                                btnStartAuth.setText("审核中");
                                btnStartAuth.setClickable(false);
                            } else if("1".equals(anchorApplyStatus)) {
                                btnStartAuth.setText(getString(R.string.auth_start));//立即认证
                                btnStartAuth.setClickable(true);
                            }
                            btnStartAuth.setTextColor(getResources().getColor(R.color.white));
                            btnStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_pre);
                        }
                    }else{
                        btnStartAuth.setClickable(false);
                        btnStartAuth.setText("确认申请");
                        btnStartAuth.setTextColor(getResources().getColor(R.color.lightGray));
                        btnStartAuth.setBackgroundResource(R.drawable.bg_live_aquire_nor);
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(mContext,msg);
            }
        });
    }


    /**
     * 备注：自拍认证状态 0未申请 1 已通过 2待审核
     * @param state
     * @return
     */
    private boolean isAuthIdCard(String state){
        if (state.equals("1")){
            tvIsAuth.setText(getString(R.string.bind_complate));
            tvIsAuth.setTextColor(Color.GRAY);
            isAuthIdCard = true;
        }else if (state.equals("2")){
            tvIsAuth.setText("审核中");
            isAuthIdCard = false;
        }else if(state.equals("0")){
            tvIsAuth.setText(getString(R.string.auth_start));
            isAuthIdCard = false;
        }
        return isAuthIdCard;
    }

    /**
     * 备注：银行卡信息 1已绑定 0未绑定
     * @param state
     * @return
     */
    private boolean isAuthBankCard(String state){
        if (state.equals("1")){
            isAuthBankCard = true;
            tvBindBankCard.setText(getResources().getString(R.string.bind_complate));
            tvBindBankCard.setTextColor(Color.GRAY);
        }else if (state.equals("0")){
            isAuthBankCard = false;
            tvBindBankCard.setText(getString(R.string.bind_start));
            tvBindBankCard.setTextColor(getResources().getColor(R.color.bind_common_bg_purple));
        }
        return isAuthBankCard;
    }

    /**
     * 备注：绑定手机号的状态 1已绑定 0 未绑定
     * @param state
     * @return
     */
    private boolean isAuthMobile(String state){
        if (state.equals("1")){
            tvBindPhone.setText(getResources().getString(R.string.bind_complate));
            tvBindPhone.setTextColor(Color.GRAY);
            isAuthMobile = true;
        }else if (state.equals("0")){
            tvBindPhone.setText(getString(R.string.bind_start));
            tvBindPhone.setTextColor(getResources().getColor(R.color.bind_common_bg_purple));
            isAuthMobile = false;
        }
        return isAuthMobile;
    }
}

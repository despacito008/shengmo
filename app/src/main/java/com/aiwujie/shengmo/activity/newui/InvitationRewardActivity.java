package com.aiwujie.shengmo.activity.newui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.base.BaseNetActivity;
import com.aiwujie.shengmo.bean.InviteStateBean;
import com.aiwujie.shengmo.kt.ui.activity.normallist.InviteRewardDetailActivity;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.CopyLinkPop;
import com.aiwujie.shengmo.view.SharePicPop;
import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvitationRewardActivity extends BaseNetActivity {
    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.tv_normal_title_more)
    TextView tvNormalTitleMore;
    @BindView(R.id.iv_invitation_reward_bg)
    ImageView ivInvitationRewardBg;
    @BindView(R.id.tv_invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.tv_invitation_code_tips)
    TextView tvInvitationCodeTips;
    @BindView(R.id.tv_invitation_code_operate)
    TextView tvInvitationCodeOperate;
    @BindView(R.id.tv_invitation_pic_share)
    TextView tvInvitationPicShare;
    @BindView(R.id.tv_invitation_reward_tips)
    TextView tvInvitationRewardTips;
    @BindView(R.id.tv_invitation_reward_operate)
    TextView tvInvitationRewardOperate;
    String inviteUrl = "";
    String copyContent = "";
    @BindView(R.id.tv_invitation_code_content)
    TextView tvInvitationCodeContent;
    @BindView(R.id.ll_invitation_reward_get)
    LinearLayout llInvitationRewardGet;
    @BindView(R.id.rb_reward_svip)
    RadioButton rbRewardSvip;
    @BindView(R.id.rb_reward_bean)
    RadioButton rbRewardBean;
    @BindView(R.id.ll_reward_type)
    LinearLayout llRewardType;
    @BindView(R.id.rg_reward_type)
    RadioGroup rgRewardType;
    @BindView(R.id.tv_invitation_rule)
    TextView tvInvitationRule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_invitation_reward);
        ButterKnife.bind(this);
        initTitleBar();
        initListener();
        getUserInviteState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserInviteState();
    }

    void initTitleBar() {
        StatusBarUtil.showLightStatusBar(this);
        tvNormalTitleTitle.setText("邀请奖励");
        tvNormalTitleMore.setText("明细");
        ivNormalTitleMore.setVisibility(View.GONE);
        tvNormalTitleMore.setVisibility(View.VISIBLE);
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvNormalTitleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(InvitationRewardActivity.this, InvitationRewardDetailActivity.class);
                Intent intent = new Intent(InvitationRewardActivity.this, InviteRewardDetailActivity.class);
                startActivity(intent);
            }
        });


    }

    void initListener() {
        tvInvitationPicShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicSharePop();
            }
        });

        tvInvitationCodeOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("立即绑定".equals(tvInvitationCodeOperate.getText().toString())) {
                    Intent intent = new Intent(InvitationRewardActivity.this, BindingMobileActivity.class);
                    intent.putExtra("neworchange", "new");
                    startActivity(intent);
                } else if ("立即复制".equals(tvInvitationCodeOperate.getText().toString())) {
                    showCopyLinkPop();
                }
            }
        });

        tvInvitationRewardOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvitationRewardActivity.this, BindingMobileActivity.class);
                intent.putExtra("neworchange", "new");
                startActivity(intent);
            }
        });

        rgRewardType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbRewardSvip.isPressed() || rbRewardBean.isPressed()) {
                    switch (checkedId) {
                        case R.id.rb_reward_svip:
                            setRewardType(1);
                            break;
                        case R.id.rb_reward_bean:
                            setRewardType(2);
                            break;
                    }
                }
            }
        });
    }

    void getUserInviteState() {
        HttpHelper.getInstance().getInviteState(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                InviteStateBean inviteStateBean = GsonUtil.GsonToBean(data, InviteStateBean.class);
                inviteUrl = inviteStateBean.getData().getInvite_url();
                copyContent = inviteStateBean.getData().getInvite_text();
                tvInvitationCode.setText(inviteStateBean.getData().getInvite_code());
                tvInvitationCodeTips.setText("被邀请者需要填写此码");
                tvInvitationCodeOperate.setText("立即复制");
                tvInvitationCodeContent.setText(inviteStateBean.getData().getInvite_text());
                if (TextUtil.isEmpty(inviteStateBean.getData().getReward_text())) {
                    llInvitationRewardGet.setVisibility(View.GONE);
                } else {
                    llInvitationRewardGet.setVisibility(View.VISIBLE);
                    tvInvitationRewardTips.setText(inviteStateBean.getData().getReward_text());
                }
                if ("1".equals(inviteStateBean.getData().getInvite_reward_type())) {
                    rbRewardSvip.setChecked(true);
                } else if ("2".equals(inviteStateBean.getData().getInvite_reward_type())) {
                    rbRewardBean.setChecked(true);
                }

                Glide.with(InvitationRewardActivity.this)
                        .load(inviteStateBean.getData().getInvite_bg_url())
                        .error(R.drawable.ic_invitation_reward_bg)
                        .into(ivInvitationRewardBg);

                tvInvitationRule.setText(inviteStateBean.getData().getInvite_rule());
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 2002) {
                    tvInvitationCode.setText("******");
                    tvInvitationCodeTips.setText("绑定手机号才能显示");
                    tvInvitationCodeOperate.setText("立即绑定");
                    if (TextUtil.isEmpty(msg)) {
                        llInvitationRewardGet.setVisibility(View.GONE);
                    } else {
                        llInvitationRewardGet.setVisibility(View.VISIBLE);
                        tvInvitationRewardTips.setText(msg);
                    }
                }
                if (!InvitationRewardActivity.this.isDestroyed()) {
                    Glide.with(InvitationRewardActivity.this).load("http://image.aiwujie.com.cn/images/invite_bg.png").into(ivInvitationRewardBg);
                }
            }
        });
    }

    void showPicSharePop() {
        // String url = "http://image.aiwujie.com.cn/invite.png?x-oss-process=image/watermark,text_MSAyIDMgNCA1IDY=,color_DB57F3,size_35,g_se,x_152,y_92";
        if (!TextUtil.isEmpty(inviteUrl)) {
            SharePicPop sharePicPop = new SharePicPop(InvitationRewardActivity.this, inviteUrl);
            sharePicPop.showPopupWindow();
        } else {
            Intent intent = new Intent(InvitationRewardActivity.this, BindingMobileActivity.class);
            intent.putExtra("neworchange", "new");
            startActivity(intent);
        }
    }

    void showCopyLinkPop() {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyContent);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        CopyLinkPop copyLinkPop = new CopyLinkPop(InvitationRewardActivity.this, copyContent);
        copyLinkPop.showPopupWindow();
    }

    void setRewardType(int type) {
        HttpHelper.getInstance().setRewardType(String.valueOf(type), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(InvitationRewardActivity.this, "设置成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(InvitationRewardActivity.this, msg);
            }
        });
    }
}

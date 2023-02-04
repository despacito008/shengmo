package com.aiwujie.shengmo.timlive.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.SendredbaoActivity;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import razerdp.basepopup.BasePopupWindow;

public class SendLiveRedEnvelopesPop extends BasePopupWindow {
    Context mContext;
    String mRoomId;

    public SendLiveRedEnvelopesPop(Context context, String roomId) {
        super(context);
        mContext = context;
        mRoomId = roomId;
        setPopupGravity(Gravity.CENTER);
        this.setAdjustInputMethod(false);
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.app_layout_live_send_red_envelopes);
    }

    EditText etRedNum, etRedMoney;
    TextView tvRedSend;
    CheckBox cbRedIsOpen;

    void initView() {
        etRedNum = findViewById(R.id.num_doudou);
        etRedMoney = findViewById(R.id.zong_doudou);
        cbRedIsOpen = findViewById(R.id.cb_red_bag_isOpen);
        tvRedSend = findViewById(R.id.tv_live_red_envelopes_send);
        tvRedSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRedEnvelopes();
            }
        });
        etRedNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    etRedMoney.setHint(Integer.parseInt(s.toString()) * 2 + "魔豆起发");
                } else {
                    etRedMoney.setHint("0");
                }
            }
        });
    }

    void sendRedEnvelopes() {
        String num = etRedNum.getText().toString();
        String money = etRedMoney.getText().toString();
        if (TextUtil.isEmpty(num)) {
            ToastUtil.show(mContext, "红包个数不能为空");
            return;
        }
        if (TextUtil.isEmpty(money)) {
            ToastUtil.show(mContext, "总金额不能为空");
            return;
        }
//        if (Integer.valueOf(money) / 10 < Integer.valueOf(num)) {
//            ToastUtil.show(mContext, "平均每个红包不能少于10魔豆");
//            return;
//        }
        HttpHelper.getInstance().sendLiveRedEnvelopes(num, money, mRoomId,cbRedIsOpen.isChecked(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(mContext, "红包发送成功");
                dismiss();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(mContext, msg);
            }
        });
    }
}


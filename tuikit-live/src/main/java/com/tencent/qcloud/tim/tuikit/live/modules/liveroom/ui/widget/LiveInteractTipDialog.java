package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;


public class LiveInteractTipDialog extends DialogFragment {

    private PositiveClickListener mPositiveClickListener;
    private NegativeClickListener mNegativeClickListener;

    private String mMessageText;
    private String mMessagePositive;
    private String mMessageNegative;

    private EditText etInteract;

    private String interactTip = "";

    public static LiveInteractTipDialog newInstance(String tips) {

        Bundle args = new Bundle();
        args.putString("tip",tips);
        LiveInteractTipDialog fragment = new LiveInteractTipDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.TUILiveDialogFragment);
        dialog.setContentView(R.layout.live_dialog_interact_tip);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        initTextMessage(dialog);
        initButtonPositive(dialog);
        initButtonNegative(dialog);

       dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }

    private void initTextMessage(Dialog dialog){
        TextView textMessage = (TextView) dialog.findViewById(R.id.tv_pop_normal_tips_info);
        textMessage.setText(mMessageText);
        etInteract = dialog.findViewById(R.id.et_live_interact);
    }

    private void initButtonPositive(Dialog dialog){
        TextView buttonPositive = (TextView) dialog.findViewById(R.id.tv_pop_normal_tips_confirm);
        buttonPositive.setText(mMessagePositive);
        if (mPositiveClickListener == null){
            buttonPositive.setVisibility(View.GONE);
            return;
        }
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etInteract.getText().toString().trim())) {
                    ToastUtil.toastShortMessage("请输入互动消息");
                } else {
                    mPositiveClickListener.onClick(etInteract.getText().toString().trim());
                }
            }
        });
    }

    private void initButtonNegative(Dialog dialog){
        TextView buttonNegative = (TextView) dialog.findViewById(R.id.tv_pop_normal_tips_cancel);
        buttonNegative.setText(mMessageNegative);
        if (mNegativeClickListener == null){
            buttonNegative.setVisibility(View.GONE);
            return;
        }

        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNegativeClickListener.onClick();
            }
        });
    }

    public void setMessage(String message){
        mMessageText = message;
    }

    public void setPositiveStr(String positiveStr) {
        mMessagePositive = positiveStr;
    }

    public void setNegativeStr(String negativeStr) {
        mMessageNegative = negativeStr;
    }

    public void setPositiveClickListener(PositiveClickListener listener) {
        this.mPositiveClickListener = listener;
    }

    public void setNegativeClickListener(NegativeClickListener listener) {
        this.mNegativeClickListener = listener;
    }

    public interface PositiveClickListener {
        void onClick(String msg);
    }

    public interface NegativeClickListener {
        void onClick();
    }

    public boolean isShowing() {
        if (this.getDialog() != null && this.getDialog().isShowing()) {
            return true;
        } else {
            return false;
        }
    }
}

package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget;

import android.app.Dialog;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;

import java.lang.reflect.Field;


public class NormalTipDialog extends DialogFragment {

    private PositiveClickListener mPositiveClickListener;
    private NegativeClickListener mNegativeClickListener;

    private String mMessageText;
    private String mMessagePositive;
    private String mMessageNegative;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.TUILiveDialogFragment);
        dialog.setContentView(R.layout.live_dialog_normal_tip);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        initTextMessage(dialog);
        initButtonPositive(dialog);
        initButtonNegative(dialog);

        return dialog;
    }

    private void initTextMessage(Dialog dialog) {
        TextView textMessage = (TextView) dialog.findViewById(R.id.tv_pop_normal_tips_info);
        textMessage.setText(mMessageText);
    }

    private void initButtonPositive(Dialog dialog) {
        TextView buttonPositive = (TextView) dialog.findViewById(R.id.tv_pop_normal_tips_confirm);
        buttonPositive.setText(mMessagePositive);
        if (mPositiveClickListener == null) {
            buttonPositive.setVisibility(View.GONE);
            return;
        }
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositiveClickListener.onClick();
            }
        });
    }

    private void initButtonNegative(Dialog dialog) {
        TextView buttonNegative = (TextView) dialog.findViewById(R.id.tv_pop_normal_tips_cancel);
        buttonNegative.setText(mMessageNegative);
        if (mNegativeClickListener == null) {
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

    public void setMessage(String message) {
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
        void onClick();
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

    /**
     * 主要时这个方法。上面两个方法只是获取布局用的，可以不要
     *
     * @param manager
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field showByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            showByMe.setAccessible(true);
            showByMe.set(this, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

}
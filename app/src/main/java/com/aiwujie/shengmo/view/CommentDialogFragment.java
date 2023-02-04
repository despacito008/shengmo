package com.aiwujie.shengmo.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AtFansActivity;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommentDialogFragment extends DialogFragment {
    @BindView(R.id.et_discuss)
    EditText etDiscuss;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;
    @BindView(R.id.view_comment_pop_bg)
    View viewCommentPopBg;
    Unbinder unbinder;
    @BindView(R.id.view_dialog_fragment_outside)
    View viewDialogFragmentOutside;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_DialogWhenLarge_NoActionBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_dialog_fragment_comment, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        unbinder = ButterKnife.bind(this, view);
        initEvent();
        return view;
    }

    void initEvent() {
        viewDialogFragmentOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialogFragment.this.dismiss();
            }
        });

        etDiscuss.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.length() > start)) {
                    return;
                }
                if ('@' == s.charAt(start) && count == 1) {
                    if (onCommentSendListener != null) {
                        onCommentSendListener.onCommentAt();
                    }
                    return;
                }
                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
                    if ('@' == s.charAt(start - 1)) {
                        etDiscuss.getText().delete(start - 1, start);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentSendListener != null) {
                    onCommentSendListener.onCommentSend(etDiscuss.getText().toString());
                }
            }
        });
    }

    public void changeHint(String hint) {
        if (etDiscuss == null) {
            return;
        }
        if (TextUtil.isEmpty(hint)) {
            etDiscuss.setHint("点击输入你的评论");
        } else {
            etDiscuss.setHint(hint);
        }
    }

    public void clearText() {
        etDiscuss.setText("");
    }


    public EditText getInputText() {
        return etDiscuss;
    }

    public TextView getSendBtn() {
        return btnConfirm;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnCommentSendListener {
        void onCommentAt();
        void onCommentSend(String content);
    }

    public OnCommentSendListener onCommentSendListener;

    public void setOnCommentSendListener(OnCommentSendListener onCommentSendListener) {
        this.onCommentSendListener = onCommentSendListener;
    }
}

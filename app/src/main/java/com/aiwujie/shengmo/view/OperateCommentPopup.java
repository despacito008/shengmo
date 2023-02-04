package com.aiwujie.shengmo.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import razerdp.basepopup.BasePopupWindow;

public class OperateCommentPopup extends BasePopupWindow {
    private int type = 2;

    public OperateCommentPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        initView();
    }

    public OperateCommentPopup(Context context, int type) {
        super(context);
        this.type = type;
        setPopupGravity(Gravity.BOTTOM);
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_comment_operate);
    }

    void initView() {
        TextView tvCopy = findViewById(R.id.tv_pop_comment_operate_copy);
        TextView tvReport = findViewById(R.id.tv_pop_comment_operate_report);
        TextView tvCancel = findViewById(R.id.tv_pop_comment_operate_cancel);
        TextView tvDelete = findViewById(R.id.tv_pop_comment_operate_delete);

        if (type == 0) { //黑v 或者自己动态的评论
            tvCopy.setVisibility(View.VISIBLE);
            tvReport.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.VISIBLE);
        } else if (type == 1) { //自己的评论
            tvCopy.setVisibility(View.VISIBLE);
            tvReport.setVisibility(View.GONE);
            tvDelete.setVisibility(View.VISIBLE);
        } else if (type == 2) { //其他人
            tvCopy.setVisibility(View.VISIBLE);
            tvReport.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.GONE);
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentOperateListener != null) {
                    onCommentOperateListener.onCommentCopy();
                }
            }
        });

        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentOperateListener != null) {
                    onCommentOperateListener.onCommentReport();
                }
            }
        });


        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentOperateListener != null) {
                    onCommentOperateListener.onCommentDelete();
                }
            }
        });
    }

    public interface OnCommentOperateListener {
        void onCommentCopy();

        void onCommentReport();

        void onCommentDelete();
    }

    public OnCommentOperateListener onCommentOperateListener;

    public void setOnCommentOperateListener(OnCommentOperateListener onCommentOperateListener) {
        this.onCommentOperateListener = onCommentOperateListener;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 300);
    }

}

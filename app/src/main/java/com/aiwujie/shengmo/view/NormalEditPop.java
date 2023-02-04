package com.aiwujie.shengmo.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.LogUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class NormalEditPop extends BasePopupWindow {

    @BindView(R.id.tv_pop_normal_tips_title)
    TextView tvPopNormalTipsTitle;
    @BindView(R.id.et_pop_normal_tips_info)
    EditText etPopNormalTipsInfo;
    @BindView(R.id.tv_pop_normal_tips_cancel)
    TextView tvPopNormalTipsCancel;
    @BindView(R.id.tv_pop_normal_tips_confirm)
    TextView tvPopNormalTipsConfirm;
    @BindView(R.id.constraint_layout_tips)
    ConstraintLayout constraintLayoutTips;

    String title;
    String info;
    String hint;
    String cancelStr = "取消";
    String confirmStr = "确认";
    Context mContext;

//    public NormalTipsPop(Context context) {
//        this(context,new Builder());
//    }

    public NormalEditPop(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        this.title = builder.title;
        this.info = builder.info;
        this.hint = builder.hint;
        this.cancelStr = builder.cancelStr;
        this.confirmStr = builder.confirmStr;
        initView();
    }

    void initView() {
        tvPopNormalTipsTitle.setText(title);
        if (!TextUtil.isEmpty(hint)) {
            etPopNormalTipsInfo.setHint(hint);
            etPopNormalTipsInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        if (!TextUtil.isEmpty(info)) {
            etPopNormalTipsInfo.setText(info);
        }

        tvPopNormalTipsCancel.setText(cancelStr);
        tvPopNormalTipsConfirm.setText(confirmStr);
        if (TextUtil.isEmpty(cancelStr)) {
            tvPopNormalTipsCancel.setVisibility(View.GONE);
        }
        tvPopNormalTipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPopClickListener != null) {
                    onPopClickListener.cancelClick();
                }
            }
        });
        tvPopNormalTipsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPopClickListener != null) {
                    onPopClickListener.confirmClick(etPopNormalTipsInfo.getText().toString());
                }
            }
        });
        constraintLayoutTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("out side click");
            }
        });
        setOutSideTouchable(true);
        setAdjustInputMethod(false);
    }

    public void updateInfo(String info) {
        etPopNormalTipsInfo.setText(info);
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_normal_edit_tips);
        ButterKnife.bind(this, view);
        return view;
    }

    public static class Builder {
        String title;
        String info;
        String hint;
        String cancelStr = "取消";
        String confirmStr = "确认";
        Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setInfo(String info) {
            this.info = info;
            return this;
        }

        public Builder setCancelStr(String cancelStr) {
            this.cancelStr = cancelStr;
            return this;
        }

        public Builder setConfirmStr(String confirmStr) {
            this.confirmStr = confirmStr;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public NormalEditPop build() {
            return new NormalEditPop(this);
        }
    }

    public interface OnPopClickListener {
        void cancelClick();

        void confirmClick(String edit);
    }

    public OnPopClickListener onPopClickListener;

    public void setOnPopClickListener(OnPopClickListener onPopClickListener) {
        this.onPopClickListener = onPopClickListener;
    }
}

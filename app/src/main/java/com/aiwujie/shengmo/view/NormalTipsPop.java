package com.aiwujie.shengmo.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class NormalTipsPop extends BasePopupWindow {

    @BindView(R.id.tv_pop_normal_tips_title)
    TextView tvPopNormalTipsTitle;
    @BindView(R.id.tv_pop_normal_tips_info)
    TextView tvPopNormalTipsInfo;
    @BindView(R.id.tv_pop_normal_tips_cancel)
    TextView tvPopNormalTipsCancel;
    @BindView(R.id.tv_pop_normal_tips_confirm)
    TextView tvPopNormalTipsConfirm;
    @BindView(R.id.constraint_layout_tips)
    ConstraintLayout constraintLayoutTips;

    String title;
    String info;
    String cancelStr = "取消";
    String confirmStr = "确认";
    Context mContext;

//    public NormalTipsPop(Context context) {
//        this(context,new Builder());
//    }

    public NormalTipsPop(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        this.title = builder.title;
        this.info = builder.info;
        this.cancelStr = builder.cancelStr;
        this.confirmStr = builder.confirmStr;
        initView();
    }

    void initView() {
        tvPopNormalTipsTitle.setText(title);
        tvPopNormalTipsInfo.setText(info);
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
                    onPopClickListener.confirmClick();
                }
            }
        });
        constraintLayoutTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("out side click");
            }
        });
        setOutSideTouchable(false);
    }

    public void updateInfo(String info) {
        tvPopNormalTipsInfo.setText(info);
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_normal_tips);
        ButterKnife.bind(this, view);
        return view;
    }

    public static class Builder {
        String title;
        String info;
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

        public NormalTipsPop build() {
            return new NormalTipsPop(this);
        }
    }

    public interface OnPopClickListener {
        void cancelClick();

        void confirmClick();
    }

    public OnPopClickListener onPopClickListener;

    public void setOnPopClickListener(OnPopClickListener onPopClickListener) {
        this.onPopClickListener = onPopClickListener;
    }
}

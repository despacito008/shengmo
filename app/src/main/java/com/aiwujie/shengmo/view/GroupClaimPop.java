package com.aiwujie.shengmo.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.listener.NormalContentListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

public class GroupClaimPop extends BasePopupWindow {
    Context context;
    @BindView(R.id.tv_pop_claim_group_cancel)
    TextView tvPopClaimGroupCancel;
    @BindView(R.id.tv_pop_claim_group_confirm)
    TextView tvPopClaimGroupConfirm;
    @BindView(R.id.et_pop_group_claim)
    EditText etPopGroupClaim;

    public GroupClaimPop(Context context) {
        super(context);
        this.context = context;
        initListener();
    }

    @Override
    public View onCreateContentView() {
        View view = createPopupById(R.layout.app_pop_claim_group);
        ButterKnife.bind(this, view);
        return view;
    }

    void initListener() {
        tvPopClaimGroupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvPopClaimGroupConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (normalContentListener != null) {
                    normalContentListener.onNormalContent(etPopGroupClaim.getText().toString());
                }
            }
        });
    }

    public NormalContentListener normalContentListener;

    public void setNormalContentListener(NormalContentListener normalContentListener) {
        this.normalContentListener = normalContentListener;
    }
}

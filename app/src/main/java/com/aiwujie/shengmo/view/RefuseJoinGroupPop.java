package com.aiwujie.shengmo.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import razerdp.basepopup.BasePopupWindow;

public class RefuseJoinGroupPop extends BasePopupWindow {
    String userName;
    public RefuseJoinGroupPop(Context context,String userName) {
        super(context);
        this.userName = userName;
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_refuse_join_group);
    }

    void initView() {
        TextView tvInfo,tvCommit;
        final EditText etReason;
        tvInfo = findViewById(R.id.tv_pop_refuse_join_group_txt);
        tvCommit = findViewById(R.id.tv_pop_refuse_join_group_commit);
        etReason = findViewById(R.id.et_pop_refuse_join_group_reason);
        //tvInfo.setText("您正拒绝"+ userName + "的入群申请");
        tvInfo.setText(userName);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onPopRefuseListener != null) {
                    if (TextUtil.isEmpty(etReason.getText().toString())) {
                        onPopRefuseListener.onPopRefuse("");
                    } else {
                        onPopRefuseListener.onPopRefuse(etReason.getText().toString());
                    }
                }
            }
        });
    }

    public interface OnPopRefuseListener {
        void onPopRefuse(String reason);
    }

    OnPopRefuseListener onPopRefuseListener;

    public void setOnPopRefuseListener(OnPopRefuseListener onPopRefuseListener) {
        this.onPopRefuseListener = onPopRefuseListener;
    }
}

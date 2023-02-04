package com.tencent.qcloud.tim.tuikit.live.component.link;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;

public class ChooseLinkTypePanel extends BottomSheetDialog {

    public ChooseLinkTypePanel(@NonNull Context context) {
        super(context);
        //super(context, R.style.live_action_sheet_theme);
        setContentView(R.layout.live_view_choose_link_type);
        TextView tvLinkManager = findViewById(R.id.tv_live_link_type_manager);
        TextView tvLinkUser = findViewById(R.id.tv_live_link_type_user);
        tvLinkManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (linkTypeListener != null) {
                    linkTypeListener.onChooseManagerLink();
                }
            }
        });
        tvLinkUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (linkTypeListener != null) {
                    linkTypeListener.onChooseUserLink();
                }
            }
        });
    }

    public interface OnChooseLinkTypeListener {
        void onChooseManagerLink();
        void onChooseUserLink();
    }

    OnChooseLinkTypeListener linkTypeListener;

    public void setPkStopListener(OnChooseLinkTypeListener linkTypeListener) {
        this.linkTypeListener = linkTypeListener;
    }
}

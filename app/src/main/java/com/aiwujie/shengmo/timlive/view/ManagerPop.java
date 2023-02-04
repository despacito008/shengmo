package com.aiwujie.shengmo.timlive.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CardInfo;


public class ManagerPop extends PopupWindow {
    private TextView tvIsSpeaking;
    private CardInfo cardInfo;
    private TextView cancel;
    private String TAG = ManagerPop.this.getClass().getSimpleName();
    private Context context;
    private View mMenuView;
    private RadioGroup rg;
    public View getmMenuView() { return mMenuView; }

    public ManagerPop(@NonNull Context context, CardInfo cardInfo,TextView tvIsSpeaking) {
        super(context);
        this.context = context;
        this.cardInfo = cardInfo;
        this.tvIsSpeaking = tvIsSpeaking;
        // 设置SelectPicPopupWindow的View
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_live_manager, null);
        setContentView(mMenuView);
        initView();
    }

    private void initView() {
        cancel = mMenuView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rg = mMenuView.findViewById(R.id.rg_control_manager);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(onMenuItemClickListener != null){
                    onMenuItemClickListener.onMenuCick(group,checkedId);
                }
            }
        });
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnStartLoadListener(OnStartLoadListener onStartLoadListener){
        onStartLoadListener.onStartLoad(mMenuView);
    }

    public OnMenuItemClickListener onMenuItemClickListener;

    public interface OnMenuItemClickListener{
        void onMenuCick(RadioGroup radioGroup,int checkedId);
    }

    public interface OnStartLoadListener{
        void onStartLoad(View mMenuView);
    }
}

package com.tencent.qcloud.tim.tuikit.live.component.bottombar;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import java.util.List;


public class BottomToolBarLayout extends ConstraintLayout {

    private static final String TAG = "LiveBottomToolBarLayout";

    private Context            mContext;
    private ConstraintLayout   mLayoutRoot;
    private LinearLayout       mLayoutFunction;
    private TextView           mTextMessage;
    private InputTextMsgDialog mInputTextMsgDialog; // 消息输入框

    private LinearLayout.LayoutParams mFunctionLayoutParams;

    private LinearLayout.LayoutParams mFunctionLayoutParams2;

    public BottomToolBarLayout(Context context) {
        super(context);
        initView(context);
    }

    public BottomToolBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BottomToolBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutRoot = (ConstraintLayout) inflate(context, R.layout.live_layout_bottom_tool_bar, this);
        mLayoutFunction = mLayoutRoot.findViewById(R.id.ll_function);

        // 设置底部功能区按钮的添加样式
        int iconSize = mContext.getResources().getDimensionPixelSize(R.dimen.live_bottom_toolbar_btn_icon_size);
        mFunctionLayoutParams = new LinearLayout.LayoutParams(iconSize, iconSize);
        mFunctionLayoutParams.rightMargin = UIUtil.dp2px(mContext, 5);

        int iconSize2 = mContext.getResources().getDimensionPixelSize(R.dimen.live_bottom_toolbar_btn_View_size);
        int iconSize3 = mContext.getResources().getDimensionPixelSize(R.dimen.live_bottom_toolbar_btn_View_size2);
        mFunctionLayoutParams2 = new LinearLayout.LayoutParams(iconSize3, iconSize2);
       // mFunctionLayoutParams2.rightMargin = UIUtil.dp2px(mContext, 5);

        initTextMessageView();
    }

    private void initTextMessageView() {
        mInputTextMsgDialog = new InputTextMsgDialog(mContext, R.style.LiveInputDialog);
        
        mTextMessage = mLayoutRoot.findViewById(R.id.tv_message);
        mTextMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputMsgDialog();
            }
        });
    }

    private void showInputMsgDialog() {
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = UIUtil.getScreenWidth(mContext);
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    private void showInputMsgDialog(String txt) {
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = UIUtil.getScreenWidth(mContext);
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.sendText(txt);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    public void setOnTextSendListener(InputTextMsgDialog.OnTextSendDelegate listener) {
        mInputTextMsgDialog.setTextSendDelegate(listener);
    }

//    public void setRightButtonsLayout(List<CircleImageView> buttonList) {
//        mLayoutFunction.removeAllViews();
//        for (CircleImageView button : buttonList) {
//            if(button != null){
//                mLayoutFunction.addView(button, mFunctionLayoutParams);
//            }
//        }
//    }

    public void setRightButtonsLayout(List<View> buttonList) {
        mLayoutFunction.removeAllViews();
        for (View button : buttonList) {
            if(button != null){
                if (button instanceof CircleImageView) {
                    mLayoutFunction.addView(button, mFunctionLayoutParams);
                } else {
                    mLayoutFunction.addView(button, mFunctionLayoutParams2);
                }
            }
        }
    }

    public void onAt(String at) {
        showInputMsgDialog(at + " ");
    }

    public void updateInputState(boolean isClear) {
        mInputTextMsgDialog.updateTextState(isClear);
    }
}

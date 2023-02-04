package com.aliyun.apsara.alivclittlevideo.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aliyun.apsara.alivclittlevideo.R;

/**
 * 底部导航栏view
 *
 * @author xlx
 */
public class AlivcBottomView extends FrameLayout implements View.OnClickListener {

    private Context context;
    private FrameLayout tabHome;
    private FrameLayout tabUser;
    private ImageView recorderView;

    /**
     * 底部item click
     */
    private OnBottomItemClickListener onBottomItemClickListener;
    private LinearLayout bottomTabBg;

    public AlivcBottomView(Context context) {
        this(context, null);
    }

    public AlivcBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setClipChildren(false);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_view, this, true);
        tabHome = view.findViewById(R.id.tab_home);
        tabUser = view.findViewById(R.id.tab_user);
        recorderView = view.findViewById(R.id.iv_recorder);
        bottomTabBg = view.findViewById(R.id.bottom_tab_bg);
        tabHome.setActivated(true);
        recorderView.setOnClickListener(this);
        tabHome.setOnClickListener(this);
        tabUser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tab_home) {
            changeButtonSelect(v);
            if (onBottomItemClickListener != null) {
                onBottomItemClickListener.onHomeItemClick();
            }
            bottomTabBg.setBackgroundColor(Color.TRANSPARENT);

        } else if (v.getId() == R.id.tab_user) {
            changeButtonSelect(v);
            if (onBottomItemClickListener != null) {
                onBottomItemClickListener.onUserItemClick();
            }
            bottomTabBg.setBackgroundColor(Color.parseColor("#1E222D"));

        } else if (v.getId() == R.id.iv_recorder) {
            if (onBottomItemClickListener != null) {
                onBottomItemClickListener.onMoreItemClick();
            }

        }

    }

    /**
     * bottom tab 下划线切换
     * @param view
     */
    private void changeButtonSelect(View view) {
        tabHome.setActivated(false);
        tabUser.setActivated(false);
        view.setActivated(true);
    }



    /**
     * 底部导航栏click listener
     */
    public interface OnBottomItemClickListener {
        /**
         * 首页 click
         */
        void onHomeItemClick();

        /**
         * 用户 click
         */
        void onUserItemClick();

        /**
         * 更多 click
         */
        void onMoreItemClick();
    }

    public void setOnBottomItemClickListener(OnBottomItemClickListener listener) {
        this.onBottomItemClickListener = listener;
    }
}
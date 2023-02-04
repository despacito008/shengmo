package com.aiwujie.shengmo.view.gloading;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.aiwujie.shengmo.R;

import static com.aiwujie.shengmo.utils.NetWorkUtils.isNetworkConnected;
import static com.aiwujie.shengmo.view.gloading.Gloading.STATUS_EMPTY_DATA;
import static com.aiwujie.shengmo.view.gloading.Gloading.STATUS_LOADING;
import static com.aiwujie.shengmo.view.gloading.Gloading.STATUS_LOAD_FAILED;
import static com.aiwujie.shengmo.view.gloading.Gloading.STATUS_LOAD_SUCCESS;


/**
 * simple loading status view for global usage
 * @author billy.qi
 * @since 19/3/19 23:12
 */
@SuppressLint("ViewConstructor")
public class GlobalLoadingStatusView extends LinearLayout implements View.OnClickListener {

    public static final int STATUS_LIVE_EMPTY = 101;

    private final TextView mTextView;
    private final Runnable mRetryTask;
    private final ImageView mImageView;

    public GlobalLoadingStatusView(Context context, Runnable retryTask) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_global_loading_status, this, true);
        mImageView = findViewById(R.id.image);
        mTextView = findViewById(R.id.text);
        this.mRetryTask = retryTask;
        //setBackgroundColor(0xFFF0F0F0);
        setBackgroundColor(0xFFFFFFFF);
    }

    public void setMsgViewVisibility(boolean visible) {
        mTextView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setStatus(int status) {
        boolean show = true;
        OnClickListener onClickListener = null;
        int image = R.drawable.ic_normal_loading;
        int str = R.string.g_none;
        switch (status) {
            case STATUS_LOAD_SUCCESS: show = false; break;
            case STATUS_LOADING: str = R.string.g_loading;  show = true; break;
            case STATUS_LOAD_FAILED:
                str = R.string.g_loading_fail;
                image = R.drawable.ic_user_info_dynamic_empty;
                Boolean networkConn = isNetworkConnected(getContext());
                if (networkConn != null && !networkConn) {
                    str = R.string.g_loading_net_error;
                    image = R.drawable.ic_user_info_dynamic_empty;
                }
                onClickListener = this;
                break;
            case STATUS_EMPTY_DATA:
                str = R.string.g_empty;
                image = R.drawable.ic_user_info_dynamic_empty;
                break;
            case STATUS_LIVE_EMPTY:
                str = R.string.g_live_empty;
                image = R.mipmap.gift_list_is_null;
                break;
            default: break;
        }
        mImageView.setImageResource(image);
        setOnClickListener(onClickListener);
        mTextView.setText(str);
        setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (mRetryTask != null) {
            mRetryTask.run();
        }
    }

}

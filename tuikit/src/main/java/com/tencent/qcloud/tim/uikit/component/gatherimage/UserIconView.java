package com.tencent.qcloud.tim.uikit.component.gatherimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import java.util.List;


public class UserIconView extends RelativeLayout {

    private SynthesizedImageView mIconView;
    private DynamicChatUserIconView mDynamicView;
    private int mDefaultImageResId;
    private int mIconRadius;
    private ImageView ivIdentity;

    public UserIconView(Context context) {
        super(context);
        init(null);
    }

    public UserIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UserIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setDynamicChatIconView(DynamicChatUserIconView dynamicView) {
        mDynamicView = dynamicView;
        mDynamicView.setLayout(this);
        mDynamicView.setMainViewId(R.id.profile_icon_group);
        if (mDynamicView.getIconRadius() >= 0) {
            mIconView.setRadius(mDynamicView.getIconRadius());
        }
    }

    public void invokeInformation(MessageInfo messageInfo) {
        mIconView.load();
        if (mDynamicView != null) {
            mDynamicView.parseInformation(messageInfo);
        }
    }

    private void init(AttributeSet attributeSet) {
        inflate(getContext(), R.layout.profile_icon_view, this);
        if (attributeSet != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.UserIconView);
            if (null != ta) {
                mDefaultImageResId = ta.getResourceId(R.styleable.UserIconView_default_image, mDefaultImageResId);
                mIconRadius = ta.getDimensionPixelSize(R.styleable.UserIconView_image_radius, mIconRadius);
                ta.recycle();
            }
        }

        mIconView = findViewById(R.id.profile_icon);
        ivIdentity = findViewById(R.id.profile_identity);
        ivIdentity.setVisibility(VISIBLE);
        if (mDefaultImageResId > 0) {
            mIconView.defaultImage(mDefaultImageResId);
        }
        if (mIconRadius > 0) {
            mIconView.setRadius(mIconRadius);
        }
    }

    public void setDefaultImageResId(int resId) {
        mDefaultImageResId = resId;
        mIconView.defaultImage(resId);
    }

    public void setRadius(int radius) {
        mIconRadius = radius;
        mIconView.setRadius(mIconRadius);
    }

    public void setIconUrls(List<Object> iconUrls) {
        mIconView.displayImage(iconUrls).load();
    }

    public void initIdentity(int type) {
        ivIdentity.setVisibility(VISIBLE);
        switch (type) {
            case 1 :
                ivIdentity.setImageResource(R.drawable.user_manager);
                break;
            case 2 :
                ivIdentity.setImageResource(R.drawable.user_svip_year);
                break;
            case 3 :
                ivIdentity.setImageResource(R.drawable.user_svip);
                break;
            case 4 :
                ivIdentity.setImageResource(R.drawable.user_vip_year);
                break;
            case 5 :
                ivIdentity.setImageResource(R.drawable.user_vip);
                break;
            default:
                ivIdentity.setVisibility(INVISIBLE);
                break;
        }
    }

}

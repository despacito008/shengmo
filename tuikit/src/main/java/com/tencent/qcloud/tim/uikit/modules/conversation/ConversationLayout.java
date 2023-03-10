package com.tencent.qcloud.tim.uikit.modules.conversation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

public class ConversationLayout extends RelativeLayout implements IConversationLayout {

    private TitleBarLayout mTitleBarLayout;
    private ConversationListLayout mConversationList;

    public ConversationLayout(Context context) {
        super(context);
        init();
    }

    public ConversationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConversationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关UI元素
     */
    private void init() {
        inflate(getContext(), R.layout.conversation_layout, this);
        mTitleBarLayout = findViewById(R.id.conversation_title);
        mConversationList = findViewById(R.id.conversation_list);
    }
    ConversationListAdapter adapter;
    public void initDefault() {
        mTitleBarLayout.setTitle(getResources().getString(R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mTitleBarLayout.getLeftGroup().setVisibility(View.GONE);
        mTitleBarLayout.setRightIcon(R.drawable.conversation_more);
        adapter = new ConversationListAdapter();
        mConversationList.setAdapter(adapter);
        mConversationList.loadConversation(0);
    }

    public void initDefault(boolean hideSearchBar) {
        mTitleBarLayout.setTitle(getResources().getString(R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mTitleBarLayout.getLeftGroup().setVisibility(View.GONE);
        mTitleBarLayout.setRightIcon(R.drawable.conversation_more);

        final IConversationAdapter adapter = new ConversationListAdapter(hideSearchBar);
        mConversationList.setAdapter(adapter);
        mConversationList.loadConversation(0);
    }

    public TitleBarLayout getTitleBar() {
        return mTitleBarLayout;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    @Override
    public ConversationListLayout getConversationList() {
        return mConversationList;
    }

    public void addConversationInfo(int position, ConversationInfo info) {
        mConversationList.getAdapter().addItem(position, info);
    }

    public void removeConversationInfo(int position) {
        mConversationList.getAdapter().removeItem(position);
    }

    @Override
    public void setConversationTop(ConversationInfo conversation, IUIKitCallBack callBack) {
        ConversationManagerKit.getInstance().setConversationTop(conversation, callBack);
    }

    @Override
    public void deleteConversation(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().deleteConversation(position, conversation);
    }

    @Override
    public void clearConversationMessage(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().clearConversationMessage(position, conversation);
    }

    public void setPad(final int top) {
        mConversationList.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter.getHeaderSearchView() != null) {
                    Log.d("logutil","searchview is not null");
                    LinearLayout linearLayout = adapter.getHeaderSearchView().findViewById(R.id.ll_search_view);
                    if (linearLayout != null) {
                        Log.d("logutil","llsearch is not null");
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)linearLayout.getLayoutParams();
                        params.topMargin = top;
                        linearLayout.setLayoutParams(params);
                    }

                } else {
                    Log.d("logutil","searchview is null");
                }
            }
        },100);
    }

    public void initHeaderView(final View headerView) {
        mConversationList.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("logutil","headerView == null ? " + (adapter.getHeaderSearchView() == null));
                if (adapter.getHeaderSearchView() != null) {
                    try {
                        FrameLayout flContent = (FrameLayout) adapter.getHeaderSearchView();
                        flContent.addView(headerView);
                    } catch (Exception e) {}
                }
            }
        },100);
    }
}

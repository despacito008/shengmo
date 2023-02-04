package com.tencent.qcloud.tim.tuikit.live.component.message;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.ArrayList;

public class ChatLayout extends LinearLayout {
    private LinearLayout mLayoutRoot;
    private ListView mListIMMessage;
    private ChatMessageListAdapter mChatMsgListAdapter;
    private ArrayList<ChatEntity> mArrayListChatEntity = new ArrayList<>();   // 消息列表集合

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public ChatLayout(Context context) {
        super(context);
        initView(context);
    }

    public ChatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mLayoutRoot = (LinearLayout) inflate(context, R.layout.live_layout_chat, this);
        mListIMMessage = mLayoutRoot.findViewById(R.id.lv_im_message);
        mListIMMessage.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mListIMMessage.requestDisallowInterceptTouchEvent(false);
                } else {
                    mListIMMessage.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        mChatMsgListAdapter = new ChatMessageListAdapter(context, mListIMMessage, mArrayListChatEntity);
        mListIMMessage.setAdapter(mChatMsgListAdapter);
        mListIMMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onChatLayoutItemClickListener != null) {
                    onChatLayoutItemClickListener.onChatUserItemClick(mArrayListChatEntity.get(position).getUid());
                }
            }
        });
    }


    public void addMessageToList(final ChatEntity entity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mArrayListChatEntity.size() > 1000) {
                    while (mArrayListChatEntity.size() > 900) {
                        mArrayListChatEntity.remove(0);
                    }
                }
                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnChatLayoutItemClickListener {
        void onChatUserItemClick(String uid);
    }

    OnChatLayoutItemClickListener onChatLayoutItemClickListener;

    public void setOnChatLayoutItemClickListener(OnChatLayoutItemClickListener onChatLayoutItemClickListener) {
        this.onChatLayoutItemClickListener = onChatLayoutItemClickListener;
    }
}

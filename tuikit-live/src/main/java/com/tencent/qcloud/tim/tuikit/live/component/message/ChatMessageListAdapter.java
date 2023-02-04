package com.tencent.qcloud.tim.tuikit.live.component.message;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.opensource.svgaplayer.utils.log.LogUtils;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils.calcNameLevel;

/**
 * Module:   TCChatMsgListAdapter
 * <p>
 * Function: 消息列表的 Adapter。
 */
public class ChatMessageListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private static final String TAG = ChatMessageListAdapter.class.getSimpleName();
    private static final int ITEM_COUNT = 7;
    private static final int MAXANIMATORCOUNT = 8;
    private static final int ANIMATORDURING = 8000;
    private static final int MAXITEMCOUNT = 50;

    private Context mContext;
    private ListView mListView;
    private int mTotalHeight;
    private List<ChatEntity> mList;
    private LinkedList<AnimatorSet> mAnimatorSetList;
    private LinkedList<AnimatorInfo> mAnimatorInfoList;
    private ArrayList<ChatEntity> mArray = new ArrayList<>();
    private boolean mScrolling = false;

    public ChatMessageListAdapter(Context context, ListView listview, List<ChatEntity> objects) {
        this.mContext = context;
        mListView = listview;
        this.mList = objects;

        mAnimatorSetList = new LinkedList<>();
        mAnimatorInfoList = new LinkedList<>();

        mListView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SpannableString spanString;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.live_item_chat_message, null);
            holder.itemView = convertView.findViewById(R.id.ll_item_chat_message);
            holder.sendContext = (TextView) convertView.findViewById(R.id.sendcontext);
            holder.sendLevel = (ImageView) convertView.findViewById(R.id.live_tv_live_anchor_level);
            holder.groupRole = (ImageView) convertView.findViewById(R.id.live_tv_live_anchor_manager);
            holder.userIdentity = (ImageView) convertView.findViewById(R.id.live_tv_live_anchor_v_level);
            holder.llUserWealth = convertView.findViewById(R.id.ll_live_message_wealth);
            holder.tvUserWealth = convertView.findViewById(R.id.tv_live_message_wealth);
            convertView.setTag(R.id.live_tag_first, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.live_tag_first);
        }

        ChatEntity item = mList.get(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.getSenderName())) {
                spanString = new SpannableString(" " +  item.getSenderName() + " " + item.getContent());
            } else {
                if (TextUtils.isEmpty(item.getContent())) item.setContent("");
                spanString = new SpannableString(" " + item.getContent());
            }

//            if ("1".equals(item.getContentType())) { //系统发送的文字
//                holder.sendContext.setTextColor(mContext.getResources().getColor(R.color.notifyTextColor));
//            } else {
//                holder.sendContext.setTextColor(mContext.getResources().getColor(R.color.white));
//            }

            if (TextUtils.isEmpty(item.getContent_text_color())) {
                holder.sendContext.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                try {
                    holder.sendContext.setTextColor(Color.parseColor(item.getContent_text_color()));
                } catch (Exception e) { }
            }


            //添加用户标识
            spanString.setSpan(new LiveMessageSpan(mContext,item),0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (!TextUtils.isEmpty(item.getName_color()) && item.getName_color().startsWith("#")) {
                spanString.setSpan(new ForegroundColorSpan(Color.parseColor(item.getName_color())),
                        1, item.getSenderName().length() + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            } else {
                // 根据名称计算颜色
                spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.live_color_send_name5)),
                        1, item.getSenderName().length() + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
//            if (item.getLevel() >= 0) {
//                holder.sendLevel.setVisibility(View.VISIBLE);
//                holder.sendLevel.setImageDrawable(calcNameLevel(mContext, item.getLevel()));
//            } else {//-1显示公告栏信息，隐藏等级view
//                holder.sendLevel.setVisibility(View.GONE);
//            }
//
//            if (!TextUtils.isEmpty(item.getWealth_val()) && !"0".equals(item.getWealth_val())) {
//                holder.llUserWealth.setVisibility(View.VISIBLE);
//                if ("1".equals(item.getWealth_val_switch())) {
//                    holder.tvUserWealth.setText("密");
//                } else {
//                    holder.tvUserWealth.setText(item.getWealth_val());
//                }
//            } else {
//                holder.llUserWealth.setVisibility(View.GONE);
//            }
//
//            //场控/主播标识
//            if (!TextUtils.isEmpty(item.getLive_group_role()) && item.getLive_group_role().equals("1")) {//是场控
//                holder.groupRole.setVisibility(View.VISIBLE);
//                holder.groupRole.setImageResource(R.drawable.live_manager_icon);
//            } else if (!TextUtils.isEmpty(item.getLive_group_role()) && item.getLive_group_role().equals("2")) {//是主播
//                holder.groupRole.setVisibility(View.VISIBLE);
//                holder.groupRole.setImageResource(R.drawable.live_anchor_icon);
//            } else {//0 普通用户
//                holder.groupRole.setVisibility(View.GONE);
//            }
//
//            if (!TextUtils.isEmpty(item.getLive_user_role())) {
//                UserIdentityUtils.getUserIdentity(mContext, Integer.valueOf(item.getLive_user_role()), holder.userIdentity);
//            } else {
//                holder.userIdentity.setVisibility(View.GONE);
//            }
            Log.d("2333",(item.getBackground_color() == null) + "ddd");
            holder.itemView.setBackgroundColor(Color.parseColor("#44000000"));
            if (!TextUtils.isEmpty(item.getBackground_color())) {
                String[] split = item.getBackground_color().split(",");
                if (split.length >= 2) {
                    String color = split[1];
                    if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                    }
                }
            }

            holder.sendContext.setText(spanString);
           // holder.sendContext.setText(item.getSenderName() + " " + item.getContent());
            // 设置控件实际宽度以便计算列表项实际高度
            holder.sendContext.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return convertView;
    }

    /**
     * 停止View属性动画
     *
     * @param itemView 当前执行动画View
     */
    private void stopViewAnimator(View itemView) {
        AnimatorSet aniSet = (AnimatorSet) itemView.getTag(R.id.live_tag_second);
        if (null != aniSet) {
            aniSet.cancel();
            mAnimatorSetList.remove(aniSet);
        }
    }

    /**
     * 播放View属性动画
     *
     * @param itemView   动画对应View
     * @param startAlpha 初始透明度
     * @param duringTime 动画时长
     */
    private void playViewAnimator(View itemView, float startAlpha, long duringTime) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", startAlpha, 0f);
        AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(duringTime);
        aniSet.play(animator);
        aniSet.start();
        mAnimatorSetList.add(aniSet);
        itemView.setTag(R.id.live_tag_second, aniSet);
    }

    /**
     * 播放渐消动画
     *
     * @param pos  位置
     * @param view 执行动画View
     */
    public void playDisappearAnimator(int pos, View view) {
        int firstVisable = mListView.getFirstVisiblePosition();
        if (firstVisable <= pos) {
            playViewAnimator(view, 1f, ANIMATORDURING);
        } else {
            Log.d(TAG, "playDisappearAnimator->unexpect pos: " + pos + "/" + firstVisable);
        }
    }

    /**
     * 继续播放渐消动画
     *
     * @param itemView 执行动画View
     * @param position 位置
     * @param item
     */
    private void continueAnimator(View itemView, int position, final ChatEntity item) {
        int animatorIdx = mList.size() - 1 - position;

        if (animatorIdx < MAXANIMATORCOUNT) {
            float startAlpha = 1f;
            long during = ANIMATORDURING;

            stopViewAnimator(itemView);

            // 播放动画
            if (position < mAnimatorInfoList.size()) {
                AnimatorInfo info = mAnimatorInfoList.get(position);
                long time = info.getCreateTime();  //  获取列表项加载的初始时间
                during = during - (System.currentTimeMillis() - time);     // 计算动画剩余时长
                startAlpha = 1f * during / ANIMATORDURING;                    // 计算动画初始透明度
                if (during < 0) {   // 剩余时长小于0直接设置透明度为0并返回
                    itemView.setAlpha(0f);
                    Log.v(TAG, "continueAnimator->already end animator:" + position + "/" + item.getContent() + "-" + during);
                    return;
                }
            }

            // 创建属性动画并播放
            Log.v(TAG, "continueAnimator->pos: " + position + "/" + mList.size() + ", alpha:" + startAlpha + ", dur:" + during);
            playViewAnimator(itemView, startAlpha, during);
        } else {
            Log.v(TAG, "continueAnimator->ignore pos: " + position + "/" + mList.size());
        }
    }

    /**
     * 播放消失动画
     */
    private void playDisappearAnimator() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View itemView = mListView.getChildAt(i);
            if (null == itemView) {
                Log.w(TAG, "playDisappearAnimator->view not found: " + i + "/" + mListView.getCount());
                break;
            }

            // 更新动画创建时间
            int position = mListView.getFirstVisiblePosition() + i;
            if (position < mAnimatorInfoList.size()) {
                mAnimatorInfoList.get(position).setCreateTime(System.currentTimeMillis());
            } else {
                Log.e(TAG, "playDisappearAnimator->error: " + position + "/" + mAnimatorInfoList.size());
            }

            playViewAnimator(itemView, 1f, ANIMATORDURING);
        }
    }

    /**
     * 播放列表项动画
     *
     * @param itemView 要播放动画的列表项
     * @param position 列表项的位置
     * @param item     列表数据
     */
    private void playViewAnimator(View itemView, int position, final ChatEntity item) {
        if (!mArray.contains(item)) {  // 首次加载的列表项动画
            mArray.add(item);
            mAnimatorInfoList.add(new AnimatorInfo(System.currentTimeMillis()));
        }

        if (mScrolling) {  // 滚动时不播放动画，设置透明度为1
            itemView.setAlpha(1f);
        } else {
            continueAnimator(itemView, position, item);
        }
    }

    /**
     * 删除超过上限(MAXITEMCOUNT)的列表项
     */
    private void clearFinishItem() {
        // 删除超过的列表项
        while (mList.size() > MAXITEMCOUNT) {
            mList.remove(0);
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            }
        }

        // 缓存列表延迟删除
        while (mArray.size() > (MAXITEMCOUNT << 1)) {
            mArray.remove(0);
        }

        while (mAnimatorInfoList.size() >= mList.size()) {
            Log.e(TAG, "clearFinishItem->error size: " + mAnimatorInfoList.size() + "/" + mList.size());
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            } else {
                break;
            }
        }
    }

    /**
     * 重新计算ITEMCOUNT条记录的高度，并动态调整ListView的高度
     */
    private void redrawListViewHeight() {

        int totalHeight = 0;
        int start = 0, lineCount = 0;

        if (mList.size() <= 0) {
            return;
        }

        int maxHeight = ((ViewGroup) mListView.getParent()).getHeight();
        if (mTotalHeight >= maxHeight) {
            return;
        }

        // 计算底部ITEMCOUNT条记录的高度
        //        mCreateAnimator = false;    // 计算高度时不播放属性动画
        for (int i = mList.size() - 1; i >= start && lineCount < ITEM_COUNT; i--, lineCount++) {
            View listItem = getView(i, null, mListView);

            listItem.measure(View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST)
                    , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            // add item height
            totalHeight += listItem.getMeasuredHeight();
            if (totalHeight > maxHeight) {
                totalHeight = maxHeight;
                break;
            }
        }
        //        mCreateAnimator = true;


        mTotalHeight = totalHeight;
        // 调整ListView高度
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (lineCount - 1));
        mListView.setLayoutParams(params);
    }

    /**
     * 停止当前所有属性动画并重置透明度
     */
    public void stopAnimator() {
        // 停止动画
        for (AnimatorSet anSet : mAnimatorSetList) {
            anSet.cancel();
        }
        mAnimatorSetList.clear();
    }

    /**
     * 重置透明度
     */
    private void resetAlpha() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
            view.setAlpha(1f);
        }
    }

    /**
     * 继续可视范围内所有动画
     */
    private void continueAllAnimator() {
        //        int startPos = mListView.getFirstVisiblePosition();
        //
        //        for (int i = 0; i < mListView.getChildCount(); i++) {
        //            View view = mListView.getChildAt(i);
        //            if (null != view && startPos + i < mList.size()) {
        //                continueAnimator(view, startPos + i, mList.get(startPos + i));
        //            }
        //        }
    }

    /**
     * 重载notifyDataSetChanged方法实现渐消动画并动态调整ListView高度
     */
    @Override
    public void notifyDataSetChanged() {
        Log.v(TAG, "notifyDataSetChanged->scroll: " + mScrolling);
                if (mScrolling) {
                    // 滑动过程中不刷新
                    super.notifyDataSetChanged();
                    return;
                }

                // 删除多余项
                clearFinishItem();

//                if (mBLiveAnimator) {
//                    // 停止之前动画
//                    stopAnimator();
//
//                    // 清除动画
//                    mAnimatorSetList.clear();
//                }

        super.notifyDataSetChanged();

        // 重置ListView高度
        redrawListViewHeight();

        //        if (mBLiveAnimator && mList.size() >= MAXITEMCOUNT) {
        //            continueAllAnimator();
        //        }

        // 自动滚动到底部
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getCount() - 1);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                //                if (mBLiveAnimator) {
                //                    // 开始滚动时停止所有属性动画
                //                    stopAnimator();
                //                    resetAlpha();
                //                }
                mScrolling = true;
                break;
            case SCROLL_STATE_IDLE:
                mScrolling = false;
                //                if (mBLiveAnimator) {
                //                    // 停止滚动时播放渐消动画
                //                    playDisappearAnimator();
                //                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    static class ViewHolder {
        public TextView sendContext;
        public ImageView userIdentity;
        public ImageView groupRole;
        public LinearLayout itemView;
        private ImageView sendLevel;
        private LinearLayout llUserWealth;
        private TextView tvUserWealth;

    }

    class AnimatorInfo {
        protected long mCreateTime;

        public AnimatorInfo(long uTime) {
            mCreateTime = uTime;
        }

        public long getCreateTime() {
            return mCreateTime;
        }

        public void setCreateTime(long createTime) {
            this.mCreateTime = createTime;
        }
    }
}

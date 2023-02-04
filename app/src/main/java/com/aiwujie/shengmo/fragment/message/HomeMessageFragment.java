package com.aiwujie.shengmo.fragment.message;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupSquareActivity;
import com.aiwujie.shengmo.eventbus.MessageEvent;
import com.aiwujie.shengmo.kt.ui.fragment.CommentMessageFragment;
import com.aiwujie.shengmo.kt.ui.fragment.FeeLinkUserFragment;
import com.aiwujie.shengmo.tim.ConversationFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeMessageFragment extends Fragment {

    @BindView(R.id.guide_line_notice)
    Guideline guideLineNotice;
    @BindView(R.id.guide_line_message)
    Guideline guideLineMessage;
    @BindView(R.id.tv_fragment_message_notice)
    TextView tvFragmentMessageNotice;
    @BindView(R.id.tv_fragment_message_message)
    TextView tvFragmentMessageMessage;
    @BindView(R.id.bottom_bar_message_notice)
    View bottomBarMessageNotice;
    @BindView(R.id.bottom_bar_message_message)
    View bottomBarMessageMessage;
    @BindView(R.id.tv_fragment_message_notice_unread)
    TextView tvFragmentMessageNoticeUnread;
    @BindView(R.id.tv_fragment_message_message_unread)
    TextView tvFragmentMessageMessageUnread;
    @BindView(R.id.view_pager_message)
    ViewPager viewPagerMessage;
    Unbinder unbinder;
    @BindView(R.id.iv_fragment_message_more)
    ImageView ivFragmentMessageMore;
    @BindView(R.id.tv_fragment_message_group_square)
    TextView tvFragmentMessageGroupSquare;
    @BindView(R.id.guide_line_link)
    Guideline guideLineLink;
    @BindView(R.id.tv_fragment_message_link)
    TextView tvFragmentMessageLink;
    @BindView(R.id.bottom_bar_message_link)
    View bottomBarMessageLink;
    private ConversationFragment conversationFragment;
    private DynamicMessageFragment dynamicMessageFragment;
    private FeeLinkUserFragment feeLinkUserFragment;
    private CommentMessageFragment commentMessageFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_message, null);
        unbinder = ButterKnife.bind(this, view);
        //StatusBarUtil.showLightStatusBar(this,R.id.cl_fragment_message_title_bar);
        initView();
        initListener();
        EventBus.getDefault().register(this);
        return view;
    }

    void initView() {
        final List<Fragment> fragmentList = new ArrayList<>();
        conversationFragment = new ConversationFragment();
        //dynamicMessageFragment = new DynamicMessageFragment();
        feeLinkUserFragment = new FeeLinkUserFragment();
        commentMessageFragment = new CommentMessageFragment();
        //fragmentList.add(feeLinkUserFragment);
        fragmentList.add(feeLinkUserFragment);
        fragmentList.add(commentMessageFragment);
        fragmentList.add(conversationFragment);
        viewPagerMessage.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewPagerMessage.setOffscreenPageLimit(fragmentList.size());
        gotoNoticePage();
    }

    void initListener() {
        tvFragmentMessageMessageUnread.setVisibility(View.GONE);
        tvFragmentMessageNoticeUnread.setVisibility(View.GONE);
        viewPagerMessage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //LogUtil.d("onPageSelected : " + position);
                switchTitleView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // LogUtil.d("onPageScrollStateChanged : " + state);
            }
        });
        tvFragmentMessageNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPagerMessage.getCurrentItem() != 1) {
                    switchTitleView(1);
                    viewPagerMessage.setCurrentItem(1);
                }
            }
        });
        tvFragmentMessageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPagerMessage.getCurrentItem() != 2) {
                    switchTitleView(2);
                    viewPagerMessage.setCurrentItem(2);
                }
            }
        });
        tvFragmentMessageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPagerMessage.getCurrentItem() != 0) {
                    switchTitleView(0);
                    viewPagerMessage.setCurrentItem(0);
                }
            }
        });
        tvFragmentMessageGroupSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupSquareActivity.class);
                startActivity(intent);
            }
        });
        ivFragmentMessageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPagerMessage.getCurrentItem()) {
                    case 0:
                        //dynamicMessageFragment.showMenu();
                        feeLinkUserFragment.showMenu();
                        break;
                    case 1:
                        commentMessageFragment.showMenu();
                        break;
                    case 2:
                        conversationFragment.showMenu();
                        break;
                }
            }
        });

    }

    public void switchTitleView(int position) {
        if (bottomBarMessageMessage == null) {
            return;
        }
        if (position == 0) {
           selectTab(bottomBarMessageLink,tvFragmentMessageLink);
           unSelectTab(bottomBarMessageNotice,tvFragmentMessageNotice);
           unSelectTab(bottomBarMessageMessage,tvFragmentMessageMessage);
           ivFragmentMessageMore.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            unSelectTab(bottomBarMessageLink,tvFragmentMessageLink);
            selectTab(bottomBarMessageNotice,tvFragmentMessageNotice);
            unSelectTab(bottomBarMessageMessage,tvFragmentMessageMessage);
            ivFragmentMessageMore.setVisibility(View.VISIBLE);
        } else {
            unSelectTab(bottomBarMessageLink,tvFragmentMessageLink);
            unSelectTab(bottomBarMessageNotice,tvFragmentMessageNotice);
            selectTab(bottomBarMessageMessage,tvFragmentMessageMessage);
            ivFragmentMessageMore.setVisibility(View.VISIBLE);
        }
    }

    void selectTab(View view,TextView textView) {
        view.setVisibility(View.VISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setTextColor(getActivity().getResources().getColor(R.color.titleBlack));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    void unSelectTab(View view,TextView textView) {
        view.setVisibility(View.GONE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        textView.setTextColor(getActivity().getResources().getColor(R.color.lightGray));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    public void gotoNoticePage() {
        viewPagerMessage.setCurrentItem(2);
        switchTitleView(2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMessageCount(MessageEvent event) {
        if (tvFragmentMessageMessageUnread == null) {
            return;
        }
        if (event.getType() == 0) {
            int count = event.getMsgCount();
            if (count <= 0) {
                tvFragmentMessageMessageUnread.setVisibility(View.GONE);
            } else {
                tvFragmentMessageMessageUnread.setVisibility(View.VISIBLE);
                tvFragmentMessageMessageUnread.setText(String.valueOf(count));
            }
        }
    }

    public void refreshNoticeMessage(int count) {
        if (tvFragmentMessageNoticeUnread == null) {
            return;
        }
        if (count == 0) {
            tvFragmentMessageNoticeUnread.setVisibility(View.GONE);
        } else {
            tvFragmentMessageNoticeUnread.setVisibility(View.VISIBLE);
            tvFragmentMessageNoticeUnread.setText(String.valueOf(count));
        }
        EventBus.getDefault().post(new MessageEvent(count, 1));
    }


}

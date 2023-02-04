package com.aiwujie.shengmo.fragment.groupfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.fragment.NormalGroupFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClaimGroupFragment extends Fragment {

    @BindView(R.id.tab_fragment_group_my)
    TabLayout tabFragmentGroupMy;
    @BindView(R.id.view_page_fragment_group_my)
    ViewPager viewPageFragmentGroupMy;
    Unbinder unbinder;
    @BindView(R.id.tv_group_rule)
    TextView tvGroupRule;

    private List<String> mTitles;

    public static ClaimGroupFragment newInstance() {
        Bundle args = new Bundle();
        ClaimGroupFragment fragment = new ClaimGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_group_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvGroupRule.setVisibility(View.VISIBLE);
        initViewPage();
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void initViewPage() {
        mTitles = Arrays.asList("待认领", "已认领");
        //Fragment createGroupFragment = MyGroupListFragment.newInstance("3");
//        Fragment joinGroupFragment = MyGroupListFragment.newInstance("2");
//        Fragment claimReadyGroupFragment = new FragmentGroupClaim();
//        Fragment claimDoneGroupFragment = new FragmentGroupClaimDone();

        Fragment claimReadyGroupFragment = NormalGroupFragment.Companion.newInstance(6);
        Fragment claimDoneGroupFragment = NormalGroupFragment.Companion.newInstance(7);

        final List<Fragment> fragmentList = Arrays.asList(claimReadyGroupFragment, claimDoneGroupFragment);
        viewPageFragmentGroupMy.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        tabFragmentGroupMy.setupWithViewPager(viewPageFragmentGroupMy);
        for (int i = 0; i < tabFragmentGroupMy.getTabCount(); i++) {
            tabFragmentGroupMy.getTabAt(i).setCustomView(getTabView(i));
        }
        tabFragmentGroupMy.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabLayoutStyle(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabLayoutStyle(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setTabLayoutStyle(tabFragmentGroupMy.getTabAt(0), true);
        setTabLayoutStyle(tabFragmentGroupMy.getTabAt(1), false);
    }

    View getTabView(int index) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_custom_tab_layout2, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitles.get(index));
        TextView mMain_messageCount = view.findViewById(R.id.mMain_messageCount);
        mMain_messageCount.setVisibility(View.GONE);
        return view;
    }

    public void setTabLayoutStyle(TabLayout.Tab tab, boolean isChecked) {
        View view = tab.getCustomView();
        TextView txt_title = view.findViewById(R.id.item_custom_tab_tv);
        LinearLayout ll = view.findViewById(R.id.ll);
        if (isChecked) {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            txt_title.setTextColor(0xffffffff);
            ll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_user_info_sex_cdts));
            viewPageFragmentGroupMy.setCurrentItem(tab.getPosition());
        } else {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            txt_title.setTextColor(0xff999999);
            GradientDrawable drawable2 = new GradientDrawable();
            drawable2.setCornerRadius(50);
            drawable2.setColor(Color.parseColor("#F5F5F5"));
            ll.setBackground(drawable2);
        }

    }

    void setListener() {
        tvGroupRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerWebActivity.class);
                intent.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/20");
                intent.putExtra("title", "圣魔");
                startActivity(intent);
            }
        });
    }
}

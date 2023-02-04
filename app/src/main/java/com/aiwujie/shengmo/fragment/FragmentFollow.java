package com.aiwujie.shengmo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.fragment.QuietlyAttentionFragment;
import com.aiwujie.shengmo.kt.ui.fragment.RelationShipFansFragment;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by 290243232 on 2017/1/7.
 */
public class FragmentFollow extends Fragment {

    @BindView(R.id.mGroupSearchKeyWord_et_sou)
    EditText mGroupSearchKeyWord_et_sou;
    @BindView(R.id.mRechargeBill_tabs)
    TabLayout mRechargeBillTabs;
    @BindView(R.id.mRechargeBill_viewpager)
    ViewPager mRechargeBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    Handler handler = new Handler();
    private String qiaoqiaorenshu="0";
    private InputMethodManager imm;
    private boolean isSelf = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_follow, null);
        ButterKnife.bind(this, view);
        isSelf = MyApp.uid.equals(getActivity().getIntent().getStringExtra(IntentKey.UID));
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setData();
        if (isSelf) {
            getQuietlyAttentionCount();
        } else {
            mGroupSearchKeyWord_et_sou.setVisibility(View.GONE);
            mRechargeBillTabs.setVisibility(View.GONE);
        }
        return view;
    }

    private void setData() {
        mRechargeBillTabs.setTabMode(TabLayout.MODE_FIXED);
        mTitleList.add("普通关注");
        mViewList.add(RelationShipFansFragment.Companion.newInstance(0));
        mRechargeBillTabs.addTab(mRechargeBillTabs.newTab().setText(mTitleList.get(0)));
        if (isSelf) {
            mTitleList.add("(悄悄关注"+qiaoqiaorenshu+"/"+"100)");
            mViewList.add(new QuietlyAttentionFragment());
            mRechargeBillTabs.addTab(mRechargeBillTabs.newTab().setText(mTitleList.get(1)));
        }
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getActivity().getSupportFragmentManager(),mTitleList,mViewList);
        mRechargeBillViewpager.setAdapter(mAdapter);
        mRechargeBillTabs.setupWithViewPager(mRechargeBillViewpager);
    }

    public void getQuietlyAttentionCount() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.getFollowCountQueit, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("retcode") == 2000) {
                        qiaoqiaorenshu = object.getString("data");
                        handler.post(() -> {
                            mTitleList.set(1, "悄悄关注" + qiaoqiaorenshu + "/" + "100");
                            mRechargeBillTabs.getTabAt(1).setText(mTitleList.get(1));
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


}

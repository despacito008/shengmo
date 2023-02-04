package com.aiwujie.shengmo.fragment.groupfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.adapter.MyGroupRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyGroupListFragment extends Fragment {

    @BindView(R.id.rv_fragment_my_group)
    RecyclerView rvFragmentMyGroup;
    @BindView(R.id.srl_fragment_my_group)
    SmartRefreshLayout srlFragmentMyGroup;
    Unbinder unbinder;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private String type;
    private int page = 0;
    private List<GroupData.DataBean> groupList;
    private MyGroupRecyclerAdapter myGroupRecyclerAdapter;

    public static MyGroupListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        MyGroupListFragment fragment = new MyGroupListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_group_list_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        groupList = new ArrayList<>();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void getData() {
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");
        }
        if (!TextUtils.isEmpty(type)) {
            getGroupList();
        }
    }

    void initListener() {
        srlFragmentMyGroup.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getGroupList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                getGroupList();
            }
        });

    }

    void getGroupList() {
        HttpHelper.getInstance().getMyGroupList(type, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (rvFragmentMyGroup == null || getActivity().isFinishing()) {
                    return;
                }
                GroupData groupData = GsonUtil.GsonToBean(data, GroupData.class);
                if (groupData == null || groupData.getData() == null) {
                    return;
                }
                if (layoutNormalEmpty.getVisibility() == View.VISIBLE) {
                    layoutNormalEmpty.setVisibility(View.GONE);
                }
                List<GroupData.DataBean> tempList = groupData.getData();
                if (page == 0) {
                    srlFragmentMyGroup.finishRefresh();
                    groupList.clear();
                    groupList.addAll(tempList);
                    myGroupRecyclerAdapter = new MyGroupRecyclerAdapter(getActivity(), groupList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    rvFragmentMyGroup.setAdapter(myGroupRecyclerAdapter);
                    rvFragmentMyGroup.setLayoutManager(linearLayoutManager);
                    myGroupRecyclerAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                        @Override
                        public void onItemListener(int position) {
                            if (SafeCheckUtil.isActivityFinish(getActivity())) {
                                return;
                            }
//                            Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
//                            intent.putExtra("groupId",groupList.get(position).getGid());
//                            startActivity(intent);
                            GroupDetailActivity.Companion.start(MyApp.getInstance(),
                                    groupList.get(position).getGid(), 0,false);
                        }
                    });
                } else {
                    srlFragmentMyGroup.finishLoadMore();
                    int temp = groupList.size();
                    groupList.addAll(tempList);
                    myGroupRecyclerAdapter.notifyItemRangeInserted(temp, tempList.size());
                }

            }

            @Override
            public void onFail(String msg) {
                if (rvFragmentMyGroup == null || getActivity().isFinishing()) {
                    return;
                }
                srlFragmentMyGroup.finishRefresh();
                srlFragmentMyGroup.finishLoadMore();
                if (page == 0) {
                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                }
                //ToastUtil.show(getActivity(),msg);
            }
        });
    }
}

package com.aiwujie.shengmo.fragment.at;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.AtGroupRecyclerAdapter;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ShareGroupFragment extends Fragment {
    RecyclerView rvGroup;
    int page = 0;
    List<GroupData.DataBean> groupList = new ArrayList<>();
    private AtGroupRecyclerAdapter atGroupRecyclerAdapter;

    public static ShareGroupFragment newInstance() {
        Bundle args = new Bundle();
        ShareGroupFragment fragment = new ShareGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_share_group,container,false);
        rvGroup = view.findViewById(R.id.rv_fragment_share_group);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUserGroup();
    }

    void getUserGroup() {
        isLoading = true;
        HttpHelper.getInstance().getUserGroupList(page, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                isLoading = false;
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                GroupData groupData = new Gson().fromJson(data, GroupData.class);
                List<GroupData.DataBean> tempList = groupData.getData();
                if (page == 0) {
                    groupList.clear();
                    groupList.addAll(tempList);
                    atGroupRecyclerAdapter = new AtGroupRecyclerAdapter(getActivity(),groupList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    rvGroup.setLayoutManager(linearLayoutManager);
                    rvGroup.setAdapter(atGroupRecyclerAdapter);
                    initListener();
                } else {
                    int temp = groupList.size();
                    groupList.addAll(tempList);
                    atGroupRecyclerAdapter.notifyItemRangeInserted(temp,tempList.size());
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                hasMore = false;
                isLoading = false;
            }
        });
    }
    boolean hasMore = true;
    boolean isLoading = false;
    List<Atbean.DataBean> dataBeanList = new ArrayList<>();
    Atbean atbean = new Atbean();
    void initListener() {
        atGroupRecyclerAdapter.setOnItemChooseChangeListener(new AtGroupRecyclerAdapter.OnItemChooseChangeListener() {
            @Override
            public void onItemChange(int index, List<Integer> indexList) {
                dataBeanList.clear();
                for (Integer position : indexList) {
                    Atbean.DataBean dataBean = new Atbean.DataBean();
                    dataBean.setUid(groupList.get(position).getGid());
                    dataBean.setNickname("[群]" + groupList.get(position).getGroupname());
                    dataBeanList.add(dataBean);
                }
                atbean.setDataBean(dataBeanList);
                Message obtain = Message.obtain();
                obtain.arg2 = 20;
                obtain.obj = atbean;
                EventBus.getDefault().post(obtain);
            }
        });

        rvGroup.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                    if (hasMore && !isLoading) {
                        LogUtil.d("加载下一页");
                        page++;
                        getUserGroup();
                    }
                }
            }

        });
    }
}

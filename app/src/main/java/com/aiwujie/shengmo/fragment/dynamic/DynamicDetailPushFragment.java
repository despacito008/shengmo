package com.aiwujie.shengmo.fragment.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.DynamicPushTopAdapter;
import com.aiwujie.shengmo.adapter.TuidingCommentAdapter;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aliyun.common.utils.L;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicDetailPushFragment extends Fragment {
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    @BindView(R.id.nested_scroll_fragment_dynamic_detail_push_empty)
    NestedScrollView nestedScrollFragmentDynamicDetailPushEmpty;
    @BindView(R.id.rv_fragment_dynamic_detail_push)
    RecyclerView rvFragmentDynamicDetailPush;
    Unbinder unbinder;
    int page = 0;
    String did = "";
    private boolean hasMore = true;
    private boolean isLoading = false;
    List<DynamicDetailBean.DataBean> pushList;
    private DynamicPushTopAdapter tuidingCommentAdapter;

    public static DynamicDetailPushFragment newInstance() {
        Bundle args = new Bundle();
        DynamicDetailPushFragment fragment = new DynamicDetailPushFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_dynamic_detail_push, container, false);
        did = getActivity().getIntent().getStringExtra("did");
        getPushList();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    void getPushList() {
        isLoading = true;
        String did = getActivity().getIntent().getStringExtra("did");
        HttpHelper.getInstance().getDynamicPush(did, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (rvFragmentDynamicDetailPush == null) {
                    return;
                }
                isLoading = false;
                DynamicDetailBean dynamicDetailBean = new Gson().fromJson(data, DynamicDetailBean.class);
                if (dynamicDetailBean != null && dynamicDetailBean.getData() != null) {
                    List<DynamicDetailBean.DataBean> tempData = dynamicDetailBean.getData();
                    if (tempData.size() > 0) {
                        if (nestedScrollFragmentDynamicDetailPushEmpty.getVisibility() == View.VISIBLE) {
                            nestedScrollFragmentDynamicDetailPushEmpty.setVisibility(View.VISIBLE);
                        }
                        if (page == 0) {
                            pushList = new ArrayList<>();
                            pushList.addAll(tempData);
                            tuidingCommentAdapter = new DynamicPushTopAdapter(getActivity(), pushList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            rvFragmentDynamicDetailPush.setAdapter(tuidingCommentAdapter);
                            rvFragmentDynamicDetailPush.setLayoutManager(linearLayoutManager);
                            tuidingCommentAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                                @Override
                                public void onItemListener(int position) {
                                    UserInfoActivity.start(getActivity(),pushList.get(position).getUid());
                                }
                            });
                        } else {
                            int temp = pushList.size();
                            pushList.addAll(tempData);
                            tuidingCommentAdapter.notifyItemRangeInserted(temp,tempData.size());
                        }
                        rvFragmentDynamicDetailPush.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                                    if (hasMore && !isLoading) {
                                        LogUtil.d("加载下一页");
                                        page++;
                                        getPushList();
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (nestedScrollFragmentDynamicDetailPushEmpty == null) {
                    return;
                }
                isLoading = false;
                if(page == 0) {
                    nestedScrollFragmentDynamicDetailPushEmpty.setVisibility(View.VISIBLE);
                } else {
                    hasMore = false;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void refreshData() {
        page = 0;
        getPushList();
    }
}

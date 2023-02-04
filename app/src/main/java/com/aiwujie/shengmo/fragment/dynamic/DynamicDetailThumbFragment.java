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
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.DynamicThumbUpAdapter;
import com.aiwujie.shengmo.adapter.ThumbUpCommentAdapter;
import com.aiwujie.shengmo.bean.LaudListData;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aliyun.common.utils.L;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicDetailThumbFragment extends Fragment {
    int page = 0;
    String did = "";
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    @BindView(R.id.nested_scroll_fragment_dynamic_detail_thumb_empty)
    NestedScrollView nestedScrollFragmentDynamicDetailThumbEmpty;
    @BindView(R.id.rv_fragment_dynamic_detail_thumb)
    RecyclerView rvFragmentDynamicDetailThumb;
    Unbinder unbinder;
    private boolean hasMore = true;
    private boolean isLoading = false;
    private List<LaudListData.DataBean> thumbList;
    private DynamicThumbUpAdapter thumbUpCommentAdapter;

    public static DynamicDetailThumbFragment newInstance() {
        Bundle args = new Bundle();
        DynamicDetailThumbFragment fragment = new DynamicDetailThumbFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_dynamic_detail_thumb, container, false);
        did = getActivity().getIntent().getStringExtra("did");
        unbinder = ButterKnife.bind(this, view);
        did = getActivity().getIntent().getStringExtra("did");
        getThumbList();
        return view;
    }

    void getThumbList() {
        isLoading = true;
        HttpHelper.getInstance().getDynamicThumb(did, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                if (nestedScrollFragmentDynamicDetailThumbEmpty != null) {
                    nestedScrollFragmentDynamicDetailThumbEmpty.setVisibility(View.GONE);
                }
                isLoading = false;
                LaudListData laudListData = GsonUtil.getInstance().fromJson(data, LaudListData.class);
                if(laudListData != null && laudListData.getData() != null) {
                    List<LaudListData.DataBean> tempData = laudListData.getData();
                    if (tempData.size() > 0) {
                        if (nestedScrollFragmentDynamicDetailThumbEmpty.getVisibility() == View.VISIBLE) {
                            nestedScrollFragmentDynamicDetailThumbEmpty.setVisibility(View.VISIBLE);
                        }
                        if (page == 0) {
                            thumbList = new ArrayList<>();
                            thumbList.addAll(tempData);
                            thumbUpCommentAdapter = new DynamicThumbUpAdapter(getActivity(), thumbList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            rvFragmentDynamicDetailThumb.setLayoutManager(linearLayoutManager);
                            rvFragmentDynamicDetailThumb.setAdapter(thumbUpCommentAdapter);
                            thumbUpCommentAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                                @Override
                                public void onItemListener(int position) {
                                    UserInfoActivity.start(getActivity(),thumbList.get(position).getUid());
                                }
                            });
                        } else {
                            int temp = thumbList.size();
                            thumbList.addAll(tempData);
                            thumbUpCommentAdapter.notifyItemRangeInserted(temp, tempData.size());
                        }
                        rvFragmentDynamicDetailThumb.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                                    if (hasMore && !isLoading) {
                                        LogUtil.d("加载下一页");
                                        page++;
                                        getThumbList();
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                isLoading = false;
                if(page == 0) {
                    if (nestedScrollFragmentDynamicDetailThumbEmpty != null) {
                        nestedScrollFragmentDynamicDetailThumbEmpty.setVisibility(View.VISIBLE);
                    }
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
        getThumbList();
    }
}

package com.aiwujie.shengmo.fragment.dynamic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
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
import com.aiwujie.shengmo.activity.OtherReasonActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.adapter.CommentLevelOneAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.OperateCommentPopup;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicDetailCommentFragment extends Fragment {
    @BindView(R.id.rv_fragment_dynamic_detail_comment)
    RecyclerView rvFragmentDynamicDetailComment;
    Unbinder unbinder;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    @BindView(R.id.nested_scroll_fragment_dynamic_detail_comment_empty)
    NestedScrollView nestedScrollFragmentDynamicDetailCommentEmpty;
    @BindView(R.id.tv_fragment_dynamic_detail_comment_sort)
    TextView tvFragmentDynamicDetailCommentSort;
    @BindView(R.id.nested_scroll_fragment_dynamic_detail_comment_sort)
    NestedScrollView nestedScrollFragmentDynamicDetailCommentSort;
    List<CommentData.DataBean> commentList;
    int page = 0;
    int sortType = 1;
    String did = "";
    private boolean hasMore = true;
    private boolean isLoading = false;
    private CommentLevelOneAdapter commentLevelOneAdapter;
    public static DynamicDetailCommentFragment newInstance() {
        Bundle args = new Bundle();
        DynamicDetailCommentFragment fragment = new DynamicDetailCommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_dynamic_detail_comment, container, false);
        did = getActivity().getIntent().getStringExtra("did");
        unbinder = ButterKnife.bind(this, view);
        sortType = (int) SharedPreferencesUtils.getParam(getActivity(),SharedPreferencesUtils.COMMENT_SORT,1);
        if(sortType == 1) {
            tvFragmentDynamicDetailCommentSort.setText("时间排序");
        } else {
            tvFragmentDynamicDetailCommentSort.setText("热度排序");
        }
        setListener();
        getCommentList();
        return view;
    }

    private void getCommentList() {
        isLoading = true;
        HttpHelper.getInstance().getDynamicComment(did, page, sortType, new HttpListener() {

            @Override
            public void onSuccess(String data) {
                if (rvFragmentDynamicDetailComment == null) {
                    return;
                }
                isLoading = false;
                CommentData commentData = GsonUtil.GsonToBean(data, CommentData.class);
                if (commentData != null && commentData.getData() != null) {
                    List<CommentData.DataBean> tempData = commentData.getData();
                    if(tempData.size() != 0) {
                        if (layoutNormalEmpty.getVisibility() == View.VISIBLE) {
                            layoutNormalEmpty.setVisibility(View.GONE);
                        }
                        if(page == 0) {
                            nestedScrollFragmentDynamicDetailCommentSort.setVisibility(View.VISIBLE);
                            commentList = new ArrayList<>();
                            commentList.addAll(tempData);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            commentLevelOneAdapter = new CommentLevelOneAdapter(getActivity(), commentList, did, 1);
                            rvFragmentDynamicDetailComment.setAdapter(commentLevelOneAdapter);
                            rvFragmentDynamicDetailComment.setLayoutManager(linearLayoutManager);
                            commentLevelOneAdapter.setOnCommentClickListener(new CommentLevelOneAdapter.OnCommentClickListener() {
                                @Override
                                public void onItemClick(View view) {
                                    int index = rvFragmentDynamicDetailComment.getChildAdapterPosition(view);
                                    showCommentPop(index);
                                }

                                @Override
                                public void onItemThumbUp(View view) {
                                    final int index = rvFragmentDynamicDetailComment.getChildAdapterPosition(view);
                                    if ("0".equals(commentList.get(index).getIs_like())) {
                                        HttpHelper.getInstance().thumbUpComment(commentList.get(index).getCmid(), new HttpListener() {
                                            @Override
                                            public void onSuccess(String data) {
                                                ToastUtil.show(getActivity(), "点赞成功");
                                                commentList.get(index).setIs_like("1");
                                                commentList.get(index).setLikenum(String.valueOf(Integer.parseInt(commentList.get(index).getLikenum()) + 1));
                                                commentLevelOneAdapter.notifyItemChanged(index);
                                            }

                                            @Override
                                            public void onFail(String msg) {
                                                ToastUtil.show(getActivity(), msg);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onItemHeadViewClick(View view) {
                                    int index = rvFragmentDynamicDetailComment.getChildAdapterPosition(view);
                                    Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
                                    intent.putExtra("uid", commentList.get(index).getUid());
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(View view) {
                                    int index = rvFragmentDynamicDetailComment.getChildAdapterPosition(view);
                                    showCommentOperatePop(index);
                                }
                            });
                        } else {
                            int temp = commentList.size();
                            commentList.addAll(tempData);
                            commentLevelOneAdapter.notifyItemRangeInserted(temp,tempData.size());
                        }
                        rvFragmentDynamicDetailComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                                    if (hasMore && !isLoading) {
                                        LogUtil.d("加载下一页");
                                        page++;
                                        getCommentList();
                                    }
                                }
                            }
                        });
                    } else {
                        if(page == 0) {
                            nestedScrollFragmentDynamicDetailCommentEmpty.setVisibility(View.VISIBLE);
                        } else {
                            hasMore = false;
                        }
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                isLoading = false;
                if(page == 0) {
                    if (nestedScrollFragmentDynamicDetailCommentEmpty != null) {
                        nestedScrollFragmentDynamicDetailCommentEmpty.setVisibility(View.VISIBLE);
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
        getCommentList();
    }

    void setListener() {
        tvFragmentDynamicDetailCommentSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortType == 1) {
                    tvFragmentDynamicDetailCommentSort.setText("热度排序");
                    sortType = 2;
                } else {
                    tvFragmentDynamicDetailCommentSort.setText("时间排序");
                    sortType = 1;
                }
                page = 0;
                SharedPreferencesUtils.setParam(getActivity(),SharedPreferencesUtils.COMMENT_SORT,sortType);
                getCommentList();
            }
        });
    }

    void showCommentPop(int index) {
        ((NewDynamicDetailActivity)getActivity()).setOtherInfo(commentList.get(index).getNickname(),commentList.get(index).getUid(),commentList.get(index).getCmid());
        //((NewDynamicDetailActivity)getActivity()).showPopupcomment();
        ((NewDynamicDetailActivity)getActivity()).showCommentDialogFragment();
    }

    void showCommentOperatePop(final int index) {
        final String cmid = commentList.get(index).getCmid();
        String otheruid = commentList.get(index).getUid();
        final String content = commentList.get(index).getContent();
        final OperateCommentPopup operateCommentPopup;
        String uid = getActivity().getIntent().getStringExtra("uid");
        String admin = (String) SharedPreferencesUtils.getParam(getContext(), "admin", "0");
        if (MyApp.uid.equals(uid) || "1".equals(admin) || MyApp.uid.equals(otheruid)) {
            operateCommentPopup = new OperateCommentPopup(getActivity(), 0);
        } else {
            operateCommentPopup = new OperateCommentPopup(getActivity());
        }
        operateCommentPopup.setOnCommentOperateListener(new OperateCommentPopup.OnCommentOperateListener() {
            @Override
            public void onCommentCopy() {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", content);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.show(getActivity(), "已复制到剪贴板");
            }

            @Override
            public void onCommentReport() {
                Intent intent = new Intent(getActivity(), OtherReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                intent.putExtra("cmid", cmid);
                startActivity(intent);
                operateCommentPopup.dismiss();
            }

            @Override
            public void onCommentDelete() {
                operateCommentPopup.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认删除吗?")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HttpHelper.getInstance().deleteComment(did, cmid, new HttpListener() {
                            @Override
                            public void onSuccess(String data) {
                                ToastUtil.show(getActivity(), "删除成功");
                                commentList.remove(index);
                                commentLevelOneAdapter.notifyItemRemoved(index);
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtil.show(getActivity(), msg);
                            }
                        });
                    }
                }).create().show();

            }

        });
        operateCommentPopup.showPopupWindow();

    }
}

package com.aiwujie.shengmo.fragment.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.OtherReasonActivity;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.UserCommentRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllCommentData;
import com.aiwujie.shengmo.bean.UserInfoBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserCommentFragment extends Fragment {

    @BindView(R.id.rv_user_info_comment)
    RecyclerView rvUserInfoComment;
    Unbinder unbinder;
    int page = 0;
    UserCommentRecyclerAdapter userCommentRecyclerAdapter;
    List<AllCommentData.DataBean> commentList = new ArrayList<>();
    boolean hasMore = true;
    boolean isLoading = false;
    @BindView(R.id.tv_user_info_comment_permission)
    TextView tvUserInfoCommentPermission;
    @BindView(R.id.tv_user_info_comment_num)
    TextView tvUserInfoCommentNum;
    @BindView(R.id.tv_user_info_comment_title)
    TextView tvUserInfoCommentTitle;
    @BindView(R.id.tv_user_info_comment_info)
    TextView tvUserInfoCommentInfo;
    @BindView(R.id.ll_user_info_comment)
    View llUserInfoComment;
    @BindView(R.id.tv_user_info_add_friend)
    TextView tvUserInfoAddFriend;
    @BindView(R.id.tv_user_info_buy_vip)
    TextView tvUserInfoBuyVip;
    @BindView(R.id.cl_user_info_comment_btn)
    ConstraintLayout clUserInfoCommentBtn;
    @BindView(R.id.iv_user_info_comment_empty)
    ImageView ivUserInfoCommentEmpty;
    @BindView(R.id.iv_user_comment_top)
    ImageView ivUserCommentTop;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getCommentList() {
        isLoading = true;
        String uid = getActivity().getIntent().getStringExtra("uid");
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCommentedListOnUserInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                LogUtil.d(response);
                if (rvUserInfoComment == null) {
                    return;
                }
                isLoading = false;
                AllCommentData listData = new Gson().fromJson(response, AllCommentData.class);
                List<AllCommentData.DataBean> data = listData.getData();
                int retcode = listData.getRetcode();
                if (retcode == 4001) {
                    hasMore = false;
                }
                if (userCommentRecyclerAdapter == null) {
                    commentList.clear();
                    commentList.addAll(data);
                    userCommentRecyclerAdapter = new UserCommentRecyclerAdapter(getActivity(), commentList,UserCommentFragment.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvUserInfoComment.setAdapter(userCommentRecyclerAdapter);
                    rvUserInfoComment.setLayoutManager(layoutManager);
                    setListener();
                } else {
                    int temp = commentList.size();
                    commentList.addAll(data);
                    userCommentRecyclerAdapter.notifyItemRangeInserted(temp, data.size());
                }
                rvUserInfoComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if (Math.abs(dy) > 20) {
                            if (dy > 0) {
                                showOrHideTopArrow(false);
                                ((UserInfoActivity) getActivity()).showOrHideTopBottomBar(false);
                            } else {
                                ((UserInfoActivity) getActivity()).showOrHideTopBottomBar(true);
                                if (rvUserInfoComment.computeVerticalScrollOffset() > 500) {
                                    showOrHideTopArrow(true);
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                isLoading = false;
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

    public void showData(final UserInfoBean.DataBean userInfo) {
        if (clUserInfoCommentBtn == null) {
            return;
        }
        String vip = (String) SharedPreferencesUtils.getParam(getActivity(), "vip", "0");
        String svip = (String) SharedPreferencesUtils.getParam(getActivity(), "svip", "0");
        if ("0".equals(userInfo.getComment_num())) {
            llUserInfoComment.setVisibility(View.VISIBLE);
            tvUserInfoCommentTitle.setText("暂无评论");
            tvUserInfoCommentInfo.setText("");
            tvUserInfoCommentNum.setText(userInfo.getComment_num());
            clUserInfoCommentBtn.setVisibility(View.GONE);
            return;
        }
        clUserInfoCommentBtn.setVisibility(View.VISIBLE);
        if ("0".equals(userInfo.getComment_rule())) { //公开
            llUserInfoComment.setVisibility(View.GONE);
            tvUserInfoCommentTitle.setText("(所有人可见)");
            tvUserInfoCommentNum.setText(userInfo.getComment_num());
            getCommentList();
        } else { //仅好友 会员可见
            tvUserInfoCommentTitle.setText("(好友/会员可见)");
            tvUserInfoCommentNum.setText(userInfo.getComment_num());
            if (MyApp.uid.equals(userInfo.getUid()) || "1".equals(vip) || "1".equals(svip) || 3 == userInfo.getFollow_state()) {
                llUserInfoComment.setVisibility(View.GONE);
                getCommentList();
            } else {
                llUserInfoComment.setVisibility(View.VISIBLE);
                tvUserInfoCommentTitle.setText("评论不可见");
                //ivUserInfoCommentEmpty.setVisibility(View.INVISIBLE);
                SpannableStringBuilder builder = new SpannableStringBuilder("该用户已将参与的评论设为好友/会员可见");
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
                builder.setSpan(purSpan, 12, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvUserInfoCommentInfo.setText(builder);
                int followState = userInfo.getFollow_state();
                if (followState == 0) {
                    tvUserInfoAddFriend.setText("加好友");
                } else if (followState == 1) {
                    tvUserInfoAddFriend.setText("已关注");
                } else if (followState == 2) {
                    tvUserInfoAddFriend.setText("关注");
                } else if (followState == 4) {
                    tvUserInfoAddFriend.setText("被关注");
                }
            }
            tvUserInfoAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int followState = userInfo.getFollow_state();
                    if (followState == 0) {
                        ((UserInfoActivity) getActivity()).follow();
                    } else if (followState == 1) {
                        ((UserInfoActivity) getActivity()).overfollow();
                    } else if (followState == 2) {
                        ((UserInfoActivity) getActivity()).follow();
                    } else if (followState == 4) {
                        ((UserInfoActivity) getActivity()).follow();
                    }
                }
            });

            tvUserInfoBuyVip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String headpic = (String) SharedPreferencesUtils.getParam(getActivity(), "headurl", "");
                    Intent intent = new Intent(getActivity(), VipCenterActivity.class);
                    intent.putExtra("uid", MyApp.uid);
                    intent.putExtra("headpic", headpic);
                    startActivity(intent);
                }
            });
        }
    }

    void setListener() {
        userCommentRecyclerAdapter.setOnCommentListener(new UserCommentRecyclerAdapter.OnItemCommentListener() {
            @Override
            public void onItemClick(int position) {
                if (SafeCheckUtil.isIndexOurOfBounds(commentList, position)) {
                    return;
                }
                Intent intent = new Intent(getActivity(), NewDynamicDetailActivity.class);
                intent.putExtra("uid", commentList.get(position).getUid());
                intent.putExtra("did", commentList.get(position).getDid());
                intent.putExtra("pos", -1);
                intent.putExtra("showwhat", -1);
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                showCommentOperatePop(position);
            }

            @Override
            public void onItemApply(int position) {

            }
        });


        ivUserCommentTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvUserInfoComment.scrollToPosition(0);
            }
        });
    }

    void showCommentOperatePop(final int index) {
        if (commentList.size() <= index) {
            return;
        }
        final String cmid = commentList.get(index).getCmid();
        String otheruid = commentList.get(index).getUid();
        final String content = commentList.get(index).getCcontent();
        final OperateCommentPopup operateCommentPopup;
        String uid = getActivity().getIntent().getStringExtra("uid");
        String admin = (String) SharedPreferencesUtils.getParam(getContext(), "admin", "0");
        if (MyApp.uid.equals(uid)) {
            operateCommentPopup = new OperateCommentPopup(getActivity(), 1);
        } else if ("1".equals(admin)) {
            operateCommentPopup = new OperateCommentPopup(getActivity(), 0);
        } else {
            operateCommentPopup = new OperateCommentPopup(getActivity(), 2);
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
                intent.putExtra("did", commentList.get(index).getDid());
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
                        HttpHelper.getInstance().deleteComment(commentList.get(index).getDid(), cmid, new HttpListener() {
                            @Override
                            public void onSuccess(String data) {
                                ToastUtil.show(getActivity(), "删除成功");
                                commentList.remove(index);
                                userCommentRecyclerAdapter.notifyItemRemoved(index);
                                ((UserInfoActivity) getActivity()).notifyCommentDelete();
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

    void showOrHideTopArrow(boolean isShow) {
        if (isShow) {
            if (ivUserCommentTop.getVisibility() == View.GONE) {
                ivUserCommentTop.setVisibility(View.VISIBLE);
                ivUserCommentTop.setAnimation(AnimationUtil.moveToViewLocation());
            }
        } else {
            if (ivUserCommentTop.getVisibility() == View.VISIBLE) {
                ivUserCommentTop.setVisibility(View.GONE);
                ivUserCommentTop.setAnimation(AnimationUtil.moveToViewBottom());
            }
        }
    }

}

package com.aiwujie.shengmo.activity.newui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.OtherReasonActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.CommentLevelOneAdapter;
import com.aiwujie.shengmo.adapter.PunishmentImgAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.bean.PunishmentBean;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.DateUtils;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;


import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;


public class PunishmentDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_punishment_detail_back)
    ImageView ivPunishmentDetailBack;
    @BindView(R.id.item_listview_warning_icon)
    ImageView itemListviewWarningIcon;
    @BindView(R.id.item_listview_warning_vip)
    ImageView itemListviewWarningVip;
    @BindView(R.id.item_listview_warning_name)
    TextView itemListviewWarningName;
    @BindView(R.id.item_listview_warning_shiming)
    ImageView itemListviewWarningShiming;
    @BindView(R.id.tv_punishment_type)
    TextView tvPunishmentType;
    @BindView(R.id.tv_punishment_time)
    TextView tvPunishmentTime;
    @BindView(R.id.item_listview_warning_ban_dynamic)
    LinearLayout itemListviewWarningBanDynamic;
    @BindView(R.id.iv_layout_user_normal_info_sex)
    ImageView ivLayoutUserNormalInfoSex;
    @BindView(R.id.tv_layout_user_normal_info_age)
    TextView tvLayoutUserNormalInfoAge;
    @BindView(R.id.ll_layout_user_normal_info_sex_age)
    LinearLayout llLayoutUserNormalInfoSexAge;
    @BindView(R.id.tv_layout_user_normal_info_role)
    TextView tvLayoutUserNormalInfoRole;
    @BindView(R.id.iv_layout_user_normal_info_wealth)
    ImageView ivLayoutUserNormalInfoWealth;
    @BindView(R.id.tv_layout_user_normal_info_wealth)
    TextView tvLayoutUserNormalInfoWealth;
    @BindView(R.id.ll_layout_user_normal_info_wealth)
    LinearLayout llLayoutUserNormalInfoWealth;
    @BindView(R.id.iv_layout_user_normal_info_charm)
    ImageView ivLayoutUserNormalInfoCharm;
    @BindView(R.id.tv_layout_user_normal_info_charm)
    TextView tvLayoutUserNormalInfoCharm;
    @BindView(R.id.ll_layout_user_normal_info_charm)
    LinearLayout llLayoutUserNormalInfoCharm;
    @BindView(R.id.tv_punishment_date)
    TextView tvPunishmentDate;
    @BindView(R.id.tv_punishment_edit)
    TextView tvPunishmentEdit;
    @BindView(R.id.tv_punishment_reason)
    TextView tvPunishmentReason;
    @BindView(R.id.rv_punishment_img)
    RecyclerView rvPunishmentImg;
    @BindView(R.id.ll_item_punishment)
    LinearLayout llItemPunishment;
    @BindView(R.id.tv_punishment_detail_sort_hot)
    TextView tvPunishmentDetailSortHot;
    @BindView(R.id.tv_punishment_detail_sort_time)
    TextView tvPunishmentDetailSortTime;
    @BindView(R.id.rv_punishment_detail_comment)
    RecyclerView rvPunishmentDetailComment;
    @BindView(R.id.srl_punishment_detail_comment)
    SmartRefreshLayout srlPunishmentDetailComment;
    @BindView(R.id.tv_punishment_detail_comment_num)
    TextView tvPunishmentDetailCommentNum;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    @BindView(R.id.et_punishment_detail_comment)
    EditText etPunishmentDetailComment;
    @BindView(R.id.tv_punishment_detail_send_comment)
    TextView tvPunishmentDetailSendComment;
    @BindView(R.id.layout_punishment_publicity)
    FrameLayout layoutPunishmentPublicity;

    private InputMethodManager mInputManager;

    private int sortType = 2;
    private int page = 0;
    private PunishmentBean.DataBean data;
    private String otherUid = "0";
    private String otherName = "";
    private String commentContent = "";
    private String pid = "";
    private List<CommentData.DataBean> commentList;
    private int currentIndex = -1;
    private CommentLevelOneAdapter levelOneAdapter;
    private int commentNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_punishment_detail);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        initPunishInfo();
        setListener();
        mInputManager = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    void initPunishInfo() {
        String punishData = getIntent().getStringExtra("punish_data");
        data = GsonUtil.GsonToBean(punishData, PunishmentBean.DataBean.class);
        if (data != null) {
            if (!"0".equals(data.getComnum()) && !TextUtil.isEmpty(data.getComnum())) {
                getCommentList();
                commentNum = Integer.valueOf(data.getComnum());
            }
            tvPunishmentDetailCommentNum.setText("评论 " + data.getComnum());
//          判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
            UserIdentityUtils.showIdentity(this, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), "-1", data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(), data.getBkvip(), data.getBlvip(), itemListviewWarningVip);
            if ("0".equals(data.getRealname())) {
                itemListviewWarningShiming.setVisibility(View.GONE);
            } else {
                itemListviewWarningShiming.setVisibility(View.VISIBLE);
            }

            if (data.getSex().equals("1")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nan_bg);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nan);
            } else if (data.getSex().equals("2")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_nv_bg);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.nv);
            } else if (data.getSex().equals("3")) {
                llLayoutUserNormalInfoSexAge.setBackgroundResource(R.drawable.item_sex_san_bg);
                ivLayoutUserNormalInfoSex.setImageResource(R.mipmap.san);
            }
            if (data.getRole().equals("S")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                tvLayoutUserNormalInfoRole.setText("斯");
            } else if (data.getRole().equals("M")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                tvLayoutUserNormalInfoRole.setText("慕");
            } else if (data.getRole().equals("SM")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                tvLayoutUserNormalInfoRole.setText("双");
            } else if (data.getRole().equals("~")) {
                tvLayoutUserNormalInfoRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                tvLayoutUserNormalInfoRole.setText("~");
            }
            tvLayoutUserNormalInfoAge.setText(data.getAge());
            if (!data.getHead_pic().equals(NetPic()) || !data.getHead_pic().equals("")) {//"http://59.110.28.150:888/"
                GlideImgManager.glideLoader(this, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewWarningIcon, 0);
            } else {
                itemListviewWarningIcon.setImageResource(R.mipmap.morentouxiang);
            }
            itemListviewWarningName.setText(data.getNickname());
            if (!data.getCharm_val_new().equals("0")) {
                llLayoutUserNormalInfoCharm.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoCharm.setText(data.getCharm_val_new());
            } else {
                llLayoutUserNormalInfoCharm.setVisibility(View.GONE);
            }

            if (!data.getWealth_val_new().equals("0")) {
                llLayoutUserNormalInfoWealth.setVisibility(View.VISIBLE);
                tvLayoutUserNormalInfoWealth.setText(data.getWealth_val_new());
            } else {
                llLayoutUserNormalInfoWealth.setVisibility(View.GONE);
            }

            switch (data.getType()) {
                case "1":
                    tvPunishmentType.setText("封禁账号");
                    break;
                case "2":
                    tvPunishmentType.setText("封禁动态");
                    break;
                case "3":
                    tvPunishmentType.setText("封禁聊天");
                    break;
                case "4":
                    tvPunishmentType.setText("封禁资料");
                    break;
                default:
                    tvPunishmentType.setText("封禁设备");
                    break;
            }

            switch (data.getBlockingalong()) {
                case "1":
                    tvPunishmentTime.setText("一天");
                    break;
                case "3":
                    tvPunishmentTime.setText("三天");
                    break;
                case "7":
                    tvPunishmentTime.setText("一周");
                    break;
                case "14":
                    tvPunishmentTime.setText("两周");
                    break;
                case "30":
                case "31":
                    tvPunishmentTime.setText("1月");
                    break;
                default:
                    tvPunishmentTime.setText("永久");
                    break;

            }
            try {
                tvPunishmentDate.setText(DateUtils.getParseTime(Long.parseLong(data.getAddtime())));
            } catch (Exception e) {

            }
            tvPunishmentReason.setText(data.getBlockreason());
            final List<String> images = data.getImage();

            if (images == null || images.size() == 0) {
                rvPunishmentImg.setVisibility(View.GONE);
            } else {
                rvPunishmentImg.setVisibility(View.VISIBLE);
                PunishmentImgAdapter punishmentImgAdapter = new PunishmentImgAdapter(images, this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
                rvPunishmentImg.setLayoutManager(gridLayoutManager);
                rvPunishmentImg.setAdapter(punishmentImgAdapter);
                punishmentImgAdapter.setOnSimpleItemListener(new OnSimpleItemViewListener() {
                    @Override
                    public void onItemListener(View view) {
                        int index = rvPunishmentImg.getChildAdapterPosition(view);
                        List<ImageInfo> imageInfo = new ArrayList<>();
                        for (int i = 0; i < images.size(); i++) {
                            ImageInfo info = new ImageInfo();
                            info.setThumbnailUrl(images.get(i));
                            info.setBigImageUrl(images.get(i));
                            imageInfo.add(info);
                        }
                        Intent intent = new Intent(PunishmentDetailActivity.this, ImagePreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfo);
                        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, index);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

        }
    }

    void setListener() {
        ivPunishmentDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvPunishmentDetailSortHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortType != 2) {
                    sortType = 2;
                    tvPunishmentDetailSortHot.setTextColor(getResources().getColor(R.color.purple_main));
                    tvPunishmentDetailSortTime.setTextColor(getResources().getColor(R.color.lightGray));
                    page = 0;
                    getCommentList();
                }
            }
        });

        tvPunishmentDetailSortTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortType != 1) {
                    sortType = 1;
                    tvPunishmentDetailSortHot.setTextColor(getResources().getColor(R.color.lightGray));
                    tvPunishmentDetailSortTime.setTextColor(getResources().getColor(R.color.purple_main));
                    page = 0;
                    getCommentList();
                }
            }
        });

        etPunishmentDetailComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                commentContent = s.toString();
            }
        });

        tvPunishmentDetailSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
                if (mInputManager.isActive()) {
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        itemListviewWarningIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PunishmentDetailActivity.this, UserInfoActivity.class);
                intent.putExtra("uid",data.getUid());
                startActivity(intent);
            }
        });

        layoutPunishmentPublicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPunishmentDetailComment.setText("");
                etPunishmentDetailComment.setHint("点击输入你的评论");
                pid = "";
                otherUid = "";
                changeKeyBoard();
            }
        });

        srlPunishmentDetailComment.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getCommentList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                getCommentList();
            }
        });
    }

    void getCommentList() {
        HttpHelper.getInstance().getCommentList(data.getDid(), sortType, page, new HttpListener() {
            @Override
            public void onSuccess(String response) {
                CommentData commentData = new Gson().fromJson(response, CommentData.class);
                if (commentData == null || commentData.getData() == null) {
                    return;
                }
                layoutNormalEmpty.setVisibility(View.GONE);
                List<CommentData.DataBean> tempData = commentData.getData();
                if(page == 0) {
                    srlPunishmentDetailComment.finishRefresh();
                    commentList = new ArrayList<>();
                    commentList.addAll(tempData);
                    initCommentRecyclerView();
                } else {
                    srlPunishmentDetailComment.finishLoadMore();
                    int temp = commentList.size();
                    commentList.addAll(tempData);
                    levelOneAdapter.notifyItemRangeInserted(temp,tempData.size());
                }
            }

            @Override
            public void onFail(String msg) {
                if(page == 0) {
                    srlPunishmentDetailComment.finishRefresh();
                } else {
                    srlPunishmentDetailComment.finishLoadMore();
                }
            }
        });
    }

    void initCommentRecyclerView() {
        levelOneAdapter = new CommentLevelOneAdapter(PunishmentDetailActivity.this, commentList, data.getDid(),1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PunishmentDetailActivity.this);
        rvPunishmentDetailComment.setLayoutManager(linearLayoutManager);
        rvPunishmentDetailComment.setAdapter(levelOneAdapter);
        levelOneAdapter.setOnCommentClickListener(new CommentLevelOneAdapter.OnCommentClickListener() {
            @Override
            public void onItemClick(View view) {
                int index = rvPunishmentDetailComment.getChildAdapterPosition(view);
                etPunishmentDetailComment.setText("");
                etPunishmentDetailComment.setHint("回复 " + commentList.get(index).getNickname());
                pid = commentList.get(index).getCmid();
                otherUid = commentList.get(index).getUid();
                otherName = commentList.get(index).getNickname();
                currentIndex = index;
                changeKeyBoard();
            }

            @Override
            public void onItemThumbUp(View view) {
                final int index = rvPunishmentDetailComment.getChildAdapterPosition(view);
                if ("0".equals(commentList.get(index).getIs_like())) {
                    HttpHelper.getInstance().thumbUpComment(commentList.get(index).getCmid(), new HttpListener() {
                        @Override
                        public void onSuccess(String data) {
                            ToastUtil.show(PunishmentDetailActivity.this, "点赞成功");
                            commentList.get(index).setIs_like("1");
                            commentList.get(index).setLikenum(String.valueOf(Integer.parseInt(commentList.get(index).getLikenum()) + 1));
                            levelOneAdapter.notifyItemChanged(index);
                        }

                        @Override
                        public void onFail(String msg) {
                            ToastUtil.show(PunishmentDetailActivity.this, msg);
                        }
                    });
                }
            }

            @Override
            public void onItemHeadViewClick(View view) {
                int index = rvPunishmentDetailComment.getChildAdapterPosition(view);
                Intent intent = new Intent(PunishmentDetailActivity.this,UserInfoActivity.class);
                intent.putExtra("uid",commentList.get(index).getUid());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {
                int index = rvPunishmentDetailComment.getChildAdapterPosition(view);
                showCommentOperatePop(index);
            }
        });


    }

    void sendComment() {
        if (TextUtil.isEmpty(commentContent)) {
            ToastUtil.show(this, "评论内容不能为空");
            return;
        }
        String did = data.getDid();
        HttpHelper.getInstance().sendChildComment(did, pid, commentContent, otherUid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(PunishmentDetailActivity.this, "评论成功");
                tvPunishmentDetailCommentNum.setText("评论 " + (++commentNum));
                CharSequence hintStr = etPunishmentDetailComment.getHint();
                if (hintStr != null && hintStr.toString().startsWith("回复")) {
                    List<CommentData.DataBean.SubsetcommentBean> subsetcomment = commentList.get(currentIndex).getSubsetcomment();
                    if(subsetcomment==null) {
                        subsetcomment = new ArrayList<>();
                    }
                    CommentData.DataBean.SubsetcommentBean temp = new CommentData.DataBean.SubsetcommentBean();
                    temp.setOthernickname(otherName);
                    temp.setContent(commentContent);
                    temp.setNickname("我");
                    temp.setUid(MyApp.uid);
                    temp.setOtheruid(otherUid);
                    subsetcomment.add(temp);
                    commentList.get(currentIndex).setSubsetcomment(subsetcomment);
                    levelOneAdapter.notifyItemChanged(currentIndex);
                } else {
                    page = 0;
                    getCommentList();
                }
                etPunishmentDetailComment.setText("");
            }

            @Override
            public void onFail(String msg) {
                etPunishmentDetailComment.setText("");
            }
        });
    }

    void changeKeyBoard() {
        if(mInputManager.isActive()) {
            mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            mInputManager.showSoftInput(etPunishmentDetailComment,0);
            etPunishmentDetailComment.setFocusable(true);
            etPunishmentDetailComment.setFocusableInTouchMode(true);
            etPunishmentDetailComment.setSelection(0);
            etPunishmentDetailComment.requestFocus();
        }
    }

    void showCommentOperatePop(final int index) {
        final String cmid = commentList.get(index).getCmid();
        String otheruid = commentList.get(index).getUid();
        final String content = commentList.get(index).getContent();
        final OperateCommentPopup operateCommentPopup;
        String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
        final String did = data.getDid();
        if ("1".equals(admin) || MyApp.uid.equals(otheruid)) {
            operateCommentPopup = new OperateCommentPopup(PunishmentDetailActivity.this, 0);
        } else {
            operateCommentPopup = new OperateCommentPopup(PunishmentDetailActivity.this);
        }
        operateCommentPopup.setOnCommentOperateListener(new OperateCommentPopup.OnCommentOperateListener() {
            @Override
            public void onCommentCopy() {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", content);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.show(PunishmentDetailActivity.this, "已复制到剪贴板");
            }

            @Override
            public void onCommentReport() {
                Intent intent = new Intent(PunishmentDetailActivity.this, OtherReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                intent.putExtra("cmid", cmid);
                startActivity(intent);
                operateCommentPopup.dismiss();
            }

            @Override
            public void onCommentDelete() {
                operateCommentPopup.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(PunishmentDetailActivity.this);
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
                                ToastUtil.show(PunishmentDetailActivity.this, "删除成功");
                                commentList.remove(index);
                                levelOneAdapter.notifyItemRemoved(index);
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtil.show(PunishmentDetailActivity.this, msg);
                            }
                        });
                    }
                }).create().show();

            }

        });
        operateCommentPopup.showPopupWindow();

    }
}

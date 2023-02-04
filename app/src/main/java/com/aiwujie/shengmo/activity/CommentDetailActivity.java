package com.aiwujie.shengmo.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.CommentDetailAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.ChildCommentBean;
import com.aiwujie.shengmo.bean.CommentDetailBean;
import com.aiwujie.shengmo.eventbus.CommentDetailEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.OkhttpUpload;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentDetailActivity extends AppCompatActivity {
    @BindView(R.id.mDynamicDetail_return)
    ImageView mDynamicDetailReturn;
    @BindView(R.id.mDynamicDetail_sandian)
    ImageView mDynamicDetailSandian;
    @BindView(R.id.mDynamicDetail_title)
    PercentRelativeLayout mDynamicDetailTitle;
    @BindView(R.id.iv_item_comment_detail_head)
    ImageView ivItemCommentDetailHead;
    @BindView(R.id.iv_item_comment_detail_sign)
    ImageView ivItemCommentDetailSign;
    @BindView(R.id.iv_item_comment_detail_thumb_up)
    ImageView ivItemCommentDetailThumbUp;
    @BindView(R.id.tv_item_comment_detail_thumb_up)
    TextView tvItemCommentDetailThumbUp;
    @BindView(R.id.tv_item_comment_detail_name)
    TextView tvItemCommentDetailName;
    @BindView(R.id.tv_item_comment_detail_date)
    TextView tvItemCommentDetailDate;
    @BindView(R.id.tv_item_comment_detail_content)
    TextView tvItemCommentDetailContent;
    @BindView(R.id.rv_comment_detail)
    RecyclerView rvCommentDetail;
    @BindView(R.id.srl_comment_detail)
    SmartRefreshLayout srlCommentDetail;
    @BindView(R.id.cl_item_comment_detail)
    ConstraintLayout clItemCommentDetail;
    private List<CommentDetailBean.DataBean.TwolevelcommentBean> twolevelcomment;
    private CommentDetailBean commentDetailBean;
    private CommentDetailBean.DataBean.OnelevelcommentBean onelevelcomment;
    private CommentDetailAdapter commentDetailAdapter;
    String cmid = "";
    String did = "";
    int page = 0;

    String currentCmid = "";
    String currentContent = "";
    String currentName = "";
    String currentOuid = "";

    int position = -1;
    boolean isOperate = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        cmid = getIntent().getStringExtra("cmid");
        did = getIntent().getStringExtra("did");
        position = getIntent().getIntExtra("position",-1);
        getCommentData();
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
        srlCommentDetail.setEnableRefresh(true);
        srlCommentDetail.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getCommentData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                srlCommentDetail.resetNoMoreData();
                page = 0;
                getCommentData();
            }
        });

        mDynamicDetailReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void getCommentData() {
        Map<String, String> map = new HashMap<>();
        map.put("cmid", cmid);
        map.put("page", String.valueOf(page));
        RequestFactory.getRequestManager().post(HttpUrl.GetCommentInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                if (SafeCheckUtil.isActivityFinish(CommentDetailActivity.this)) {
                    return;
                }
                if (!GsonUtil.isVaild(response)) {
                    ToastUtil.show(CommentDetailActivity.this, "没有更多数据");
                    srlCommentDetail.finishLoadMoreWithNoMoreData();
                    return;
                }
                commentDetailBean = GsonUtil.GsonToBean(response, CommentDetailBean.class);
                if (commentDetailBean.getRetcode() == 2000) {
                    if (page == 0) {
                        onelevelcomment = commentDetailBean.getData().getOnelevelcomment();
                        twolevelcomment = commentDetailBean.getData().getTwolevelcomment();
                        showTopComment();
                        showCommentRecyclerView();
                    } else {
                        List<CommentDetailBean.DataBean.TwolevelcommentBean> tempData = commentDetailBean.getData().getTwolevelcomment();
                        int tempIndex = twolevelcomment.size();
                        twolevelcomment.addAll(tempData);
                        commentDetailAdapter.notifyItemRangeInserted(tempIndex, twolevelcomment.size());
                        srlCommentDetail.finishLoadMore();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                srlCommentDetail.finishRefresh();
                srlCommentDetail.finishLoadMore();
            }
        });
    }

    public void showTopComment() {
        tvItemCommentDetailName.setText(onelevelcomment.getNickname());
        tvItemCommentDetailDate.setText(onelevelcomment.getSendtime());
        tvItemCommentDetailThumbUp.setText(onelevelcomment.getLikenum());
        tvItemCommentDetailContent.setText(onelevelcomment.getContent());
        //  判断显示标识，官方管理>年svip>svip>年vip>vip
        ivItemCommentDetailSign.setVisibility(View.VISIBLE);
        if (onelevelcomment.getIs_admin().equals("1")) {
            Glide.with(CommentDetailActivity.this).load(R.drawable.user_manager).into(ivItemCommentDetailSign);
        } else if (onelevelcomment.getSvipannual().equals("1")) {
            Glide.with(CommentDetailActivity.this).load(R.drawable.user_svip_year).into(ivItemCommentDetailSign);
        } else if (onelevelcomment.getSvip().equals("1")) {
            Glide.with(CommentDetailActivity.this).load(R.drawable.user_svip).into(ivItemCommentDetailSign);
        } else if (onelevelcomment.getVipannual().equals("1")) {
            Glide.with(CommentDetailActivity.this).load(R.drawable.user_vip_year).into(ivItemCommentDetailSign);
        } else if (onelevelcomment.getVip().equals("1")) {
            Glide.with(CommentDetailActivity.this).load(R.drawable.user_vip).into(ivItemCommentDetailSign);
        } else {
            ivItemCommentDetailSign.setVisibility(View.INVISIBLE);
        }
        if (!SafeCheckUtil.isActivityFinish(CommentDetailActivity.this)) {
            ImageLoader.loadCircleImage(CommentDetailActivity.this, onelevelcomment.getHead_pic(), ivItemCommentDetailHead, R.mipmap.morentouxiang);
        }
        if ("1".equals(onelevelcomment.getIs_like())) {
            Glide.with(CommentDetailActivity.this).load(R.drawable.thumb_up_done).into(ivItemCommentDetailThumbUp);
        } else {
            Glide.with(CommentDetailActivity.this).load(R.drawable.thumb_up_normal).into(ivItemCommentDetailThumbUp);
        }
        if ("0".equals(onelevelcomment.getLikenum())) {
            tvItemCommentDetailThumbUp.setVisibility(View.INVISIBLE);
        } else {
            tvItemCommentDetailThumbUp.setVisibility(View.VISIBLE);
            tvItemCommentDetailThumbUp.setText(onelevelcomment.getLikenum());
        }

        ivItemCommentDetailHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentDetailActivity.this, PesonInfoActivity.class);
                intent.putExtra("uid", onelevelcomment.getUid());
                startActivity(intent);
            }
        });

        ivItemCommentDetailThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(onelevelcomment.getIs_like())) {
                    HttpHelper.getInstance().thumbUpComment(onelevelcomment.getCmid(), new HttpListener() {
                        @Override
                        public void onSuccess(String data) {
                            ToastUtil.show(CommentDetailActivity.this, "点赞成功");
                            Glide.with(CommentDetailActivity.this).load(R.drawable.thumb_up_done).into(ivItemCommentDetailThumbUp);
                            onelevelcomment.setIs_like("1");
                            tvItemCommentDetailThumbUp.setVisibility(View.VISIBLE);
                            tvItemCommentDetailThumbUp.setText(String.valueOf(Integer.parseInt(onelevelcomment.getLikenum()) + 1));
                        }

                        @Override
                        public void onFail(String msg) {
                            ToastUtil.show(CommentDetailActivity.this, msg);
                        }
                    });
                }
            }
        });

        mDynamicDetailSandian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentOperatePop();
            }
        });

        clItemCommentDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCmid = onelevelcomment.getCmid();
                currentName = onelevelcomment.getNickname();
                currentOuid = onelevelcomment.getUid();
                showPopupcomment();
            }
        });
    }

    public void showCommentRecyclerView() {
        commentDetailAdapter = new CommentDetailAdapter(CommentDetailActivity.this, twolevelcomment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentDetailActivity.this);
        rvCommentDetail.setLayoutManager(linearLayoutManager);
        rvCommentDetail.setAdapter(commentDetailAdapter);
        srlCommentDetail.finishRefresh();
        commentDetailAdapter.setOnCommentClickListener(new CommentDetailAdapter.OnCommentClickListener() {
            @Override
            public void onItemClick(View view) {
                int index = rvCommentDetail.getChildAdapterPosition(view);
//                if (twolevelcomment.get(index).getUid().equals(MyApp.uid)) {
//                    return;
//                }
                currentCmid = twolevelcomment.get(index).getCmid();
                currentName = twolevelcomment.get(index).getNickname();
                currentOuid = twolevelcomment.get(index).getUid();
                showPopupcomment();
            }

            @Override
            public void onItemThumbUp(View view) {
                final int index = rvCommentDetail.getChildAdapterPosition(view);
                if ("0".equals(twolevelcomment.get(index).getIs_like())) {
                    HttpHelper.getInstance().thumbUpComment(twolevelcomment.get(index).getCmid(), new HttpListener() {
                        @Override
                        public void onSuccess(String data) {
                            ToastUtil.show(CommentDetailActivity.this, "点赞成功");
                            twolevelcomment.get(index).setIs_like("1");
                            twolevelcomment.get(index).setLikenum(String.valueOf(Integer.parseInt(twolevelcomment.get(index).getLikenum()) + 1));
                            commentDetailAdapter.notifyItemChanged(index);
                        }

                        @Override
                        public void onFail(String msg) {
                            ToastUtil.show(CommentDetailActivity.this, msg);
                        }
                    });
                }


            }

            @Override
            public void onItemHeadViewClick(View view) {
                int index = rvCommentDetail.getChildAdapterPosition(view);
                Intent intent = new Intent(CommentDetailActivity.this, PesonInfoActivity.class);
                intent.putExtra("uid", twolevelcomment.get(index).getUid());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {
                int index = rvCommentDetail.getChildAdapterPosition(view);
                showCommentOperatePop(index);
            }
        });
    }

    void showCommentOperatePop(final int index) {
        final String cmid = twolevelcomment.get(index).getCmid();
        String otheruid = twolevelcomment.get(index).getUid();
        final String content = twolevelcomment.get(index).getContent();
        final OperateCommentPopup operateCommentPopup;
        String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
        String uid = onelevelcomment.getUid();
        if (MyApp.uid.equals(onelevelcomment.getUid()) || "1".equals(admin)) {
            operateCommentPopup = new OperateCommentPopup(CommentDetailActivity.this, 0);
        } else if(MyApp.uid.equals(otheruid)) {
            operateCommentPopup = new OperateCommentPopup(CommentDetailActivity.this, 1);
        }else {
            operateCommentPopup = new OperateCommentPopup(CommentDetailActivity.this);
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
                ToastUtil.show(CommentDetailActivity.this, "已复制到剪贴板");
            }

            @Override
            public void onCommentReport() {
                Intent intent = new Intent(CommentDetailActivity.this, OtherReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                intent.putExtra("cmid", cmid);
                startActivity(intent);
                operateCommentPopup.dismiss();
            }

            @Override
            public void onCommentDelete() {
                operateCommentPopup.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(CommentDetailActivity.this);
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
                                ToastUtil.show(CommentDetailActivity.this, "删除成功");
                                isOperate = true;
                                twolevelcomment.remove(index);
                                commentDetailAdapter.notifyItemRemoved(index);
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtil.show(CommentDetailActivity.this, msg);
                            }
                        });
                    }
                }).create().show();
            }
        });
        operateCommentPopup.showPopupWindow();
    }

    void showCommentOperatePop() {
        final String cmid = onelevelcomment.getCmid();
        String otheruid = onelevelcomment.getUid();
        final String content = onelevelcomment.getContent();
        final OperateCommentPopup operateCommentPopup;
        String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
        String uid = onelevelcomment.getUid();
        if ( MyApp.uid.equals(uid) || "1".equals(admin) || MyApp.uid.equals(otheruid)) {
            operateCommentPopup = new OperateCommentPopup(CommentDetailActivity.this, 0);
        } else {
            operateCommentPopup = new OperateCommentPopup(CommentDetailActivity.this);
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
                ToastUtil.show(CommentDetailActivity.this, "已复制到剪贴板");
            }

            @Override
            public void onCommentReport() {
                Intent intent = new Intent(CommentDetailActivity.this, OtherReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                intent.putExtra("cmid", cmid);
                startActivity(intent);
                operateCommentPopup.dismiss();
            }

            @Override
            public void onCommentDelete() {
                operateCommentPopup.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(CommentDetailActivity.this);
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
                                if(position != -1 && !isOperate) {
                                    EventBus.getDefault().post(new CommentDetailEvent(position, 2));
                                }
                                finish();
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtil.show(CommentDetailActivity.this, msg);
                            }
                        });
                    }
                }).create().show();
            }
        });
        operateCommentPopup.showPopupWindow();
    }

    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private View rl_input_container;
    private InputMethodManager mInputManager;

    @SuppressLint("WrongConstant")
    private void showPopupcomment() {
        if (popupView == null) {
            //加载评论框的资源文件
            popupView = LayoutInflater.from(CommentDetailActivity.this).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        if (!TextUtil.isEmpty(currentName)) {
            inputComment.setHint("回复" + currentName);
        }
        btn_submit = (TextView) popupView.findViewById(R.id.btn_confirm);
        inputComment.setText("");
        rl_input_container =  popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputComment.setFocusable(true);
                        inputComment.setFocusableInTouchMode(true);
                        inputComment.requestFocus();
                        mInputManager = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mInputManager.showSoftInput(inputComment, 0);
                    }
                });

            }
        }, 200);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    popupWindow.dismiss();
                return false;
            }
        });
        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {
                if (mInputManager.isActive()) {
                    popupWindow = null;
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputManager.isActive()) {
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                popupWindow.dismiss();
            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nInputContentText = inputComment.getText().toString().trim();
                if (nInputContentText == null || "".equals(nInputContentText)) {
                    return;
                }
                btn_submit.setEnabled(false);
                currentContent = nInputContentText;
                sendComment();
                inputComment.setText("");
                if (mInputManager.isActive()) {
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                popupWindow.dismiss();
            }
        });

        setKeyListener();

    }

    List<String> atunamelist = new ArrayList<>();
    List<String> atuidlist = new ArrayList<>();

    private void setKeyListener() {

        inputComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!(s.length() > start)) {
                    return;
                }
                if ('@' == s.charAt(start) && count == 1) {
//                    Intent intent = new Intent(CommentDetailActivity.this, AtFansActivity.class);
                    Intent intent = new Intent(CommentDetailActivity.this, AtMemberActivity.class);
                    intent.putExtra("type", "commentDetail");
                    startActivityForResult(intent, 100);
                    return;
                }

                if ((s.charAt(start) == '@') && (s.charAt(start + count - 1) == ' ')) {
                    if ('@' == s.charAt(start - 1)) {
                        inputComment.getText().delete(start - 1, start);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        inputComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = inputComment.getSelectionStart();

                ATSpan[] atSpans = inputComment.getText().getSpans(0, inputComment.getText().length(), ATSpan.class);
                int length = atSpans.length;

                if (0 == length) {
                    return;

                }

                for (ATSpan atSpan : atSpans) {
                    int start = inputComment.getText().getSpanStart(atSpan);
                    int end = inputComment.getText().getSpanEnd(atSpan);
                    if (selectionStart >= start && selectionStart <= end) {
                        inputComment.setSelection(end);
                        return;
                    }
                }
            }
        });

        inputComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = inputComment.getSelectionStart();

                    ATSpan[] atSpans = inputComment.getText().getSpans(0, inputComment.getText().length(), ATSpan.class);
                    int length = atSpans.length;

                    if (0 == length) {
                        return false;
                    }

                    for (ATSpan atSpan : atSpans) {
                        int start = inputComment.getText().getSpanStart(atSpan);
                        int end = inputComment.getText().getSpanEnd(atSpan);
                        if (selectionStart >= start + 1 && selectionStart <= end) {
                            inputComment.getText().delete(start + 1, end);
                            for (int i = 0; i < atuidlist.size(); i++) {
                                String value = atSpan.getValue();
                                if (atuidlist.get(i).equals(value)) {
                                    atuidlist.remove(i);
                                    atunamelist.remove(i);
                                    break;
                                }
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gethaoyouliebiao(Message message) {
        if (message.arg2 == 10001) {
            Atbean atbean = (Atbean) message.obj;
            if (inputComment.getText().toString().equals("@")) {
                inputComment.setText(" ");
            }
            List<Atbean.DataBean> dataBean = atbean.getDataBean();
            for (int i = 0; i < dataBean.size(); i++) {
                String uid = dataBean.get(i).getUid();
                atuidlist.add(uid);
                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");

//                ATSpan atSpan = new ATSpan(uid);

                SpannableString span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
                if (dataBean.get(i).getNickname().contains("[群]")){
                    ATSpan atSpan = new ATSpan(uid,"群");
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }else if (dataBean.get(i).getNickname().contains("[高端]")){
                    ATSpan atSpan = new ATSpan(uid,"高端");
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                else {
                    ATSpan atSpan = new ATSpan(uid);
                    span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
//                span.setSpan(atSpan, 0, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                inputComment.append(span);
            }
            inputComment.setSelection(inputComment.getText().length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isOperate && position != -1) {
            EventBus.getDefault().post(new CommentDetailEvent(position,1));
        }
        EventBus.getDefault().unregister(this);
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
    }

    void sendComment() {
        HttpHelper.getInstance().sendChildComment(did, cmid, currentContent, currentOuid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                btn_submit.setEnabled(true);
                ChildCommentBean childCommentBean = GsonUtil.GsonToBean(data, ChildCommentBean.class);
                String temp = GsonUtil.getInstance().toJson(childCommentBean.getData().getCommentinfo());
                Log.d("xmf", temp);
                if (!TextUtil.isEmpty(temp)) {
                    CommentDetailBean.DataBean.TwolevelcommentBean tempBean = GsonUtil.GsonToBean(temp, CommentDetailBean.DataBean.TwolevelcommentBean.class);
                    twolevelcomment.add(0, tempBean);
                    commentDetailAdapter.notifyItemRangeChanged(0, twolevelcomment.size());
                    isOperate = true;
                }
            }

            @Override
            public void onFail(String msg) {
                btn_submit.setEnabled(true);
                ToastUtil.show(CommentDetailActivity.this, msg);
            }
        });
    }




}

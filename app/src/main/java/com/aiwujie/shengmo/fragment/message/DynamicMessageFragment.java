package com.aiwujie.shengmo.fragment.message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AllRewardActivity;
import com.aiwujie.shengmo.activity.AllTuidingActivity;
import com.aiwujie.shengmo.activity.AllZanActivity;
import com.aiwujie.shengmo.activity.AtFansActivity;
import com.aiwujie.shengmo.activity.AtMinedActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.OtherReasonActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.AllCommentAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllCommentData;
import com.aiwujie.shengmo.bean.TimDynamicMessageBean;
import com.aiwujie.shengmo.bean.UnreadMessageData;
import com.aiwujie.shengmo.eventbus.ClearDynamicNewCount;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.normallist.AtUsActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.PushTopUsActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.ThumbUpUsActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.android.percent.support.PercentLinearLayout;

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
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.aliyun.svideo.common.utils.ThreadUtils.runOnUiThread;

public class DynamicMessageFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mDynamicMessage_return)
    ImageView mDynamicMessageReturn;
    Handler handler = new Handler();
    @BindView(R.id.mDynamic_message_listview)
    PullToRefreshListView mDynamicMessageListview;
    TextView mDynamicMessageZancount;
    PercentLinearLayout mDynamicMessageLlZan;
    TextView mDynamicMessageDscount;
    PercentLinearLayout mDynamicMessageLlDs;
    TextView mDynamicMessageAtcount;
    PercentLinearLayout mDynamicMessageLlAt;
    @BindView(R.id.tv_message_dynamic_top)
    TextView tvMessageDynamicTop;
    private List<AllCommentData.DataBean> comments = new ArrayList<>();
    private AllCommentAdapter commentAdapter;
    int page = 0;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private AllCommentData allCommentData;
    private UnreadMessageData unreadMessageData;
    private int atcount;
    private PercentLinearLayout mDynamic_message_ll_at_tuiding;
    private TextView mDynamic_tuiding_atcount;
    private TextView mDynamic_message_tvHeadview2;
    Unbinder unbinder;
    View viewZan, viewReword, viewRecommend, viewAt;
    int zanCount, rewordCount, pushCount, atCount, commentCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dynamic_message, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        //X_SystemBarUI.initSystemBar(getActivity(), R.color.title_color);
        setData();
        setListener();
        return view;
    }

    private void setData() {
        View view = View.inflate(getActivity(), R.layout.item_dynamic_messageheadview, null);
        mDynamicMessageZancount = (TextView) view.findViewById(R.id.mDynamic_message_zancount);
        mDynamic_tuiding_atcount = view.findViewById(R.id.mDynamic_tuiding_atcount);
        //mDynamicMessageLlZan= (PercentLinearLayout) view.findViewById(R.id.mDynamic_message_ll_zan);
        mDynamicMessageDscount = (TextView) view.findViewById(R.id.mDynamic_message_dscount);
        //mDynamicMessageLlDs= (PercentLinearLayout) view.findViewById(R.id.mDynamic_message_ll_ds);
        mDynamicMessageAtcount = (TextView) view.findViewById(R.id.mDynamic_message_atcount);
        //mDynamicMessageLlAt= (PercentLinearLayout) view.findViewById(R.id.mDynamic_message_ll_at_mined);
        //mDynamic_message_ll_at_tuiding = view.findViewById(R.id.mDynamic_message_ll_at_tuiding);
        mDynamic_message_tvHeadview2 = view.findViewById(R.id.mDynamic_message_tvHeadview2);
        mDynamicMessageListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mDynamicMessageListview.getRefreshableView().addHeaderView(view);
        mDynamicMessageListview.setOnRefreshListener(this);
        mDynamicMessageListview.setOnItemClickListener(this);

        commentAdapter = new AllCommentAdapter(getActivity(), comments, 1);
        commentAdapter.setOnCommentListener(new AllCommentAdapter.OnItemCommentListener() {
            @Override
            public void onItemClick(int position) {
                if (comments.size() <= position) {
                    return;
                }
                Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                intent.putExtra("uid", comments.get(position).getDuid());
                intent.putExtra("did", comments.get(position).getDid());
                intent.putExtra("pos", position);
                intent.putExtra("showwhat", 1);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                //Toast.makeText(getActivity(), "po" + position, Toast.LENGTH_SHORT).show();
                showCommentOperatePop(position);

            }

            @Override
            public void onItemApply(int position) {
                AllCommentData.DataBean dataBean = comments.get(position);
                cmid = dataBean.getCmid();
                did = dataBean.getDid();
                currentOuid = dataBean.getUid();
                currentName = dataBean.getNickname();
                showPopupcomment();
            }
        });
        mDynamicMessageListview.setAdapter(commentAdapter);

        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDynamicMessageZancount != null && mDynamicMessageListview != null) {
                    mDynamicMessageListview.setRefreshing();
                }
            }
        }, 100);
        mDynamicMessageListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getAllComment();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        viewZan = view.findViewById(R.id.view_message_thumb_bg);
        viewReword = view.findViewById(R.id.view_message_reword_bg);
        viewRecommend = view.findViewById(R.id.view_message_recommend_bg);
        viewAt = view.findViewById(R.id.view_message_alite_bg);

    }

    private void setListener() {
        viewRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), AllTuidingActivity.class);
                Intent intent = new Intent(getActivity(), PushTopUsActivity.class);
                startActivity(intent);
                pushCount = 0;
                changeUnreadNoticeNum(rewordCount + pushCount + atCount + commentCount);
            }
        });
        viewZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), AllZanActivity.class);
                Intent intent = new Intent(getActivity(), ThumbUpUsActivity.class);
                startActivity(intent);
                zanCount = 0;
                //changeUnreadNoticeNum(zanCount + rewordCount + pushCount + atCount + commentCount);
            }
        });
        viewReword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllRewardActivity.class);
                startActivity(intent);
                rewordCount = 0;
                changeUnreadNoticeNum(rewordCount + pushCount + atCount + commentCount);
            }
        });
        viewAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), AtMinedActivity.class);
                Intent intent = new Intent(getActivity(), AtUsActivity.class);
                startActivity(intent);
                atCount = 0;
                changeUnreadNoticeNum(rewordCount + pushCount + atCount + commentCount);
            }
        });
    }

    private void getAllComment() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCommentedList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                try {
                    allCommentData = new Gson().fromJson(response, AllCommentData.class);
                    if (allCommentData.getData().size() == 0) {
                        if (page != 0) {
                            isReresh = false;
                            page = page - 1;
                            ToastUtil.show(getActivity(), "没有更多");
                        } else {
                            comments.addAll(allCommentData.getData());
                            commentAdapter.notifyDataSetChanged();
                        }

                    } else {
                        isReresh = true;
                        if (page == 0) {
                            comments.addAll(allCommentData.getData());
                            try {
                                commentAdapter.notifyDataSetChanged();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            comments.addAll(allCommentData.getData());
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                mDynamicMessageListview.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getAtNum() {
        String atjson = (String) SharedPreferencesUtils.getParam(getActivity(), "atjson", "");

        if (!TextUtil.isEmpty(atjson)) {
            atcount = atjson.split("@-@").length;
        } else {
            atcount = 0;
        }
        if (atcount == 0) {
            mDynamicMessageAtcount.setVisibility(View.GONE);
        } else {
            mDynamicMessageAtcount.setVisibility(View.VISIBLE);
            mDynamicMessageAtcount.setText(String.valueOf(atcount));
        }
    }

    private void getUnreadNum() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetUnreadNum, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                LogUtil.d(response);
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                try {
                    unreadMessageData = new Gson().fromJson(response, UnreadMessageData.class);
                    mDynamicMessageZancount.setVisibility(View.VISIBLE);
                    if ("0".equals(unreadMessageData.getData().getLaudnum())) {
                        mDynamicMessageZancount.setVisibility(View.GONE);
                    } else {
                        mDynamicMessageZancount.setVisibility(View.VISIBLE);
                        mDynamicMessageZancount.setText(String.valueOf(unreadMessageData.getData().getLaudnum()));
                    }
                    if ("0".equals(unreadMessageData.getData().getTopnum())) {
                        mDynamic_tuiding_atcount.setVisibility(View.GONE);
                    } else {
                        mDynamic_tuiding_atcount.setVisibility(View.VISIBLE);
                        mDynamic_tuiding_atcount.setText(String.valueOf(unreadMessageData.getData().getTopnum()));
                    }

                    if ("0".equals(unreadMessageData.getData().getRewardnum())) {
                        mDynamicMessageDscount.setVisibility(View.GONE);
                    } else {
                        mDynamicMessageDscount.setVisibility(View.VISIBLE);
                        mDynamicMessageDscount.setText(String.valueOf(unreadMessageData.getData().getRewardnum()));
                    }

                    if ("0".equals(unreadMessageData.getData().getAtnum())) {
                        mDynamicMessageAtcount.setVisibility(View.GONE);
                    } else {
                        mDynamicMessageAtcount.setVisibility(View.VISIBLE);
                        mDynamicMessageAtcount.setText(String.valueOf(unreadMessageData.getData().getAtnum()));
                    }
                    zanCount = Integer.parseInt(unreadMessageData.getData().getLaudnum());
                    rewordCount = Integer.parseInt(unreadMessageData.getData().getRewardnum());
                    pushCount = Integer.parseInt(unreadMessageData.getData().getTopnum());
                    atCount = Integer.parseInt(unreadMessageData.getData().getAtnum());
                    commentCount = Integer.parseInt(unreadMessageData.getData().getComnum());
                    commentAdapter.setNewMessageNum(commentCount);
                    unreadMessageData.getData().getComnum();
                    int allNum = rewordCount + pushCount + atCount + commentCount;
                    changeUnreadNoticeNum(allNum);
//                    if (unreadMessageData.getData().getComnum().equals("0")) {
//                        mDynamic_message_tvHeadview2.setTextColor(0xff999999);
//                    }
                    mDynamic_message_tvHeadview2.setVisibility(View.VISIBLE);
                    mDynamic_message_tvHeadview2.setText("刚刚有" + unreadMessageData.getData().getComnum() + "条评论");

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void clearUnreadNum() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ClearUnreadNumAll, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                //Log.i("clearpinglun", "onSuccess: " + response);
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                mDynamicMessageAtcount.setVisibility(View.GONE);
                mDynamicMessageDscount.setVisibility(View.GONE);
                mDynamic_tuiding_atcount.setVisibility(View.GONE);
                mDynamicMessageZancount.setVisibility(View.GONE);
                changeUnreadNoticeNum(0);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    @OnClick({R.id.mDynamicMessage_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mDynamicMessage_return:
                //finish();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnreadNum();
        // getAtNum();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UpLocationUtils.LogintimeAndLocation();
        //clearUnreadNum();
        SharedPreferencesUtils.setParam(getActivity(), "dongtaixiaoxirednum", 0);
        SharedPreferencesUtils.setParam(getActivity(), "atjson", "");
        //EventBus.getDefault().post(new ClearDynamicNewCount());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        comments.clear();
        if (commentAdapter != null) {
            commentAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUnreadNum();
                getAllComment();
            }
        }, 100);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllComment();
            }
        }, 300);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 2) {
            return;
        }
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        intent.putExtra("uid", comments.get(position - 2).getDuid());
        intent.putExtra("did", comments.get(position - 2).getDid());
        intent.putExtra("pos", position - 2);
        intent.putExtra("showwhat", 1);
        startActivity(intent);
    }


    //快捷回复相关
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private View rl_input_container;
    private InputMethodManager mInputManager;
    private String did, cmid, currentContent, currentOuid, currentName;

    @SuppressLint("WrongConstant")
    private void showPopupcomment() {
        if (popupView == null) {
            //加载评论框的资源文件
            popupView = LayoutInflater.from(getActivity()).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        if (!TextUtil.isEmpty(currentName)) {
            inputComment.setHint("回复" + currentName);
        }
        btn_submit = (TextView) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = popupView.findViewById(R.id.rl_input_container);
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
                        mInputManager = (InputMethodManager) getActivity().getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
//                    Intent intent = new Intent(getActivity(), AtFansActivity.class);
                    Intent intent = new Intent(getActivity(), AtMemberActivity.class);
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


    void sendComment() {
        HttpHelper.getInstance().sendChildComment(did, cmid, currentContent, currentOuid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                btn_submit.setEnabled(true);
//                ChildCommentBean childCommentBean = GsonUtil.GsonToBean(data, ChildCommentBean.class);
//                String temp = GsonUtil.getInstance().toJson(childCommentBean.getData().getCommentinfo());
//                Log.d("xmf", temp);
//                if (!TextUtil.isEmpty(temp)) {
//                    CommentDetailBean.DataBean.TwolevelcommentBean tempBean = GsonUtil.GsonToBean(temp, CommentDetailBean.DataBean.TwolevelcommentBean.class);
//                }
                ToastUtil.show(getActivity(), "回复成功");
                inputComment.setText("");
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                btn_submit.setEnabled(true);
                ToastUtil.show(getActivity(), msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCount(TimDynamicMessageBean timDynamicMessageBean) {
        if (TimDynamicMessageBean.DYNAMIC.equals(timDynamicMessageBean.getCostomMassageType())) {
            mDynamicMessageZancount.setVisibility(View.VISIBLE);
            mDynamicMessageZancount.setText(String.valueOf(timDynamicMessageBean.getNum()));
        } else if (TimDynamicMessageBean.REWORD.equals(timDynamicMessageBean.getCostomMassageType())) {
            mDynamicMessageDscount.setVisibility(View.VISIBLE);
            mDynamicMessageDscount.setText(String.valueOf(timDynamicMessageBean.getNum()));
        } else if (TimDynamicMessageBean.PUSH.equals(timDynamicMessageBean.getCostomMassageType())) {
            mDynamic_tuiding_atcount.setVisibility(View.VISIBLE);
            mDynamic_tuiding_atcount.setText(String.valueOf(timDynamicMessageBean.getNum()));
        } else if (TimDynamicMessageBean.AT.equals(timDynamicMessageBean.getCostomMassageType())) {
            mDynamicMessageAtcount.setVisibility(View.VISIBLE);
            mDynamicMessageAtcount.setText(String.valueOf(timDynamicMessageBean.getNum()));
        } else if (TimDynamicMessageBean.COMMENT.equals(timDynamicMessageBean.getCostomMassageType())) {
            mDynamic_message_tvHeadview2.setText("刚刚有" + timDynamicMessageBean.getNum() + "条评论");
            commentAdapter.setNewMessageNum(timDynamicMessageBean.getNum());
        }
        changeUnreadNoticeNum(timDynamicMessageBean.getAllnum());
    }

    public void showMenu() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_notice_pop, null);
        final PopupWindow mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f, getActivity());
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f, getActivity());
            }
        });
        LinearLayout ll01 = (LinearLayout) contentView.findViewById(R.id.item_notice_pop_ll01);
        ll01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                clearUnreadNum();
            }
        });
        mPopWindow.showAsDropDown(tvMessageDynamicTop);
    }

    void backgroundAlpha(float bgAlpha, Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void changeUnreadNoticeNum(int num) {
        ((HomeMessageFragment) getParentFragment()).refreshNoticeMessage(num);
    }

    boolean isFirstLoad = true;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirstLoad) {
            isFirstLoad = false;
            page = 0;
            comments.clear();
            getUnreadNum();
            getAllComment();
        }
    }

    void showCommentOperatePop(final int index) {
        final String cmid = comments.get(index).getCmid();
        String otheruid = comments.get(index).getUid();
        final String content = comments.get(index).getCcontent();
        final OperateCommentPopup operateCommentPopup;
        String uid = getActivity().getIntent().getStringExtra("uid");
        String admin = (String) SharedPreferencesUtils.getParam(getContext(), "admin", "0");
        operateCommentPopup = new OperateCommentPopup(getActivity(), 0);

//        if (MyApp.uid.equals(uid)) {
//            operateCommentPopup = new OperateCommentPopup(getActivity(), 1);
//        } else if ("1".equals(admin)){
//            operateCommentPopup = new OperateCommentPopup(getActivity(), 0);
//        } else {
//            operateCommentPopup = new OperateCommentPopup(getActivity(), 2);
//        }
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
                intent.putExtra("did", comments.get(index).getDid());
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
                        HttpHelper.getInstance().deleteComment(comments.get(index).getDid(), cmid, new HttpListener() {
                            @Override
                            public void onSuccess(String data) {
                                ToastUtil.show(getActivity(), "删除成功");
                                mDynamicMessageListview.setRefreshing();
//                                comments.remove(index);
//                                commentAdapter.notifyItemRemoved(index);
//                                ((UserInfoActivity)getActivity()).notifyCommentDelete();
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

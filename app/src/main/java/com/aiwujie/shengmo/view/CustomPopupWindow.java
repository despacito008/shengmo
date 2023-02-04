package com.aiwujie.shengmo.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AtFansActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.OtherReasonActivity;

import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.adapter.CommentLevelOneAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.eventbus.CommentDetailEvent;
import com.aiwujie.shengmo.eventbus.CommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CustomPopupWindow extends PopupWindow {
    private View mPopView;
    private RecyclerView rlvComment;

    private Activity mContext;
    public int page;
    private String did,comnum;
    private CommentLevelOneAdapter commentLevelOneAdapter;
    private ArrayList<CommentData.DataBean>commentdatas  ;
    private SmartRefreshLayout refreshLayout;
    private ImageView ivClose;
    private TextView tvCount;

    private TextView tvSend;
    private  int position;
    List<String> atunamelist = new ArrayList<>();
    List<String> atuidlist = new ArrayList<>();

    private String pid = "";

    private String uid = "";

    public CustomPopupWindow(Activity context, String did,String uid, String comnum,int position) {
        super(context);
        this.mContext = context;
        this.position=position;
        this.did = did;
        this.uid = uid;
        this.comnum=comnum;
        EventBus.getDefault().register(this);
        init(context);
        setPopupWindow();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.popupwindow_comment, null);
        rlvComment = mPopView.findViewById(R.id.rlv_comment);
        refreshLayout = mPopView.findViewById(R.id.refreshLayout);
        ivClose = mPopView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvSend = mPopView.findViewById(R.id.tv_send);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pid = "";
                showPopupcomment("");

//                if (!tvSend.getText().toString().equals("")) {
//                    tvSend.setEnabled(false);
//                    sendComment();
//                } else {
//                    ToastUtil.show(MyApp.getInstance(), "说点什么吧...");
//                }
            }
        });



        tvCount = mPopView.findViewById(R.id.tv_count);
        tvCount.setText(comnum+"条评论");
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getComList( did);
            }
        });
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressWarnings("InlinedApi")
    private void setPopupWindow() {

        this.setContentView(mPopView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽

        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        this.setHeight((int) (height*0.6));
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mPopView.findViewById(R.id.id_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    //获取评论数据
    private void getComList(final String did) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        map.put("did", did);
        if (page==0){
            refreshLayout. resetNoMoreData();
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCommentListNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                        CommentData data = new Gson().fromJson(response, CommentData.class);
                        if (data.getData().size() == 0) {
                            if (page != 0) {
                                page = page - 1;
                                refreshLayout.finishLoadMoreWithNoMoreData();

                            }
                        } else {
                            if (commentLevelOneAdapter==null||page==0) {
                                commentdatas.clear();
                                commentdatas.addAll(data.getData());

                                commentLevelOneAdapter = new CommentLevelOneAdapter(mContext, commentdatas,did);
                                LinearLayoutManager linearLayoutManager =new LinearLayoutManager(mContext);
                                rlvComment.setLayoutManager(linearLayoutManager);
                                rlvComment.setAdapter(commentLevelOneAdapter);
                                commentLevelOneAdapter.setOnCommentClickListener(new CommentLevelOneAdapter.OnCommentClickListener() {
                                    @Override
                                    public void onItemClick(View view) {
                                        int index = rlvComment.getChildAdapterPosition(view);
                                        //回复评论
                                        otheruid = commentdatas.get(index).getUid();

                                            pid = commentdatas.get(index).getCmid();
                                            showPopupcomment(commentdatas.get(index).getNickname());
//                                            if (mDynamicDetailLlEt.getVisibility() == View.GONE) {
//                                                mDynamicDetailLlEt.setVisibility(View.VISIBLE);
//                                                mDynamicDetailEt.setText("");
//                                                mDynamicDetailEt.requestFocus();
//                                                imm = (InputMethodManager) mDynamicDetailEt.getContext().getSystemService(INPUT_METHOD_SERVICE);
//                                                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                                                mDynamicDetailEt.setHint("回复 " + commentdatas.get(index).getNickname() + ":");
//                                                pid = commentdatas.get(index).getCmid();
//                                            } else {
//                                                mDynamicDetailLlEt.setVisibility(View.GONE);
//                                            }

                                    }

                                    @Override
                                    public void onItemThumbUp(View view) {
                                        final int index = rlvComment.getChildAdapterPosition(view);
                                        if ("0".equals(commentdatas.get(index).getIs_like())) {
                                            HttpHelper.getInstance().thumbUpComment(commentdatas.get(index).getCmid(), new HttpListener() {
                                                @Override
                                                public void onSuccess(String data) {
                                                    ToastUtil.show(mContext, "点赞成功");
                                                    commentdatas.get(index).setIs_like("1");
                                                    commentdatas.get(index).setLikenum(String.valueOf(Integer.parseInt(commentdatas.get(index).getLikenum()) + 1));
                                                    commentLevelOneAdapter.notifyItemChanged(index);
                                                }
                                                @Override
                                                public void onFail(String msg) {
                                                    ToastUtil.show(mContext, msg);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onItemHeadViewClick(View view) {
                                        int index = rlvComment.getChildAdapterPosition(view);
                                        Intent intent = new Intent(mContext, PesonInfoActivity.class);
                                        intent.putExtra("uid", commentdatas.get(index).getUid());
                                        mContext.startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view) {
                                        int index = rlvComment.getChildAdapterPosition(view);
                                        showCommentOperatePop(index);
                                    }
                                });

                            } else {
                                commentdatas.addAll(data.getData());
                                commentLevelOneAdapter.notifyDataSetChanged();
                                refreshLayout.finishLoadMore();

                            }
                        }


            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        page=0;
        commentdatas = new ArrayList<>();
        getComList(did);
        super.showAtLocation(parent, gravity, x, y);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gethaoyouliebiao(Message message) {
        if (message.arg2 == 11) {
            Atbean atbean = (Atbean) message.obj;
            if (inputComment.getText().toString().equals("@")) {
                inputComment.setText(" ");
            }
            List<Atbean.DataBean> dataBean = atbean.getDataBean();
            for (int i = 0; i < dataBean.size(); i++) {
                String uid = dataBean.get(i).getUid();
                atuidlist.add(uid);
                atunamelist.add("@" + dataBean.get(i).getNickname() + " ");
                //atStr+=uid+",";
                //atuname+="@"+dataBean.get(i).getNickname()+" ,";
//                ATSpan atSpan = new ATSpan(uid);
                SpannableString span = new SpannableString("@" + dataBean.get(i).getNickname() + " ");
                //  whl  群组颜色
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentListChange(CommentDetailEvent event) {
        if(event.getType() == 1) {
            page = 0;
           getComList(did);
        } else if (event.getType() == 2) {
            commentdatas.remove(event.getPosition());
            commentLevelOneAdapter.notifyItemRemoved(event.getPosition());
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        page=0;
        EventBus.getDefault().unregister(this);

    }
    private String otheruid = "0";
    private String platuid;
    private String platuname;
    //发布评论
    private void sendComment() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("content", inputComment.getText().toString());
        map.put("otheruid", otheruid);
        if(!TextUtil.isEmpty(pid)) {
            map.put("pid",pid);
        }

        String substring = "";
        String substring2 = "";
        platuid = "";
        platuname = "";
        for (int i = 0; i < atuidlist.size(); i++) {
            platuid += atuidlist.get(i) + ",";
            platuname += atunamelist.get(i) + ",";
        }
        if (atuidlist.size() > 0) {
            substring = platuid.substring(0, platuid.length() - 1);
            substring2 = platuname.substring(0, platuname.length() - 1);
        }

        map.put("atuid", substring);
        map.put("atuname", substring2);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendCommentNewred, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                btn_submit.setEnabled(true);
                     tvSend.setEnabled(true);
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {

                                case 2000:
                                    tvCount.setText( (Integer.parseInt(comnum) + 1)+"条评论");
                                    InputMethodManager   imm = (InputMethodManager) inputComment.getContext().getSystemService(INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputComment.getWindowToken(), 0);
                                    inputComment.setText("");
                                    commentdatas.clear();
                                    page=0;
                                    getComList(did);
                                    EventBus.getDefault().post(new CommentEvent(position, (Integer.parseInt(comnum) + 1)));
                                    comnum = (Integer.parseInt(comnum) + 1) + "";
                                    break;
                                case 3000:
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                    ToastUtil.show(MyApp.getInstance(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {
                btn_submit.setEnabled(true);
                tvSend.setEnabled(true);
            }
        });
    }

    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private View rl_input_container;
    private InputMethodManager mInputManager;
    @SuppressLint("WrongConstant")
    private void showPopupcomment(String nickname) {
        if (popupView == null){
//加载评论框的资源文件
            popupView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        btn_submit = (TextView) popupView.findViewById(R.id.btn_confirm);
        if(TextUtil.isEmpty(nickname)) {
            inputComment.setHint("");
        } else {
            inputComment.setHint("回复 " + nickname + ":");
        }
        inputComment.setText("");
        rl_input_container = popupView.findViewById(R.id.rl_input_container);
//利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run()
            {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputComment.setFocusable(true);
                        inputComment.setFocusableInTouchMode(true);
                        inputComment.requestFocus();
                        mInputManager = (InputMethodManager)mContext.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mInputManager.showSoftInput(inputComment, 0);
                    }
                });

            }
        }, 200);
        if (popupWindow == null){
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
                if (mInputManager.isActive()){
                    popupWindow=null;
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
//外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputManager.isActive()){
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
                sendComment();
                inputComment.setText("");
                if (mInputManager.isActive()){
                    mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                popupWindow.dismiss();
            }
        });

        setKeyListener();

    }

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
//                    Intent intent = new Intent(mContext, AtFansActivity.class);
                    Intent intent = new Intent(mContext, AtMemberActivity.class);
                    mContext.startActivityForResult(intent, 100);
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


    void showCommentOperatePop(final int index) {
        final String cmid = commentdatas.get(index).getCmid();
        String otheruid = commentdatas.get(index).getUid();
        final String content =  commentdatas.get(index).getContent();
        final OperateCommentPopup operateCommentPopup;
        String admin = (String) SharedPreferencesUtils.getParam(mContext, "admin", "0");
        if(MyApp.uid.equals(uid) || "1".equals(admin) || MyApp.uid.equals(otheruid) ) {
            operateCommentPopup = new OperateCommentPopup(mContext,0);
        } else {
            operateCommentPopup = new OperateCommentPopup(mContext);
        }

        operateCommentPopup.setOnCommentOperateListener(new OperateCommentPopup.OnCommentOperateListener() {
            @Override
            public void onCommentCopy() {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", content);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.show(mContext, "已复制到剪贴板");
            }

            @Override
            public void onCommentReport() {
                Intent intent = new Intent(mContext, OtherReasonActivity.class);
                intent.putExtra("uid", MyApp.uid);
                intent.putExtra("did", did);
                intent.putExtra("cmid", cmid);
                mContext.startActivity(intent);
                operateCommentPopup.dismiss();
            }

            @Override
            public void onCommentDelete() {
                operateCommentPopup.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                                ToastUtil.show(mContext,"删除成功");
                                commentdatas.remove(index);
                                commentLevelOneAdapter.notifyItemRemoved(index);
                            }

                            @Override
                            public void onFail(String msg) {
                                ToastUtil.show(mContext,msg);
                            }
                        });
                    }
                }).create().show();

            }

        });
        operateCommentPopup.showPopupWindow();

    }
}

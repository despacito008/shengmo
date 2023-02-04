package com.aiwujie.shengmo.videoplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AtFansActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.eventbus.CommentEvent;
import com.aiwujie.shengmo.eventbus.DianZanEvent;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.AtMemberActivity;
import com.aiwujie.shengmo.media.VideoPlayAdapter;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.videoplay.view.VideoLoadingProgressbar;
import com.aiwujie.shengmo.view.CustomPopupWindow;
import com.aiwujie.shengmo.view.love.Love;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class VideoAdapter extends VideoPlayAdapter<VideoAdapter.ViewHolder> {
    private CustomPopupWindow mPop;
    private Activity mContext;
    private List<DynamicListData.DataBean> list;
    private int mCurrentPosition;
    private ViewHolder mCurrentHolder;
    private RefreshDataListener mRefreshDataListener;
    private VideoPlayer videoPlayer;
    private TextureView textureView;

    private final Display defaultDisplay;
    private GestureDetector myGestureDetector;

    public VideoAdapter(Activity mContext, List<DynamicListData.DataBean> list) {
        this.mContext = mContext;
        this.list = list;

        videoPlayer = new VideoPlayer();
        textureView = new TextureView(mContext);
        videoPlayer.setTextureView(textureView);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        defaultDisplay = wm.getDefaultDisplay();
        EventBus.getDefault().register(this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (1 == list.get(position).getFollow_state() || 3 == list.get(position).getFollow_state() || MyApp.uid.equals(list.get(position).getUid())) {
            holder.ivAttention.setVisibility(View.GONE);
        } else {
            holder.ivAttention.setVisibility(View.VISIBLE);
        }

        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

//        if (!"0".equals(list.get(position).getWidth()) && !"0".equals(list.get(position).getHeight())) {
//            float width = defaultDisplay.getWidth();
//            float height = defaultDisplay.getHeight();
//            float videoWidth = Float.parseFloat(list.get(position).getWidth());
//            float videoHeight = Float.parseFloat(list.get(position).getHeight());
//            height = width * (videoHeight / videoWidth);
//            Glide.with(mContext).load(list.get(position).getCoverUrl()).override((int)width,(int)height).apply(options).into(holder.ivCover);
//        } else {
//            Glide.with(mContext).load(list.get(position).getCoverUrl()).apply(options).into(holder.ivCover);
//        }
        Glide.with(mContext).load(list.get(position).getCoverUrl()).apply(options).into(holder.ivCover);
        holder.tvNickname.setText(list.get(position).getNickname());


        GlideImgManager.glideLoader(mContext, list.get(position).getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.ivHead, 0);

        if ("1".equals(list.get(position).getIs_admin())) {
            holder.ivVipFlag.setImageResource(R.drawable.user_manager);//黑V
        } else if ("1".equals(list.get(position).getSvipannual())) {
            holder.ivVipFlag.setImageResource(R.drawable.user_svip_year);
        } else if ("1".equals(list.get(position).getSvip())) {
            holder.ivVipFlag.setImageResource(R.drawable.user_svip);
        } else if ("1".equals(list.get(position).getVipannual())) {
            holder.ivVipFlag.setImageResource(R.drawable.user_vip_year);
        } else if ("1".equals(list.get(position).getVip())) {
            holder.ivVipFlag.setImageResource(R.drawable.user_vip);
        } else {
            holder.ivVipFlag.setImageResource(R.color.transparent);
        }

        holder.tvContent.setText(Html.fromHtml("<font color='#E025E6'>" + list.get(position).getTopictitle() + "</font>&#160" + list.get(position).getContent()));
        holder.tvCommentNum.setText(list.get(position).getComnum());
        holder.tvRedHeartNum.setText(list.get(position).getLaudnum());
        if ("1".equals(list.get(position).getLaudstate())) {
            holder.ivRedHeart.setImageResource(R.mipmap.icon_red_heart_checked);
        } else {
            holder.ivRedHeart.setImageResource(R.mipmap.icon_red_heart_unchecked);
        }
        holder.pbLoading.setIIsLoading(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onPageSelected(final int itemPosition, View itemView) {
        mCurrentPosition = itemPosition;
        mCurrentHolder = new ViewHolder(itemView);

        addViewCount(itemPosition);

        Glide.with(mContext)
                .asBitmap()
                .load(list.get(itemPosition).getCoverUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        float width = defaultDisplay.getWidth();
                        float height = defaultDisplay.getHeight();
                        height = (float) (width * ((resource.getHeight()*1.0) / (resource.getWidth())*1.0f));
                        ViewGroup.LayoutParams layoutParams = mCurrentHolder.flVideo.getLayoutParams();
                        ViewGroup.LayoutParams layoutParams1 = mCurrentHolder.ivCover.getLayoutParams();
                        LogUtil.d("width = "+ width);
                        LogUtil.d("height = "+ height);
                        if (layoutParams != null) {
                            layoutParams.height = (int) height;
                            layoutParams.width = (int) width;
                            layoutParams1.height = (int) height;
                            layoutParams1.width = (int) width;
                            mCurrentHolder.flVideo.setLayoutParams(layoutParams);
                            mCurrentHolder.ivCover.setLayoutParams(layoutParams1);
                        }
                    }
                });

//        float width = defaultDisplay.getWidth();
//        float height = defaultDisplay.getHeight();
//
//        if (!"0".equals(list.get(itemPosition).getWidth()) && !"0".equals(list.get(itemPosition).getHeight())) {
//            float videoWidth = Float.parseFloat(list.get(itemPosition).getWidth());
//            float videoHeight = Float.parseFloat(list.get(itemPosition).getHeight());
//            if (width * (videoHeight / videoWidth) > height) {
//                width = height / (videoHeight / videoWidth);
//            } else {
//                height = width * (videoHeight / videoWidth);
//            }
//        }

//        ViewGroup.LayoutParams layoutParams = mCurrentHolder.flVideo.getLayoutParams();
//        ViewGroup.LayoutParams layoutParams1 = mCurrentHolder.ivCover.getLayoutParams();
//        if (layoutParams != null) {
//            layoutParams.height = (int) height;
//            layoutParams.width = (int) width;
//            layoutParams1.height = (int) height;
//            layoutParams1.width = (int) width;
//            mCurrentHolder.flVideo.setLayoutParams(layoutParams);
//            mCurrentHolder.ivCover.setLayoutParams(layoutParams1);
//        }


        mCurrentHolder.ivAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow(list.get(itemPosition).getUid(), itemPosition);
            }
        });
        //点击头像
        mCurrentHolder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(itemPosition).getUid());
                intent.putExtra("position", itemPosition);
                mContext.startActivity(intent);
            }
        });
        //点击用户名字
        mCurrentHolder.tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(itemPosition).getUid());
                intent.putExtra("position", itemPosition);
                mContext.startActivity(intent);
            }
        });
        //点击分享
        mCurrentHolder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareWay(itemPosition, mCurrentHolder.clVideo);
            }
        });
        //更多
        mCurrentHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtra("uid", list.get(itemPosition).getUid());
                intent.putExtra("did", list.get(itemPosition).getDid());
                intent.putExtra("pos", itemPosition);
                intent.putExtra("showwhat", 1);
                mContext.startActivity(intent);
            }
        });

        //点赞
        mCurrentHolder.ivRedHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"1".equals(list.get(itemPosition).getLaudstate())) {
                    laudDynamic(itemPosition);
                }
            }
        });


        //评论
        mCurrentHolder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPop == null) {
                    mPop = new CustomPopupWindow(mContext, list.get(itemPosition).getDid(), list.get(itemPosition).getUid(), list.get(itemPosition).getComnum(), itemPosition);
                }

                mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mPop = null;
                    }
                });
                mPop.showAtLocation(mCurrentHolder.clVideo, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                mPop.setFocusable(true);
                mPop.setOutsideTouchable(true);
                // mPop.update();
            }
        });
        myGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mCurrentHolder.love.addLoveView(e.getRawX(), e.getRawY());
                if (!"1".equals(list.get(itemPosition).getLaudstate())) {
                    laudDynamic(itemPosition);
                }
                return true;
                //   return super.onSingleTapUp(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                switch (videoPlayer.getState()) {
                    case PAUSE:
                        mCurrentHolder.ivPlay.setVisibility(View.GONE);
                        videoPlayer.start();
                        break;

                    case PLAYING:
                        videoPlayer.pause();
                        mCurrentHolder.ivPlay.setVisibility(View.VISIBLE);
                        break;
                }
                return super.onSingleTapUp(e);

            }
        });
        mCurrentHolder.clVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mCurrentHolder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupcomment();
            }
        });

        playVideo(itemPosition);
        if (mRefreshDataListener != null && itemPosition >= list.size() - 3) {
            mRefreshDataListener.refreshDataView(list.get(0).getDid(), list.get(list.size() - 1).getUid());

        }
    }

    //播放视频
    private void playVideo(int position) {
        if (videoPlayer == null) {
            return;
        }
        videoPlayer.reset();
        mCurrentHolder.pbLoading.setVisibility(View.VISIBLE);
        mCurrentHolder.ivPlay.setVisibility(View.GONE);
        videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
            @Override
            public void onReset() {
                mCurrentHolder.ivCover.setVisibility(View.VISIBLE);
                //  mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRenderingStart() {
                mCurrentHolder.ivCover.setVisibility(View.GONE);
                //   mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onProgressUpdate(float per) {
                mCurrentHolder.pbLoading.setVisibility(View.VISIBLE);
                mCurrentHolder.pbLoading.setIIsLoading(false);
                mCurrentHolder.pbLoading.setProgress(per);
            }

            @Override
            public void onPause() {
                // mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStop() {
                mCurrentHolder.ivCover.setVisibility(View.VISIBLE);
                // mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                videoPlayer.start();
            }
        });
        if (textureView.getParent() != mCurrentHolder.flVideo) {
            if (textureView.getParent() != null) {
                ((FrameLayout) textureView.getParent()).removeView(textureView);
            }
            mCurrentHolder.flVideo.addView(textureView);
        }

        videoPlayer.setDataSource(list.get(position).getPlayUrl());
        videoPlayer.prepare();
    }

    public void setRefreshDataListener(RefreshDataListener refreshDataListener) {
        this.mRefreshDataListener = refreshDataListener;
    }

    public void release() {
        videoPlayer.release();
    }

    public void pause() {
        if (videoPlayer.getState() == VideoPlayer.State.PAUSE) {
            return;
        }
        videoPlayer.pause();
    }

    public void start() {
        if (videoPlayer.getState() == VideoPlayer.State.PAUSE) {
            videoPlayer.start();
            mCurrentHolder.ivPlay.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout flVideo;
        private ConstraintLayout clVideo;
        private ImageView ivPlay, ivCover, ivHead, ivVipFlag, ivAttention, ivShare, ivComment, ivRedHeart, ivMore;
        private VideoLoadingProgressbar pbLoading;
        private TextView tvNickname, tvContent, tvCommentNum, tvRedHeartNum, tvComment;
        private Love love;

        ViewHolder(View itemView) {
            super(itemView);
            flVideo = itemView.findViewById(R.id.flVideo);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivPlay = itemView.findViewById(R.id.iv_play);
            pbLoading = itemView.findViewById(R.id.pbLoading);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            clVideo = itemView.findViewById(R.id.cl_video);
            ivHead = itemView.findViewById(R.id.iv_head);
            ivVipFlag = itemView.findViewById(R.id.iv_vip_flag);
            ivAttention = itemView.findViewById(R.id.iv_attention);//关注
            tvContent = itemView.findViewById(R.id.tv_content);//内容
            ivShare = itemView.findViewById(R.id.iv_share);// 分享
            tvCommentNum = itemView.findViewById(R.id.tv_comment_num);// 评论数量
            ivComment = itemView.findViewById(R.id.iv_comment);// 评论
            tvRedHeartNum = itemView.findViewById(R.id.tv_red_heart_num);// 点赞数量
            ivRedHeart = itemView.findViewById(R.id.iv_red_heart);// 点赞
            ivMore = itemView.findViewById(R.id.iv_more);// 点赞
            love = itemView.findViewById(R.id.love);
            tvComment = itemView.findViewById(R.id.tvComment);//发布评论


        }
    }

    interface RefreshDataListener {
        void refreshDataView(String did, String uid);
    }

    //点关注
    private void follow(String uid, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                        case 4787:
                            switch (list.get(position).getFollow_state()) {
                                case 4:
                                case 0:
                                    list.get(position).setFollow_state(1);
                                    EventBus.getDefault().post(new FollowEvent(1, position));
                                    break;
                                case 2:
                                    list.get(position).setFollow_state(3);
                                    EventBus.getDefault().post(new FollowEvent(3, position));
                                    break;
                                case 3:
                                    list.get(position).setFollow_state(2);
                                    EventBus.getDefault().post(new FollowEvent(2, position));
                                    break;

                            }
                            notifyItemChanged(position);
                            break;
                        case 4001:
                        case 4002:
                        case 8881:
                        case 8882:

                            ToastUtil.show(mContext, obj.getString("msg") + "");
                            break;
                        case 5000:

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

            }
        });
    }

    //分享功能
    private void showShareWay(int position, View view) {

        SharedPop sharedPop = new SharedPop(mContext, HttpUrl.NetPic() + HttpUrl.ShareDynamicDetail + list.get(position).getDid(), "来自圣魔的动态", list.get(position).getNickname() + " 在圣魔发布了精彩的动态。", list.get(position).getCoverUrl(), 0, 1, list.get(position).getDid(), list.get(position).getContent(), list.get(position).getCoverUrl(), MyApp.uid);
        sharedPop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {mContext.getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        mContext.getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = mContext.getWindow().getAttributes();
                params[0].alpha = 1f;
                mContext.getWindow().setAttributes(params[0]);
            }
        });
    }


    //点赞
    private void laudDynamic(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", list.get(pos).getDid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.LaudDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("laudDynamic", "onSuccess: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            list.get(pos).setLaudnum((Integer.parseInt(list.get(pos).getLaudnum()) + 1) + "");
                            list.get(pos).setLaudstate("1");
                            EventBus.getDefault().post(new DianZanEvent(pos));
                            notifyItemChanged(pos);
                            break;
                        case 4003:
                        case 4004:
                            ToastUtil.show(mContext.getApplicationContext(), obj.getString("msg"));
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
    private void showPopupcomment() {
        if (popupView == null) {
//加载评论框的资源文件
            popupView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        btn_submit = (TextView) popupView.findViewById(R.id.btn_confirm);
        inputComment.setText("");
        rl_input_container = popupView.findViewById(R.id.rl_input_container);
//利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inputComment.setFocusable(true);
                        inputComment.setFocusableInTouchMode(true);
                        inputComment.requestFocus();
                        mInputManager = (InputMethodManager) mContext.getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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


    private String otheruid = "0";
    private String platuid;
    private String platuname;

    //发布评论
    private void sendComment() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", list.get(mCurrentPosition).getDid());
        map.put("content", inputComment.getText().toString());
        map.put("otheruid", otheruid);

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
                inputComment.setEnabled(true);
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {

                        case 2000:
                            InputMethodManager imm = (InputMethodManager) inputComment.getContext().getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(inputComment.getWindowToken(), 0);
                            inputComment.setText("");
                            EventBus.getDefault().post(new CommentEvent(mCurrentPosition, (Integer.parseInt(list.get(mCurrentPosition).getComnum()) + 1)));
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
                inputComment.setEnabled(true);
            }
        });
    }

    public void addViewCount(int index) {
        HttpHelper.getInstance().addDynamicViewCount(list.get(index).getDid(), new HttpListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    //设置避免视频播放时拉伸，复制可直接使用
//    private void stretching(float mtextureViewWidth,float mtextureViewHeight){
//        //mtextureViewWidth为textureView宽，mtextureViewHeight为textureView高
//        //mtextureViewWidth宽高，为什么需要用传入的，因为全屏显示时宽高不会及时更新
//        Matrix matrix = new Matrix();
//        //videoView为new MediaPlayer()
//        mVideoWidth=videoView.getVideoWidth();
//        mVideoHeight=videoView.getVideoHeight();
//
////        mtextureViewWidth=textureView.getWidth();
////        mtextureViewHeight=textureView.getHeight();
//
//        //得到缩放比，从而获得最佳缩放比
//        float sx = mtextureViewWidth / mVideoWidth;
//        float sy = mtextureViewHeight / mVideoHeight;
//        //先将视频变回原来的大小
//        float sx1 =  mVideoWidth / mtextureViewWidth;
//        float sy1 = mVideoHeight/mtextureViewHeight;
//        matrix.preScale(sx1,sy1);
//        Log.d("mat",matrix.toString());
//        //然后判断最佳比例，满足一边能够填满
//        if(sx>=sy){
//            matrix.preScale(sy,sy);
//            //然后判断出左右偏移，实现居中，进入到这个判断，证明y轴是填满了的
//            float leftX=(mtextureViewWidth - mVideoWidth * sy) / 2;
//            matrix.postTranslate(leftX, 0);
//        }else{
//            matrix.preScale(sx,sx);
//            float leftY=(mtextureViewHeight - mVideoHeight*sx) / 2;
//            matrix.postTranslate(0, leftY);
//        }
//
//        textureView.setTransform(matrix);//将矩阵添加到textureView
//        textureView.postInvalidate();//重绘视图
//
//
//    }


}

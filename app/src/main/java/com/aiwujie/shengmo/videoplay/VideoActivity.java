package com.aiwujie.shengmo.videoplay;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.CommentDetailEvent;
import com.aiwujie.shengmo.eventbus.CommentEvent;
import com.aiwujie.shengmo.eventbus.DianZanEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.media.VideoPlayRecyclerView;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.OkhttpUpload;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.immersionbar.ImmersionBar;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoActivity extends AppCompatActivity {
    private VideoPlayRecyclerView mRvVideo;
    private VideoAdapter adapter;
    private DynamicListData.DataBean videoBean;
    private List<DynamicListData.DataBean> list = new ArrayList<>();
    String type;
    private String sex = "";

    private int page = 0;
    private String sexual = "";
    private String order;
    private boolean isRequestData = false;
    private AudioManager am;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;
    private List<DynamicListData.DataBean> videoList;
    private String currentPage, currentPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
        ImmersionBar.with(this).init();
        Intent intent = getIntent();
        order = intent.getStringExtra("order");
        type = intent.getStringExtra("type");
        videoBean = (DynamicListData.DataBean) intent.getSerializableExtra("videoBean");
        videoList = (List<DynamicListData.DataBean>) intent.getSerializableExtra("videoList");
        currentPage = intent.getStringExtra("page");
        currentPostion = intent.getStringExtra("currentPosition");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();
    }


    private void initData() {
        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {

            }
        };
        am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        sex = (String) SharedPreferencesUtils.getParam(MyApp.getInstance(), "dynamicSex", "0");
        sexual = (String) SharedPreferencesUtils.getParam(MyApp.getInstance(), "dynamicSexual", "0");
    }

    private void initView() {
        ImageButton ivBack = findViewById(R.id.iv_back);
        ((ViewGroup.MarginLayoutParams) ivBack.getLayoutParams()).topMargin = StatusBarUtil.getStatusHeight(getApplicationContext());
        ((ViewGroup.MarginLayoutParams) ivBack.getLayoutParams()).leftMargin = 5;

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!TextUtil.isEmpty(currentPage)) {
            page = Integer.parseInt(currentPage);
            page++;
        }
        if (videoBean != null) {
            list.add(videoBean);
        }
        mRvVideo = (VideoPlayRecyclerView) findViewById(R.id.rvVideo);
        adapter = new VideoAdapter(this, list);
        adapter.setRefreshDataListener(new VideoAdapter.RefreshDataListener() {
            @Override
            public void refreshDataView(String did, String uid) {
                if (!isRequestData) {
                    getDynamicList(did, uid, page++);
                }
            }
        });
        if (videoList != null) {
            list.clear();
            list.addAll(videoList);

        }

        mRvVideo.setAdapter(adapter);
        if (!TextUtil.isEmpty(currentPostion)) {
            mRvVideo.scrollToPosition(Integer.parseInt(currentPostion));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.pause();
        am.abandonAudioFocus(onAudioFocusChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        adapter.release();
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
    }

    private void getDynamicList(String did, String uid, int currentPage) {
        Map<String, String> map = new HashMap<>();
        if ("3".equals(type)) {
            map.put("uid", uid);
        } else {
            map.put("uid", MyApp.uid);
        }
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", type);
        map.put("page", currentPage + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", did);
        if (!"2".equals(type) && !"3".equals(type)) {
            map.put("did", did);
        }
        if (!TextUtil.isEmpty(getIntent().getStringExtra("tid"))) {
            map.put("tid",getIntent().getStringExtra("tid"));
        }
        if (!TextUtil.isEmpty(order)) {
            map.put("order", order);
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        isRequestData = true;
        manager.post(HttpUrl.DynamicShortVideoList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    final DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                    if (listData.getData().size() != 0) {
                        list.addAll(listData.getData());
                        adapter.notifyDataSetChanged();
                        isRequestData = false;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Throwable throwable) {
                isRequestData = false;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void secondEventBus(CommentEvent event) {
        if (list.size() != 0) {
            list.get(event.getPosition()).setComnum(event.getCommentcount() + "");
            adapter.notifyItemChanged(event.getPosition());
        }
    }

    /**
     * 设置关注
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void followEventBus(FollowEvent event) {
        if (list.size() != 0 && event.getPosition() != -1) {
            list.get(event.getPosition()).setFollow_state(event.getFollowStateNew());
            if (adapter != null) {
                adapter.notifyItemChanged(event.getPosition());
            }
        }
    }

    /***
     * 点赞
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(DynamicEvent event) {
        if (list.size() != 0 && event.getPosition() != -1) {
            list.get(event.getPosition()).setLaudstate(event.getLaudstate() + "");
            list.get(event.getPosition()).setLaudnum(event.getZancount() + "");
            if (adapter != null) {
                adapter.notifyItemChanged(event.getPosition());
            }
            if (videoList != null && videoList.size() > 0) {
                EventBus.getDefault().post(new DianZanEvent(event.getPosition()));
            }
        }
    }


    int mx, my;
    int lastx, lasty;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //获取坐标点：
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (x > 50)
            return super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deletx = x - mx;
                int delety = y - my;
                if (Math.abs(deletx) > Math.abs(delety)) {
                    finish();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
        lastx = x;
        lasty = y;
        mx = x;
        my = y;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();

    }
}
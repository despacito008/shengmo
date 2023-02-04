package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.SoundLoveListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.SoundLoveData;
import com.aiwujie.shengmo.eventbus.MediaCompleteEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.MediaPlayerManager;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoundLoveActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2{
    Handler handler = new Handler();
    @BindView(R.id.mSound_love_return)
    ImageView mSoundLoveReturn;
    @BindView(R.id.mSound_love_luzhi)
    ImageView mSoundLoveLuzhi;
    @BindView(R.id.mSound_love_listview)
    PullToRefreshListView mSoundLoveListview;
    private int page=0;
    private SoundLoveListviewAdapter listviewAdapter;
    private List<SoundLoveData.DataBean> soundloves=new ArrayList<>();
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh=true;
    private TimeSecondUtils refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_love);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setData();
        setListener();
    }
    private void setData() {
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mSoundLoveListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mSoundLoveListview.setFocusable(false);
        //实现自动刷新
        mSoundLoveListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mSoundLoveListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mSoundLoveListview.setRefreshing();
                    mSoundLoveListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void setListener() {
        mSoundLoveListview.setOnRefreshListener(this);
        mSoundLoveListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getSoundLoveList();
                            }
                        }
                        break;

                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    private void getSoundLoveList() {
        Map<String, String> map = new HashMap<>();
        map.put("layout", "1");
        map.put("type", "4");
        map.put("page",page+"");
        map.put("loginid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.UserListNewth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("soundloveresponse", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SoundLoveData data = new Gson().fromJson(response, SoundLoveData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    isReresh=false;
                                    page = page - 1;
                                    ToastUtil.show(SoundLoveActivity.this.getApplicationContext(), "没有更多");
                                }
                                if (listviewAdapter != null) {
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    soundloves.addAll(data.getData());
                                    try {
                                        listviewAdapter = new SoundLoveListviewAdapter(SoundLoveActivity.this, soundloves);
                                        mSoundLoveListview.setAdapter(listviewAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    soundloves.addAll(data.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        mSoundLoveListview.onRefreshComplete();
                        if(refresh!=null) {
                            refresh.cancel();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    @OnClick({R.id.mSound_love_return, R.id.mSound_love_luzhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mSound_love_return:
                finish();
                break;
            case R.id.mSound_love_luzhi:
                Intent intent = new Intent(this, RecorderActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(MediaCompleteEvent event) {
        listviewAdapter.setSelectIndex(-1);
        listviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        soundloves.clear();
        if(listviewAdapter!=null){
            listviewAdapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(this,mSoundLoveListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSoundLoveList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager mediaPlayerManager=MediaPlayerManager.getInstance(this);
        mediaPlayerManager.stop();
        EventBus.getDefault().unregister(this);
    }
}

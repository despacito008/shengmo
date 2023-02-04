package com.aiwujie.shengmo.fragment.redwomenfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.RedWomenDetailActivity;
import com.aiwujie.shengmo.adapter.RedwomenAdapter;
import com.aiwujie.shengmo.bean.BeautifulData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AutoLoadListener;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/8/11.
 */

public class RedWomenBeautifulManFragment extends Fragment implements AdapterView.OnItemClickListener ,PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.mFragment_redwomen_man_gridview)
    PullToRefreshGridView mFragmentRedwomenManGridview;
    private int page=0;
    private AutoLoadListener autoLoadListener;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh=true;
    private Handler handler=new Handler();
    private List<BeautifulData.DataBean> beautifuls=new ArrayList<>();
    private TimeSecondUtils refresh;
    private RedwomenAdapter beautifulAdapter;
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    private boolean isCanLoad=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_redwomen_gridview, null);
        ButterKnife.bind(this, view);
//        setData();
        isPrepared = true;
        lazyLoad();
        return view;
    }
    private void setData() {
        mFragmentRedwomenManGridview.setFocusable(false);
        mFragmentRedwomenManGridview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentRedwomenManGridview.setOnRefreshListener(this);
        mFragmentRedwomenManGridview.setOnItemClickListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentRedwomenManGridview.setRefreshing();
            }
        },100);
        //添加自动读页的事件
        autoLoadListener = new AutoLoadListener(callBack);
        mFragmentRedwomenManGridview.setOnScrollListener(autoLoadListener);
        mFragmentRedwomenManGridview.getRefreshableView().setOnItemClickListener(this);
    }

    private void getBeautifulMan() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("sex","0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMatchUsersList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("GetMatchUsersList", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object=new JSONObject(response);
                            if(object.getInt("retcode")==2000) {
                                BeautifulData listData = new Gson().fromJson(response, BeautifulData.class);
                                if (listData.getData().size() == 0) {
                                    if (page != 0) {
                                        isReresh = false;
                                        page = page - 1;
                                        ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                    }
                                } else {
                                    isReresh = true;
                                    if (page == 0) {
                                        beautifuls.addAll(listData.getData());
                                        try {
                                            beautifulAdapter = new RedwomenAdapter(getActivity(), beautifuls);
                                            mFragmentRedwomenManGridview.setAdapter(beautifulAdapter);
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        beautifuls.addAll(listData.getData());
                                        beautifulAdapter.notifyDataSetChanged();
                                    }
                                }
                            }else if(object.getInt("retcode")==3000){
                                isReresh = false;
                            }else if(object.getInt("retcode")==3001){
                                ToastUtil.show(getActivity().getApplicationContext(),object.getString("msg"));
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        isCanLoad=true;
                        mFragmentRedwomenManGridview.onRefreshComplete();
                        if (refresh != null) {
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getActivity(), RedWomenDetailActivity.class);
        intent.putExtra("uid",beautifuls.get(position).getUid());
        getActivity().startActivity(intent);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        beautifuls.clear();
        if (beautifulAdapter != null) {
            beautifulAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentRedwomenManGridview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBeautifulMan();
            }
        }, 300);
    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute(int pages) {
            if(isCanLoad) {
                isCanLoad=false;
                if (isReresh) {
                    page = pages;
                    getBeautifulMan();
                }
            }
        }

    };
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            isFirstLoad = false;
            setData();
        }
    }

}

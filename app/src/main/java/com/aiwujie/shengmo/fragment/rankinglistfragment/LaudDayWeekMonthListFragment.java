package com.aiwujie.shengmo.fragment.rankinglistfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankPopularitydAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.RankPopularityData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/5/11.
 */

public class LaudDayWeekMonthListFragment extends Fragment {
    @BindView(R.id.item_fragment_fragment_rank_listview)
    ListView itemFragmentFragmentRankListview;
    //（ 0：日榜 1：周榜 2：月榜）
    private int State;
    private int page=0;
    Handler handler=new Handler();
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
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh=true;
    List<RankPopularityData.DataBean> ranks=new ArrayList<>();
    private RankPopularitydAdapter ranksAdapter;

    public LaudDayWeekMonthListFragment() {

    }

    public static LaudDayWeekMonthListFragment newInstance(int State) {
        LaudDayWeekMonthListFragment newFragment = new LaudDayWeekMonthListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("State", State);
        newFragment.setArguments(bundle);
        return newFragment;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad=true;
        View view = inflater.inflate(R.layout.item_fragment_fragment_rank_list, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    private void setData() {
        itemFragmentFragmentRankListview.setDividerHeight(0);
        itemFragmentFragmentRankListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getBeLaudedRankingList();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    private void getBeLaudedRankingList() {
        Map<String,String> map=new HashMap<>();
        map.put("type",State+"");
        map.put("page",page+"");
        map.put("uid", MyApp.uid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBeLaudedRankingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("dayweekmonthlist", "run: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RankPopularityData data = new Gson().fromJson(response, RankPopularityData.class);
                            if(data.getData().size()==0){
                                if(page!=0) {
                                    page = page - 1;
                                    isReresh=false;
                                    ToastUtil.show(getActivity().getApplicationContext(),data.getMsg());
                                }
                            }else{
                                isReresh=true;
                                Log.i("dayweekmonthlist", "run: "+page);
                                if (page == 0) {
                                    ranks.addAll(data.getData());
                                    ranksAdapter = new RankPopularitydAdapter(getActivity(),ranks);
                                    itemFragmentFragmentRankListview.setAdapter(ranksAdapter);
                                } else {
                                    ranks.addAll(data.getData());
                                    ranksAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
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
            Bundle args = getArguments();
            if (args != null) {
                State = args.getInt("State");
            }
            getBeLaudedRankingList();
            itemFragmentFragmentRankListview.animate().alpha(1).setStartDelay(120).start();
        }
    }
}

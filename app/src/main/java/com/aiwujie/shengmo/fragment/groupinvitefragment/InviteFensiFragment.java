package com.aiwujie.shengmo.fragment.groupinvitefragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GrouplistActivity;
import com.aiwujie.shengmo.activity.GrouptoaddActivity;
import com.aiwujie.shengmo.adapter.InviteFollow2Adapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/6/8.
 */

public class InviteFensiFragment extends Fragment implements AdapterView.OnItemClickListener ,PullToRefreshBase.OnRefreshListener2,TextView.OnEditorActionListener{
    @BindView(R.id.mFragment_invite_follow_listview)
    PullToRefreshListView mFragmentInviteFollowListview;
    @BindView(R.id.mGroupSearchKeyWord_et_sou)
    EditText mGroupSearchKeyWord_et_sou;
    private int page = 0;
    private ArrayList<GzFsHyListviewData.DataBean> datas = new ArrayList<>();
    Handler handler = new Handler();
    private TimeSecondUtils refresh;
    private InputMethodManager imm;
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
    private InviteFollow2Adapter adapter;
    public static String fensiUidstr="";
    String name="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad=true;
        View view = inflater.inflate(R.layout.fragment_invite_follow, null);
        ButterKnife.bind(this, view);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mGroupSearchKeyWord_et_sou.setOnEditorActionListener(this);
        mGroupSearchKeyWord_et_sou.setVisibility(View.VISIBLE);
        isPrepared = true;
        lazyLoad();
        return view;
    }
    private void setData() {

        adapter = new InviteFollow2Adapter(getActivity(), datas,GrouptoaddActivity.inviteUids);
        mFragmentInviteFollowListview.setAdapter(adapter);
        mFragmentInviteFollowListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentInviteFollowListview.setFocusable(false);
        mFragmentInviteFollowListview.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mFragmentInviteFollowListview.getRefreshableView().getCheckedItemPositions();
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentInviteFollowListview.setRefreshing();
            }
        },100);
    }

    private void setListener() {
        mFragmentInviteFollowListview.setOnRefreshListener(this);
        mFragmentInviteFollowListview.setOnItemClickListener(this);
        mFragmentInviteFollowListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getFollewingList();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }

            datas.clear();
            page=0;
            name=mGroupSearchKeyWord_et_sou.getText().toString();
            getFollewingList();
            //getsouAtList(mGroupSearchKeyWord_et_sou.getText().toString());
            return true;
        }
        return false;
    }

    private void getFollewingList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        map.put("type", "1");
        map.put("name", name);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollewingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            GzFsHyListviewData dataList = new Gson().fromJson(response, GzFsHyListviewData.class);
                            if (dataList.getData().size() == 0) {
                                if(page!=0) {
                                    page = page - 1;
                                    isReresh=false;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    datas.addAll(dataList.getData());
                                   adapter.notifyDataSetChanged();
                                } else {
                                    datas.addAll(dataList.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFragmentInviteFollowListview.onRefreshComplete();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //fensiUidstr= InviteMemberClickUtils2.clickss(getActivity(),mFragmentInviteFollowListview,datas,position);
        if (GrouplistActivity.yiyouhaoyou.contains(datas.get(position-1).getUid())){

        }else {
            if (datas.get(position-1).isIscheck()){
                datas.get(position-1).setIscheck(false);
                GrouptoaddActivity.inviteUids.remove(datas.get(position-1).getUid());
            }else {
                datas.get(position-1).setIscheck(true);
                GrouptoaddActivity.inviteUids.add(datas.get(position-1).getUid());
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        name="";
        datas.clear();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(getActivity(),mFragmentInviteFollowListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFollewingList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
          if (null !=adapter){
              adapter.notifyDataSetChanged();
          }
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
            setListener();
        }
    }
}

package com.aiwujie.shengmo.fragment.at;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.AtFansListviewAdapter;
import com.aiwujie.shengmo.adapter.AtSouListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.AtsouBean;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/26.
 */

public class FragmentatGuanzhu extends Fragment implements AdapterView.OnItemClickListener,PullToRefreshBase.OnRefreshListener2,TextView.OnEditorActionListener{

    private PullToRefreshListView mAtFansListview;
    List<GzFsHyListviewData.DataBean> fansdatas=new ArrayList<>();
    Handler handler=new Handler();
    int page=0;
    private AtFansListviewAdapter adapter;
    private TimeSecondUtils refresh;
    private int atsize=0;
    List<Atbean.DataBean> dataBeanList = new ArrayList<>();
    List<AtsouBean.DataBean> souList = new ArrayList<>();
    Atbean atbean = new Atbean();
    private EditText mGroupSearchKeyWord_et_sou;
    private InputMethodManager imm;
    private AtSouListviewAdapter atSouListviewAdapter;
    int liebiaosou=0;
    String name="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.atguanzhu,container,false);

        mAtFansListview = view.findViewById(R.id.mAt_fans_listview);
        mGroupSearchKeyWord_et_sou = view.findViewById(R.id.mGroupSearchKeyWord_et_sou);
        mGroupSearchKeyWord_et_sou.setOnEditorActionListener(this);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        atSouListviewAdapter = new AtSouListviewAdapter(getContext(), souList);
        setData();
        setListener();
        return view;
    }

    private void setData() {
        mAtFansListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mAtFansListview.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mAtFansListview.getRefreshableView().getCheckedItemPositions();
        //实现自动刷新
        mAtFansListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mAtFansListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mAtFansListview.setRefreshing();
                    mAtFansListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });

    }

    private void setListener() {
        mAtFansListview.setOnRefreshListener(this);
        mAtFansListview.setOnItemClickListener(this);
        mAtFansListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&visibleItemCount!=totalItemCount) {
                    if(!mAtFansListview.isShownHeader()) {
                        page = page + 1;
                        getFansList();
                    }
                }
            }
        });
    }
    private void getFansList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        map.put("type", "0");
        map.put("name", name);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollewingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfans", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            GzFsHyListviewData data = new Gson().fromJson(response, GzFsHyListviewData.class);
                            if (data.getData().size() == 0) {
                                page = page - 1;
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            } else {
                                if (page == 0) {
                                    fansdatas.addAll(data.getData());
                                    try{
                                        adapter = new AtFansListviewAdapter(getContext(), fansdatas);
                                        mAtFansListview.setAdapter(adapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    fansdatas.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mAtFansListview.onRefreshComplete();
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
        adapter.notifyDataSetChanged();
        atSouListviewAdapter.notifyDataSetChanged();
        String uid="";
        String uid1="";
        Log.i("atposition", "onItemClick: "+(position-1));
        SparseBooleanArray booleanArray = mAtFansListview.getRefreshableView().getCheckedItemPositions();
        dataBeanList.clear();

        for (int j = 0; j < booleanArray.size(); j++) {
            int key = booleanArray.keyAt(j);
            //放入SparseBooleanArray，未必选中
            if (booleanArray.get(key)) {
                //这样mAdapter.getItem(key)就是选中的项
//                            String json = new Gson ().toJson(fansdatas.get(key-1).getUserInfo());
                Atbean.DataBean dataBean2 = new Atbean.DataBean();
                if (liebiaosou==0){
                    uid = fansdatas.get(key - 1).getUid();
                    uid1  = fansdatas.get(key - 1).getUserInfo().getNickname();
                }else {
                    uid = souList.get(key - 1).getUid();
                    uid1 = souList.get(key - 1).getNickname();
                }

                dataBean2.setUid(uid);
                dataBean2.setNickname(uid1);
                dataBeanList.add(dataBean2);

            } else {
                //这里是用户刚开始选中，后取消选中的项
//                        Log.d("atFansSelectedKey", "" + key + ": false");
            }
        }

        atbean.setDataBean(dataBeanList);
        Message obtain = Message.obtain();
        obtain.arg2=12;
        obtain.obj=atbean;
        EventBus.getDefault().post(obtain);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        name="";
        fansdatas.clear();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        dataBeanList.clear();
        atbean.setDataBean(dataBeanList);
        Message obtain = Message.obtain();
        obtain.arg2=12;
        obtain.obj=atbean;
        EventBus.getDefault().post(obtain);
        refresh=new TimeSecondUtils(getContext(),mAtFansListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFansList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }

            souList.clear();
            fansdatas.clear();
            page=0;
            name=mGroupSearchKeyWord_et_sou.getText().toString();
                getFansList();


                //getsouAtList(mGroupSearchKeyWord_et_sou.getText().toString());


            return true;
        }
        return false;
    }

    private void getsouAtList(String name) {
        liebiaosou=1;
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("name", name);
        map.put("page", page + "");
        map.put("type", "0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getAtList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfans", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            AtsouBean data = new Gson().fromJson(response, AtsouBean.class);
                            if (data.getData().size() == 0) {
                                page = page - 1;
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            } else {
                                if (page == 0) {
                                    souList.addAll(data.getData());
                                    try{
                                        atSouListviewAdapter = new AtSouListviewAdapter(getContext(), souList);
                                        mAtFansListview.setAdapter(atSouListviewAdapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    souList.addAll(data.getData());
                                    atSouListviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mAtFansListview.onRefreshComplete();
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

}

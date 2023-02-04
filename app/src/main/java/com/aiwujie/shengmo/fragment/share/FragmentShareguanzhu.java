package com.aiwujie.shengmo.fragment.share;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.aiwujie.shengmo.adapter.FenxiangFansListviewAdapter;
import com.aiwujie.shengmo.adapter.FenxiangSouListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AtsouBean;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.customview.BindSvipDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.bean.DynamicMessageBean;
import com.aiwujie.shengmo.tim.bean.LiveAnchorMessageBean;
import com.aiwujie.shengmo.tim.helper.MessageHelper;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by Administrator on 2019/7/30.
 */

public class FragmentShareguanzhu extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener,TextView.OnEditorActionListener{


    PullToRefreshListView mFragmentFriendListview;
    private String type = "0";
    private int page = 0;
    private ArrayList<GzFsHyListviewData.DataBean> datas = new ArrayList<>();
    List<AtsouBean.DataBean> souList = new ArrayList<>();
    Handler handler = new Handler();
    private FenxiangFansListviewAdapter adapter;
    private TimeSecondUtils refresh;

    private boolean isReresh=true;
    private boolean isCanLoad=true;
    private int leixing;
    private String did;
    private String content;
    private String pic;
    private String svip="0";
    private String userId="";
    int liebiaosou=0;
    private FenxiangSouListviewAdapter atSouListviewAdapter;
    private InputMethodManager imm;
    private EditText mGroupSearchKeyWord_et_sou;
    String name="";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_haoyou,container,false);

        mFragmentFriendListview = view.findViewById(R.id.mFragment_friend_listview);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        atSouListviewAdapter = new FenxiangSouListviewAdapter(getContext(), souList);
        mGroupSearchKeyWord_et_sou = view.findViewById(R.id.mGroupSearchKeyWord_et_sou);
        mGroupSearchKeyWord_et_sou.setOnEditorActionListener(this);
        setData();
        setListener();
        Intent intent = getActivity().getIntent();
        leixing = intent.getIntExtra("leixing", 0);
        did = intent.getStringExtra("id");
        content = intent.getStringExtra("content");
        pic = intent.getStringExtra("pic");
        userId = intent.getStringExtra("userId");
        isSVIP();


        return view;
    }







    private void setData() {
        mFragmentFriendListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentFriendListview.setFocusable(false);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentFriendListview.setRefreshing();
            }
        },100);
    }

    private void setListener() {
        mFragmentFriendListview.setOnRefreshListener(this);
        mFragmentFriendListview.setOnItemClickListener(this);
        mFragmentFriendListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if (isCanLoad) {
                                isCanLoad = false;
                                if (isReresh) {
                                    page = page + 1;
                                    getFriendList();
                                }
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    private void getFriendList() {
        liebiaosou=0;
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        map.put("type", type);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("name", name);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollewingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfriend", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            GzFsHyListviewData data = new Gson().fromJson(response, GzFsHyListviewData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh=false;
                                }
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    datas.addAll(data.getData());
                                    int retcode = data.getRetcode();
                                    adapter = new FenxiangFansListviewAdapter(getContext(), datas);
                                    mFragmentFriendListview.setAdapter(adapter);
                                } else {
                                    datas.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        isCanLoad=true;
                        mFragmentFriendListview.onRefreshComplete();
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if (svip.equals("0")){
            BindSvipDialog.bindAlertDialog(getActivity(), "SVIP可分享至关注");
            return;
        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle("提示");
        if (leixing==1){
            if (liebiaosou==0){
                builder1.setMessage("分享动态给 "+datas.get(position-1).getUserInfo().getNickname());
            }else if (liebiaosou==1){
                builder1.setMessage("分享动态给 "+souList.get(position-1).getNickname());
            }

        }else if (leixing==2){
            if (liebiaosou==0){
                builder1.setMessage("分享同好给 "+datas.get(position-1).getUserInfo().getNickname());
            }else if (liebiaosou==1){
                builder1.setMessage("分享同好给 "+souList.get(position-1).getNickname());
            }

        } else if (leixing == 4) {
            if (liebiaosou==0){
                builder1.setMessage("分享直播给 "+datas.get(position-1).getUserInfo().getNickname());
            }else if (liebiaosou==1){
                builder1.setMessage("分享直播给 "+souList.get(position-1).getNickname());
            }
        }

        builder1.setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (leixing==1){
                    if (liebiaosou==0){
                        sendPhone(content, datas.get(position-1).getUid(),did,pic);
                    }else if (liebiaosou==1){
                        sendPhone(content, souList.get(position-1).getUid(),did,pic);
                    }

                }else if (leixing==2){
                    if (liebiaosou==0){
                        sendPhone2(content, datas.get(position-1).getUid(),did,pic);
                    }else if (liebiaosou==1){
                        sendPhone2(content, souList.get(position-1).getUid(),did,pic);
                    }
                } else if (leixing == 4) {
                    if (liebiaosou==0){
                        shareAnchor(datas.get(position-1).getUid());
                    }else if (liebiaosou==1){
                        shareAnchor(souList.get(position-1).getUid());
                    }
                }


            }
        }).create().show();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        name="";
        datas.clear();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(getContext(),mFragmentFriendListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFriendList();
            }
        }, 100);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFriendList();
            }
        }, 500);
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
            datas.clear();
            page=0;
            name=mGroupSearchKeyWord_et_sou.getText().toString();
                getFriendList();


                //getsouAtList(mGroupSearchKeyWord_et_sou.getText().toString());

            return true;
        }
        return false;
    }



    public void sendPhone(String message1,String chatUserid,String did,String pic){
//
//        final Message_Sharedynamic info =new Message_Sharedynamic();
//        info.setContent(message1);
//        info.setNewid(did);
//        info.setIcon(pic);
//        info.setUserId(userId);
//        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.PRIVATE,info);
//        message.setExtra("0");
//        RongIM.getInstance().sendMessage(message, info.toString(), null, new IRongCallback.ISendMessageCallback() {
//            @Override //表示消息添加到本地数据库
//            public void onAttached(Message message) {
//
//            }
//            @Override//消息发送成功
//            public void onSuccess(Message message) {
//                getActivity().finish();
//            }
//            @Override //消息发送失败
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

        DynamicMessageBean dynamicMessageBean = new DynamicMessageBean();
        DynamicMessageBean.ContentDictBean contentDictBean = new DynamicMessageBean.ContentDictBean();
        contentDictBean.setShareType(leixing);
        if(leixing == 1) {
            contentDictBean.setContTitle("hi,给你推荐一个动态");
        } else {
            contentDictBean.setContTitle("hi,给你推荐一位同好");
        }
        contentDictBean.setUserId(chatUserid);
        contentDictBean.setNewid(did);
        contentDictBean.setIcon(pic);
        contentDictBean.setContent(message1);
        dynamicMessageBean.setContentDict(contentDictBean);
        MessageInfo customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(dynamicMessageBean));
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, false, chatUserid, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.show(getActivity(),"分享成功");
                getActivity().finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.show(getActivity(),"分享失败");
            }
        });

    }

    public void sendPhone2(String message1,String chatUserid,String did,String pic){

//        final Message_Sharperson info =new Message_Sharperson();
//        info.setContent(message1);
//        info.setNewid(did);
//        info.setIcon(pic);
//        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.PRIVATE,info);
//        message.setExtra("0");
//        RongIM.getInstance().sendMessage(message, info.toString(), null, new IRongCallback.ISendMessageCallback() {
//            @Override //表示消息添加到本地数据库
//            public void onAttached(Message message) {
//
//            }
//            @Override//消息发送成功
//            public void onSuccess(Message message) {
//                getActivity().finish();
//            }
//            @Override //消息发送失败
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

        DynamicMessageBean dynamicMessageBean = new DynamicMessageBean();
        DynamicMessageBean.ContentDictBean contentDictBean = new DynamicMessageBean.ContentDictBean();
        contentDictBean.setShareType(leixing);
        if(leixing == 1) {
            contentDictBean.setContTitle("hi,给你推荐一个动态");
        } else {
            contentDictBean.setContTitle("hi,给你推荐一位同好");
        }
        contentDictBean.setUserId(chatUserid);
        contentDictBean.setNewid(did);
        contentDictBean.setIcon(pic);
        contentDictBean.setContent(message1);
        dynamicMessageBean.setContentDict(contentDictBean);
        MessageInfo customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(dynamicMessageBean));
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, false, chatUserid, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.show(getActivity(),"分享成功");
                getActivity().finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.show(getActivity(),"分享失败");
            }
        });

    }
    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            svip = data.getData().getSvip();
                            PrintLogUtils.log(svip, "--");
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
                                        atSouListviewAdapter = new FenxiangSouListviewAdapter(getContext(), souList);
                                        mFragmentFriendListview.setAdapter(atSouListviewAdapter);
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
                        mFragmentFriendListview.onRefreshComplete();
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

    public void shareAnchor(String userId) {
        LiveAnchorMessageBean anchorMessageBean = new LiveAnchorMessageBean();
        LiveAnchorMessageBean.ContentDictBean contentDictBean = new LiveAnchorMessageBean.ContentDictBean();
        contentDictBean.setAnchorId(getActivity().getIntent().getStringExtra("anchorId"));
        contentDictBean.setLiveTitle(getActivity().getIntent().getStringExtra("anchorName"));
        contentDictBean.setLivePoster(getActivity().getIntent().getStringExtra("anchorCover"));
        anchorMessageBean.setContentDict(contentDictBean);
        MessageInfo customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(anchorMessageBean));
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, false, userId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.show(getActivity(),"分享成功");
                getActivity().finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.show(getActivity(),"分享失败");
            }
        });
    }
}

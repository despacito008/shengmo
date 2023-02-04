package com.aiwujie.shengmo.fragment.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GroupListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
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
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by Administrator on 2019/7/30.
 */

public class FragmentSharequnzu extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener{



    PullToRefreshListView mFragmentFriendListview;
    private String type = "2";
    private int page = 0;
    Handler handler = new Handler();
    private TimeSecondUtils refresh;

    private boolean isReresh=true;
    private boolean isCanLoad=true;
    private int leixing;
    private String did;
    private String content;
    private String pic;
    private String userId="";
    private GroupListviewAdapter groupAdapter;
    List<GroupData.DataBean> groups = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_haoyou,container,false);

        mFragmentFriendListview = view.findViewById(R.id.mFragment_friend_listview);
        EditText  mGroupSearchKeyWord_et_sou = view.findViewById(R.id.mGroupSearchKeyWord_et_sou);
        mGroupSearchKeyWord_et_sou.setVisibility(View.GONE);
        setData();
        setListener();
        Intent intent = getActivity().getIntent();
        leixing = intent.getIntExtra("leixing", 0);
        did = intent.getStringExtra("id");
        content = intent.getStringExtra("content");
        pic = intent.getStringExtra("pic");
        userId = intent.getStringExtra("userId");


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
                                    getGroupList();
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

    //获取 我加入的和我创建的群组
    private void getGroupList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "5");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupListFilter, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentgroupmy", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GroupData listData = new Gson().fromJson(response, GroupData.class);
                        if (listData.getData().size() == 0) {
                            if (page != 0) {
                                isReresh = false;
                                page = page - 1;
                                ToastUtil.show(getContext(), "没有更多");
                            } else {

                            }
                        } else {
                            isReresh = true;

                            if (page == 0) {
                                groups.addAll(listData.getData());
                                int retcode = listData.getRetcode();
                                groupAdapter = new GroupListviewAdapter(getContext(), groups, retcode);
                                mFragmentFriendListview.setAdapter(groupAdapter);
                            } else {
                                groups.addAll(listData.getData());
                                groupAdapter.notifyDataSetChanged();
                            }
                        }
                        mFragmentFriendListview.onRefreshComplete();
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("提示");
        if (leixing==1){
            builder1.setMessage("分享动态给 "+groups.get(position-1).getGroupname());
        }else if (leixing==2){
            builder1.setMessage("分享同好给 "+groups.get(position-1).getGroupname());
        } else if (leixing == 4) {
            builder1.setMessage("分享直播给 " + groups.get(position-1).getGroupname());
        }

        builder1.setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (leixing==1){
                    sendPhone(content, groups.get(position-1).getGid(),did,pic);
                }else if (leixing==2){
                    sendPhone2(content, groups.get(position-1).getGid(),did,pic);
                } else if (leixing == 4) {
                    shareAnchor(groups.get(position-1).getGid());
                }
            }
        }).create().show();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        groups.clear();
        refresh=new TimeSecondUtils(getContext(),mFragmentFriendListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
            }
        }, 100);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
            }
        }, 500);
    }




    public void sendPhone(String message1,String chatUserid,String did,String pic){

//        final Message_Sharedynamic info =new Message_Sharedynamic();
//        info.setContent(message1);
//        info.setNewid(did);
//        info.setIcon(pic);
//        info.setUserId(userId);
//        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.GROUP,info);
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
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, true, chatUserid, new IUIKitCallBack() {
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
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.GROUP,info);
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
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, true, chatUserid, new IUIKitCallBack() {
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

    public void shareAnchor(String groupId) {
        LiveAnchorMessageBean anchorMessageBean = new LiveAnchorMessageBean();
        LiveAnchorMessageBean.ContentDictBean contentDictBean = new LiveAnchorMessageBean.ContentDictBean();
        contentDictBean.setAnchorId(getActivity().getIntent().getStringExtra("anchorId"));
        contentDictBean.setLiveTitle(getActivity().getIntent().getStringExtra("anchorName"));
        contentDictBean.setLivePoster(getActivity().getIntent().getStringExtra("anchorCover"));
        anchorMessageBean.setContentDict(contentDictBean);
        MessageInfo customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(anchorMessageBean));
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, true, groupId, new IUIKitCallBack() {
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

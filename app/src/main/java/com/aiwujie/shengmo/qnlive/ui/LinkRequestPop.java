package com.aiwujie.shengmo.qnlive.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RecyclerViewAdapter;
import com.aiwujie.shengmo.qnlive.adapter.RequestLinkAdapter;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

public class LinkRequestPop extends BasePopupWindow {
    Context context;
    List<String> userList;

    RecyclerView rvLinkList;
    SmartRefreshLayout smartLinkList;

    public LinkRequestPop(Context context, List<String> userList) {
        super(context);
        this.context = context;
        this.userList = userList;
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.app_pop_link_request);
    }

    void initView() {
        rvLinkList = findViewById(R.id.rv_link_request);
        smartLinkList = findViewById(R.id.smart_refresh_link_request);
        getLinkUserList();
    }

    void getLinkUserList() {
        V2TIMManager.getInstance().getUsersInfo(userList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> userFullInfoList) {
                rvLinkList.setVisibility(View.VISIBLE);
                RequestLinkAdapter requestLinkAdapter = new RequestLinkAdapter(context,userFullInfoList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                rvLinkList.setAdapter(requestLinkAdapter);
                rvLinkList.setLayoutManager(layoutManager);
                requestLinkAdapter.setOnRequestLinkListener(new RequestLinkAdapter.onRequestLinkListener() {
                    @Override
                    public void doLinkAgree(String uid) {
                        if (onLinkPopListener != null) {
                            onLinkPopListener.doLinkAgree(uid);
                        }
                    }

                    @Override
                    public void doLinkReject(String uid) {
                        if (onLinkPopListener != null) {
                            onLinkPopListener.doLinkReject(uid);
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                //ToastUtil.show(context,i+s);
                ToastUtil.show(context,"暂无连麦用户");
                rvLinkList.setVisibility(View.GONE);
            }
        });
    }

    public interface OnLinkPopListener {
        void doLinkAgree(String uid);
        void doLinkReject(String uid);
    }

    OnLinkPopListener onLinkPopListener;

    public void setOnLinkPopListener(OnLinkPopListener onLinkPopListener) {
        this.onLinkPopListener = onLinkPopListener;
    }

    public void refreshData(List<String> userList) {
        this.userList = userList;

        getLinkUserList();
    }
}

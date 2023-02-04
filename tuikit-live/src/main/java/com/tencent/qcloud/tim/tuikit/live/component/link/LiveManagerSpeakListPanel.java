package com.tencent.qcloud.tim.tuikit.live.component.link;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.link.adapter.SpeakManagerAdapter;

import java.util.List;

public class LiveManagerSpeakListPanel extends BottomSheetDialog {
    List<String> managerList;
    RecyclerView rvSpeakManager;
    Context context;
    public LiveManagerSpeakListPanel(@NonNull Context context, List<String> liveManagerList) {
        super(context);
        //super(context, R.style.live_action_sheet_theme);
        this.context = context;
        setContentView(R.layout.live_view_manager_speak_list);
       this.managerList = liveManagerList;
       rvSpeakManager = findViewById(R.id.rv_live_manager_speak_list);
       getManagerData();
    }

    void getManagerData() {
        V2TIMManager.getInstance().getUsersInfo(managerList, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMUserFullInfo> userFullInfoList) {
                SpeakManagerAdapter adapter = new SpeakManagerAdapter(context,userFullInfoList);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                rvSpeakManager.setAdapter(adapter);
                rvSpeakManager.setLayoutManager(manager);
                adapter.setOnItemSpeakManagerListener(new SpeakManagerAdapter.OnItemSpeakManagerListener() {
                    @Override
                    public void doItemClick(String uid) {
                        if (onItemSpeakManagerListener != null) {
                            onItemSpeakManagerListener.doItemClick(uid);
                        }
                    }

                    @Override
                    public void doItemClose(String uid) {
                        if (onItemSpeakManagerListener != null) {
                            onItemSpeakManagerListener.doItemClose(uid);
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    SpeakManagerAdapter.OnItemSpeakManagerListener onItemSpeakManagerListener;

    public void setOnItemSpeakManagerListener(SpeakManagerAdapter.OnItemSpeakManagerListener onItemSpeakManagerListener) {
        this.onItemSpeakManagerListener = onItemSpeakManagerListener;
    }
}

package com.aiwujie.shengmo.fragment.at;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.InviteChat3Adapter;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.bean.ConversationListData;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/6/6.
 */

public class ShareChatPeopleFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.mFragment_invite_chat_listview)
    ListView mFragmentInviteChatListview;
    private List<ConversationListData> conversationListDatas;
    private InviteChat3Adapter adapter;
    public static String chatUidstr = "";
    public static String chatGroupIdstr = "";
    List<Atbean.DataBean> dataBeanList = new ArrayList<>();
    Atbean atbean = new Atbean();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite2_chat, null);
        ButterKnife.bind(this, view);
        setData();
        setListener();
        return view;
    }

    private void setData() {
        conversationListDatas = new ArrayList<>();
        //mFragmentInviteChatListview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        //mFragmentInviteChatListview.getCheckedItemPositions();

        V2TIMManager.getConversationManager().getConversationList(0, Integer.MAX_VALUE, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.show(getContext(), "网络错误，请稍后再试");
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                for (V2TIMConversation c : v2TIMConversationResult.getConversationList()) {
                    if (c.getType() == V2TIMConversation.V2TIM_C2C ) {
                        if (Integer.parseInt(c.getUserID()) > 13) {
                            ConversationListData conversationListData = new ConversationListData();
                            switch (c.getType()) {
                                case V2TIMConversation.V2TIM_C2C:
                                    conversationListData.setType(1);
                                    conversationListData.setUid(c.getUserID());
                                    conversationListData.setName(c.getShowName());
                                    conversationListData.setHeadurl(c.getFaceUrl());
                                    break;
                                default:
                                    conversationListData.setType(0);
                                    break;
                            }
                            conversationListDatas.add(conversationListData);
                        }
                    } else {
                        ConversationListData conversationListData = new ConversationListData();
                        conversationListData.setType(2);
                        conversationListData.setUid(c.getGroupID());
                        conversationListData.setName(c.getShowName());
                        conversationListData.setHeadurl(c.getFaceUrl());
                        conversationListDatas.add(conversationListData);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new InviteChat3Adapter(getActivity(), conversationListDatas);
        mFragmentInviteChatListview.setAdapter(adapter);
    }

    private void setListener() {
        mFragmentInviteChatListview.setOnItemClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (conversationListDatas.get(position).isIscheck()) {
            conversationListDatas.get(position).setIscheck(false);
        } else {
            conversationListDatas.get(position).setIscheck(true);
        }
        adapter.notifyDataSetChanged();

        String uid = "";
        String uid1 = "";
        Log.i("atposition", "onItemClick: " + (position - 1));
        dataBeanList.clear();

        for (int j = 0; j < conversationListDatas.size(); j++) {
            boolean ischeck = conversationListDatas.get(j).isIscheck();
            if (ischeck) {
                Atbean.DataBean dataBean2 = new Atbean.DataBean();
                uid = conversationListDatas.get(j).getUid();
                if (conversationListDatas.get(j).getType() == 2) {
                    uid1 = "[群]" + conversationListDatas.get(j).getName();
                } else {
                    uid1 = conversationListDatas.get(j).getName();
                }
                dataBean2.setUid(uid);
                dataBean2.setNickname(uid1);
                dataBeanList.add(dataBean2);
            }
        }

        atbean.setDataBean(dataBeanList);
        Message obtain = Message.obtain();
        obtain.arg2 = 16;
        obtain.obj = atbean;
        EventBus.getDefault().post(obtain);
    }
}

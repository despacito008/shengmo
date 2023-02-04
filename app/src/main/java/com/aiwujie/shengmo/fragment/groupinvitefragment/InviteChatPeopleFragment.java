package com.aiwujie.shengmo.fragment.groupinvitefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GrouplistActivity;
import com.aiwujie.shengmo.activity.GrouptoaddActivity;
import com.aiwujie.shengmo.adapter.InviteChat2Adapter;
import com.aiwujie.shengmo.bean.ConversationListData;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 290243232 on 2017/6/6.
 */

public class InviteChatPeopleFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.mFragment_invite_chat_listview)
    ListView mFragmentInviteChatListview;
    private List<ConversationListData> conversationListDatas = new ArrayList<>();
    private InviteChat2Adapter adapter;
    public static String chatUidstr="";
    public static String chatGroupIdstr="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_chat, null);
        ButterKnife.bind(this, view);
        setData();
        setListener();
        return view;
    }

    private void setData() {
        mFragmentInviteChatListview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mFragmentInviteChatListview.getCheckedItemPositions();
        V2TIMManager.getConversationManager().getConversationList(0, Integer.MAX_VALUE, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.show(getContext(), "网络错误，请稍后再试");
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                for (V2TIMConversation c : v2TIMConversationResult.getConversationList()) {
                    if (c.getType() == V2TIMConversation.V2TIM_C2C && Integer.parseInt(c.getUserID()) > 13) {
                        ConversationListData conversationListData = new ConversationListData();
                        switch (c.getType()){
                            case V2TIMConversation.V2TIM_C2C:
                                conversationListData.setType(1);
                                conversationListData.setUid(c.getUserID());
                                break;
                            default:
                                conversationListData.setType(0);
                                break;
                        }

                        conversationListDatas.add(conversationListData);

                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter=new InviteChat2Adapter(getActivity(),conversationListDatas);
        mFragmentInviteChatListview.setAdapter(adapter);
    }

    private void setListener() {
        mFragmentInviteChatListview.setOnItemClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (null !=adapter){
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (GrouplistActivity.yiyouhaoyou.contains(conversationListDatas.get(position).getUid())){

        }else {
            if (conversationListDatas.get(position).isIscheck()){
                conversationListDatas.get(position).setIscheck(false);
                GrouptoaddActivity.inviteUids.remove(conversationListDatas.get(position).getUid());
            }else {
                conversationListDatas.get(position).setIscheck(true);
                GrouptoaddActivity.inviteUids.add(conversationListDatas.get(position).getUid());
            }
            adapter.notifyDataSetChanged();
        }
    }
}

package com.aiwujie.shengmo.fragment.groupinvitefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.InviteGroupMemberActivity;
import com.aiwujie.shengmo.adapter.InviteChatAdapter;
import com.aiwujie.shengmo.utils.LogUtil;
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

public class InviteChatFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.mFragment_invite_chat_listview)
    ListView mFragmentInviteChatListview;
    private List<V2TIMConversation> conversationListDatas;
    private InviteChatAdapter adapter;
    public static String chatUidstr = "";
    public static String chatGroupIdstr = "";

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
        final String groupId = getArguments().getString("groupId");
        mFragmentInviteChatListview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mFragmentInviteChatListview.getCheckedItemPositions();
        conversationListDatas = new ArrayList<>();
        adapter = new InviteChatAdapter(getActivity(), conversationListDatas);
        mFragmentInviteChatListview.setAdapter(adapter);
        V2TIMManager.getConversationManager().getConversationList(0, Integer.MAX_VALUE, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.show(getContext(), "网络错误，请稍后再试");
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                for (V2TIMConversation c : v2TIMConversationResult.getConversationList()) {
                    if (c.getType() == V2TIMConversation.V2TIM_GROUP || Integer.parseInt(c.getUserID()) > 13) {
                        if (c.getType() == V2TIMConversation.V2TIM_GROUP && !TextUtils.isEmpty(groupId) && groupId.equals(c.getGroupID())) {
                            continue;
                        }
                        conversationListDatas.add(c);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void setListener() {
        mFragmentInviteChatListview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (conversationListDatas.get(position).getType() == 2) {
            if (mFragmentInviteChatListview.isItemChecked(position)) {
                if ((InviteGroupMemberActivity.inviteGroupCount + InviteGroupMemberActivity.inviteMemberCount) > 19) {
                    mFragmentInviteChatListview.setItemChecked(position, false);
                    ToastUtil.show(getActivity().getApplicationContext(), "最多选择20个...");
                } else {
                    InviteGroupMemberActivity.inviteGroupCount = InviteGroupMemberActivity.inviteGroupCount + 1;
                }
            } else {
                InviteGroupMemberActivity.inviteGroupCount = InviteGroupMemberActivity.inviteGroupCount - 1;
            }
        } else {
            if (mFragmentInviteChatListview.isItemChecked(position)) {
                if (!InviteGroupMemberActivity.inviteUids.contains(conversationListDatas.get(position).getUserID())) {
                    if ((InviteGroupMemberActivity.inviteGroupCount + InviteGroupMemberActivity.inviteMemberCount) > 19) {
                        mFragmentInviteChatListview.setItemChecked(position, false);
                        ToastUtil.show(getActivity().getApplicationContext(), "最多选择20个...");
                    } else {
                        InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount + 1;
                        InviteGroupMemberActivity.inviteUids.add(conversationListDatas.get(position).getUserID());
                    }
                } else {
                    mFragmentInviteChatListview.setItemChecked(position, false);
                    ToastUtil.show(getActivity().getApplicationContext(), "请您不要重复选择...");
                }
            } else {
                InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount - 1;
                InviteGroupMemberActivity.inviteUids.remove(conversationListDatas.get(position).getUserID());
            }

        }
        adapter.notifyDataSetChanged();
        //多选模式下，获取listview选中的集合
        SparseBooleanArray booleanArray = mFragmentInviteChatListview.getCheckedItemPositions();
        if ((booleanArray.size() != 0)) {
            chatUidstr = "";
            chatGroupIdstr = "";
            for (int j = 0; j < booleanArray.size(); j++) {
                int key = booleanArray.keyAt(j);
                //放入SparseBooleanArray，未必选中
                if (booleanArray.get(key)) {
                    //这样mAdapter.getItem(key)就是选中的项
                    if (conversationListDatas.get(key).getType() == 1) {
                        chatUidstr += conversationListDatas.get(key).getUserID() + ",";
                    } else if (conversationListDatas.get(key).getType() == 2) {
                        chatGroupIdstr += conversationListDatas.get(key).getGroupID() + ",";
                    }
                } else {
                    //这里是用户刚开始选中，后取消选中的项
                }
            }
        }
        if (!chatUidstr.equals("")) {
            chatUidstr = chatUidstr.substring(0, chatUidstr.length() - 1);
        }
        if (!chatGroupIdstr.equals("")) {
            chatGroupIdstr = chatGroupIdstr.substring(0, chatGroupIdstr.length() - 1);
        }
        LogUtil.d(chatUidstr + "-------" + chatGroupIdstr);
    }
}

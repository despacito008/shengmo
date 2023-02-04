package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.util.SparseBooleanArray;

import com.aiwujie.shengmo.activity.InviteGroupMemberActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.bean.HomeListviewData;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by 290243232 on 2018/2/2.
 */

public class InviteMemberClickUtils {
    public static String click(Context context, PullToRefreshListView listView, List<HomeNewListData.DataBean> listData, int position){
        if (listView.getRefreshableView().isItemChecked(position)) {
            if(!InviteGroupMemberActivity.inviteUids.contains(listData.get(position-1).getUid())) {
                if ((InviteGroupMemberActivity.inviteGroupCount + InviteGroupMemberActivity.inviteMemberCount) > 19) {
                    listView.getRefreshableView().setItemChecked(position, false);
                    ToastUtil.show(context.getApplicationContext(), "最多选择20个...");
                }else if (listData.get(position-1).getUid().equals(MyApp.uid)){
                    listView.getRefreshableView().setItemChecked(position, false);
                    ToastUtil.show(context.getApplicationContext(), "不能选择自己...");
                } else {
                    InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount + 1;
                    InviteGroupMemberActivity.inviteUids.add(listData.get(position-1).getUid());
                }
            } else {
                listView.getRefreshableView().setItemChecked(position, false);
                ToastUtil.show(context.getApplicationContext(), "请您不要重复选择...");
            }
        }else{
            InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount - 1;
            InviteGroupMemberActivity.inviteUids.remove(listData.get(position-1).getUid());
        }
        //多选模式下，获取listview选中的集合
        SparseBooleanArray booleanArray = listView.getRefreshableView().getCheckedItemPositions();
        String uidstr = "";
        if ((booleanArray.size() != 0)) {
            for (int j = 0; j < booleanArray.size(); j++) {
                int key = booleanArray.keyAt(j);
                //放入SparseBooleanArray，未必选中
                if (booleanArray.get(key)) {
                    //这样mAdapter.getItem(key)就是选中的项
                    uidstr+=listData.get(key-1).getUid()+",";
                } else {
                    //这里是用户刚开始选中，后取消选中的项
//                        Log.d("atFansSelectedKey", "" + key + ": false");
                }
            }
            if(!uidstr.equals("")){
                uidstr=uidstr.substring(0, uidstr.length() - 1);
            }

        }
        return uidstr;
    }
    public static String clicks(Context context, PullToRefreshListView listView, List<GzFsHyListviewData.DataBean> listData, int position){
        if (listView.getRefreshableView().isItemChecked(position)) {
            if(!InviteGroupMemberActivity.inviteUids.contains(listData.get(position-1).getUid())) {
                if ((InviteGroupMemberActivity.inviteGroupCount + InviteGroupMemberActivity.inviteMemberCount) > 19) {
                    listView.getRefreshableView().setItemChecked(position, false);
                    ToastUtil.show(context.getApplicationContext(), "最多选择20个...");
                } else {
                    InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount + 1;
                    InviteGroupMemberActivity.inviteUids.add(listData.get(position-1).getUid());
                }
            } else {
                listView.getRefreshableView().setItemChecked(position, false);
                ToastUtil.show(context.getApplicationContext(), "请您不要重复选择...");
            }
        }else{
            InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount - 1;
            InviteGroupMemberActivity.inviteUids.remove(listData.get(position-1).getUid());
        }
        //多选模式下，获取listview选中的集合
        SparseBooleanArray booleanArray = listView.getRefreshableView().getCheckedItemPositions();
        String uidstr = "";
        if ((booleanArray.size() != 0)) {
            for (int j = 0; j < booleanArray.size(); j++) {
                int key = booleanArray.keyAt(j);
                //放入SparseBooleanArray，未必选中
                if (booleanArray.get(key)) {
                    //这样mAdapter.getItem(key)就是选中的项
                    uidstr+=listData.get(key-1).getUid()+",";
                } else {
                    //这里是用户刚开始选中，后取消选中的项
//                        Log.d("atFansSelectedKey", "" + key + ": false");
                }
            }
            if(!uidstr.equals("")){
                uidstr=uidstr.substring(0, uidstr.length() - 1);
            }

        }
        return uidstr;
    }
    public static String clickss(Context context, PullToRefreshListView listView, List<HomeListviewData.DataBean> listData, int position){
        if (listView.getRefreshableView().isItemChecked(position)) {
            if(!InviteGroupMemberActivity.inviteUids.contains(listData.get(position-1).getUid())) {
                if ((InviteGroupMemberActivity.inviteGroupCount + InviteGroupMemberActivity.inviteMemberCount) > 19) {
                    listView.getRefreshableView().setItemChecked(position, false);
                    ToastUtil.show(context.getApplicationContext(), "最多选择20个...");
                } else {
                    InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount + 1;
                    InviteGroupMemberActivity.inviteUids.add(listData.get(position-1).getUid());
                }
            } else {
                listView.getRefreshableView().setItemChecked(position, false);
                ToastUtil.show(context.getApplicationContext(), "请您不要重复选择...");
            }
        }else{
            InviteGroupMemberActivity.inviteMemberCount = InviteGroupMemberActivity.inviteMemberCount - 1;
            InviteGroupMemberActivity.inviteUids.remove(listData.get(position-1).getUid());
        }
        //多选模式下，获取listview选中的集合
        SparseBooleanArray booleanArray = listView.getRefreshableView().getCheckedItemPositions();
        String uidstr = "";
        if ((booleanArray.size() != 0)) {
            for (int j = 0; j < booleanArray.size(); j++) {
                int key = booleanArray.keyAt(j);
                //放入SparseBooleanArray，未必选中
                if (booleanArray.get(key)) {
                    //这样mAdapter.getItem(key)就是选中的项
                    uidstr+=listData.get(key-1).getUid()+",";
                } else {
                    //这里是用户刚开始选中，后取消选中的项
//                        Log.d("atFansSelectedKey", "" + key + ": false");
                }
            }
            if(!uidstr.equals("")){
                uidstr=uidstr.substring(0, uidstr.length() - 1);
            }

        }
        return uidstr;
    }
}

package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ConversationListData;
import com.aiwujie.shengmo.bean.GroupUserInfoData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class InviteChat3Adapter extends BaseAdapter {
    private Context context;
    private List<ConversationListData> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();

    public InviteChat3Adapter(Context context, List<ConversationListData> list) {
        super();
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_invite_chat, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ConversationListData data = list.get(position);
//        if (data.getType() == 1) {
//            Map<String, String> map = new HashMap<>();
//            map.put("uid", data.getUid());
//            IRequestManager manager = RequestFactory.getRequestManager();
//            manager.post(HttpUrl.GetHeadAndNicknameNew, map, new IRequestCallback() {
//                @Override
//                public void onSuccess(final String response) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                JSONObject json = new JSONObject(response);
//                                if (json.getInt("retcode") == 2000) {
//                                    JSONObject obj = json.getJSONObject("data");
//                                    String headpic = obj.getString("head_pic");
//                                    String nickname = obj.getString("nickname");
//                                    if (headpic.equals("") || headpic.equals(NetPic())) {//"http://59.110.28.150:888/"
//                                        holder.itemListviewInviteChatIcon.setImageResource(R.mipmap.morentouxiang);
//                                    } else {
//                                        GlideImgManager.glideLoader(context, headpic, R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewInviteChatIcon, 0);
//                                    }
//                                    data.setName(nickname + "");
//                                    holder.itemListviewInviteChatName.setText(nickname);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//
//                }
//            });
//        } else if (data.getType() == 2) {
//            Map<String, String> map = new HashMap<>();
//            map.put("gid", data.getUid());
//            IRequestManager manager = RequestFactory.getRequestManager();
//            manager.post(HttpUrl.GroupUserInfo, map, new IRequestCallback() {
//                @Override
//                public void onSuccess(final String response) {
//                    try {
//                        GroupUserInfoData data = new Gson().fromJson(response, GroupUserInfoData.class);
//                        if (data.getRetcode() == 2000) {
//                            if (data.getData() != null) {
//                                String groupname = data.getData().getGroupname();
//                                String groupicon = data.getData().getGroup_pic();
//                                if (groupicon.equals("") || groupicon.equals(NetPic())) {//"http://59.110.28.150:888/"
//                                    holder.itemListviewInviteChatIcon.setImageResource(R.mipmap.morentouxiang);
//                                } else {
//                                    GlideImgManager.glideLoader(context, groupicon, R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewInviteChatIcon, 0);
//                                }
//                                holder.itemListviewInviteChatName.setText(groupname);
//                            }
//                        }
//                    } catch (JsonSyntaxException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Throwable throwable) {
//
//                }
//            });
//        }
        String content = data.getLastContent();
        holder.itemListviewInviteChatSign.setText(content);
        GlideImgManager.glideLoader(context, data.getHeadurl(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewInviteChatIcon, 0);
        if (data.getType() == 2) {
            holder.itemListviewInviteChatName.setText("[群] " + data.getName());
        } else {
            holder.itemListviewInviteChatName.setText(data.getName());
        }

        if (data.isIscheck()) {
            holder.itemNearListviewFlag.setImageResource(R.mipmap.atxuanzhong);
        } else {
            holder.itemNearListviewFlag.setImageResource(R.mipmap.atweixuanzhong);
        }


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_near_listview_flag)
        ImageView itemNearListviewFlag;
        @BindView(R.id.item_listview_invite_chat_icon)
        ImageView itemListviewInviteChatIcon;
        @BindView(R.id.item_listview_invite_chat_name)
        TextView itemListviewInviteChatName;
        @BindView(R.id.item_listview_invite_chat_sign)
        TextView itemListviewInviteChatSign;
        @BindView(R.id.auto_auto)
        AutoLinearLayout autoLinearLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}



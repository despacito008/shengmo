package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
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
public class InviteChatAdapter extends BaseAdapter {
    private Context context;
    private List<V2TIMConversation> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();

    public InviteChatAdapter(Context context, List<V2TIMConversation> list) {
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
        final V2TIMConversation data = list.get(position);
            if (TextUtils.isEmpty(data.getFaceUrl())) {//"http://59.110.28.150:888/"
                holder.itemListviewInviteChatIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, data.getFaceUrl(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewInviteChatIcon, 0);
            }
            holder.itemListviewInviteChatName.setText(data.getShowName());
      /*  String content=data.getLastMessage().getTextElem().getText();
        holder.itemListviewInviteChatSign.setText(content);*/
        if (((ListView) parent).isItemChecked(position)) {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}



package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.CommentDetailActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.CommentData;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.LinkMovementMethodOverride;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.aiwujie.shengmo.zdyview.ATSpan;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class CommentAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<CommentData.DataBean> list;
    private LayoutInflater inflater;
    private String duid;
    private String did;
    private int dynamicPos;
    private String pingluncount;
    Handler handler = new Handler();

    public CommentAdapter(Context context, List<CommentData.DataBean> list, String duid, String did, int dynamicPos, String pingluncount) {
        this.context = context;
        this.list = list;
        this.duid = duid;
        this.did = did;
        this.dynamicPos = dynamicPos;
        this.pingluncount = pingluncount;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommentData.DataBean data = list.get(position);
//        if (data.getHead_pic().equals("") || data.getHead_pic().equals("http://59.110.28.150:888/")) {
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {
            holder.itemListviewCommentIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewCommentIcon, 0);
        }
        holder.itemListviewCommentName.setText(data.getNickname());
        if (data.getOtheruid().equals("0")) {
            SpannableString spanableInfo = new SpannableString(data.getContent());
            if (!"".equals(data.getAtuname())&&null!= data.getAtuname() && !"".equals(data.getAtuid())&&null!= data.getAtuid()){
                holder.itemListviewArguementContent.setOnTouchListener(new LinkMovementMethodOverride());
                String[] split = data.getAtuid().split(",");
                String[] split1 = data.getAtuname().split(",");
                for (int i = 0; i <split1.length; i++) {
                    if (split1[i].trim().contains("*")){
                        split1[i] =  split1[i].trim().replace("*","\\*");
                    }
                    String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                    Matcher matcher = compile.matcher(data.getContent());
                    while (matcher.find()){
                        int start = matcher.start();
                        if (split1[i].trim().contains("*")){
                            split1[i] =  split1[i].trim().replace("\\","");
                        }
                        spanableInfo.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            holder.itemListviewArguementContent.setText(spanableInfo);
        } else {
//            holder.itemListviewArguementContent.setText("回复 " +data.getOthernickname()+ ": " + data.getContent());
            SpannableStringBuilder builder = new SpannableStringBuilder("回复 " + data.getOthernickname() + ": " + data.getContent());
            ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#b73acb"));
            builder.setSpan(purSpan, 3, data.getOthernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (!"".equals(data.getAtuname())&&null!= data.getAtuname() && !"".equals(data.getAtuid())&&null!= data.getAtuid()){
                holder.itemListviewArguementContent.setOnTouchListener(new LinkMovementMethodOverride());
                String[] split = data.getAtuid().split(",");
                String[] split1 = data.getAtuname().split(",");
                for (int i = 0; i <split1.length; i++) {
                    if (split1[i].trim().contains("*")){
                        split1[i] =  split1[i].trim().replace("*","\\*");
                    }
                    String patten = Pattern.quote(split1[i].trim());
                        Pattern compile = Pattern.compile(patten);
                    Matcher matcher = compile.matcher("回复 " + data.getOthernickname() + ": " + data.getContent());
                    while (matcher.find()){
                        int start = matcher.start();
                        if (split1[i].trim().contains("*")){
                            split1[i] =  split1[i].trim().replace("\\","");
                        }
                        builder.setSpan(new ATSpan(split[i]),start,start+split1[i].trim().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            holder.itemListviewArguementContent.setText(builder);
        }
        String admin = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "admin", "0");
        String bkvip = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "bkvip", "0");
        String blvip = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "blvip", "0");
        if (admin.equals("0")&&bkvip.equals("0")&&blvip.equals("0")) {
            if (duid.equals(MyApp.uid)) {
                holder.itemListviewCommentDelete.setVisibility(View.VISIBLE);
                holder.item_listview_comment_garbage.setVisibility(View.VISIBLE);//垃圾桶的父布局
            } else {
                if (data.getUid().equals(MyApp.uid)) {
                    holder.itemListviewCommentDelete.setVisibility(View.VISIBLE);
                    holder.item_listview_comment_garbage.setVisibility(View.VISIBLE);//垃圾桶的父布局
                } else {
                    holder.itemListviewCommentDelete.setVisibility(View.GONE);
                    //holder.item_listview_comment_garbage.setVisibility(View.GONE);//垃圾桶的父布局
                }
            }
        } else {
            holder.itemListviewCommentDelete.setVisibility(View.VISIBLE);
            holder.item_listview_comment_garbage.setVisibility(View.VISIBLE);//垃圾桶的父布局
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(), data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemListviewCommentVip);
        if (data.getIs_hand().equals("0")) {
            holder.itemListviewCommentHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemListviewCommentHongniang.setVisibility(View.VISIBLE);
        }

        holder.itemListviewCommentTime.setText(data.getSendtime());
        holder.itemListviewCommentIcon.setTag(position);
        holder.itemListviewCommentIcon.setOnClickListener(this);
        holder.itemListviewCommentDelete.setTag(position);
        holder.itemListviewCommentDelete.setOnClickListener(this);

        List<CommentData.DataBean.SubsetcommentBean> subsetcomment = data.getSubsetcomment();
        if(subsetcomment == null || subsetcomment.size() == 0) {
            holder.rvChildComment.setVisibility(View.GONE);
        } else {
            holder.rvChildComment.setVisibility(View.VISIBLE);
            ChildCommentAdapter childCommentAdapter = new ChildCommentAdapter(context,subsetcomment,Integer.parseInt(data.getTwolevelcommentnum()));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            holder.rvChildComment.setAdapter(childCommentAdapter);
            holder.rvChildComment.setLayoutManager(linearLayoutManager);
            childCommentAdapter.setOnSimpleItemViewListener(new OnSimpleItemViewListener() {
                @Override
                public void onItemListener(View view) {
                    Intent intent = new Intent(context, CommentDetailActivity.class);
                    intent.putExtra("cmid",data.getCmid());
                    intent.putExtra("did",did);
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    @Override
    public void onClick(final View v) {
        final int pos = (int) v.getTag();
        Intent intent;
        switch (v.getId()) {
            case R.id.item_listview_comment_icon:
                intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(pos).getUid());
                context.startActivity(intent);
                break;
            case R.id.item_listview_comment_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确认删除吗?")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        v.setEnabled(false);
                        deleteComment(pos, dialog, (ImageView) v);
                    }
                }).create().show();
                break;
        }
    }

    private void deleteComment(final int postion, final DialogInterface dialog, final ImageView v) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did", did);
        map.put("cmid", list.get(postion).getCmid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DelComment, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("deletecomment", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    dialog.dismiss();
                                    list.remove(postion);
                                    notifyDataSetChanged();
                                    v.setEnabled(true);
                                    EventBus.getDefault().post(new DynamicCommentEvent(dynamicPos, (Integer.parseInt(pingluncount) - 1)));
                                    EventBus.getDefault().post("deleteSuccess");
                                    ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                    ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.item_listview_comment_icon)
        ImageView itemListviewCommentIcon;
        @BindView(R.id.item_listview_comment_vip)
        ImageView itemListviewCommentVip;
        @BindView(R.id.item_listview_comment_hongniang)
        ImageView itemListviewCommentHongniang;
        @BindView(R.id.item_listview_comment_name)
        TextView itemListviewCommentName;
        @BindView(R.id.item_listview_comment_time)
        TextView itemListviewCommentTime;
        @BindView(R.id.item_listview_arguement_content)
        TextView itemListviewArguementContent;
        @BindView(R.id.item_listview_comment_delete)
        ImageView itemListviewCommentDelete;
        @BindView(R.id.item_listview_comment_garbage)
        PercentLinearLayout item_listview_comment_garbage;
        @BindView(R.id.item_list_comment_layout)
        PercentLinearLayout itemView;
        @BindView(R.id.rv_item_child_comment)
        RecyclerView rvChildComment;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    interface OnCommentItemListener {
        void onItemThumbUp(int postion);
    }

    OnCommentItemListener onCommentItemListener;

    public void setOnCommentItemListener(OnCommentItemListener onCommentItemListener) {
        this.onCommentItemListener = onCommentItemListener;
    }
}

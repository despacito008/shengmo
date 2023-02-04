package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.bean.FriendData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class FriendListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<FriendData.DataBean> list;
    private LayoutInflater inflater;
    private int retcode;
    private Handler handler = new Handler();

    public FriendListviewAdapter(Context context, List<FriendData.DataBean> list, int retcode) {
        super();
        this.context = context;
        this.list = list;
        this.retcode = retcode;
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
            convertView = inflater.inflate(R.layout.item_near_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FriendData.DataBean data = list.get(position);
        if (TextUtil.isEmpty(list.get(position).getUserInfo().getHead_pic()) ||NetPic().equals(list.get(position).getUserInfo().getHead_pic()) ) {//"http://59.110.28.150:888/"
            holder.itemNearListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, list.get(position).getUserInfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemNearListviewIcon, 0);
        }
       /* if (!"".equals(data.getUserInfo().getIntroduce())) {
            holder.itemNearListviewSign.setText(data.getUserInfo().getIntroduce());
        } else {
            holder.itemNearListviewSign.setText("(用户未设置签名)");
        }*/
        if (!TextUtil.isEmpty(data.getUserInfo().getLmarkname())){
            holder.itemNearListviewSign.setText(data.getUserInfo().getLmarkname()+"");
        }else {
            holder.itemNearListviewSign.setText("");
        }

//        holder.itemNearListviewIcon.setImageURI(Uri.parse(data.getHead_pic()));
        if (retcode == 2000) {
            if ("1".equals(data.getUserInfo().getLocation_city_switch())){
                holder.itemNearListviewDistance.setText("隐身");
            }else {

                if(TextUtil.isEmpty(data.getUserInfo().getCity()) &&TextUtil.isEmpty(data.getUserInfo().getProvince())){
                    holder.itemNearListviewDistance.setText("隐身");
                }else {
                    if(data.getUserInfo().getProvince().equals(data.getUserInfo().getCity())){
                        holder.itemNearListviewDistance.setText(data.getUserInfo().getCity());
                    }else{
                        holder.itemNearListviewDistance.setText(data.getUserInfo().getProvince()+" "+data.getUserInfo().getCity());
                    }
                }
            }
            holder.itemNearListviewDistance.setVisibility(View.VISIBLE);
        } else {
            holder.itemNearListviewDistance.setVisibility(View.INVISIBLE);
        }
        holder.itemNearListviewSex.setText(data.getUserInfo().getAge());
        if (data.getUserInfo().getOnlinestate() == 0) {
            holder.itemNearListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewOnline.setVisibility(View.VISIBLE);
        }
//        if (data.getUserInfo().getVip().equals("0")) {
//            holder.itemNearListviewVip.setVisibility(View.INVISIBLE);
//        } else {
//            holder.itemNearListviewVip.setVisibility(View.VISIBLE);
//        }
        holder.itemNearListviewVip.setVisibility(View.INVISIBLE);
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context,data.getUserInfo().getHead_pic(), data.getUid(), data.getUserInfo().getIs_volunteer(), data.getUserInfo().getIs_admin(), data.getUserInfo().getSvipannual(), data.getUserInfo().getSvip(), data.getUserInfo().getVipannual(), data.getUserInfo().getVip(),data.getUserInfo().getBkvip(),data.getUserInfo().getBlvip(), holder.itemNearListviewVip);

        if ("0".equals(data.getUserInfo().getIs_hand())) {
            holder.itemNearListviewHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemNearListviewHongniang.setVisibility(View.VISIBLE);
        }
        if ("0".equals(data.getUserInfo().getRealname())) {
            holder.itemNearListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemNearListviewShiming.setVisibility(View.VISIBLE);
        }

        if ("1".equals(data.getUserInfo().getSex()) ){
            holder.itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("2".equals(data.getUserInfo().getSex())) {
            holder.itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if ("3".equals(data.getUserInfo().getSex())) {
            holder.itemNearListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemNearListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if ("S".equals(data.getUserInfo().getRole())) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemNearListviewRole.setText("斯");
        } else if ("M".equals(data.getUserInfo().getRole())) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemNearListviewRole.setText("慕");
        } else if ("SM".equals(data.getUserInfo().getRole())) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemNearListviewRole.setText("双");
        } else if ("~".equals(data.getUserInfo().getRole())) {
            holder.itemNearListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemNearListviewRole.setText("~");
        }
        if (!"0".equals(data.getUserInfo().getCharm_val())) {
            holder.itemNearListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemNearListviewBeautyCount.setText(data.getUserInfo().getCharm_val());
        } else {
            holder.itemNearListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!"0".equals(data.getUserInfo().getWealth_val())) {
            holder.itemNearListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemNearListviewRichCount.setText(data.getUserInfo().getWealth_val());
        } else {
            holder.itemNearListviewLlRichCount.setVisibility(View.GONE);
        }
        holder.itemNearListviewName.setText(data.getUserInfo().getNickname());
        if (TextUtil.isEmpty(data.getUserInfo().getMarkname())){
            holder.itemNearListviewName.setText(data.getUserInfo().getNickname()+"");
        }else {
            holder.itemNearListviewName.setText(data.getUserInfo().getMarkname()+"");
        }
        holder.itemNearListviewTime.setText(data.getUserInfo().getLast_login_time());
        holder.itemNearListviewTime.setVisibility(View.GONE);
        holder.itemNearListviewShiming.setTag(position);
        holder.itemNearListviewShiming.setOnClickListener(this);
        return convertView;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.item_near_listview_shiming:
                intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
                break;
        }
    }


    static class ViewHolder {
        @BindView(R.id.item_near_listview_icon)
        ImageView itemNearListviewIcon;
        @BindView(R.id.item_near_listview_vip)
        ImageView itemNearListviewVip;
        @BindView(R.id.item_near_listview_hongniang)
        ImageView itemNearListviewHongniang;
        @BindView(R.id.item_near_listview_flag)
        TextView itemNearListviewFlag;
        @BindView(R.id.item_near_listview_name)
        TextView itemNearListviewName;
        @BindView(R.id.item_near_listview_shiming)
        ImageView itemNearListviewShiming;
        @BindView(R.id.item_near_listview_online)
        ImageView itemNearListviewOnline;
        @BindView(R.id.item_near_listview_distance)
        TextView itemNearListviewDistance;
        @BindView(R.id.item_near_listview_time)
        TextView itemNearListviewTime;
        @BindView(R.id.item_near_listview_Sex)
        TextView itemNearListviewSex;
        @BindView(R.id.item_near_listview_role)
        TextView itemNearListviewRole;
        @BindView(R.id.item_near_listview_richCount)
        TextView itemNearListviewRichCount;
        @BindView(R.id.item_near_listview_ll_richCount)
        AutoLinearLayout itemNearListviewLlRichCount;
        @BindView(R.id.item_near_listview_beautyCount)
        TextView itemNearListviewBeautyCount;
        @BindView(R.id.item_near_listview_ll_beautyCount)
        AutoLinearLayout itemNearListviewLlBeautyCount;
        @BindView(R.id.item_near_listview_banSomeOne)
        ImageView itemNearListviewBanSomeOne;
        @BindView(R.id.item_near_listview_sign)
        TextView itemNearListviewSign;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}



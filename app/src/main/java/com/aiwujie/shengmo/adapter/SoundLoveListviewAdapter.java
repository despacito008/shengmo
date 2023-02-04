package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.SoundLoveData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.MediaPlayerManager;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UserIdentityUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class SoundLoveListviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<SoundLoveData.DataBean> list;
    private LayoutInflater inflater;
    public int selectIndex = -1;
    public int currentIndex = -1;

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public SoundLoveListviewAdapter(Context context, List<SoundLoveData.DataBean> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_soundlove_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SoundLoveData.DataBean data = list.get(position);
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemSoundloveListviewIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemSoundloveListviewIcon, 0);
        }
        holder.itemSoundloveListviewSex.setText(data.getAge());
        if (data.getOnlinestate() == 0) {
            holder.itemSoundloveListviewOnline.setVisibility(View.GONE);
        } else {
            holder.itemSoundloveListviewOnline.setVisibility(View.VISIBLE);
        }
        //  判断显示标识，志愿者>官方管理>年svip>svip>年vip>vip
        UserIdentityUtils.showIdentity(context, data.getHead_pic(),data.getUid(), data.getIs_volunteer(), data.getIs_admin(), data.getSvipannual(), data.getSvip(), data.getVipannual(), data.getVip(),data.getBkvip(),data.getBlvip(), holder.itemSoundloveListviewVip);
        if (data.getIs_hand().equals("0")) {
            holder.itemSoundloveListviewHongniang.setVisibility(View.INVISIBLE);
        } else {
            holder.itemSoundloveListviewHongniang.setVisibility(View.VISIBLE);
        }
        if (data.getRealname().equals("0")) {
            holder.itemSoundloveListviewShiming.setVisibility(View.GONE);
        } else {
            holder.itemSoundloveListviewShiming.setVisibility(View.VISIBLE);
        }

        if (data.getSex().equals("1")) {
            holder.itemSoundloveListviewSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemSoundloveListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemSoundloveListviewSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemSoundloveListviewSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemSoundloveListviewSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemSoundloveListviewSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemSoundloveListviewRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemSoundloveListviewRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemSoundloveListviewRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemSoundloveListviewRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemSoundloveListviewRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemSoundloveListviewRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemSoundloveListviewRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemSoundloveListviewRole.setText("~");
        }
        holder.itemSoundloveListviewName.setText(data.getNickname());

        if(data.getCity().equals("")&&data.getProvince().equals("")){
            holder.itemSoundloveListviewSign.setText("未知");
        }else {
            if (!data.getCity().equals("") && data.getCity() != null) {
                holder.itemSoundloveListviewSign.setText(data.getCity());
            } else {
                holder.itemSoundloveListviewSign.setText(data.getProvince());
            }
        }

        if (!data.getCharm_val().equals("0")) {
            holder.itemSoundloveListviewLlBeautyCount.setVisibility(View.VISIBLE);
            holder.itemSoundloveListviewBeautyCount.setText(data.getCharm_val());
        } else {
            holder.itemSoundloveListviewLlBeautyCount.setVisibility(View.GONE);
        }

        if (!data.getWealth_val().equals("0")) {
            holder.itemSoundloveListviewLlRichCount.setVisibility(View.VISIBLE);
            holder.itemSoundloveListviewRichCount.setText(data.getWealth_val());
        } else {
            holder.itemSoundloveListviewLlRichCount.setVisibility(View.GONE);
        }
//        if(!data.getMediaalong().equals("0")) {
//            holder.mSoundLoveMeidaTime.setVisibility(View.VISIBLE);
//            holder.mSoundLoveMeidaTime.setText(data.getMediaalong()+"\"");
//        }else{
//            holder.mSoundLoveMeidaTime.setVisibility(View.GONE);
//        }
        if (selectIndex == position) {
            holder.mSoundLoveMeida.setImageResource(R.mipmap.yuyintiaostop);
            holder.mSoundLoveMeidaTime.setVisibility(View.GONE);
            holder.mSoundLoveMeidaAnni.setVisibility(View.VISIBLE);
            holder.mSoundLoveMeidaAnni.setImageResource(R.drawable.animation_voice);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.mSoundLoveMeidaAnni.getDrawable();
            animationDrawable.start();
        } else {
            holder.mSoundLoveMeida.setImageResource(R.mipmap.yuyintiao);
            holder.mSoundLoveMeidaAnni.setVisibility(View.GONE);
            if (!data.getMediaalong().equals("0")) {
                holder.mSoundLoveMeidaTime.setVisibility(View.VISIBLE);
                if(data.getMediaalong().equals("11")){
                    holder.mSoundLoveMeidaTime.setText("10\"");
                }else {
                    holder.mSoundLoveMeidaTime.setText(data.getMediaalong() + "\"");
                }
            } else {
                holder.mSoundLoveMeidaTime.setVisibility(View.GONE);
            }
        }
        holder.mSoundLoveMeidaAddTime.setText("发布时间：" + data.getUpdate_media_time());
        holder.itemSoundloveListviewIcon.setTag(position);
        holder.itemSoundloveListviewIcon.setOnClickListener(this);
        holder.mSoundLoveMeida.setTag(position);
        holder.mSoundLoveMeida.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        final int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.item_soundlove_listview_icon:
                Intent intent = new Intent(context, PesonInfoActivity.class);
                intent.putExtra("uid", list.get(position).getUid());
                context.startActivity(intent);
                break;
            case R.id.mSound_love_meida:
                //判断是否是当前点击的是否是上一次的
                if (currentIndex == position) {
                    currentIndex = -1;
                } else {
                    ToastUtil.show(context.getApplicationContext(), "正在缓冲，请稍后...");
                    currentIndex = position;
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(context);
                        if (currentIndex == -1) {
                            mediaPlayerManager.stop();
                        } else {
                            mediaPlayerManager.start(list.get(currentIndex).getMedia());
                        }
//                        String dura = String.valueOf((int) Math.floor(Double.valueOf(duration) / 1000));
//                        Log.i("timeduration", "run: " + dura);
                    }
                }.start();
                setSelectIndex(currentIndex);
                notifyDataSetChanged();
                break;
        }
    }


    static class ViewHolder {
        @BindView(R.id.item_soundlove_listview_icon)
        ImageView itemSoundloveListviewIcon;
        @BindView(R.id.item_soundlove_listview_vip)
        ImageView itemSoundloveListviewVip;
        @BindView(R.id.item_soundlove_listview_hongniang)
        ImageView itemSoundloveListviewHongniang;
        @BindView(R.id.item_soundlove_listview_name)
        TextView itemSoundloveListviewName;
        @BindView(R.id.item_soundlove_listview_shiming)
        ImageView itemSoundloveListviewShiming;
        @BindView(R.id.item_soundlove_listview_online)
        ImageView itemSoundloveListviewOnline;
        @BindView(R.id.item_soundlove_listview_Sex)
        TextView itemSoundloveListviewSex;
        @BindView(R.id.item_soundlove_listview_role)
        TextView itemSoundloveListviewRole;
        @BindView(R.id.item_soundlove_listview_richCount)
        TextView itemSoundloveListviewRichCount;
        @BindView(R.id.item_soundlove_listview_ll_richCount)
        AutoLinearLayout itemSoundloveListviewLlRichCount;
        @BindView(R.id.item_soundlove_listview_beautyCount)
        TextView itemSoundloveListviewBeautyCount;
        @BindView(R.id.item_soundlove_listview_ll_beautyCount)
        AutoLinearLayout itemSoundloveListviewLlBeautyCount;
        @BindView(R.id.item_soundlove_listview_sign)
        TextView itemSoundloveListviewSign;
        @BindView(R.id.mSound_love_meida)
        ImageView mSoundLoveMeida;
        @BindView(R.id.mSound_love_meidaTime)
        TextView mSoundLoveMeidaTime;
        @BindView(R.id.mSound_love_meidaAnni)
        ImageView mSoundLoveMeidaAnni;
        @BindView(R.id.mSound_love_meidaAddTime)
        TextView mSoundLoveMeidaAddTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}



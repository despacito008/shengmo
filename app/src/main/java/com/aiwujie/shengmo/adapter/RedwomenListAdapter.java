package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.RedWomenIntroData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class RedwomenListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<RedWomenIntroData.DataBean> list;
    private LayoutInflater inflater;
    private String admin;
    Handler handler = new Handler();
    //声明接口对象
    public OnRedwomenClickListener mClickListener;

    //创建接口
    public interface OnRedwomenClickListener {
        void onRedwomenClick(int position);
    }

    public void setRedwomenClick(OnRedwomenClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public RedwomenListAdapter(Context context, List<RedWomenIntroData.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.admin = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "admin", "0");
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
            convertView = inflater.inflate(R.layout.item_listview_redwomen_person_center, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RedWomenIntroData.DataBean data = list.get(position);
        if (admin.equals("1")) {
            holder.itemListviewRedwomenPersonCenterHnjy.setVisibility(View.VISIBLE);
        } else {
            holder.itemListviewRedwomenPersonCenterHnjy.setVisibility(View.GONE);
        }
        holder.itemListviewRedwomenPersonCenterName.setText(data.getNickname());
        if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
            holder.itemListviewRedwomenPersonCenterIcon.setImageResource(R.mipmap.morentouxiang);
        } else {
            GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemListviewRedwomenPersonCenterIcon, 0);
        }
        holder.itemListviewRedwomenPersonCenterSex.setText(data.getAge());
        if (data.getCity().equals("") && data.getProvince().equals("")) {
            holder.itemListviewRedwomenPersonCenterCity.setText("未知");
        } else {
            if (!data.getCity().equals("")) {
                holder.itemListviewRedwomenPersonCenterCity.setText(data.getCity());
            } else {
                holder.itemListviewRedwomenPersonCenterCity.setText(data.getProvince());
            }
        }
        if (data.getRealname().equals("0")) {
            holder.itemListviewRedwomenPersonCenterRealname.setVisibility(View.GONE);
        } else {
            holder.itemListviewRedwomenPersonCenterRealname.setVisibility(View.VISIBLE);
        }
        if (data.getSex().equals("1")) {
            holder.itemListviewRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemListviewRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemListviewRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemListviewRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemListviewRedwomenPersonCenterRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemListviewRedwomenPersonCenterRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemListviewRedwomenPersonCenterRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemListviewRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemListviewRedwomenPersonCenterRole.setText("~");
        }
        holder.itemListviewRedwomenPersonCenterTime.setText("介绍时间：" + data.getAddtime());
        holder.itemListviewRedwomenPersonCenterTv.setText(data.getRemarks());
        holder.itemListviewRedwomenPersonCenterHnjy.setTag(position);
        holder.itemListviewRedwomenPersonCenterHnjy.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        this.mClickListener.onRedwomenClick(pos);

    }


    static class ViewHolder {
        @BindView(R.id.item_listview_redwomen_person_center_icon)
        ImageView itemListviewRedwomenPersonCenterIcon;
        @BindView(R.id.item_listview_redwomen_person_center_name)
        TextView itemListviewRedwomenPersonCenterName;
        @BindView(R.id.item_listview_redwomen_person_center_realname)
        ImageView itemListviewRedwomenPersonCenterRealname;
        @BindView(R.id.item_listview_redwomen_person_center_sex)
        TextView itemListviewRedwomenPersonCenterSex;
        @BindView(R.id.item_listview_redwomen_person_center_role)
        TextView itemListviewRedwomenPersonCenterRole;
        @BindView(R.id.item_listview_redwomen_person_center_city)
        TextView itemListviewRedwomenPersonCenterCity;
        @BindView(R.id.item_listview_redwomen_person_center_time)
        TextView itemListviewRedwomenPersonCenterTime;
        @BindView(R.id.item_listview_redwomen_person_center_tv)
        TextView itemListviewRedwomenPersonCenterTv;
        @BindView(R.id.item_listview_redwomen_person_center_hnjy)
        ImageView itemListviewRedwomenPersonCenterHnjy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

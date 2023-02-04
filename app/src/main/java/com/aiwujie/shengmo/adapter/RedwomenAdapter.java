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
import com.aiwujie.shengmo.bean.BeautifulData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by 290243232 on 2017/1/23.
 */
public class RedwomenAdapter extends BaseAdapter {
    private Context context;
    private List<BeautifulData.DataBean> list;
    private LayoutInflater inflater;
    Handler handler = new Handler();

    public RedwomenAdapter(Context context, List<BeautifulData.DataBean> list) {
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
            convertView = inflater.inflate(R.layout.item_gridview_redwomen, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BeautifulData.DataBean data = list.get(position);
        holder.itemGridviewRedwomenName.setText(data.getMatch_num());
        //自己是不是牵线会员  0、不是牵线会员
        String match_state = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "match_state", "0");
        String admin = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "admin", "0");
        if (admin.equals("1")) {
//            if (data.getMatch_photo().equals("") || data.getMatch_photo().equals("http://59.110.28.150:888/")) {
            if (data.getMatch_photo().equals("") || data.getMatch_photo().equals(HttpUrl.NetPic() )) {
                holder.itemGridviewRedwomenIcon.setImageResource(R.mipmap.morentouxiang);
            } else {
                GlideImgManager.glideLoader(context, HttpUrl.NetPic() + data.getMatch_photo(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, holder.itemGridviewRedwomenIcon);
            }
        } else {
            if (match_state.equals("0")) {
                if (data.getMatch_photo_lock().equals("0")) {
//                    if (data.getMatch_photo().equals("") || data.getMatch_photo().equals("http://59.110.28.150:888/")) {
                    if (data.getMatch_photo().equals("") || data.getMatch_photo().equals(HttpUrl.NetPic())) {
                        holder.itemGridviewRedwomenIcon.setImageResource(R.mipmap.morentouxiang);
                    } else {
                        GlideImgManager.glideLoader(context, HttpUrl.NetPic() + data.getMatch_photo(), R.mipmap.default_error, R.mipmap.default_error, holder.itemGridviewRedwomenIcon);
                    }
                } else {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.mipmap.default_error);
//                requestOptions.error(R.drawable.rc_image_error);
                    requestOptions.transform(new BlurTransformation(75));
                    Glide.with(context).load(HttpUrl.NetPic() + data.getMatch_photo())
                            .apply(requestOptions)
                            .into(holder.itemGridviewRedwomenIcon);
                }
            } else {
                if (data.getMatch_photo_lock().equals("2")) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.mipmap.default_error);
//                requestOptions.error(R.drawable.rc_image_error);
                    requestOptions.transform(new BlurTransformation(75));
                    Glide.with(context).load(HttpUrl.NetPic() + data.getMatch_photo())
                            .apply(requestOptions)
                            .into(holder.itemGridviewRedwomenIcon);
                } else {
                    if (data.getMatch_photo().equals("") || data.getMatch_photo().equals(HttpUrl.NetPic())) {
                        holder.itemGridviewRedwomenIcon.setImageResource(R.mipmap.default_error);
                    } else {
                        GlideImgManager.glideLoader(context, HttpUrl.NetPic() + data.getMatch_photo(), R.mipmap.default_error, R.mipmap.default_error, holder.itemGridviewRedwomenIcon);
                    }
                }
            }
        }
        holder.itemGridviewRedwomenSex.setText(data.getAge());
        if (data.getCity().equals("") && data.getProvince().equals("")) {
            holder.itemGridviewRedwomenCity.setText("未知");
        } else {
            if (!data.getCity().equals("")) {
                holder.itemGridviewRedwomenCity.setText(data.getCity());
            } else {
                holder.itemGridviewRedwomenCity.setText(data.getProvince());
            }
        }
        if (data.getRealname().equals("0")) {
            holder.itemGridviewRedwomenRenzheng.setVisibility(View.GONE);
        } else {
            holder.itemGridviewRedwomenRenzheng.setVisibility(View.VISIBLE);
        }
        if (data.getSex().equals("1")) {
            holder.itemGridviewRedwomenSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGridviewRedwomenSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("2")) {
            holder.itemGridviewRedwomenSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.nv);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGridviewRedwomenSex.setCompoundDrawables(drawable, null, null, null);
        } else if (data.getSex().equals("3")) {
            holder.itemGridviewRedwomenSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.san);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.itemGridviewRedwomenSex.setCompoundDrawables(drawable, null, null, null);
        }
        if (data.getRole().equals("S")) {
            holder.itemGridviewRedwomenRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            holder.itemGridviewRedwomenRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            holder.itemGridviewRedwomenRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            holder.itemGridviewRedwomenRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            holder.itemGridviewRedwomenRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            holder.itemGridviewRedwomenRole.setText("双");
        } else if (data.getRole().equals("~")) {
            holder.itemGridviewRedwomenRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            holder.itemGridviewRedwomenRole.setText("~");
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_gridview_redwomen_icon)
        ImageView itemGridviewRedwomenIcon;
        @BindView(R.id.item_gridview_redwomen_name)
        TextView itemGridviewRedwomenName;
        @BindView(R.id.item_gridview_redwomen_renzheng)
        ImageView itemGridviewRedwomenRenzheng;
        @BindView(R.id.item_gridview_redwomen_Sex)
        TextView itemGridviewRedwomenSex;
        @BindView(R.id.item_gridview_redwomen_role)
        TextView itemGridviewRedwomenRole;
        @BindView(R.id.item_gridview_redwomen_city)
        TextView itemGridviewRedwomenCity;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

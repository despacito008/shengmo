package com.aiwujie.shengmo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


/**
 * Created by Administrator on 2020/6/2.
 */

public abstract class QuickAdapter<T> extends RecyclerView.Adapter<QuickAdapter.VH> {

    private List<T> mDatas;
    private static Activity context;

    public QuickAdapter(List<T> datas) {
        this.mDatas = datas;
    }

    public QuickAdapter(Activity context, List<T> datas) {
        this.mDatas = datas;
        this.context=context;
    }

    public abstract int getLayoutId(int viewType);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.get(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(VH holder, T data, int position);

    public static class VH extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;
        private View mConvertView;

        public VH(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutId) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new VH(convertView);
        }

        public <T extends View> T getView(int id) {
            View v = mViews.get(id);
            if (v == null) {
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T) v;
        }

        public void setText(int id, String value) {
            TextView view = getView(id);
            view.setText(value);
        }

        public void setText(int id,Boolean state) {
            RadioButton view = getView(id);
            view.setChecked(state);
            view.setEnabled(false);
        }

        public void setViewBg(int id, int value) {
            View view = getView(id);
            view.setBackgroundResource(value);
        }

        public void setImage(int id, String url, int errResourcesId) {
            ImageView view = getView(id);
//            Glide.with(context).load()
            RequestOptions requestOptions=new RequestOptions();
            requestOptions.placeholder(errResourcesId);
            requestOptions.error(errResourcesId)
            .placeholder(errResourcesId).error(errResourcesId);
            Glide.with(view.getContext()).load(url).apply(requestOptions).into(view);
        }

        public void setImage(int id, int resourcesId) {
            ImageView view = getView(id);
            Glide.with(view.getContext()).load(resourcesId).into(view);
        }



        public void setOnClickListener(int id, View.OnClickListener clickListener) {
            View view = getView(id);
            view.setOnClickListener(clickListener);
        }

        public void setOnClickListener(View view, View.OnClickListener clickListener) {
            view.setOnClickListener(clickListener);
        }

        public void setOnFocusChangeListener(int id, View.OnFocusChangeListener listener) {
            View view = getView(id);
            view.setOnFocusChangeListener(listener);
        }
    }

    public void updateData(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

}


package com.aiwujie.shengmo.recycleradapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.ChatRoomMaiPeopleBean;
import com.aiwujie.shengmo.utils.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class ChartRoommairenAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChatRoomMaiPeopleBean> mEntityList;
    private Jiekouhuidiao jiekouhuidiao;

    public ChartRoommairenAdapter(Context context, List<ChatRoomMaiPeopleBean> entityList, Jiekouhuidiao jiekouhuidiao){
        this.mContext = context;
        this.mEntityList = entityList;
        this.jiekouhuidiao=jiekouhuidiao;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chatroom_renitem2, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ChatRoomMaiPeopleBean entity = mEntityList.get(position);

      /*  if (null !=entity.getRule() &&entity.getRule().equals("1")){
            ((DemoViewHolder)holder).mivrenguanli.setImageResource(R.mipmap.lanseren);
            ((DemoViewHolder)holder).mivrenguanli.setVisibility(View.VISIBLE);
        }else {
            ((DemoViewHolder)holder).mivrenguanli.setVisibility(View.GONE);
        }*/
      if (entity.getJianghua().equals("1")){
          ((DemoViewHolder)holder).ren_kuang.setVisibility(View.VISIBLE);
      }else {
          ((DemoViewHolder)holder).ren_kuang.setVisibility(View.GONE);
      }

        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.rc_image_error);
        requestOptions.error(R.drawable.mai2);
        requestOptions.transform(new GlideCircleTransform());
        Glide.with(mContext).load(entity.getPeopleicon()).apply(requestOptions).into(((DemoViewHolder)holder).mivrenhead);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiekouhuidiao.onclickya2(entity.getMaiid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder{

        private ImageView mivrenhead;
        private ImageView mivrenguanli;
        ImageView ren_kuang;

        public DemoViewHolder(View itemView) {
            super(itemView);
            mivrenhead = (ImageView) itemView.findViewById(R.id.ren_head);
            mivrenguanli = itemView.findViewById(R.id.ren_guanli);
            ren_kuang = itemView.findViewById(R.id.ren_kuang);
        }
    }
    public interface Jiekouhuidiao{
        void onclickya2(String uidba);
    }
}
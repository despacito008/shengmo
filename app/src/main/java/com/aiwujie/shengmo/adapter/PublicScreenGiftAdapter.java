package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.NoticePresentData;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class PublicScreenGiftAdapter extends RecyclerView.Adapter<PublicScreenGiftAdapter.PublicScreenGiftHolder> {
    Context context;
    List<NoticePresentData.DataBean> noticeList;
    Fragment fragment;

    public PublicScreenGiftAdapter(Context context, List<NoticePresentData.DataBean> noticeList, Fragment fragment) {
        this.context = context;
        this.noticeList = noticeList;
        this.fragment = fragment;
    }

    @Override
    public PublicScreenGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listview_notice_present, parent, false);
        return new PublicScreenGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(PublicScreenGiftHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    class PublicScreenGiftHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_listview_notice_present_sendname)
        TextView itemListviewNoticePresentSendname;
        @BindView(R.id.item_listview_notice_present_receivename)
        TextView itemListviewNoticePresentReceivename;
        @BindView(R.id.item_listview_notice_present_place)
        TextView itemListviewNoticePresentPlace;
        @BindView(R.id.ll_item_listview_name)
        LinearLayout llItemListviewName;
        @BindView(R.id.item_listview_notice_present_name)
        TextView itemListviewNoticePresentName;
        @BindView(R.id.ll_item_listview_name2)
        LinearLayout llItemListviewName2;
        @BindView(R.id.item_bg_gift_heart)
        View itemBgGiftHeart;
        @BindView(R.id.item_listview_notice_present_sendicon)
        ImageView itemListviewNoticePresentSendicon;
        @BindView(R.id.item_listview_notice_present_icon)
        ImageView itemListviewNoticePresentIcon;
        @BindView(R.id.item_listview_notice_present_othericon)
        ImageView itemListviewNoticePresentOthericon;
        @BindView(R.id.item_listview_notice_present_sendtime)
        TextView itemListviewNoticePresentSendtime;

        public PublicScreenGiftHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void display(int index) {
            NoticePresentData.DataBean data = noticeList.get(index);
            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                itemListviewNoticePresentSendicon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewNoticePresentSendicon, 0);
                ImageLoader.loadCircleImage(fragment, data.getHead_pic(), itemListviewNoticePresentSendicon,R.mipmap.morentouxiang);
            }
            if (data.getFhead_pic().equals("") || data.getFhead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                itemListviewNoticePresentOthericon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, data.getFhead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewNoticePresentOthericon, 0);
                ImageLoader.loadCircleImage(fragment, data.getFhead_pic(), itemListviewNoticePresentOthericon,R.mipmap.morentouxiang);
            }
            if ("1".equals(data.getNum())) {
                itemListviewNoticePresentName.setText(data.getGift_name() + " (" + data.getBeans() + "魔豆)");
            } else {
                itemListviewNoticePresentName.setText(data.getGift_name() + "x" + data.getNum() + " (" + data.getBeans() + "魔豆)");
            }
            ImageLoader.loadImage(fragment, data.getGift_image(), itemListviewNoticePresentIcon);
            itemListviewNoticePresentSendname.setText(data.getNickname());
            itemListviewNoticePresentReceivename.setText(data.getFnickname());
            itemListviewNoticePresentSendtime.setText(data.getAddtime());
            itemListviewNoticePresentPlace.setText(data.getSource_type());
            itemListviewNoticePresentSendicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoticeItemClick != null) {
                        onNoticeItemClick.doItemSendIconClick(index);
                    }
                }
            });
            itemListviewNoticePresentOthericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoticeItemClick != null) {
                        onNoticeItemClick.doItemReceiveIconClick(index);
                    }
                }
            });
        }
    }

    public interface OnNoticeItemClick {
        void doItemSendIconClick(int index);

        void doItemReceiveIconClick(int index);
    }

    OnNoticeItemClick onNoticeItemClick;

    public void setOnNoticeItemClick(OnNoticeItemClick onNoticeItemClick) {
        this.onNoticeItemClick = onNoticeItemClick;
    }
}

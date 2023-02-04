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
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class PublicScreenVipAdapter extends RecyclerView.Adapter<PublicScreenVipAdapter.PublicScreenGiftHolder> {
    Context context;
    List<NoticePresentData.DataBean> noticeList;
    Fragment fragment;

    public PublicScreenVipAdapter(Context context, List<NoticePresentData.DataBean> noticeList,Fragment fragment) {
        this.context = context;
        this.noticeList = noticeList;
        this.fragment = fragment;
    }

    @Override
    public PublicScreenGiftHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listview_notice_vip, parent, false);
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
        @BindView(R.id.item_listview_notice_vip_sendname)
        TextView itemListviewNoticeVipSendname;
        @BindView(R.id.item_listview_notice_vip_receivename)
        TextView itemListviewNoticeVipReceivename;
        @BindView(R.id.item_listview_notice_vip_name)
        TextView itemListviewNoticeVipName;
        @BindView(R.id.ll_item_listview_name)
        AutoLinearLayout llItemListviewName;
        @BindView(R.id.item_listview_notice_vip_sendicon)
        ImageView itemListviewNoticeVipSendicon;
        @BindView(R.id.item_listview_notice_vip_icon)
        ImageView itemListviewNoticeVipIcon;
        @BindView(R.id.item_listview_notice_vip_othericon)
        ImageView itemListviewNoticeVipOthericon;
        @BindView(R.id.item_listview_notice_vip_sendtime)
        TextView itemListviewNoticeVipSendtime;

        public PublicScreenGiftHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void display(int index) {
            NoticePresentData.DataBean data = noticeList.get(index);
            if (data.getHead_pic().equals("") || data.getHead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                itemListviewNoticeVipSendicon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, data.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewNoticeVipSendicon, 0);
                ImageLoader.loadCircleImage(fragment, data.getHead_pic(), itemListviewNoticeVipSendicon,R.mipmap.morentouxiang);
            }
            if (data.getFhead_pic().equals("") || data.getFhead_pic().equals(NetPic())) {//"http://59.110.28.150:888/"
                itemListviewNoticeVipOthericon.setImageResource(R.mipmap.morentouxiang);
            } else {
                //GlideImgManager.glideLoader(context, data.getFhead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, itemListviewNoticeVipOthericon, 0);
                ImageLoader.loadCircleImage(fragment, data.getFhead_pic(), itemListviewNoticeVipOthericon,R.mipmap.morentouxiang);
            }
//            if ("1".equals(data.getNum())) {
//                itemListviewNoticeVipName.setText(data.getGift_name() + " (" + data.getBeans() + "魔豆)");
//            } else {
//                itemListviewNoticeVipName.setText(data.getGift_name() + "x" + data.getNum() + " (" + data.getBeans() + "魔豆)");
//            }
            //Glide.with(context).load(data.getGift_image()).into(itemListviewNoticePresentIcon);
            itemListviewNoticeVipSendname.setText(data.getNickname());
            itemListviewNoticeVipReceivename.setText(data.getFnickname());
            itemListviewNoticeVipSendtime.setText(data.getAddtime());
            itemListviewNoticeVipSendicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoticeItemClick != null) {
                        onNoticeItemClick.doItemSendIconClick(index);
                    }
                }
            });
            itemListviewNoticeVipOthericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoticeItemClick != null) {
                        onNoticeItemClick.doItemReceiveIconClick(index);
                    }
                }
            });
            String vipContent = "";
            if ("2".equals(data.getState())) {
                switch (data.getType()) {
                    case "1":
                        vipContent = "1个月VIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_vip);
                        break;
                    case "2":
                        vipContent = "3个月VIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_vip);
                        break;
                    case "3":
                        vipContent = "6个月VIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_vip);
                        break;
                    case "4":
                        vipContent = "12个月VIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_vip_year);
                        break;
                }
            } else if ("3".equals(data.getState())) {
                switch (data.getType()) {
                    case "1":
                        vipContent = "1个月SVIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_svip);
                        break;
                    case "2":
                        vipContent = "3个月SVIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_svip);
                        break;
                    case "3":
                        vipContent = "6个月SVIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_svip);
                        break;
                    case "4":
                        vipContent = "12个月SVIP";
                        itemListviewNoticeVipIcon.setImageResource(R.drawable.user_svip_year);
                        break;
                }
            }
            itemListviewNoticeVipName.setText(vipContent);
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

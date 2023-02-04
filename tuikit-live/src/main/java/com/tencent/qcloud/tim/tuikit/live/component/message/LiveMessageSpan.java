package com.tencent.qcloud.tim.tuikit.live.component.message;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils;

public class LiveMessageSpan extends ReplacementSpan {

    private  View liveMessageView;
    private int mWidth;
    public LiveMessageSpan(Context context,ChatEntity item) {
        liveMessageView = LayoutInflater.from(context).inflate(R.layout.live_item_message_sign,null);

        ImageView ivGroupRole = liveMessageView.findViewById(R.id.live_tv_live_anchor_manager);
        ImageView ivUserIdentity = liveMessageView.findViewById(R.id.live_tv_live_anchor_v_level);
        LinearLayout llUserWealth = liveMessageView.findViewById(R.id.ll_live_message_wealth);
        TextView tvUserWealth = liveMessageView.findViewById(R.id.tv_live_message_wealth);
        LinearLayout llAnchorLevel = liveMessageView.findViewById(R.id.ll_live_message_anchor_level);
        LinearLayout llAudienceLevel = liveMessageView.findViewById(R.id.ll_live_message_audience_level);
        TextView tvAnchorLevel = liveMessageView.findViewById(R.id.tv_live_anchor_level);
        TextView tvAudienceLevel = liveMessageView.findViewById(R.id.tv_live_audience_level);
        TextView tvFansClubName = liveMessageView.findViewById(R.id.tv_live_message_fans_club_name);
        TextView tvFansClubLevel = liveMessageView.findViewById(R.id.tv_live_message_fans_club_level);
        ImageView ivFansClub = liveMessageView.findViewById(R.id.iv_live_message_fans_club);
        LinearLayout llFansClub = liveMessageView.findViewById(R.id.ll_live_message_fans_club);



//        if (!TextUtils.isEmpty(item.getWealth_val()) && !"0".equals(item.getWealth_val())) {
//            llUserWealth.setVisibility(View.VISIBLE);
//            if ("1".equals(item.getWealth_val_switch())) {
//                tvUserWealth.setText("密");
//            } else {
//                tvUserWealth.setText(item.getWealth_val());
//            }
//        } else {
//            llUserWealth.setVisibility(View.GONE);
//        }


        if (!TextUtils.isEmpty(item.getLive_group_role()) && item.getLive_group_role().equals("2")) { //主播
            ivGroupRole.setVisibility(View.VISIBLE);
            ivGroupRole.setImageResource(R.drawable.live_anchor_icon);

            //llAudienceLevel.setVisibility(View.GONE);
//            if ("-1".equals(item.getAnchor_level()) || "0".equals(item.getAnchor_level())) {
//                llAnchorLevel.setVisibility(View.GONE);
//            } else {
//                llAnchorLevel.setVisibility(View.VISIBLE);
//            }
//            if ("-1".equals(item.getUser_level()) || "0".equals(item.getUser_level()) || TextUtils.isEmpty(item.getUser_level())) {
//                llAudienceLevel.setVisibility(View.GONE);
//            } else {
//                llAudienceLevel.setVisibility(View.VISIBLE);
//            }
//            tvAudienceLevel.setText(TextUtils.isEmpty(item.getUser_level())?"0":item.getUser_level());
//            tvAnchorLevel.setText(TextUtils.isEmpty(item.getAnchor_level())?"0":item.getAnchor_level());
        } else { //用户
            //llAnchorLevel.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getLive_group_role()) && item.getLive_group_role().equals("1")) {//场控
                ivGroupRole.setVisibility(View.VISIBLE);
                ivGroupRole.setImageResource(R.drawable.live_manager_icon);
            } else { //普通用户
                ivGroupRole.setVisibility(View.GONE);
            }
        }

        if ("-1".equals(item.getUser_level()) || "0".equals(item.getUser_level()) || TextUtils.isEmpty(item.getUser_level())) {
            llAudienceLevel.setVisibility(View.GONE);
        } else {
            llAudienceLevel.setVisibility(View.VISIBLE);
        }
        if ("-1".equals(item.getAnchor_level()) || "0".equals(item.getAnchor_level()) || TextUtils.isEmpty(item.getAnchor_level())) {
            llAnchorLevel.setVisibility(View.GONE);
        } else {
            llAnchorLevel.setVisibility(View.VISIBLE);
        }
        tvAudienceLevel.setText(TextUtils.isEmpty(item.getUser_level())?"0":item.getUser_level());
        tvAnchorLevel.setText(TextUtils.isEmpty(item.getAnchor_level())?"0":item.getAnchor_level());


        if (!TextUtils.isEmpty(item.getLive_user_role())) {
            UserIdentityUtils.showUserIdentity(context, Integer.valueOf(item.getLive_user_role()), ivUserIdentity);
        } else {
            ivUserIdentity.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.getFanclub_status())) {
            switch (item.getFanclub_status()) {
                case "0":
                    llFansClub.setVisibility(View.VISIBLE);
                    tvFansClubLevel.setText(item.getFanclub_level());
                    tvFansClubName.setText(item.getFanclub_card());
                    llFansClub.setBackgroundColor(Color.parseColor("#99666666"));
                    ivFansClub.setColorFilter(Color.parseColor("#ff666666"));
                    break;
                case "1":
                    llFansClub.setVisibility(View.VISIBLE);
                    tvFansClubLevel.setText(item.getFanclub_level());
                    tvFansClubName.setText(item.getFanclub_card());
                    llFansClub.setBackgroundColor(Color.parseColor("#99db57f3"));
                    //ivFansClub.setColorFilter(R.color.transparent);
                    break;
                case "2":
                    llFansClub.setVisibility(View.GONE);
                    break;
            }
        } else {
            llFansClub.setVisibility(View.GONE);
        }

        int width = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        liveMessageView.measure(width,height);
        mWidth = liveMessageView.getMeasuredWidth();

    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setAntiAlias(true);

//        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
//        int transY = (y + fm.descent + y + fm.ascent) /2 -liveMessageView.getMeasuredHeight() /2;
//        canvas.save();
//        canvas.translate(x,transY);
//        liveMessageView.draw(canvas);
//        canvas.restore();

        Bitmap bitmap = view2Bitmap(liveMessageView);
        canvas.drawBitmap(bitmap,x,y+paint.ascent(),paint);
    }

    public static Bitmap view2Bitmap(final View view) {
        if (view == null) {
            return null;
        }
        boolean drawingCacheEnabled = view.isDrawingCacheEnabled();
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setDrawingCacheEnabled(true);
        view.setWillNotCacheDrawing(false);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (null == drawingCache || drawingCache.isRecycled()) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
            view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
            view.buildDrawingCache();
            drawingCache = view.getDrawingCache();
            if (null == drawingCache || drawingCache.isRecycled()) {
                bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),view.getMeasuredHeight(),Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            } else {
                bitmap = Bitmap.createBitmap(drawingCache);
            }
        } else {
            bitmap = Bitmap.createBitmap(drawingCache);
        }
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        return bitmap;
    }
}

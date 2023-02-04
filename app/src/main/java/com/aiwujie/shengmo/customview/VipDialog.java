package com.aiwujie.shengmo.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;

/**
 * Created by 290243232 on 2017/10/10.
 */

public class VipDialog  {
    private Context context;
    private String toast;

    public VipDialog(Context context, String toast) {
        this.context = context;
        this.toast = toast;
        showDialog(context,toast);
    }

    private void showDialog(final Context context, String toast) {
        View view = View.inflate(context, R.layout.item_open_vip_dialog, null);
        final AlertDialog startDialog = new AlertDialog.Builder(context).create();
        startDialog.setView(view);
        startDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = startDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 85 / 100;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(lp);
        startDialog.setCanceledOnTouchOutside(true);
        TextView message = (TextView) window.findViewById(R.id.item_open_vip_message);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_vip_buy);
        message.setText(toast);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headpic = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "headurl", "");
                Intent intent = new Intent(context, VipCenterActivity.class);
                intent.putExtra("headpic", headpic);
                intent.putExtra("uid", MyApp.uid);
                context.startActivity(intent);
                startDialog.dismiss();
            }
        });
    }

}


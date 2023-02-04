package com.aiwujie.shengmo.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.SettingActivity;

/**
 * Created by 290243232 on 2017/9/11.
 */

public class BindPhoneAlertDialog {

    public static void bindAlertDialog(final Context context, String toast){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_open_vip_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView message = (TextView) window.findViewById(R.id.item_open_vip_message);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_vip_buy);
        message.setText(toast);
        buy.setText("绑定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                context. startActivity(intent);
                alertDialog.dismiss();
            }
        });

    }
}

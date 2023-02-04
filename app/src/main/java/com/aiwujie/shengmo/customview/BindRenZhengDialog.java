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
import com.aiwujie.shengmo.activity.PhotoRzActivity;

/**
 * date: 2018/7/21
 * author: Gao Huiquan
 * function:
 */

public class BindRenZhengDialog {

    public static void bindAlertDialog(final Context context, String toast) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_renzheng_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        TextView message = (TextView) window.findViewById(R.id.item_open_vip_message);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_vip_cancel);
        TextView item_open_renzheng = window.findViewById(R.id.item_open_renzheng);
        item_open_renzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
                alertDialog.dismiss();
            }
        });
        message.setText(toast);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
}

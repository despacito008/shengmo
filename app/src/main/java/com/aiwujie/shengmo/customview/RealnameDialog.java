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
import com.aiwujie.shengmo.activity.PhotoRzActivity;

/**
 * Created by 290243232 on 2017/10/10.
 */

public class RealnameDialog {
    private Context context;
    private String toast;

    public RealnameDialog(Context context, String toast) {
        this.context = context;
        this.toast = toast;
        showDialog(context,toast);
    }

    private void showDialog(final Context context, String toast) {
        View view = View.inflate(context, R.layout.item_open_realname_dialog, null);
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
        TextView message = (TextView) window.findViewById(R.id.item_open_realname_message);
        TextView cancel = (TextView) window.findViewById(R.id.item_open_realname_cancel);
        TextView buy = (TextView) window.findViewById(R.id.item_open_realname);
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
                Intent intent = new Intent(context, PhotoRzActivity.class);
                context.startActivity(intent);
                startDialog.dismiss();
            }
        });
    }

}


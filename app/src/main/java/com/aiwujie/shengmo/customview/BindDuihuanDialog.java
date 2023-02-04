package com.aiwujie.shengmo.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

/**
 * date: 2018/7/21
 * author: Gao Huiquan
 * function:
 */

public class BindDuihuanDialog {

    private static EditText mStamp_etCount;
    private static TextView duoshao;
    private static TextView mStamp_buy;
    static  Context context;

    public static void bindAlertDialog(final Context context, String toast) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_duihuan_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        mStamp_etCount = window.findViewById(R.id.mStamp_etCount);
        duoshao = window.findViewById(R.id.duoshao);
        mStamp_buy = window.findViewById(R.id.mStamp_buy);
        mStamp_etCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mStamp_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mStamp_etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = mStamp_etCount.getText().toString().trim();
                Integer integer = Integer.valueOf(trim);
                int i = integer / 2;
                duoshao.setText(i+"  充值魔豆");
            }
        });

    }

}

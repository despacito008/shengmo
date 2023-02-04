package com.aiwujie.shengmo.zdyview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.aiwujie.shengmo.activity.PesonInfoActivity;

import org.feezu.liuli.timeselector.Utils.TextUtil;

public class ATSpan extends ClickableSpan {

    private String userId;
    private String type = "";

    public ATSpan(String userId) {
        this.userId = userId;
    }

    public ATSpan(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }

    public String getValue() {
        return userId;
    }

    public void setValue(String userId) {
        this.userId = userId;
    }

    @Override
    public void onClick(View widget) {
        Context context = widget.getContext();
        /*Intent intent = new Intent(context, OthersCCActivity.class);
        intent.putExtra("UserId", userId);
        context.startActivity(intent);*/
        //Toast.makeText(context,userId,Toast.LENGTH_SHORT).show();
        if (TextUtil.isEmpty(userId)) {
            return;
        }
        Intent intent = new Intent(context, PesonInfoActivity.class);
        intent.putExtra("uid", userId);
        context.startActivity(intent);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if ("群".equals(type)) {
            ds.setColor(Color.parseColor("#FF66ccff"));
        } else if ("高端".equals(type)) {
            ds.setColor(Color.parseColor("#FF66ccff"));
        } else {
            ds.setColor(Color.parseColor("#FFdb57f3"));
        }

    }

}
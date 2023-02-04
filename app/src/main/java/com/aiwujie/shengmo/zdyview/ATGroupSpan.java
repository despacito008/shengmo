package com.aiwujie.shengmo.zdyview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;

import org.feezu.liuli.timeselector.Utils.TextUtil;

public class ATGroupSpan extends ClickableSpan {

    private String groupId;

    public ATGroupSpan(String groupId) {
        this.groupId = groupId;
    }

    public String getValue(){
        return groupId;
    }

    public void setValue(String groupId){
        this.groupId = groupId;
    }

    @Override
    public void onClick(View widget) {
        Context context = widget.getContext();
        /*Intent intent = new Intent(context, OthersCCActivity.class);
        intent.putExtra("UserId", userId);
        context.startActivity(intent);*/
        //Toast.makeText(context,userId,Toast.LENGTH_SHORT).show();
        if (TextUtil.isEmpty(groupId)) {
            return;
        }
        Intent intent = new Intent(context, GroupInfoActivity.class);
        intent.putExtra("groupId",groupId);
        context.startActivity(intent);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#FF66ccff"));
    }
}
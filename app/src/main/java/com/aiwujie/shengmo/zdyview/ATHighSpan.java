package com.aiwujie.shengmo.zdyview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity;
import com.aiwujie.shengmo.kt.util.IntentKey;

import org.feezu.liuli.timeselector.Utils.TextUtil;

public class ATHighSpan extends ClickableSpan {

    private String groupId;

    public ATHighSpan(String groupId) {
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
        Intent intent = new Intent(context, HighEndUserActivity.class);
        intent.putExtra(IntentKey.UID,groupId);
        context.startActivity(intent);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#FF66ccff"));
    }
}
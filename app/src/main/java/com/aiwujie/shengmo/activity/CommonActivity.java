package com.aiwujie.shengmo.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.ClearConversationEvent;
import com.aiwujie.shengmo.utils.DataCleanManager;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonActivity extends AppCompatActivity {

    @BindView(R.id.mCommon_return)
    ImageView mCommonReturn;
    @BindView(R.id.mCommon_clearCache)
    TextView mCommonClearCache;
    @BindView(R.id.mCommon_clearChat)
    TextView mCommonClearChat;
    private String totalcache;
    private AlertDialog dialog;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
    }

    @OnClick({R.id.mCommon_return, R.id.mCommon_clearCache, R.id.mCommon_clearChat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mCommon_return:
                finish();
                break;
            case R.id.mCommon_clearCache:
                clearCache();
                break;
            case R.id.mCommon_clearChat:
                clearChatRecorder();
                break;
        }
    }
    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        ProgressBar bar=new ProgressBar(this);
        builder.setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }
    private void clearChatRecorder() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("提示").setMessage("确认清空记录吗？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    showdialog();
                    clearMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }

    private void clearMessage() {

        EventBus.getDefault().post(new ClearConversationEvent());
            ToastUtil.show(getApplicationContext(),"清除聊天记录成功");
            dialog.dismiss();

    }


    private void clearCache() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("提示").setMessage("确认清理缓存吗？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataCleanManager.clearAllCache(CommonActivity.this);
                try {
                    mCommonClearCache.setText(DataCleanManager.getTotalCacheSize(CommonActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    totalcache= DataCleanManager.getTotalCacheSize(CommonActivity.this);
                    mCommonClearCache.setText(totalcache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }
}

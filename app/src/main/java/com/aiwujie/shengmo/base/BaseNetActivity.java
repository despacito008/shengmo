package com.aiwujie.shengmo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aiwujie.shengmo.net.OkHttpRequestManager;

public class BaseNetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
    }
}

package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GiftItemRecyclerAdapter;
import com.aiwujie.shengmo.adapter.PersonAllPresentGridViewAdapter;
import com.aiwujie.shengmo.bean.MyPresentData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.immersionbar.ImmersionBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PresentListActivity extends AppCompatActivity {
    @BindView(R.id.mPresentList_return)
    ImageView mPresentListReturn;
    @BindView(R.id.rv_person_present)
    RecyclerView rvPersonPresent;
    @BindView(R.id.activity_present_list)
    FrameLayout activityPresentList;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_list);
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        uid = getIntent().getStringExtra("uid");
    }

    @OnClick(R.id.mPresentList_return)
    public void onViewClicked() {
        finish();
    }

    private void getPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMyPresent, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    MyPresentData myPresentData = new Gson().fromJson(response, MyPresentData.class);
                    if (myPresentData.getData().getGiftArr().size() != 0) {
                        GiftItemRecyclerAdapter giftItemRecyclerAdapter = new GiftItemRecyclerAdapter(PresentListActivity.this, myPresentData.getData().getGiftArr(),1);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PresentListActivity.this, 3);
                        rvPersonPresent.setAdapter(giftItemRecyclerAdapter);
                        rvPersonPresent.setLayoutManager(gridLayoutManager);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresent();
    }
}

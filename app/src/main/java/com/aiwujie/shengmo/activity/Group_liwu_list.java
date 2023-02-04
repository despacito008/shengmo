package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GroupliwulistAdapter;
import com.aiwujie.shengmo.bean.Group_liwulistbean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group_liwu_list extends AppCompatActivity {

    List<Group_liwulistbean.DataBean.DatasBean> list = new ArrayList<>();
    private String orderid;
    private String number;
    private ListView shouredbaolist;
    Handler handler = new Handler();
    private ImageView iv_ya,ivHeadOver;
    private TextView tv_asd;
    private int image;
    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_liwu_list);
        ImmersionBar.with(this).init();

        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        number = intent.getStringExtra("number");
        image = intent.getIntExtra("image", R.mipmap.presentnew32);
        sendredbao();

        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        shouredbaolist = (ListView) findViewById(R.id.shouredbaolist);
        View view = View.inflate(this,R.layout.liwu_head,null);
        shouredbaolist.addHeaderView(view);
        iv_ya = view.findViewById(R.id.iv_ya);
        ivHeadOver = view.findViewById(R.id.iv_red_bao_header_over);
        Glide.with(getApplicationContext()).load(image).into(iv_ya);
        tv_asd = view.findViewById(R.id.tv_asd);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                sendredbao();

            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                sendredbao();
            }
        });
    }

    public void sendredbao(){
        Map<String, String> map = new HashMap<>();
        map.put("orderid", orderid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.getQunGiftList, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                        case 2001:

                            Gson gson = new Gson();
                            final Group_liwulistbean redbagshouBean = gson.fromJson(response, Group_liwulistbean.class);
                            final List<Group_liwulistbean.DataBean.DatasBean> datas = redbagshouBean.getData().getDatas();
                            if (datas==null){
                                return;
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_asd.setText("共"+number+"个礼物,剩余"+redbagshouBean.getData().getNum()+"个礼物");
                                    shouredbaolist.setAdapter(new GroupliwulistAdapter(Group_liwu_list.this,datas));
                                    if ("1".equals(redbagshouBean.getData().getStatus())) {
                                        showOverNum();
                                    } else if ("2".equals(redbagshouBean.getData().getStatus())) {
                                        showOverDate();
                                    }
                                }
                            });
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore();
                            break;
                        case 3000:
                        case 3001:
                        case 4000:
                        case 4001:
                        case 4002:
                        case 4003:
                        case 4004:
                        case 4005:
                        case 4006:
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    void showOverNum() {
        ivHeadOver.setVisibility(View.VISIBLE);
        ivHeadOver.setImageResource(R.drawable.ic_over_num);
    }

    void showOverDate() {
        ivHeadOver.setVisibility(View.VISIBLE);
        ivHeadOver.setImageResource(R.drawable.ic_over_date);
    }
}

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
import com.aiwujie.shengmo.adapter.RedbagGroupAdapter;
import com.aiwujie.shengmo.bean.RedbagshouGroupBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedBaoshougroupActivity extends AppCompatActivity {


    List<RedbagshouGroupBean.DataBean.DatasBean> list = new ArrayList<>();
    private String orderid;
    private ListView shouredbaolist;
    Handler handler = new Handler();
    private TextView sssdredbag;
    private TextView tv_asd;
    private SmartRefreshLayout smartRefreshLayout;

    private TextView tvHeadName,tvHeadInfo,tvHeadDesc;
    private ImageView ivHeadBack,ivHeadIcon,ivHeadOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_baoshougroup);
        ImmersionBar.with(this).transparentBar().init();
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");

        sendredbao();

        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        shouredbaolist = (ListView) findViewById(R.id.shouredbaolist);
//        View view = View.inflate(this,R.layout.redbao_head,null);
//        shouredbaolist.addHeaderView(view);
//        sssdredbag = view.findViewById(R.id.sssdredbag);
//        tv_asd = view.findViewById(R.id.tv_asd);

        View view = View.inflate(this,R.layout.layout_red_bao_header,null);
        shouredbaolist.addHeaderView(view);
        ivHeadBack = view.findViewById(R.id.iv_red_bao_header_back);
        ivHeadIcon = view.findViewById(R.id.iv_red_bao_header_icon);
        tvHeadName = view.findViewById(R.id.tv_red_bao_header_name);
        tvHeadInfo = view.findViewById(R.id.tv_red_bao_header_info);
        tvHeadDesc = view.findViewById(R.id.tv_red_bao_header_desc);
        tvHeadName.setText(getIntent().getStringExtra("name")+"的红包");
        ivHeadOver = view.findViewById(R.id.iv_red_bao_header_over);
        Glide.with(RedBaoshougroupActivity.this).load(getIntent().getStringExtra("url")).transform(new CircleCrop()).into(ivHeadIcon);
        tvHeadInfo.setText(getIntent().getStringExtra("info"));
        ivHeadBack.setOnClickListener(new View.OnClickListener() {
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
        requestManager.post(HttpUrl.getQunRedbagList, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                LogUtil.d(response);
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                        case 2001:
                            Gson gson = new Gson();
                            final RedbagshouGroupBean redbagshouBean = gson.fromJson(response, RedbagshouGroupBean.class);
                            final List<RedbagshouGroupBean.DataBean.DatasBean> datas = redbagshouBean.getData().getDatas();
                            if(datas == null) {
                                return;
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    int stnum = 0;
                                    for (int i = 0; i < datas.size(); i++) {
                                        RedbagshouGroupBean.DataBean.DatasBean datasBean = datas.get(i);
                                        String num = datasBean.getNum();
                                        stnum+=Integer.valueOf(num);
                                    }
                                    tvHeadDesc.setText("共"+redbagshouBean.getData().getNums()+"个红包/"+redbagshouBean.getData().getTnum()+"魔豆，已领取"+redbagshouBean.getData().getDatas().size()+"个红包/"+stnum+"魔豆");
                                    shouredbaolist.setAdapter(new RedbagGroupAdapter(RedBaoshougroupActivity.this,datas));
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

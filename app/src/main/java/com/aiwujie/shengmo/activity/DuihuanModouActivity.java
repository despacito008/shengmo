package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ModouHuanBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.HashMap;
import java.util.Map;

public class DuihuanModouActivity extends AppCompatActivity {

    private TextView duoshao;
    private EditText mStamp_etCount;
    String beans_receivecont="0";
    String beanscont="0";
    Handler handler = new Handler();
    private String liwudou;
    private TextView shengyin;
    String caifu="0";
    private PercentLinearLayout percentLinearLayout;
    private ImageView iv_up_lb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duihuan_modou);

        Intent intent = getIntent();
        liwudou = intent.getStringExtra("liwudou");

        shengyin = (TextView) findViewById(R.id.shengyin);
        percentLinearLayout = (PercentLinearLayout) findViewById(R.id.rl_up_lb);
        iv_up_lb = (ImageView) findViewById(R.id.iv_up_lb);

        shengyin.setText("剩余"+liwudou+"银魔豆");

        ImageView mStamp_return = (ImageView) findViewById(R.id.mStamp_return);
        mStamp_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        percentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caifu.equals("0")){
                    iv_up_lb.setImageResource(R.mipmap.yuandiantaohui);
                    caifu="1";
                }else {
                    iv_up_lb.setImageResource(R.mipmap.yuandiantaozi);
                    caifu="0";
                }
            }
        });


        TextView mStamp_buy = (TextView) findViewById(R.id.mStamp_buy);
        mStamp_etCount = (EditText) findViewById(R.id.mStamp_etCount);
        mStamp_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beans_receivecont=mStamp_etCount.getText().toString().trim();
                if (beans_receivecont.equals("")||beans_receivecont==null){
                    return;
                }
                Integer integer = Integer.valueOf(beans_receivecont);
                Integer dou = Integer.valueOf(liwudou);
                if(dou<integer){
                    ToastUtil.show(DuihuanModouActivity.this,"您的魔豆不足");
                    return;
                }
                if(integer%2==0){
                    int i = integer / 2;
                    beanscont=i+"";
                }else {
                    integer =  integer-1;
                    beans_receivecont=integer+"";
                    int i = integer / 2;
                    beanscont=i+"";
                }
                getusertopcard();

            }
        });
        duoshao = (TextView) findViewById(R.id.duoshao);

        mStamp_etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = mStamp_etCount.getText().toString().trim();
                if(s1!=null && !s1.equals("")){
                    Integer integer = Integer.valueOf(s1);
                    int i = integer / 2;
                    duoshao.setText(i+" 充值魔豆");
                }

            }
        });

    }
    //魔豆兑换
    public void getusertopcard(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("beans_receive", beans_receivecont);
        map.put("beans", beanscont);
        map.put("caifu", caifu);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.exBeans, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.e("----", "onSuccess: "+response );
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        ModouHuanBean ejectionBean = gson.fromJson(response, ModouHuanBean.class);
                        Toast.makeText(DuihuanModouActivity.this, ""+ejectionBean.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}

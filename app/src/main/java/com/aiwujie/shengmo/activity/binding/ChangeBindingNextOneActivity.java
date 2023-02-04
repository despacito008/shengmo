package com.aiwujie.shengmo.activity.binding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeBindingNextOneActivity extends AppCompatActivity {

    @BindView(R.id.mChange_Binding_next_one_return)
    ImageView mChangeBindingNextOneReturn;
    @BindView(R.id.mChange_Binding_next_one_mobile)
    TextView mChangeBindingNextOneMobile;
    @BindView(R.id.mChange_Binding_next_one_btn)
    Button mChangeBindingNextOneBtn;
    private String realmobile;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_binding_next_one);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        mobile = getIntent().getStringExtra("mobile");
        realmobile=getIntent().getStringExtra("realmobile");
        mChangeBindingNextOneMobile.setText(mobile);
    }

    @OnClick({R.id.mChange_Binding_next_one_return, R.id.mChange_Binding_next_one_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChange_Binding_next_one_return:
                finish();
                break;
            case R.id.mChange_Binding_next_one_btn:
                Intent intent=new Intent(this,ChargeCodeActivity.class);
                intent.putExtra("realmobile",realmobile);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
                break;
        }
    }

}

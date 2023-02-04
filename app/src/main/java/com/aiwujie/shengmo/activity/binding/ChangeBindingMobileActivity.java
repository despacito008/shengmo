package com.aiwujie.shengmo.activity.binding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeBindingMobileActivity extends AppCompatActivity {

    @BindView(R.id.mChange_Binding_mobile_return)
    ImageView mChangeBindingMobileReturn;
    @BindView(R.id.mChange_Binding_mobile_mobile)
    TextView mChangeBindingMobileMobile;
    @BindView(R.id.mChange_Binding_mobile_btn)
    Button mChangeBindingMobileBtn;
    private String realmobile;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_binding_mobile);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mobile = getIntent().getStringExtra("mobile");
        realmobile=getIntent().getStringExtra("realmobile");
        mChangeBindingMobileMobile.setText("您的手机号：" + mobile);

    }


    @OnClick({R.id.mChange_Binding_mobile_return, R.id.mChange_Binding_mobile_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChange_Binding_mobile_return:
                finish();
                break;
            case R.id.mChange_Binding_mobile_btn:
                Intent intent=new Intent(ChangeBindingMobileActivity.this,ChangeBindingNextOneActivity.class);
                intent.putExtra("realmobile",realmobile);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
                break;
        }
    }

}

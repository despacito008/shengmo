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

public class ChangeBindingEmailActivity extends AppCompatActivity {

    @BindView(R.id.mChange_Binding_email_return)
    ImageView mChangeBindingEmailReturn;
    @BindView(R.id.mChange_Binding_email_email)
    TextView mChangeBindingEmailEmail;
    @BindView(R.id.mChange_Binding_email_btn)
    Button mChangeBindingEmailBtn;
    private String emails;
    private String realemails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_binding_email);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        emails = getIntent().getStringExtra("emails");
        realemails = getIntent().getStringExtra("realemails");
        mChangeBindingEmailEmail.setText("您的邮箱是：" + emails);
    }

    @OnClick({R.id.mChange_Binding_email_return, R.id.mChange_Binding_email_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChange_Binding_email_return:
                finish();
                break;
            case R.id.mChange_Binding_email_btn:
                Intent intent = new Intent(this, ChangeBindingEmailNextActivity.class);
                intent.putExtra("emails", emails);
                intent.putExtra("realemails", realemails);
                startActivity(intent);
                finish();
                break;
        }
    }
}

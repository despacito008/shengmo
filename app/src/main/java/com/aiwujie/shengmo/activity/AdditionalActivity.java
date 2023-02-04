package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdditionalActivity extends AppCompatActivity {

    @BindView(R.id.mAdditional_return)
    ImageView mAdditionalReturn;
    @BindView(R.id.mAdditional_confirm)
    TextView mAdditionalConfirm;
    @BindView(R.id.mAdditional_edittext)
    EditText mAdditionalEdittext;
    @BindView(R.id.mAdditional_count)
    TextView mAdditionalCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mAdditionalEdittext.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mAdditional_return, R.id.mAdditional_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mAdditional_return:
                finish();
                break;
            case R.id.mAdditional_confirm:
                Intent intent = new Intent();
                intent.putExtra("addition", mAdditionalEdittext.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdditionalCount.setText("(" + s.length() + "/256)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 256) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }
}

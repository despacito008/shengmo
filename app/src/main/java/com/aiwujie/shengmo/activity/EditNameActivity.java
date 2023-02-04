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
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditNameActivity extends AppCompatActivity {

    @BindView(R.id.mEdit_name_return)
    ImageView mEditNameReturn;
    @BindView(R.id.mEdit_name_Tijiao)
    TextView mEditNameTijiao;
    @BindView(R.id.mEdit_name_edittext)
    EditText mEditNameEdittext;
    @BindView(R.id.mEdit_name_count)
    TextView mEditNameCount;
    @BindView(R.id.mEdit_name_guifan)
    TextView mEditNameGuifan;
    private String[] minganText ={"奴"};
    private CharSequence temp;//监听前的文本
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent = getIntent();
        mEditNameEdittext.setText(intent.getStringExtra("name"));
        mEditNameEdittext.addTextChangedListener(new EditTitleChangedListener());
        mEditNameEdittext.setSelection(mEditNameEdittext.length());
    }

    @OnClick({R.id.mEdit_name_return, R.id.mEdit_name_Tijiao,R.id.mEdit_name_guifan})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mEdit_name_return:
                finish();
                break;
            case R.id.mEdit_name_Tijiao:
                String etContent = mEditNameEdittext.getText().toString().trim();
                if (etContent.length() > 10) {
                    ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
                    return;
                }
                if (etContent.indexOf(" ") == -1) {
                    if(etContent.indexOf(minganText[0])==-1&&etContent.indexOf(minganText[1])==-1&&etContent.indexOf(minganText[2])==-1&&etContent.indexOf(minganText[3])==-1&&etContent.indexOf(minganText[4])==-1&&etContent.indexOf(minganText[5])==-1&&etContent.indexOf(minganText[6])==-1&&etContent.indexOf(minganText[7])==-1&&etContent.indexOf(minganText[8])==-1&&etContent.indexOf(minganText[9])==-1&&etContent.indexOf(minganText[10])==-1&&etContent.indexOf(minganText[11])==-1&&etContent.indexOf(minganText[12])==-1&&etContent.indexOf(minganText[13])==-1) {
                        intent = new Intent();
                        intent.putExtra("name", mEditNameEdittext.getText().toString().trim());
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        ToastUtil.show(getApplicationContext(), "很抱歉，您的昵称包含有禁止使用的字，请重试~");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "昵称中不能有空格...");
                }
                break;
            case R.id.mEdit_name_guifan:
                intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "图文规范");
                intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.PicTextHtml);
                startActivity(intent);
                break;
        }
    }

    class EditTitleChangedListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEditNameCount.setText("(" + s.length() + "/10)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 10) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    ;
}

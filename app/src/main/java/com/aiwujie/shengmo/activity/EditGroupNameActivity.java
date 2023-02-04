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
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditGroupNameActivity extends AppCompatActivity {

    @BindView(R.id.mEdit_group_name_return)
    ImageView mEditGroupNameReturn;
    @BindView(R.id.mEdit_group_name_Tijiao)
    TextView mEditGroupNameTijiao;
    @BindView(R.id.mEdit_group_name_edittext)
    EditText mEditGroupNameEdittext;
    @BindView(R.id.mEdit_name_count)
    TextView mEditNameCount;
    private String oldName;
    private String[] minganText ={"奴"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_name);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(EditGroupNameActivity.this);
        Intent intent=getIntent();
        oldName=intent.getStringExtra("name");
        mEditGroupNameEdittext.setText(oldName);
        mEditGroupNameEdittext.addTextChangedListener(new EditTitleChangedListener());
        mEditGroupNameEdittext.setSelection(mEditGroupNameEdittext.length());
    }

    @OnClick({R.id.mEdit_group_name_return, R.id.mEdit_group_name_Tijiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEdit_group_name_return:
                finish();
                break;
            case R.id.mEdit_group_name_Tijiao:
                String groupName = mEditGroupNameEdittext.getText().toString().trim();
                //判断昵称中是否包含空格
                if(groupName.indexOf(" ")==-1) {
                    //if(groupName.indexOf(minganText[0])==-1&&groupName.indexOf(minganText[1])==-1&&groupName.indexOf(minganText[2])==-1&&groupName.indexOf(minganText[3])==-1&&groupName.indexOf(minganText[4])==-1&&groupName.indexOf(minganText[5])==-1&&groupName.indexOf(minganText[6])==-1&&groupName.indexOf(minganText[7])==-1&&groupName.indexOf(minganText[8])==-1&&groupName.indexOf(minganText[9])==-1&&groupName.indexOf(minganText[10])==-1&&groupName.indexOf(minganText[11])==-1&&groupName.indexOf(minganText[12])==-1&&groupName.indexOf(minganText[13])==-1) {
                        Intent intent = new Intent();
                        intent.putExtra("name", mEditGroupNameEdittext.getText().toString().trim());
                        setResult(RESULT_OK, intent);
                        finish();
//                    }else{
//                        ToastUtil.show(getApplicationContext(), "很抱歉，您的昵称包含有禁止使用的字，请重试~");
//                    }
                }else{
                    ToastUtil.show(getApplicationContext(),"群昵称中不能有空格...");
                }
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
            mEditNameCount.setText("("+s.length()+"/10)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length()>=10) {
                ToastUtil.show(getApplicationContext(),"字数已经达到了限制！");
            }
        }
    };
}

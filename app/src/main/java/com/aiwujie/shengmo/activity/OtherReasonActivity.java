package com.aiwujie.shengmo.activity;

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

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.ReportEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherReasonActivity extends AppCompatActivity {

    @BindView(R.id.mOtherReason_return)
    ImageView mOtherReasonReturn;
    @BindView(R.id.mOtherReason_confirm)
    TextView mOtherReasonConfirm;
    @BindView(R.id.mOtherReason_edittext)
    EditText mOtherReasonEdittext;
    @BindView(R.id.mOtherReason_count)
    TextView mOtherReasonCount;
    String uid;
    String did;
    String cmid;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_reason);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        uid=getIntent().getStringExtra("uid");
        did=getIntent().getStringExtra("did");
        cmid = getIntent().getStringExtra("cmid");
        mOtherReasonEdittext.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mOtherReason_return, R.id.mOtherReason_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mOtherReason_return:
                finish();
                break;
            case R.id.mOtherReason_confirm:
                mOtherReasonConfirm.setClickable(false);
                reportDynamic();
                break;
        }
    }

    private void reportDynamic() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",uid);
        map.put("did",did);
        if(!TextUtil.isEmpty(cmid)) {
            map.put("cmid",cmid);
        }
        map.put("reason",mOtherReasonEdittext.getText().toString());
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.ReportDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("otherreason", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    EventBus.getDefault().post(new ReportEvent());
                                    setResult(200);
                                    finish();
                                    break;
                                case 4001:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mOtherReasonCount.setText("(" + s.length() + "/256)");
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

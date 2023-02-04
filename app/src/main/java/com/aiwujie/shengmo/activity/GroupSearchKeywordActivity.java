package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GroupKeywordListviewAdapter;
import com.aiwujie.shengmo.bean.KeyWordData;
import com.aiwujie.shengmo.customview.MyListView;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.statistical.GroupSearchResultActivity;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupSearchKeywordActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    public static EditText mGroupSearchKeyWordEtSearch;
    @BindView(R.id.mGroupSearchKeyWord_cancel)
    TextView mGroupSearchKeyWordCancel;
    @BindView(R.id.mGroupSearchKeyWord_listview)
    MyListView mGroupSearchKeyWordListview;
    private InputMethodManager imm;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search_keyword);
        ButterKnife.bind(this);
        mGroupSearchKeyWordEtSearch = (EditText) findViewById(R.id.mGroupSearchKeyWord_et_search);
        StatusBarUtil.showLightStatusBar(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
//        EventBus.getDefault().register(this);
        setListener();
        getGroupKeyWord();
    }

    private void getGroupKeyWord() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.GetGroupSearchText, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(GroupSearchKeywordActivity.this)) {
                    return;
                }
                LogUtil.d(response);
                try {
                    KeyWordData keyWordData = new Gson().fromJson(response, KeyWordData.class);
                    mGroupSearchKeyWordListview.setAdapter(new GroupKeywordListviewAdapter(GroupSearchKeywordActivity.this, keyWordData.getData()));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    private void setListener() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mGroupSearchKeyWordEtSearch.setOnEditorActionListener(this);
    }

    @OnClick(R.id.mGroupSearchKeyWord_cancel)
    public void onViewClicked() {
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            /*隐藏软键盘*/
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
           // Intent intent = new Intent(this, SearchGroupActivity.class);
            Intent intent = new Intent(this, GroupSearchResultActivity.class);
            intent.putExtra("search", mGroupSearchKeyWordEtSearch.getText().toString().trim());
            startActivity(intent);
            return true;
        }
        return false;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void noEventBus(KeyWordEvent event) {
//        Log.i("mGroupSearchKey", "helloEventBus: "+"皱眉");
//        mGroupSearchKeyWordEtSearch.setText(event.getKeyword());
//        mGroupSearchKeyWordEtSearch.setSelection(mGroupSearchKeyWordEtSearch.getText().length());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch.setText("");
        GroupSearchKeywordActivity.mGroupSearchKeyWordEtSearch = null;
    }
}

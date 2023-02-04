package com.aiwujie.shengmo.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.RedWomenOwnerData;
import com.aiwujie.shengmo.customview.CommonDialog;
import com.aiwujie.shengmo.fragment.redwomenfragment.RedWomenCenterOne;
import com.aiwujie.shengmo.fragment.redwomenfragment.RedWomenCenterThree;
import com.aiwujie.shengmo.fragment.redwomenfragment.RedWomenCenterTwo;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class RedwomenPersonCenterActivity extends AppCompatActivity {

    @BindView(R.id.mRedwomen_person_center_return)
    ImageView mRedwomenPersonCenterReturn;
    @BindView(R.id.mRedwomen_person_center_title)
    TextView mRedwomenPersonCenterTitle;
    @BindView(R.id.activity_redwomen_person_center)
    PercentLinearLayout activityRedwomenPersonCenter;
    @BindView(R.id.mRedwomen_person_center_icon)
    ImageView mRedwomenPersonCenterIcon;
    @BindView(R.id.mRedwomen_person_center_name)
    TextView mRedwomenPersonCenterName;
    @BindView(R.id.mRedwomen_person_center_renzheng)
    ImageView mRedwomenPersonCenterRenzheng;
    @BindView(R.id.mRedwomen_person_center_Sex)
    TextView mRedwomenPersonCenterSex;
    @BindView(R.id.mRedwomen_person_center_role)
    TextView mRedwomenPersonCenterRole;
    @BindView(R.id.mRedwomen_person_center_city)
    TextView mRedwomenPersonCenterCity;
    @BindView(R.id.mRedwomen_person_center_serviceState)
    TextView mRedwomenPersonCenterServiceState;
    @BindView(R.id.mRedwomen_person_center_btn01)
    Button mRedwomenPersonCenterBtn01;
    @BindView(R.id.mRedwomen_person_center_btn02)
    Button mRedwomenPersonCenterBtn02;
    @BindView(R.id.mRedwomen_person_center_btn03)
    Button mRedwomenPersonCenterBtn03;
    @BindView(R.id.mRedwomen_person_center_bottom_ll)
    PercentLinearLayout mRedwomenPersonCenterBottomLl;
    private List<Button> buttons;
    private FragmentManager mFm;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private String[] mFragmentTagList = {"OneFragment", "TwoFragment", "ThreeFragment"};
    private Fragment mCurrentFragmen = null; // 记录当前显示的Fragment
    private Handler handler=new Handler();
    private String match_state;
    private String admin;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redwomen_person_center);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        ButterKnife.bind(this);
        setData();
        getUserCenterHeader();
    }

    private void setData() {
        uid=getIntent().getStringExtra("uid");
        admin= (String) SharedPreferencesUtils.getParam(getApplicationContext(),"admin","0");
        buttons=new ArrayList<>();
        buttons.add(mRedwomenPersonCenterBtn01);
        buttons.add(mRedwomenPersonCenterBtn02);
        buttons.add(mRedwomenPersonCenterBtn03);
        mRedwomenPersonCenterBtn01.setSelected(true);
        RedWomenCenterOne redWomenCenterOne = new RedWomenCenterOne();
        RedWomenCenterTwo redWomenCenterTwo = new RedWomenCenterTwo();
        RedWomenCenterThree redWomenCenterThree = new RedWomenCenterThree();
        mFragmentList.add(0, redWomenCenterOne);
        mFragmentList.add(1, redWomenCenterTwo);
        mFragmentList.add(2, redWomenCenterThree);
        mCurrentFragmen = mFragmentList.get(0);
        // 初始化首次进入时的Fragment
        mFm = getFragmentManager();
        android.app.FragmentTransaction transaction = mFm.beginTransaction();
        transaction.add(R.id.mRedwomen_person_center_bottom_ll, mCurrentFragmen, mFragmentTagList[0]);
        transaction.commitAllowingStateLoss();
    }

    private void getUserCenterHeader() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetUserCenterHeader, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        RedWomenOwnerData data=new Gson().fromJson(response,RedWomenOwnerData.class);
                        RedWomenOwnerData.DataBean datas=data.getData();
//                        if (datas.getHead_pic().equals("") || datas.getHead_pic().equals("http://59.110.28.150:888/")) {
                            if (datas.getHead_pic().equals("") || datas.getHead_pic().equals(NetPic())) {
                            mRedwomenPersonCenterIcon.setImageResource(R.mipmap.morentouxiang);
                        } else {
                            GlideImgManager.glideLoader(getApplicationContext(), datas.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, mRedwomenPersonCenterIcon, 0);
                        }
                        mRedwomenPersonCenterName.setText(datas.getNickname());
                        if (datas.getRealname().equals("0")) {
                            mRedwomenPersonCenterRenzheng.setVisibility(View.GONE);
                        } else {
                            mRedwomenPersonCenterRenzheng.setVisibility(View.VISIBLE);
                        }
                        mRedwomenPersonCenterSex.setText(datas.getAge());
                        if (datas.getSex().equals("1")) {
                            mRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
                            Drawable drawable = getResources().getDrawable(R.mipmap.nan);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            mRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null);
                        } else if (datas.getSex().equals("2")) {
                            mRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
                            Drawable drawable = getResources().getDrawable(R.mipmap.nv);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            mRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null);
                        } else if (datas.getSex().equals("3")) {
                            mRedwomenPersonCenterSex.setBackgroundResource(R.drawable.item_sex_san_bg);
                            Drawable drawable = getResources().getDrawable(R.mipmap.san);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            mRedwomenPersonCenterSex.setCompoundDrawables(drawable, null, null, null);
                        }
                        if (datas.getRole().equals("S")) {
                            mRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
                            mRedwomenPersonCenterRole.setText("斯");
                        } else if (datas.getRole().equals("M")) {
                            mRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
                            mRedwomenPersonCenterRole.setText("慕");
                        } else if (datas.getRole().equals("SM")) {
                            mRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_san_bg);
                            mRedwomenPersonCenterRole.setText("双");
                        } else if (datas.getRole().equals("~")) {
                            mRedwomenPersonCenterRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
                            mRedwomenPersonCenterRole.setText("~");
                        }
                        if (datas.getCity().equals("") && datas.getProvince().equals("")) {
                            mRedwomenPersonCenterCity.setText("未知");
                        } else {
                            if (datas.getProvince().equals(datas.getCity())) {
                                mRedwomenPersonCenterCity.setText(datas.getCity());
                            } else {
                                mRedwomenPersonCenterCity.setText(datas.getProvince() + " " + datas.getCity());
                            }
                        }
                        match_state = datas.getMatch_state();
                        switch (match_state){
                            case "0":
                                mRedwomenPersonCenterServiceState.setText("服务状态：非牵线会员");
                                mRedwomenPersonCenterTitle.setText("申请服务");
                                break;
                            case "1":
                                mRedwomenPersonCenterServiceState.setText("服务状态：资料待完善");
                                mRedwomenPersonCenterTitle.setText("个人中心");
                                break;
                            case "2":
                                mRedwomenPersonCenterServiceState.setText("服务状态：资料审核中");
                                mRedwomenPersonCenterTitle.setText("个人中心");
                                break;
                            case "3":
                                mRedwomenPersonCenterServiceState.setText("服务状态：正在牵线服务中");
                                mRedwomenPersonCenterTitle.setText("个人中心");
                                break;
                            case "4":
                                mRedwomenPersonCenterServiceState.setText("服务状态：服务已结束");
                                mRedwomenPersonCenterTitle.setText("个人中心");
                                break;
                            case "5":
                                mRedwomenPersonCenterServiceState.setText("服务状态：服务已结束");
                                mRedwomenPersonCenterTitle.setText("个人中心");
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    @OnClick({R.id.mRedwomen_person_center_btn01,R.id.mRedwomen_person_center_btn02,R.id.mRedwomen_person_center_btn03,R.id.mRedwomen_person_center_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRedwomen_person_center_return:
                finish();
                break;
            case R.id.mRedwomen_person_center_btn01:
                switchButtonState(0);
                switchFragment(mFragmentList.get(0), mFragmentTagList[0]);
                break;
            case R.id.mRedwomen_person_center_btn02:
                if(match_state.equals("0")&&admin.equals("0")) {
                    new CommonDialog(this,"仅限红娘牵线会员...");
                }else {
                    switchButtonState(1);
                    switchFragment(mFragmentList.get(1), mFragmentTagList[1]);
                }
                break;
            case R.id.mRedwomen_person_center_btn03:
                if(match_state.equals("0")&&admin.equals("0")) {
                    new CommonDialog(this,"仅限红娘牵线会员...");
                }else {
                    switchButtonState(2);
                    switchFragment(mFragmentList.get(2), mFragmentTagList[2]);
                }
                break;
        }
    }

    private void switchButtonState(int currentIndex) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelected(false);
        }
        buttons.get(currentIndex).setSelected(true);
    }

    // 转换Fragment
    private void switchFragment(Fragment to, String tag){
        if(mCurrentFragmen != to){
            FragmentTransaction transaction = mFm.beginTransaction();
            if(!to.isAdded()){
                // 没有添加过:
                // 隐藏当前的，添加新的，显示新的
                transaction.hide(mCurrentFragmen).add(R.id.mRedwomen_person_center_bottom_ll, to, tag).show(to);
            }else{
                // 隐藏当前的，显示新的
                transaction.hide(mCurrentFragmen).show(to);
            }
            mCurrentFragmen = to;
            transaction.commitAllowingStateLoss();
        }
    }

}

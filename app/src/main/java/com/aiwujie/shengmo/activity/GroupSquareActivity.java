package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.GroupSxEvent;
import com.aiwujie.shengmo.fragment.groupfragment.ClaimGroupFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.fragment.GroupClaimFragment;
import com.aiwujie.shengmo.kt.ui.fragment.MyGroupFragment;
import com.aiwujie.shengmo.kt.ui.fragment.NormalGroupFragment;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.FilterGroupUtils;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.GradientColorTextView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupSquareActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.mGroupSquare_return)
    ImageView mGroupSquareReturn;
    @BindView(R.id.mGroupSquare_title_tuijian)
    GradientColorTextView mGroupSquareTitleTuijian;
    @BindView(R.id.mGroupSquare_hot_dian)
    ImageView mGroupSquareHotDian;
    //    @BindView(R.id.mGroupSquare_title_near)
//    TextView mGroupSquareTitleNear;
//    @BindView(R.id.mGroupSquare_near_dian)
//    ImageView mGroupSquareNearDian;
    @BindView(R.id.mGroupSquare_title_my)
    GradientColorTextView mGroupSquareTitleMy;
    @BindView(R.id.mGroupSquare_my_dian)
    ImageView mGroupSquareMyDian;
    @BindView(R.id.mGroupSquare_make)
    TextView mGroupSquareMake;
    @BindView(R.id.mGroupSquare_viewpager)
    ViewPager mGroupSquareViewpager;
    @BindView(R.id.mGroupSquare_tv_search)
    TextView mGroupSquareTvSearch;
    @BindView(R.id.tv_layout_normal_search_text)
    TextView tvLayoutNormalSearchText;
    @BindView(R.id.tv_layout_normal_search_search)
    TextView tvLayoutNormalSearchSearch;
    @BindView(R.id.ll_normal_search)
    LinearLayout llNormalSearch;
    //    @BindView(R.id.mGroupSquare_ll_iv)
//    PercentLinearLayout mGroupSquareLlIv;
//    @BindView(R.id.mGroupSquare_title_new)
//    TextView mGroupSquareTitleNew;
//    @BindView(R.id.mGroupSquare_new_dian)
//    ImageView mGroupSquareNewDian;
//    @BindView(R.id.mGroupSquare_new_newCount)
//    TextView mGroupSquareNewNewCount;
    @BindView(R.id.mGroupSquare_Sx)
    ImageView mGroupSquareSx;
    @BindView(R.id.mGroupSquare_new_dian)
    ImageView mGroupSquareNewDian;
    @BindView(R.id.mGroupSquare_title_new)
    GradientColorTextView mGroupSquareTitleNew;
    @BindView(R.id.mGroupSquare_near_dian)
    ImageView mGroupSquareNearDian;
    @BindView(R.id.mGroupSquare_title_near)
    GradientColorTextView mGroupSquareTitleNear;
    @BindView(R.id.mGroupSquare_claim_dian)
    ImageView mGroupSquareClaimDian;
    @BindView(R.id.mGroupSquare_title_claim)
    GradientColorTextView mGroupSquareTitleClaim;
    @BindView(R.id.tv_group_create)
    TextView tvGroupCreate;
    @BindView(R.id.tv_group_rule)
    TextView tvGroupRule;
    @BindView(R.id.mMy_banner)
    Banner mMyBanner;
    @BindView(R.id.mMy_banner_close)
    ImageView mMyBannerClose;
    @BindView(R.id.mMy_banner_framlayout)
    FrameLayout mMyBannerFramlayout;
    @BindView(R.id.ll_group_square_header)
    LinearLayout llGroupSquareHeader;
    private List<Fragment> fragments;
    private List<GradientColorTextView> titles;
    private List<ImageView> tvs;
    Handler handler = new Handler();
    private boolean groupState = true;
    private PopupWindow mPopWindow;
    private String groupSex;
    private String groupSexual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_square);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        StatusBarUtil.showLightStatusBar(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        setData();
        setListener();
        getBanner();
//        int groupCount = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "tempGroupCount", 0);
//        if (groupCount != 0) {
//            mGroupSquareNewNewCount.setVisibility(View.VISIBLE);
//            if (groupCount > 99) {
//                mGroupSquareNewNewCount.setText("99+");
//            } else {
//                mGroupSquareNewNewCount.setText(groupCount + "");
//            }
//        }
//        String groupSwitch = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "groupSwitch", "0");
//        if (groupSwitch.equals("0")) {
//            mGroupSquareSx.setImageResource(R.mipmap.popshaixuan);
//        } else {
//            mGroupSquareSx.setImageResource(R.mipmap.shuaixuanlv);
//        }
        SharedPreferencesUtils.setParam(getApplicationContext(), "tempGroupCount", 0);
        EventBus.getDefault().post(new ClearRedPointEvent("clearGroupRedPoint", 2));
        String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
//        if (admin.equals("1") ) {
//            mGroupSquareMake.setVisibility(View.VISIBLE);
//        } else {
//            mGroupSquareMake.setVisibility(View.INVISIBLE);
//        }
        tvLayoutNormalSearchText.setText("群名称");
        findViewById(R.id.tv_qunweihu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupSquareActivity.this, VipWebActivity.class);
                intent.putExtra("title", "圣魔");
                intent.putExtra("path", HttpUrl.NetPic() + "/Home/Info/news/id/31");
                startActivity(intent);
            }
        });
    }

    private void getGroupState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.MakeGroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("groupsqueareresponse", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 4001:
                                case 4002:
                                case 5000:
                                    groupState = false;
                                    break;
                                case 4003:
                                    groupState = true;
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

    private void setData() {
        //区分首先展示哪个页面。viewFlag为1的时候，展示自己群组的界面
        int viewFlag = getIntent().getIntExtra("groupFlag", -1);
        fragments = new ArrayList<>();

        //fragments.add(new FragmentGroupNew());
        //fragments.add(new FragmentGroupNear());
        //fragments.add(new FragmentGroupClaim());
        //fragments.add(new FragmentGroupMy());

//        fragments.add(new FragmentGroupTj());
//        fragments.add(new NearGroupFragment());

        fragments.add(NormalGroupFragment.Companion.newInstance(0));
        fragments.add(NormalGroupFragment.Companion.newInstance(1));
        //fragments.add(new ClaimGroupFragment());
        fragments.add(new GroupClaimFragment());
        fragments.add(new MyGroupFragment());




        mGroupSquareViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mGroupSquareViewpager.setOffscreenPageLimit(4);

        titles = new ArrayList<>();
        titles.add(mGroupSquareTitleTuijian);
        //titles.add(mGroupSquareTitleNew);
        titles.add(mGroupSquareTitleNear);
        titles.add(mGroupSquareTitleClaim);
        titles.add(mGroupSquareTitleMy);
        tvs = new ArrayList<>();
        tvs.add(mGroupSquareHotDian);
        //tvs.add(mGroupSquareNewDian);
        tvs.add(mGroupSquareNearDian);
        tvs.add(mGroupSquareClaimDian);
        tvs.add(mGroupSquareMyDian);


        if (viewFlag == -1) {
            changeTabTextViewState(titles.get(0), 1);
            changeTabTextViewState(titles.get(1), 0);
            changeTabTextViewState(titles.get(2), 0);
            changeTabTextViewState(titles.get(3), 0);
            mGroupSquareHotDian.setVisibility(View.VISIBLE);
            selected(0);
        } else {
            changeTabTextViewState(titles.get(0), 0);
            changeTabTextViewState(titles.get(1), 0);
            changeTabTextViewState(titles.get(2), 0);
            changeTabTextViewState(titles.get(3), 1);
            mGroupSquareMyDian.setVisibility(View.VISIBLE);
            selected(3);
        }

        String whatFlag = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "groupFlag", "0");

        if (whatFlag.equals("0")) {
            mGroupSquareSx.setImageResource(R.drawable.shuaixuan);
        } else {
            mGroupSquareSx.setImageResource(R.drawable.iv_home_select_done);
        }
    }

    private void setListener() {
        mGroupSquareViewpager.addOnPageChangeListener(this);
//        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mGroupSquareEtSearch.setOnEditorActionListener(this);
        mMyBannerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBannerFramlayout.setVisibility(View.GONE);
            }
        });
        mMyBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = null;
                if (linkType.get(position).equals("0")) {
                    intent = new Intent(GroupSquareActivity.this, BannerWebActivity.class);
                    intent.putExtra("path", bannerUrl.get(position) + "?uid=" + MyApp.uid);
                    intent.putExtra("title", bannerTitle.get(position));
                } else if (linkType.get(position).equals("1")) {
                    intent = new Intent(GroupSquareActivity.this, TopicDetailActivity.class);
                    intent.putExtra("tid", linkId.get(position));
                    intent.putExtra("topictitle", bannerTitle.get(position));
                } else if (linkType.get(position).equals("2")) {
                    intent = new Intent(GroupSquareActivity.this, DynamicDetailActivity.class);
                    intent.putExtra("uid", MyApp.uid);
                    intent.putExtra("did", linkId.get(position));
                    intent.putExtra("pos", 1);
                    intent.putExtra("showwhat", 1);
                } else {
                    intent = new Intent(GroupSquareActivity.this, PesonInfoActivity.class);
                    intent.putExtra("uid", linkId.get(position));
                }
                startActivity(intent);
            }
        });
    }

    //R.id.mGroupSquare_title_near,R.id.mGroupSquare_title_new, R.id.mGroupSquare_Sx
    @OnClick({R.id.mGroupSquare_tv_search, R.id.ll_normal_search, R.id.mGroupSquare_return, R.id.mGroupSquare_title_tuijian, R.id.mGroupSquare_title_my, R.id.mGroupSquare_make, R.id.mGroupSquare_title_near, R.id.mGroupSquare_title_new,
            R.id.mGroupSquare_Sx, R.id.mGroupSquare_title_claim, R.id.tv_group_create, R.id.tv_group_rule})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mGroupSquare_return:
                finish();
                break;
            case R.id.mGroupSquare_title_tuijian:
                selected(0);
                break;
            case R.id.mGroupSquare_title_new:
                //selected(1);
                break;
            case R.id.mGroupSquare_title_near:
                selected(1);
                break;
            case R.id.mGroupSquare_title_claim:
                selected(2);
                break;
            case R.id.mGroupSquare_title_my:
                selected(3);
                break;
            case R.id.mGroupSquare_make:
            case R.id.tv_group_create:
//                if (groupState) {
//                    intent = new Intent(this, CreateGroupActivity.class);
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(this, MakeGroupFailActivity.class);
//                    startActivity(intent);
//                }
                String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
                if (vip.equals("1") || admin.equals("1")) {
                    if (groupState) {
                        intent = new Intent(this, CreateGroupActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(this, MakeGroupFailActivity.class);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(this, MakeGroupFailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mGroupSquare_Sx:
                showOption();
                break;
            case R.id.item_group_pop_llAll:
                groupSex = "";
                groupSexual = "";
                groupSx(false, "0");
                mGroupSquareSx.setImageResource(R.drawable.shuaixuan);
                break;
            case R.id.item_group_pop_llSexual:
                groupSex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mygroupSex", "");
                groupSexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mygroupSexual", "");
                if (!groupSex.equals("") && !groupSexual.equals("")) {
                    String whatSexual = FilterGroupUtils.isWhatSexual(groupSex, groupSexual);
                    groupSx(true, whatSexual);
                    mGroupSquareSx.setImageResource(R.drawable.iv_home_select_done);
                } else {
                    ToastUtil.show(getApplicationContext(), "请您重新登录,才可以使用此功能...");
                }
                break;
            case R.id.mGroupSquare_tv_search:
            case R.id.ll_normal_search:
                intent = new Intent(this, GroupSearchKeywordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_group_rule:
                if (mGroupSquareViewpager.getCurrentItem() == 2) {
                    Intent intent1 = new Intent(GroupSquareActivity.this, BannerWebActivity.class);
                    intent1.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/20");
                    intent1.putExtra("title", "圣魔");
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(GroupSquareActivity.this, BannerWebActivity.class);
                    intent2.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/21");
                    intent2.putExtra("title", "圣魔");
                    startActivity(intent2);
                }
                break;
        }
    }

    private void groupSx(boolean isOpen, String whatSexual) {
        SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", whatSexual);
        if (isOpen) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "groupSwitch", "1");
            // mGroupSquareSx.setImageResource(R.mipmap.shuaixuanlv);
        } else {
            SharedPreferencesUtils.setParam(getApplicationContext(), "groupSwitch", "0");
            // mGroupSquareSx.setImageResource(R.mipmap.popshaixuan);
        }
        EventBus.getDefault().post(new GroupSxEvent(whatSexual));
        EventBus.getDefault().post("group sx event");
        mPopWindow.dismiss();
    }

    private void selected(int j) {
//        for (int i = 0; i < fragments.size(); i++) {
//            titles.get(i).setSelected(false);
//            tvs.get(i).setVisibility(View.GONE);
//            changeTabTextViewState(titles.get(i),0);
//
//        }
//        titles.get(j).setSelected(true);
//        tvs.get(j).setVisibility(View.VISIBLE);
//        titles.get(j).setEnabled(false);
//        changeTabTextViewState(titles.get(j),1);
        mGroupSquareViewpager.setCurrentItem(j, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < fragments.size(); i++) {
            titles.get(i).setSelected(false);
            tvs.get(i).setVisibility(View.GONE);
            titles.get(i).setEnabled(true);
            changeTabTextViewState(titles.get(i), 0);
        }
        titles.get(position).setSelected(true);
        tvs.get(position).setVisibility(View.VISIBLE);
        titles.get(position).setEnabled(false);
        changeTabTextViewState(titles.get(position), 1);
        if (position == 3) {
            mGroupSquareSx.setVisibility(View.INVISIBLE);
//            String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
//            if ("1".equals(admin)) {
//                mGroupSquareSx.setVisibility(View.GONE);
//                tvGroupCreate.setVisibility(View.VISIBLE);
//            } else {
//                mGroupSquareSx.setVisibility(View.INVISIBLE);
//            }
        } else {
            mGroupSquareSx.setVisibility(View.VISIBLE);
            tvGroupCreate.setVisibility(View.GONE);
        }
//        if (position == 0 || position == 2) {
//            tvGroupRule.setVisibility(View.VISIBLE);
//        } else {
//            tvGroupRule.setVisibility(View.INVISIBLE);
//        }
//        if (position == 2) {
//            tvGroupRule.setText(R.string.claim_rule);
//        } else {
//            tvGroupRule.setText(R.string.group_rule);
//        }
        if (position == 0) {
            llGroupSquareHeader.setVisibility(View.VISIBLE);
        } else {
            llGroupSquareHeader.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    /*隐藏软键盘*/
//            if (imm.isActive()) {
//                imm.hideSoftInputFromWindow(
//                        v.getApplicationWindowToken(), 0);
//            }
//            Intent intent = new Intent(this, SearchGroupActivity.class);
//            intent.putExtra("search", mGroupSquareEtSearch.getText().toString().trim());
//            startActivity(intent);
//            return true;
//        }
//        return false;
//    }

    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ClearRedPointEvent event) {
        switch (event.getFlag()) {
            case 5:
//                mGroupSquareNewNewCount.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void showOption() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_group_pop, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(llNormalSearch);
        this.mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        TextView llAll = (TextView) contentView.findViewById(R.id.item_group_pop_llAll);
        LinearLayout llSexual = (LinearLayout) contentView.findViewById(R.id.item_group_pop_llSexual);
        TextView tvSexual = (TextView) contentView.findViewById(R.id.item_group_pop_tvSexual);
        String whatFlag = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "groupFlag", "0");

        if (whatFlag.equals("0")) {
            llAll.setTextColor(Color.parseColor("#b73acb"));
        } else {
            tvSexual.setTextColor(Color.parseColor("#b73acb"));
        }
        llAll.setOnClickListener(this);
        llSexual.setOnClickListener(this);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        this.getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getGroupState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void changeTabTextViewState(GradientColorTextView textView, int type) {
        if (type == 1) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textView.setColor(0xffffb1e6, 0xffdb57f3);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            textView.setColor(0xff666666, 0xff666666);
        }
    }

    private List<String> bannerTitle;
    private List<String> bannerPath;
    private List<String> bannerUrl;
    private List<String> linkType;
    private List<String> linkId;

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "12");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetSlideMore, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(GroupSquareActivity.this)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            //广告轮播
                            BannerUtils.setBannerView(mMyBanner);
                            BannerNewData data = new Gson().fromJson(response, BannerNewData.class);
                            bannerTitle = new ArrayList<>();
                            bannerPath = new ArrayList<>();
                            bannerUrl = new ArrayList<>();
                            linkType = new ArrayList<>();
                            linkId = new ArrayList<>();
                            for (int i = 0; i < data.getData().size(); i++) {
                                bannerTitle.add(data.getData().get(i).getTitle());
                                bannerPath.add(data.getData().get(i).getPath());
                                bannerUrl.add(data.getData().get(i).getUrl());
                                linkType.add(data.getData().get(i).getLink_type());
                                linkId.add(data.getData().get(i).getLink_id());
                            }
                            mMyBannerFramlayout.setVisibility(View.VISIBLE);
                            //设置图片集合
                            mMyBanner.setImages(bannerPath);
                            mMyBanner.start();
                            break;
                        case 4000:
                            mMyBannerFramlayout.setVisibility(View.GONE);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}

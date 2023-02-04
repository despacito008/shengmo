package com.aiwujie.shengmo.fragment.mainfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicNoticeActivity;
import com.aiwujie.shengmo.activity.HomeFilterActivity;
import com.aiwujie.shengmo.activity.SearchActivity;
import com.aiwujie.shengmo.eventbus.BigHornEvent;
import com.aiwujie.shengmo.eventbus.ChangeHighLayoutEvent;
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent;
import com.aiwujie.shengmo.eventbus.ClickHomeEvent;
import com.aiwujie.shengmo.eventbus.HomeFilterStateEvent;
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent;
import com.aiwujie.shengmo.fragment.homefragment.FragmentNearNear;
import com.aiwujie.shengmo.fragment.homefragment.FragmentNearOnline;
import com.aiwujie.shengmo.fragment.homefragment.FragmentNearRecommend;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.SearchUserActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.PublicScreenActivity;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageHighFragment;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageNearFragment;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageRecommendFragment;
import com.aiwujie.shengmo.utils.FilterGroupUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2016/12/18.
 */
public class FragmentNear extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.mNear_findPeople)
    ImageView mNearFindPeople;
    @BindView(R.id.mNear_Sx)
    RelativeLayout mNearSx;
    @BindView(R.id.rl_findPeople)
    RelativeLayout rlFindPeople;
    //    @BindView(R.id.mNear_viewpager)
//    MyViewpager mNearViewpager;
    @BindView(R.id.mNear_title_near)
    TextView mNearTitleNear;
    @BindView(R.id.mNear_near_dian)
    ImageView mNearNearDian;
    @BindView(R.id.mNear_title_recommend)
    TextView mNearTitleRecommend;
    @BindView(R.id.mNear_recommend_dian)
    ImageView mNearRecommendDian;
    @BindView(R.id.mNear_title_hot)
    TextView mNearTitleHot;
    @BindView(R.id.mNear_hot_dian)
    ImageView mNearHotDian;
    @BindView(R.id.mNear_viewpager)
    ViewPager mNearViewpager;
    @BindView(R.id.iv_near_sx)
    ImageView ivNearSx;
    @BindView(R.id.iv_home_notice_horn)
    ImageView ivHomeNoticeHorn;
    @BindView(R.id.tv_home_notice_horn)
    TextView tvHomeNoticeHorn;
    @BindView(R.id.rl_home_notice_horn)
    RelativeLayout rlHomeNoticeHorn;
    @BindView(R.id.tv_near_rule)
    TextView tvNearRule;

    @BindView(R.id.mNear_title_high)
    TextView mNearTitleHigh;

    @BindView(R.id.mNear_high_dian)
    ImageView mNearHighDian;
    /**
     * 判断是否是初始化Fragment
     */
    private boolean hasStarted = false;
    private PopupWindow mPopWindow;
    private String onlinestate = "";
    private String realname = "";
    private String age = "";
    private String sex = "";
    private String sexual = "";
    private String role = "";
    private String culture = "";
    private String monthly = "";
    private String upxzya = "";
    private List<Fragment> fragments;
    private List<TextView> titles;
    private List<ImageView> tvs;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
//        getSharedprefrence();
//        setData();
//        setListener();
        getSharedprefrence();
        setData();
        setListener();
//        TextView tvNearRule = view.findViewById(R.id.tv_near_rule);
//        tvNearRule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), BannerWebActivity.class);
//                intent.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/2");
//                intent.putExtra("title", "圣魔");
//                startActivity(intent);
//            }
//        });
        return view;
    }

    private void getSharedprefrence() {

        //左上角图标状态
        if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0").equals("1")) {
            mNearFindPeople.setImageResource(R.drawable.liebiao);
        } else {
            mNearFindPeople.setImageResource(R.drawable.gongge);
        }
        onlinestate = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "ckAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpMoney", "");
        upxzya = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpxzya", "");
        EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly, upxzya));
        showSxState();

        int labaallnum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "labaallnum", 0);
        if (labaallnum > 0) {
            tvHomeNoticeHorn.setText(labaallnum + "");
            tvHomeNoticeHorn.setVisibility(View.VISIBLE);
        }
    }


    private void setData() {
        fragments = new ArrayList<>();
        //fragments.add(new FragmentNearRecommend());
        //fragments.add(new FragmentNearNear());
//        fragments.add(new HomePageNearFragment());
//        fragments.add(new FragmentNearOnline());
        fragments.add(new HomePageRecommendFragment());
        fragments.add(HomePageNearFragment.Companion.newInstance(1));
        //fragments.add(HomePageNearFragment.Companion.newInstance(1));
        fragments.add(HomePageNearFragment.Companion.newInstance(7));
        fragments.add(HomePageHighFragment.Companion.newInstance());

        mNearViewpager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mNearViewpager.setOffscreenPageLimit(4);
        mNearViewpager.setCurrentItem(0, false);
        mNearTitleNear.setSelected(true);
        mNearTitleNear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = mNearTitleNear.getPaint();
        tp.setFakeBoldText(true);
        mNearNearDian.setVisibility(View.VISIBLE);
        titles = new ArrayList<>();
        titles.add(mNearTitleNear);
        titles.add(mNearTitleHot);
        titles.add(mNearTitleRecommend);
        titles.add(mNearTitleHigh);
        //titles.add(mNearTitleNew);
        tvs = new ArrayList<>();
        tvs.add(mNearNearDian);
        tvs.add(mNearHotDian);
        tvs.add(mNearRecommendDian);
        tvs.add(mNearHighDian);
        //tvs.add(mNearNewDian);
    }

    private void setListener() {
        mNearViewpager.addOnPageChangeListener(this);

        rlHomeNoticeHorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(getActivity(), DynamicNoticeActivity.class);
                Intent intent = new Intent(getActivity(), PublicScreenActivity.class);
                startActivity(intent);
            }
        });

    }

    //, R.id.mNear_banner, R.id.mNear_banner_close  R.id.mNear_title_new,
    @OnClick({R.id.rl_findPeople, R.id.mNear_title_near, R.id.mNear_title_hot, R.id.mNear_title_recommend, R.id.mNear_Sx, R.id.mNear_title_high})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_findPeople:
//                intent = new Intent(getActivity(), SeeActivity.class);
//                startActivity(intent);
//                boolean fastClick = isFastClick();
//                if (!fastClick) {
//                    return;
//                }
//                if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0").equals("0")) {
//                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "modle", "1");
//                    mNearFindPeople.setImageResource(R.drawable.liebiao);
//                    EventBus.getDefault().post(new ChangeLayoutEvent(1));
//                } else {
//                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "modle", "0");
//                    mNearFindPeople.setImageResource(R.drawable.gongge);
//                    EventBus.getDefault().post(new ChangeLayoutEvent(0));
//                }
                break;
//            case R.id.ll_menu_show_type:
////                intent = new Intent(getActivity(), SeeActivity.class);
////                startActivity(intent);
//                boolean fastClick2 = isFastClick();
//                if (!fastClick2) {
//                    return;
//                }
//                if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0").equals("0")) {
//                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "modle", "1");
//                    mNearFindPeople.setImageResource(R.drawable.liebiao);
//                    EventBus.getDefault().post(new ChangeLayoutEvent(1));
//                } else {
//                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "modle", "0");
//                    mNearFindPeople.setImageResource(R.drawable.gongge);
//                    EventBus.getDefault().post(new ChangeLayoutEvent(0));
//                }
//                break;
            case R.id.mNear_title_near:
                Selected(0);
                // EventBus.getDefault().post(new RecommendEvent());
                break;
            case R.id.mNear_title_hot:
                Selected(1);
                break;
            case R.id.mNear_title_recommend:
                Selected(2);
                break;
            case R.id.mNear_title_high:
                Selected(3);
                break;
//            case R.id.mNear_title_new:
//                Selected(3);
//                mFragmentNearNewCount.setVisibility(View.GONE);
//                //清除首页附近红点
//                EventBus.getDefault().post(new ClearRedPointEvent("clearNearRedPoint", 0));
//                break;
            case R.id.mNear_Sx:
                if (mNearViewpager.getCurrentItem() == 3) {
                    if (!isFastClick()) {
                        return;
                    }
                    if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "HighModel", "0").equals("0")) {
                        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "HighModel", "1");
                        EventBus.getDefault().post(new ChangeHighLayoutEvent(1));
                    } else {
                        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "HighModel", "0");
                        EventBus.getDefault().post(new ChangeHighLayoutEvent(0));
                    }
                } else {
                    showOption();
                }
                break;
            case R.id.item_home_pop_llNan:
                sex = "1";
                // mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.setSelected(true);
                simpleSp(sex, "", "1", false);
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
                break;
            case R.id.item_home_pop_llNv:
                sex = "2";
                // mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.setSelected(true);
                simpleSp(sex, "", "1", false);
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
                break;
            case R.id.item_home_pop_llCdts:
                sex = "3";
                // mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.setSelected(true);
                simpleSp(sex, "", "1", false);
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
                break;
            case R.id.item_home_pop_llAll:
                sex = "";
                // mNearSx.setImageResource(R.mipmap.popshaixuan);
                view.setSelected(false);
                simpleSp(sex, "", "", false);
                ivNearSx.setImageResource(R.drawable.shuaixuan);
                break;
            case R.id.item_home_pop_llSexual:
                //根据自己性取向筛选
                String ownerSex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "mysex", "");
                String ownerSexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "mysexual", "");
                if (!ownerSexual.equals("") && !ownerSex.equals("")) {
                    simpleSp(ownerSex, ownerSexual, "1", true);
                    view.setSelected(true);
                    // mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                    mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "nearSexual", "1");
                    ivNearSx.setImageResource(R.drawable.iv_home_select_done);
                } else {
                    ToastUtil.show(getActivity().getApplicationContext(), "请您重新登录,才可以使用此功能...");
                }
                break;
            case R.id.item_home_pop_llSx:

                intent = new Intent(getActivity(), HomeFilterActivity.class);
                getActivity().startActivity(intent);
                mPopWindow.dismiss();


//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                break;
        }
    }

    boolean isFistLoad = true;
    LinearLayout llMenuShowType;
    ImageView ivMenuShowType;
    TextView tvMenuShowType;

    private void showOption() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_pop, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(mNearSx);
        mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        LinearLayout llSx = (LinearLayout) contentView.findViewById(R.id.item_home_pop_llSx);
        LinearLayout llSexual = (LinearLayout) contentView.findViewById(R.id.item_home_pop_llSexual);
        TextView tvSexual = (TextView) contentView.findViewById(R.id.item_home_pop_tvSexual);
        TextView llNan = (TextView) contentView.findViewById(R.id.item_home_pop_llNan);
        TextView llNv = (TextView) contentView.findViewById(R.id.item_home_pop_llNv);
        TextView llNao = (TextView) contentView.findViewById(R.id.item_home_pop_llCdts);
        TextView llAll = (TextView) contentView.findViewById(R.id.item_home_pop_llAll);
        TextView tvSx = (TextView) contentView.findViewById(R.id.item_home_pop_isOpen);
        TextView item_home_pop_tvAdvancedScreening = contentView.findViewById(R.id.item_home_pop_tvAdvancedScreening);

        llMenuShowType = contentView.findViewById(R.id.ll_menu_show_type);
        ivMenuShowType = contentView.findViewById(R.id.iv_menu_show_type);
        tvMenuShowType = contentView.findViewById(R.id.tv_menu_show_type);

        //左上角图标状态
        if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0").equals("1")) {
            ivMenuShowType.setImageResource(R.drawable.gongge);
            tvMenuShowType.setText("宫格模式");
        } else {
            ivMenuShowType.setImageResource(R.drawable.liebiao);
            tvMenuShowType.setText("列表模式");
        }

        llMenuShowType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fastClick = isFastClick();
                if (!fastClick) {
                    return;
                }
                if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0").equals("0")) {
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "modle", "1");
                    ivMenuShowType.setImageResource(R.drawable.gongge);
                    tvMenuShowType.setText("宫格模式");
                    EventBus.getDefault().post(new ChangeLayoutEvent(1));
                } else {
                    SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "modle", "0");
                    ivMenuShowType.setImageResource(R.drawable.liebiao);
                    tvMenuShowType.setText("列表模式");
                    EventBus.getDefault().post(new ChangeLayoutEvent(0));
                }
            }
        });

        if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterZong", "").equals("1")) {
            tvSx.setText("已开启");
//            item_home_pop_tvAdvancedScreening.setTextColor(Color.parseColor("#b73acb"));//设置自定义高级筛选为紫色色
        } else {
            tvSx.setText("未开启");
//            item_home_pop_tvAdvancedScreening.setTextColor(Color.parseColor("#646464"));
        }
        onlinestate = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "ckAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpMoney", "");
        upxzya = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpxzya", "");

        if (getActivity().getIntent().getIntExtra("gotoByRegister", -1) != -1 && isFistLoad) {
            String filter = FilterGroupUtils.isWhatSexual2(sex, sexual);
            if ("1".equals(filter)) {
                llNan.setTextColor(Color.parseColor("#b73acb"));
            } else if ("2".equals(filter)) {
                llNv.setTextColor(Color.parseColor("#b73acb"));
            } else if ("3".equals(filter)) {
                llNao.setTextColor(Color.parseColor("#b73acb"));
            } else {
                llAll.setTextColor(Color.parseColor("#b73acb"));
            }
        } else {
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                llAll.setTextColor(Color.parseColor("#b73acb"));
            }
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("1") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                llNan.setTextColor(Color.parseColor("#b73acb"));
            }
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("2") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                llNv.setTextColor(Color.parseColor("#b73acb"));
            }
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("3") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                llNao.setTextColor(Color.parseColor("#b73acb"));
            }
        }


        //筛选取向选中
        if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "nearSexual", "").equals("1")) {
            tvSexual.setTextColor(Color.parseColor("#b73acb"));
        }
        llSx.setOnClickListener(this);
        llNan.setOnClickListener(this);
        llNv.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llNao.setOnClickListener(this);
        llSexual.setOnClickListener(this);
        isFistLoad = false;

        LinearLayout llSearch = contentView.findViewById(R.id.ll_normal_search);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), SearchActivity.class);
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llRule = contentView.findViewById(R.id.ll_normal_rule);
        llRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerWebActivity.class);
                intent.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/2");
                intent.putExtra("title", "圣魔");
                startActivity(intent);
            }
        });
    }

    private void simpleSp(String sex, String sexual, String switchZ, boolean isSxOwner) {
        onlinestate = "";
        realname = "";
        age = "";
        role = "";
        culture = "";
        monthly = "";
        upxzya = "";
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterZong", switchZ);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterLine", onlinestate);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "ckAuthen", realname);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterUpAge", age);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterRole", role);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterCulture", culture);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterUpxzya", upxzya);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterxzya", upxzya);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterUpMoney", monthly);
        SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "nearSexual", "0");
        if (isSxOwner) {
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterSex", sexual);
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterQx", sex);
            EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sexual, sex, role, culture, monthly, upxzya));
        } else {
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterSex", sex);
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "filterQx", sexual);
            EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly, upxzya));
        }
        mPopWindow.dismiss();
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < fragments.size(); i++) {
            titles.get(i).setSelected(false);
            titles.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            TextPaint tp = titles.get(i).getPaint();
            tp.setFakeBoldText(false);
            tvs.get(i).setVisibility(View.GONE);
//            titles.get(i).setEnabled(true);
        }
        titles.get(position).setSelected(true);
        titles.get(position).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = titles.get(position).getPaint();
        tp.setFakeBoldText(true);
        tvs.get(position).setVisibility(View.VISIBLE);
        if (position == 0) {
//            mNearSx.setVisibility(View.GONE);   //新需求所有fragment显示筛选图
            mNearSx.setVisibility(View.VISIBLE);
        } else {
            mNearSx.setVisibility(View.VISIBLE);
        }
//        titles.get(position).setEnabled(false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*
    切换fragment
     */
    public void Selected(int j) {
        for (int i = 0; i < fragments.size(); i++) {
            titles.get(i).setSelected(false);
            titles.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            TextPaint tp = titles.get(i).getPaint();
            tp.setFakeBoldText(false);
            tvs.get(i).setVisibility(View.GONE);
//            titles.get(j).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
//        titles.get(j).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        titles.get(j).setSelected(true);
        titles.get(j).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        TextPaint tp = titles.get(j).getPaint();
        tp.setFakeBoldText(true);
        tvs.get(j).setVisibility(View.VISIBLE);
        mNearViewpager.setCurrentItem(j, false);
        if (j == 0) {
//            mNearSx.setVisibility(View.GONE);       //新需求 fragment都显示筛选
            mNearSx.setVisibility(View.VISIBLE);


        } else {
            mNearSx.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }


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
    public void onMessages(ClickHomeEvent event) {
//        for (int i = 0; i < fragments.size(); i++) {
//            titles.get(i).setSelected(false);
//            titles.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
//            TextPaint tp = titles.get(i).getPaint();
//            tp.setFakeBoldText(false);
//            tvs.get(i).setVisibility(View.GONE);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(HomeFilterStateEvent event) {
        //右上角筛选图标状态
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    void showSxState() {

        onlinestate = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "ckAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpMoney", "");
        upxzya = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpxzya", "");

        if (getActivity().getIntent().getIntExtra("gotoByRegister", -1) != -1 && isFistLoad) {
            String filter = FilterGroupUtils.isWhatSexual2(sex, sexual);
            if ("1".equals(filter)) {
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
            } else if ("2".equals(filter)) {
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
            } else if ("3".equals(filter)) {
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
            } else {
                ivNearSx.setImageResource(R.drawable.shuaixuan);
            }
        } else {
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                ivNearSx.setImageResource(R.drawable.shuaixuan);
            }
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("1") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
            }
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("2") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
            }
            if (onlinestate.equals("") && realname.equals("") && age.equals("") && sex.equals("3") && sexual.equals("") && role.equals("") && culture.equals("") && monthly.equals("") && upxzya.equals("")) {
                ivNearSx.setImageResource(R.drawable.iv_home_select_done);
            }
        }

        //筛选取向选中
        if (SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "nearSexual", "").equals("1")) {
            ivNearSx.setImageResource(R.drawable.iv_home_select_done);
        }
    }

    /**
     * 两次点击间隔不能少于500ms
     */
    private static final int MIN_DELAY_TIME = 1500;
    private static long lastClickTime = 0;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) <= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(BigHornEvent event) {
        if (event.getIsRedPoint() == 1) {
            int labaallnum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "labaallnum", 0);
            if (labaallnum > 0) {
                tvHomeNoticeHorn.setText(labaallnum + "");
                tvHomeNoticeHorn.setVisibility(View.VISIBLE);
            }
        } else {
            tvHomeNoticeHorn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("onActivityResult", "FragmentNear");
    }
}

package com.aiwujie.shengmo.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.UserInfoBean;
import com.aiwujie.shengmo.utils.SafeCheckUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserInfoFragment extends Fragment {
    @BindView(R.id.tv_user_info_height)
    TextView tvUserInfoHeight;
    @BindView(R.id.tv_user_info_weight)
    TextView tvUserInfoWeight;
    @BindView(R.id.tv_user_info_education)
    TextView tvUserInfoEducation;
    @BindView(R.id.tv_user_info_salary)
    TextView tvUserInfoSalary;
    @BindView(R.id.tv_user_info_constellation)
    TextView tvUserInfoConstellation;
    @BindView(R.id.tv_user_info_sex_orientation)
    TextView tvUserInfoSexOrientation;
    @BindView(R.id.tv_user_info_contact_time)
    TextView tvUserInfoContactTime;
    @BindView(R.id.tv_user_info_contact_practice)
    TextView tvUserInfoContactPractice;
    @BindView(R.id.tv_user_info_degree)
    TextView tvUserInfoDegree;
    @BindView(R.id.tv_user_info_want)
    TextView tvUserInfoWant;
    Unbinder unbinder;
    @BindView(R.id.view_line_role)
    View viewLineRole;
    @BindView(R.id.tv_user_info_contact_txt)
    TextView tvUserInfoContactTxt;
    @BindView(R.id.tv_user_info_contact_practice_txt)
    TextView tvUserInfoContactPracticeTxt;
    @BindView(R.id.tv_user_info_degree_txt)
    TextView tvUserInfoDegreeTxt;
    @BindView(R.id.tv_user_info_want_txt)
    TextView tvUserInfoWantTxt;
    @BindView(R.id.tv_user_info_attribute)
    TextView tvUserInfoAttribute;
    @BindView(R.id.tv_user_info_attribute_txt)
    TextView tvUserInfoAttributeTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_user_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void showData(UserInfoBean.DataBean userInfo) {
        if (SafeCheckUtil.isActivityFinish(getActivity())) {
            return;
        }
        if (tvUserInfoAttributeTxt == null) {
            return;
        }
        suitText(tvUserInfoHeight, userInfo.getTall(), userInfo);
        suitText(tvUserInfoWeight, userInfo.getWeight(), userInfo);
        suitText(tvUserInfoEducation, userInfo.getCulture(), userInfo);
        suitText(tvUserInfoSalary, userInfo.getMonthly(), userInfo);
        suitText(tvUserInfoConstellation, userInfo.getStarchar(), userInfo);
        suitText(tvUserInfoSexOrientation, userInfo.getSexual(), userInfo);
        if (TextUtil.isEmpty(userInfo.getAttribute())) {
            tvUserInfoAttributeTxt.setVisibility(View.INVISIBLE);
            tvUserInfoAttribute.setVisibility(View.INVISIBLE);
            suitText(tvUserInfoContactTime, userInfo.getAlong(), userInfo);
            suitText(tvUserInfoContactPractice, userInfo.getExperience(), userInfo);
            suitText(tvUserInfoDegree, userInfo.getLevel(), userInfo);
            suitText(tvUserInfoWant, userInfo.getWant(), userInfo);
            tvUserInfoContactTxt.setText("接触");
            tvUserInfoContactPracticeTxt.setText("实践");
            tvUserInfoDegreeTxt.setText("程度");
            tvUserInfoWantTxt.setText("想找");
        } else {
            tvUserInfoAttributeTxt.setVisibility(View.VISIBLE);
            tvUserInfoAttribute.setVisibility(View.VISIBLE);
            suitText(tvUserInfoContactTime, userInfo.getAttribute(), userInfo);
            suitText(tvUserInfoContactPractice, userInfo.getAlong(), userInfo);
            suitText(tvUserInfoDegree, userInfo.getExperience(), userInfo);
            suitText(tvUserInfoWant, userInfo.getLevel(), userInfo);
            suitText(tvUserInfoAttribute, userInfo.getWant(), userInfo);
            tvUserInfoContactTxt.setText("属性");
            tvUserInfoContactPracticeTxt.setText("接触");
            tvUserInfoDegreeTxt.setText("实践");
            tvUserInfoWantTxt.setText("程度");
            tvUserInfoAttributeTxt.setText("想找");
        }

    }

    void suitText(TextView tv, String str, UserInfoBean.DataBean userInfo) {
        if (tv == null) {
            return;
        }
        if (TextUtil.isEmpty(str)) {
            tv.setText("-");
        } else {
            tv.setText(str);
        }
        if ("1".equals(userInfo.getSex())) { //男
            tv.setTextColor(getResources().getColor(R.color.boyColor));
        } else if ("2".equals(userInfo.getSex())) { //女
            tv.setTextColor(getResources().getColor(R.color.girlColor));
        } else {
            tv.setTextColor(getResources().getColor(R.color.cdtColor));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

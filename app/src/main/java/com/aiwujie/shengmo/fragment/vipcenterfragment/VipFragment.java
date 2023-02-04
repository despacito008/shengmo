package com.aiwujie.shengmo.fragment.vipcenterfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity;
import com.aiwujie.shengmo.adapter.VipGoodsRecyclerAdapter;
import com.aiwujie.shengmo.bean.VipGoodsBean;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VipFragment extends Fragment {

    @BindView(R.id.rv_fragment_vip)
    RecyclerView rvFragmentVip;
    Unbinder unbinder;
    private List<VipGoodsBean.DataBean> vipGoodsList;
    int currentIndex = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_vip, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        return view;
    }

    void getData() {
        HttpHelper.getInstance().getVipGoodsList("2", new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                VipGoodsBean vipGoodsBean = GsonUtil.GsonToBean(data, VipGoodsBean.class);
                vipGoodsList = vipGoodsBean.getData();
                VipGoodsRecyclerAdapter vipGoodsRecyclerAdapter = new VipGoodsRecyclerAdapter(getActivity(), vipGoodsList);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rvFragmentVip.setAdapter(vipGoodsRecyclerAdapter);
                rvFragmentVip.setLayoutManager(gridLayoutManager);
                vipGoodsRecyclerAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        String vip_price = vipGoodsList.get(position).getVip_price();
                        String subject = vipGoodsList.get(position).getSubject();
                        ((VipMemberCenterActivity)getActivity()).changeItem(subject,vip_price);
                        currentIndex = position;
                    }
                });
                currentIndex = 0;
//                String vip_price = vipGoodsList.get(0).getVip_price();
//                String subject = vipGoodsList.get(0).getSubject();
//                ((VipMemberCenterActivity)getActivity()).changeItem(subject,vip_price);
            }

            @Override
            public void onFail(String msg) {

            }
        });


//        vipGoodsList = new ArrayList<>();
//        addData("1", "1个月", "31天超值特权", "30", "", "");
//        addData("2", "3个月", "93天超值特权", "88", "90", "3");
//        addData("3", "6个月", "186天超值特权", "168", "180", "7");
//        addData("4", "12个月", "372天超值特权", "298", "360", "18");
//        VipGoodsBean vipGoodsBean = new VipGoodsBean();
//        vipGoodsBean.setData(vipGoodsList);
//        vipGoodsBean.setRetcode(2000);
//        vipGoodsBean.setMsg("");
//        LogUtil.d(GsonUtil.getInstance().toJson(vipGoodsBean));

    }

    public void changePrice() {
        if (currentIndex != -1 && vipGoodsList != null) {
            String vip_price = vipGoodsList.get(currentIndex).getVip_price();
            String subject = vipGoodsList.get(currentIndex).getSubject();
            ((VipMemberCenterActivity)getActivity()).changeItem(subject,vip_price);
        }
    }

    void addData(String sub, String name, String info, String price, String o_price, String discount) {
        VipGoodsBean.DataBean vgb1 = new VipGoodsBean.DataBean();
        vgb1.setSubject(sub);
        vgb1.setVip_discount(discount);
        vgb1.setVip_name(name);
        vgb1.setVip_price(price);
        vgb1.setVip_original_price(o_price);
        vgb1.setVip_info(info);
        vipGoodsList.add(vgb1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

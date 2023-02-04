package com.aiwujie.shengmo.fragment.redwomenfragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.bean.RedwomenMarkerData;
import com.aiwujie.shengmo.customview.ContactAlertDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.MatchmakerAdapter;
import com.aiwujie.shengmo.recycleradapter.ServiceInfoAdapter;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2017/12/15.
 */

public class RedWomenCenterOne extends Fragment {


    @BindView(R.id.mRedwomen_person_center_telephone)
    TextView mRedwomenPersonCenterTelephone;
    @BindView(R.id.mRedwomen_person_center_phone)
    TextView mRedwomenPersonCenterPhone;
    @BindView(R.id.rlv_service_info)
    RecyclerView rlvServiceInfo;
    //红娘uid
    MatchmakerAdapter matchmakerAdapter;
    List<RedwomenMarkerData.DataBean> list = new ArrayList<>();
    private Handler handler = new Handler();
    private String isSpeak;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_redwomen_one, null);
        bind = ButterKnife.bind(this, view);
        //是否禁言  forbiduserchat为禁言  resumeuser解除禁言
        isSpeak = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "nospeak", "1");
        getMatchmakerInfo();
        return view;
    }


    @OnClick({R.id.mRedwomen_person_center_telephone, R.id.mRedwomen_person_center_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRedwomen_person_center_telephone:
                ContactAlertDialog.openDialog("红娘热线", mRedwomenPersonCenterTelephone.getText().toString(), getActivity());
                break;
            case R.id.mRedwomen_person_center_phone:
                ContactAlertDialog.openDialog("红娘热线", mRedwomenPersonCenterPhone.getText().toString(), getActivity());
                break;
        }
    }

    private void getMatchmakerInfo() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.NewGetMatchmakerInfo, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {

                    private LinearLayoutManager linearLayoutManager;

                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("retcode") == 2000) {
                                RedwomenMarkerData data = new Gson().fromJson(response, RedwomenMarkerData.class);
                                list=data.getData();
                                if (matchmakerAdapter==null){
                                    matchmakerAdapter = new MatchmakerAdapter(getActivity(),list);
                                    matchmakerAdapter.setmOnItemClickListener(new MatchmakerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view,int position ) {
                                            Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
                                            intent.putExtra("uid", list.get(position).getUid());
                                            getActivity().startActivity(intent);
                                        }
                                    });
                                    linearLayoutManager = new LinearLayoutManager(getActivity());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                                    rlvServiceInfo.setLayoutManager(linearLayoutManager);
                                    rlvServiceInfo.setAdapter(matchmakerAdapter);
                                }else {
                                    matchmakerAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}

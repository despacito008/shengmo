package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.BankListAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BankListData;
import com.aiwujie.shengmo.eventbus.AddBankCardEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetBankcardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @BindView(R.id.mSetBankCard_return)
    ImageView mSetBankCardReturn;
    @BindView(R.id.mSetBankCard_add)
    TextView mSetBankCardAdd;
    @BindView(R.id.mSetBankCard_ll)
    PercentLinearLayout mSetBankCardLl;
    @BindView(R.id.mSetBankCard_listview)
    ListView mSetBankCardListview;
    Handler handler=new Handler();
    public static String bid;
    private BankListData data;
    private AlertDialog alertDialog;
    private String bankname;
    private String bankcard;
    private String realname;
    private String longbid;

    boolean isBandCard = false;
    boolean isBandAliPay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bankcard);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        setListener();
        getbankcard();
    }

    private void setListener() {
        mSetBankCardListview.setOnItemClickListener(this);
        mSetBankCardListview.setOnItemLongClickListener(this);
    }

    @OnClick({R.id.mSetBankCard_return, R.id.mSetBankCard_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSetBankCard_return:
                finish();
                break;
            case R.id.mSetBankCard_add:
                showAddTypePop();
                break;
        }
    }
    private void getbankcard() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getbankcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            data = new Gson().fromJson(response, BankListData.class);
                            switch (data.getRetcode()) {
                                case 2000:
                                    mSetBankCardListview.setVisibility(View.VISIBLE);
                                    mSetBankCardLl.setVisibility(View.GONE);
                                    BankListAdapter adapter = new BankListAdapter(SetBankcardActivity.this, data.getData());
                                    mSetBankCardListview.setAdapter(adapter);
                                    checkBandStatus(data.getData());
                                    break;
                                case 4002:
                                    mSetBankCardLl.setVisibility(View.VISIBLE);
                                    mSetBankCardListview.setVisibility(View.GONE);
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        }catch (JsonSyntaxException e){
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
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void helloEventBus(AddBankCardEvent event){
            getbankcard();
    }

    private void bankOperation() {
        alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_select_operation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        RelativeLayout rlEdit = (RelativeLayout) window.findViewById(R.id.item_select_operation_edit);
        RelativeLayout rlDefault = (RelativeLayout) window.findViewById(R.id.item_select_operation_set_default);
        RelativeLayout tvDelete= (RelativeLayout) window.findViewById(R.id.item_select_operation_delete);
        rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(SetBankcardActivity.this,AddBackCardActivity.class);
//                intent.putExtra("operation","edit");
//                intent.putExtra("realname",realname);
//                intent.putExtra("bankcard",bankcard);
//                intent.putExtra("bankname",bankname);
//                intent.putExtra("bid",longbid);
//                startActivity(intent);
                alertDialog.dismiss();
                if ("1".equals(data.getData().get(chooseIndex).getPay_status())) {
                    editAliPayAccount();
                } else {
                    editBankCardAccount();
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBankCard();
                alertDialog.dismiss();
            }
        });

        rlDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                setCardDefault();
            }
        });

    }

    private void deleteBankCard() {
        Map<String,String> map=new HashMap<>();
        map.put("bid",longbid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.Deletebankcard, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BankListData data = new Gson().fromJson(response,BankListData.class);
                        switch (data.getRetcode()){
                            case 2000:
                                ToastUtil.show(getApplicationContext(),data.getMsg());
                                getbankcard();
//                                EventBus.getDefault().post("addsuccess");
                                break;
                            case 50001:
                            case 50002:
                                EventBus.getDefault().post(new TokenFailureEvent());
                                break;
                            default:
                                ToastUtil.show(getApplicationContext(),data.getMsg());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bid=data.getData().get(position).getBid();
        if(!TextUtils.isEmpty(data.getData().get(position).getBankcard()) && WithdrawalsActivity.mWithdrawalsCard != null){
            WithdrawalsActivity.mWithdrawalsCard.setText("银行卡号: " + data.getData().get(position).getBankcard());
        }
        EventBus.getDefault().post(new AddBankCardEvent());
        Intent intent = new Intent();
        intent.putExtra("bid",bid);
        intent.putExtra("bank",data.getData().get(position).getBankname());
        intent.putExtra("account",data.getData().get(position).getBankcard());
        setResult(200,intent);
        finish();
    }

    int chooseIndex = 0;
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        realname=data.getData().get(position).getRealname();
        bankcard=data.getData().get(position).getBankcard();
        bankname=data.getData().get(position).getBankname();
        longbid=data.getData().get(position).getBid();
        chooseIndex = position;
        bankOperation();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void checkBandStatus(List<BankListData.DataBean> cardList) {
        for (BankListData.DataBean bank: cardList) {
            if ("1".equals(bank.getPay_status())) {
                isBandAliPay = true;
            } else if ("0".equals(bank.getPay_status())) {
                isBandCard = true;
            }
        }
    }

//    void showAddTypePop() {
//        if (isBandCard && isBandAliPay) {
//            ToastUtil.show(SetBankcardActivity.this,"每人最多绑定一张银行卡和一个支付宝账号");
//            return;
//        }
//        if (!isBandCard && isBandAliPay) {
//            AlertView alertView = new AlertView("请选择绑定类型", null, "取消", null,
//                    new String[]{"银行卡"},
//                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
//                @Override
//                public void onItemClick(Object o, int position, String data) {
//                   addBankCardAccount();
//                }
//            });
//            alertView.show();
//        }
//
//        if (isBandCard && !isBandAliPay) {
//            AlertView alertView = new AlertView("请选择绑定类型", null, "取消", null,
//                    new String[]{"支付宝"},
//                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
//                @Override
//                public void onItemClick(Object o, int position, String data) {
//                    addAliPayAccount();
//                }
//            });
//            alertView.show();
//        }
//
//        if (!isBandCard && !isBandAliPay) {
//            AlertView alertView = new AlertView("请选择绑定类型", null, "取消", null,
//                    new String[]{"支付宝", "银行卡"},
//                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
//                @Override
//                public void onItemClick(Object o, int position, String data) {
//                    if (data.equals("支付宝")) {
//                        addAliPayAccount();
//                    } else if (data.equals("银行卡")) {
//                        addBankCardAccount();
//                    }
//                }
//            });
//            alertView.show();
//        }
//    }

    void showAddTypePop() {
        if (isBandAliPay) {
            ToastUtil.show(SetBankcardActivity.this,"每人最多绑定一个支付宝账号");
            return;
        }

        AlertView alertView = new AlertView("请选择绑定类型", null, "取消", null,
                new String[]{"支付宝"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                addAliPayAccount();
            }
        });
        alertView.show();

    }

    void addBankCardAccount() {
        Intent intent=new Intent(SetBankcardActivity.this,AddBackCardActivity.class);
        intent.putExtra("operation","add");
        startActivity(intent);
    }

    void addAliPayAccount() {
        Intent intent=new Intent(SetBankcardActivity.this,AddAliPayAccountActivity.class);
        intent.putExtra("operation","add");
        startActivity(intent);
    }

    void editBankCardAccount() {
        Intent intent=new Intent(SetBankcardActivity.this,AddBackCardActivity.class);
        intent.putExtra("operation","edit");
        intent.putExtra("realname",realname);
        intent.putExtra("bankcard",bankcard);
        intent.putExtra("bankname",bankname);
        intent.putExtra("bid",longbid);
        startActivity(intent);
        alertDialog.dismiss();
    }

    void editAliPayAccount() {
        Intent intent=new Intent(SetBankcardActivity.this,AddAliPayAccountActivity.class);
        intent.putExtra("operation","edit");
        intent.putExtra("realname",realname);
        intent.putExtra("bankcard",bankcard);
        intent.putExtra("bankname",bankname);
        intent.putExtra("bid",longbid);
        startActivity(intent);
        alertDialog.dismiss();
    }

    void setCardDefault() {
        HttpHelper.getInstance().setCardDefault(longbid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(SetBankcardActivity.this,"设置成功");
                getbankcard();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(SetBankcardActivity.this,msg);
            }
        });
    }
}

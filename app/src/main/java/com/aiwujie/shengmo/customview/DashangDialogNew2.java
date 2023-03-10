package com.aiwujie.shengmo.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.FallingViewActivity;
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.adapter.PresentGridViewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.PresentData;
import com.aiwujie.shengmo.bean.SystemPresentData;
import com.aiwujie.shengmo.bean.WalletData;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.OwnerPresentEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by mac on 16/9/23.
 */
public class DashangDialogNew2 extends AlertDialog implements TextWatcher, View.OnClickListener {
    //    private final EditText et;
    private AlertDialog alertDialog;
    private Context context;
    private String dmid;
    private Handler handler = new Handler();
    private int pos;
    private String rewardcount;
    private int psid;
    private String fuid;
    private String[] titles = {"?????????", "??????", "??????", "??????", "????????????", "??????", "??????", "?????????", "?????????", "??????",
            "????????????", "??????", "?????????", "?????????", "????????????", "????????????", "????????????", "?????????", "666", "??????", "??????", "??????", "??????",
            "??????", "?????????", "??????", "??????"};
    private String[] titles2 = {"?????????", "??????", "??????", "??????", "????????????", "??????", "??????", "?????????", "?????????", "??????",
            "????????????", "??????", "?????????", "?????????", "????????????", "????????????", "????????????", "?????????", "666", "??????", "??????", "??????", "??????",
            "??????", "?????????", "??????", "??????","?????????", "??????", "?????????", "??????", "TT"};
    private String[] titletos = { "?????????", "??????", "?????????", "??????", "TT"};
    private String[] moneys = {"2??????","6??????","10??????","38??????","99??????","88??????","123??????","166??????","199??????","520??????","666??????","250??????","777??????","888??????","999??????","1314??????","1666??????","1999??????","666??????","999??????","1888??????","2899??????","3899??????","6888??????","9888??????","52000??????","99999??????"};
    private String[] moneytos = {"1??????","3??????","5??????","10??????","8??????"};
    private int[] presents = {R.mipmap.presentnew01,R.mipmap.presentnew02,R.mipmap.presentnew03,R.mipmap.presentnew04,R.mipmap.presentnew05,
            R.mipmap.presentnew06,R.mipmap.presentnew07,R.mipmap.presentnew08,R.mipmap.presentnew09,
            R.mipmap.presentnew10,R.mipmap.presentnew11,R.mipmap.presentnew12,R.mipmap.presentnew13,
            R.mipmap.presentnew14,R.mipmap.presentnew15,R.mipmap.presentnew16,R.mipmap.presentnew17,
            R.mipmap.presentnew18,R.mipmap.presentnew19,R.mipmap.presentnew20,R.mipmap.presentnew21,
            R.mipmap.presentnew22,R.mipmap.presentnew23,R.mipmap.presentnew24,R.mipmap.presentnew25,
            R.mipmap.presentnew26,R.mipmap.presentnew27};
    private int[] presenttos = {R.mipmap.presentnew28,R.mipmap.presentnew29,R.mipmap.presentnew30, R.mipmap.presentnew31,R.mipmap.presentnew32};
    private ViewPager mPager;
    private List<View> mPagerList;
    private List<PresentData> mDatas;
    private LinearLayout mLlDot;
    private LayoutInflater inflater;
    /**
     * ????????????
     */
    private int pageCount;
    /**
     * ????????????????????????
     */
    private int pageSize = 9;
    /**
     * ???????????????????????????
     */
    private int curIndex = 0;
    private AlertDialog selectDialog;
    //???????????????
    private int allmoney;
    private TextView allMoney;
    private ImageView ivGonggao;
    private EditText etCount;
    private TextView tvTtileTwo;
    private TextView tvTitle;
    private int buyOrSystemFlg=0;
    String uid;
    String dalaba="2";
    int zongdoudoushu;
    private LinearLayout duoxufanya;

    private ChatManagerKit chatManagerKit;

    public DashangDialogNew2(final Context context, String fuid) {
        super(context);
        this.context = context;
        this.fuid=fuid;
        this.uid=fuid;
        //??????????????????
        initDatas(context);
    }
    public DashangDialogNew2(final Context context, String dmid, int pos, String rewardcount, String uid) {
        super(context);
        this.context = context;
        this.dmid = dmid;
        this.pos = pos;
        this.rewardcount = rewardcount;
        this.uid=uid;
        //??????????????????
        initDatas(context);
    }

    public DashangDialogNew2(final Context context, String fuid,ChatManagerKit chatManager) {
        super(context);
        this.context = context;
        this.fuid=fuid;
        this.uid=fuid;
        this.chatManagerKit = chatManager;
        //??????????????????
        initDatas(context);
    }

    /**
     * ??????????????????
     */
    private void initDatas(final Context context) {
        View view = View.inflate(context, R.layout.dashang_dialog_viewpager_layout, null);
        mPager = (ViewPager) view.findViewById(R.id.dashang_dialog_viewpager);
        mLlDot = (LinearLayout) view.findViewById(R.id.dashang_dialog_dot);
        TextView tvMyMoney= (TextView) view.findViewById(R.id.dashang_dialog_modou);
        //??????????????????
        getmywallet(tvMyMoney);
        TextView tvChongzhi= (TextView) view.findViewById(R.id.dashang_dialog_chongzhi);
        tvTitle= (TextView) view.findViewById(R.id.dashang_dialog_title);
        tvTtileTwo= (TextView) view.findViewById(R.id.dashang_dialog_titletwo);
        tvTitle.setTextColor(Color.parseColor("#ff9d00"));
        tvChongzhi.setOnClickListener(this);
        tvTtileTwo.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        mDatas = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < titles.length; i++) {
            mDatas.add(new PresentData(titles[i], moneys[i], presents[i],i+10));
        }
        //????????????=??????/????????????????????????
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPager.setOffscreenPageLimit(pageCount);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            // ??????????????????inflate??????????????????
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
            PresentGridViewAdapter presentGridViewAdapter=new PresentGridViewAdapter(context,mDatas,i,pageSize);
            gridView.setAdapter(presentGridViewAdapter);
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(context.getApplicationContext(), mDatas.get((int) id).getName()+","+id, Toast.LENGTH_SHORT).show();
                    //???????????????id   ???10??????
                    psid= (int) (id+10);
                    //????????????
                    selectPresentCount(id);
                }
            });
        }
        //???????????????
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //????????????
        setOvalLayout();
        alertDialog = new Builder(context, R.style.MyDialog).create();
        alertDialog.setView(view);
        alertDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 90 / 100;//???????????????????????????
        lp.height = dm.heightPixels * 75 / 100;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(true);// ??????????????????Dialog?????????
        //?????????????????????????????????
        alertDialog.getWindow().setDimAmount(0);
        if (uid.equals(MyApp.uid)) {
            tvTtileTwo.setVisibility(View.GONE);
                }
    }

    private void selectPresentCount(long id) {
        View view = View.inflate(context, R.layout.dashang_count_layout, null);
        selectDialog = new Builder(context,R.style.TransparentDialog).create();
        selectDialog.setView(view);
        selectDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = selectDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 85 / 100;//???????????????????????????
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        selectDialog.getWindow().setAttributes(lp);
        selectDialog.setCanceledOnTouchOutside(false);// ??????????????????Dialog?????????
        ImageView iv= (ImageView) view.findViewById(R.id.dashang_count_iv);
        TextView tvName= (TextView) view.findViewById(R.id.dashang_count_name);
        allMoney= (TextView) view.findViewById(R.id.dashang_count_allMoney);
        duoxufanya = view.findViewById(R.id.duoxuanya);
        ivGonggao= (ImageView) view.findViewById(R.id.dashang_count_gonggao);
        View tvCancel=  view.findViewById(R.id.dashang_count_cancel);
        TextView tvZeng= (TextView) view.findViewById(R.id.dashang_count_zeng);
        etCount= (EditText) view.findViewById(R.id.dashang_count_et);
        etCount.setSelection(etCount.getText().toString().length());
        TextView tvPrice = view.findViewById(R.id.dashang_count_price);
        tvPrice.setText(moneys[(int)(id)]);
        if(buyOrSystemFlg==0) {
            iv.setImageResource(presents[(int) id]);
            tvName.setText(titles[(int) id]);
            allmoney = Integer.parseInt(moneys[(int) id].substring(0, moneys[(int) id].length() - 2));
            allMoney.setText(allmoney + " ??????");
            if (Integer.parseInt(moneys[(int) id].substring(0, moneys[(int) id].length() - 2)) >= 500) {
                ivGonggao.setImageResource(R.mipmap.yuandiantaozi);
                dalaba="1";
            }
        }else{
            iv.setImageResource(presenttos[(int) id]);
            tvName.setText(titletos[(int) id]);
            allmoney = Integer.parseInt(moneytos[(int) id].substring(0, moneytos[(int) id].length() - 2));
            allMoney.setText(allmoney + " ??????");
            if (Integer.parseInt(moneytos[(int) id].substring(0, moneytos[(int) id].length() - 2)) >= 500) {
                ivGonggao.setImageResource(R.mipmap.yuandiantaozi);
                dalaba="1";
            }
        }
        duoxufanya.setOnClickListener(this);
        ivGonggao.setOnClickListener(this);
        etCount.addTextChangedListener(this);
        tvCancel.setOnClickListener(this);
        tvZeng.setOnClickListener(this);
    }

    /**
     * ????????????
     */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // ?????????????????????
        mLlDot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // ??????????????????
                mLlDot.getChildAt(curIndex).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_normal);
                // ????????????
                mLlDot.getChildAt(position).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * Edittext??????
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().equals("")||Integer.parseInt(s.toString())==0){
            allMoney.setText(0+" ??????");
            if(allmoney>=500){
                ivGonggao.setImageResource(R.mipmap.yuandiantaozi);
                dalaba="1";
            }else{
                ivGonggao.setImageResource(R.mipmap.yuandiantaohui);
                dalaba="2";
            }
        }else {
            zongdoudoushu=allmoney * Integer.parseInt(s.toString());
            allMoney.setText(allmoney * Integer.parseInt(s.toString())+" ??????");
            if(allmoney * Integer.parseInt(s.toString())>=500){
                ivGonggao.setImageResource(R.mipmap.yuandiantaozi);
                dalaba="1";
            }else{
                ivGonggao.setImageResource(R.mipmap.yuandiantaohui);
                dalaba="2";
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dashang_dialog_chongzhi:
                alertDialog.dismiss();
                Intent intent=new Intent(context,MyPurseActivity.class);
                context.startActivity(intent);
                break;
            case R.id.dashang_count_cancel:
                selectDialog.dismiss();
                break;
            case R.id.dashang_count_zeng:
                if(etCount.getText().toString().equals("")||etCount.getText().toString().equals("0")){
                    ToastUtil.show(context.getApplicationContext(),"??????????????????...");
                }else {
                    alertdialog();
                }
                break;
            case R.id.dashang_dialog_title:
                switchPresent(0);
                break;
            case R.id.dashang_dialog_titletwo:
                //??????????????????
                switchPresent(1);
                break;
            case R.id.dashang_count_gonggao:
            case R.id.duoxuanya:
                if(allmoney>=500||zongdoudoushu>500){
                    if(dalaba.equals("1")){
                        ivGonggao.setImageResource(R.mipmap.yuandiantaohui);
                        dalaba="2";
                    }else {
                        ivGonggao.setImageResource(R.mipmap.yuandiantaozi);
                        dalaba="1";
                    }
                }
                break;
        }
    }

    private void switchPresent(final int presentType) {
        mDatas.clear();
        mLlDot.removeAllViews();
        curIndex=0;
        if(presentType==1) {
            getMySystemPresent();
            tvTitle.setTextColor(Color.parseColor("#ffffff"));
            tvTtileTwo.setTextColor(Color.parseColor("#ff9d00"));
        }else {
            tvTitle.setTextColor(Color.parseColor("#ff9d00"));
            tvTtileTwo.setTextColor(Color.parseColor("#ffffff"));
            for (int i = 0; i < titles.length; i++) {
                mDatas.add(new PresentData(titles[i], moneys[i], presents[i],i+10));
            }
            //????????????=??????/????????????????????????
            pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
            mPager.setOffscreenPageLimit(pageCount);
            mPagerList = new ArrayList<View>();
            for (int i = 0; i < pageCount; i++) {
                // ??????????????????inflate??????????????????
                GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
                PresentGridViewAdapter presentGridViewAdapter = new PresentGridViewAdapter(context, mDatas, i, pageSize);
                gridView.setAdapter(presentGridViewAdapter);
                mPagerList.add(gridView);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        buyOrSystemFlg=0;
                        //???????????????id   ???10??????
                        psid = (int) (id + 10);
                        //????????????
                        selectPresentCount(id);
                    }
                });
            }
            //???????????????
            mPager.setAdapter(new ViewPagerAdapter(mPagerList));
            //????????????
            if(pageCount!=0) {
                setOvalLayout();
            }
        }

    }

    private void getMySystemPresent() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",MyApp.uid);
        IRequestManager manager=RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetMyBasicGift, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("sizesize", "switchPresent: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final SystemPresentData data=new Gson().fromJson(response,SystemPresentData.class);
                            if(data.getData().size()!=0) {
                                for (int i = 0; i < data.getData().size(); i++) {
                                    if (!data.getData().get(i).getNum().equals("0")) {
                                        mDatas.add(new PresentData(titletos[Integer.parseInt(data.getData().get(i).getType()) - 37] + "x" + data.getData().get(i).getNum(), moneytos[Integer.parseInt(data.getData().get(i).getType()) - 37], presenttos[Integer.parseInt(data.getData().get(i).getType()) - 37],Integer.parseInt(data.getData().get(i).getType())));
                                    }
                                }
                            }
                            if(mDatas.size()!=0){
                                //????????????=??????/????????????????????????
                                pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
                                //????????????
                                if(pageCount!=0) {
                                    setOvalLayout();
                                }
                                mPager.setOffscreenPageLimit(pageCount);
                                mPagerList = new ArrayList<View>();
                                for (int i = 0; i < pageCount; i++) {
                                    // ??????????????????inflate??????????????????
                                    GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
                                    PresentGridViewAdapter presentGridViewAdapter = new PresentGridViewAdapter(context,  mDatas, i, pageSize);
                                    gridView.setAdapter(presentGridViewAdapter);
                                    mPagerList.add(gridView);
                                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            buyOrSystemFlg=1;
                                                //???????????????id   ???10??????
//                                                psid = (int) (Integer.parseInt(data.getData().get((int) id).getType()));
                                                psid=mDatas.get((int)id).getPsid();
                                                //??????????????????
                                                selectPresentCount(psid- 37);
                                            Log.i("systempresent", "onItemClick: "+psid+","+(psid-37));
                                        }
                                    });
                                }
                                //???????????????
                                mPager.setAdapter(new ViewPagerAdapter(mPagerList));
                            }else{
                                GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
                                PresentGridViewAdapter presentGridViewAdapter = new PresentGridViewAdapter(context,  mDatas, 0, pageSize);
                                gridView.setAdapter(presentGridViewAdapter);
                                mPagerList.add(gridView);
                                //???????????????
                                mPager.setAdapter(new ViewPagerAdapter(mPagerList));
                                ToastUtil.show(getContext().getApplicationContext(),"???????????????????????????...");
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

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public ViewPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return (mViewList.get(position));
        }

        @Override
        public int getCount() {
            if (mViewList == null)
                return 0;
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private void getmywallet(final TextView tvMoney) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Getmywallet, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WalletData data = new Gson().fromJson(response, WalletData.class);
                            tvMoney.setText("???????????????" + data.getData().getWallet());
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
    private void alertdialog() {
        JSONObject rewardObj=getRewardObj();
        new DonatePresentTask().execute(new DonateRequest(rewardObj.toString()));
        alertDialog.dismiss();
        selectDialog.dismiss();
//        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
//        dialog.setMessage("?????????????")
//                .setPositiveButton("??????", new OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setNegativeButton("??????", new OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                JSONObject rewardObj=getRewardObj();
//                new DonatePresentTask().execute(new DonateRequest(rewardObj.toString()));
//                alertDialog.dismiss();
//                selectDialog.dismiss();
//            }
//        }).create();
//        dialog.show();
    }


    private JSONObject getRewardObj() {
        JSONObject rewardObj=new JSONObject();
        try {
            if(fuid==null) {
                rewardObj.put("uid", MyApp.uid);
                rewardObj.put("did", dmid);
                rewardObj.put("amount", allmoney * Integer.parseInt(etCount.getText().toString()));
                rewardObj.put("psid", psid + "");
                rewardObj.put("num", etCount.getText().toString());
                rewardObj.put("dalaba", dalaba);
            }else{
                rewardObj.put("uid", MyApp.uid);
                rewardObj.put("fuid", fuid);
                rewardObj.put("beans", allmoney * Integer.parseInt(etCount.getText().toString()));
                rewardObj.put("num", etCount.getText().toString());
                rewardObj.put("type", psid + "");
                rewardObj.put("dalaba", dalaba);
                rewardObj.put("is_home", "2");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rewardObj;
    }

    class DonatePresentTask extends AsyncTask<DonateRequest, Void, String> {

        @Override
        protected String doInBackground(DonateRequest... params) {
            DonateRequest donateRequest = params[0];
            String data = null;
            String json=donateRequest.donateObjectStr;
            try {
                if(fuid==null) {
                    if(buyOrSystemFlg==0) {
                        //????????????
                        data = postJson(HttpUrl.RewardDynamicNew, json);
                    }else{
                        //????????????????????????
                        data = postJson(HttpUrl.RewardBasicGDynamic, json);
                    }
                }else{
                    if(buyOrSystemFlg==0) {
                        //????????????
                        data = postJson(HttpUrl.RewardOne, json);
                    }else{
                        //????????????????????????
                        data = postJson(HttpUrl.RewardOneBasicG, json);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                return;
            }
            try{
                JSONObject obj =new JSONObject(data);
                LogUtil.d(data.toString());
                if(fuid==null) {
                    if(buyOrSystemFlg==0) {
                        //????????????
                        switch (obj.getString("retcode")) {
                            case "2000":
                                ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                EventBus.getDefault().post(new DynamicRewardEvent(pos, (Integer.parseInt(rewardcount) + 1)));
                                Intent intent=new Intent(context, FallingViewActivity.class);
                                intent.putExtra("fallingFlag",psid-10);
                                context.startActivity(intent);
                                break;
                            case "3006":
                            case "4000":
                            case "4002":
                            case "4003":
                            case "4005":
                            case "4006":
                                ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                break;
                            case "4004":
                                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
                                dialog.setTitle("??????").setMessage("????????????,????????????????").setNegativeButton("??????", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, MyPurseActivity.class);
                                        context.startActivity(intent);
                                    }
                                }).setPositiveButton("??????", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                                break;
                        }
                    }else{
                        //????????????????????????
                        switch (obj.getString("retcode")) {
                            case "2000":
                                ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                EventBus.getDefault().post(new DynamicRewardEvent(pos, (Integer.parseInt(rewardcount) + 1)));
                                Intent intent=new Intent(context, FallingViewActivity.class);
                                intent.putExtra("fallingFlag",psid-10);
                                context.startActivity(intent);
                                break;
                            case "3006":
                            case "3007":
                            case "3008":
                                ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                break;
                        }
                    }
                }else{
                    if(buyOrSystemFlg==0) {
                        //????????????
                        switch (obj.getString("retcode")) {
                            case "2000":
                                sendPhone(titles2[psid-10]+"",etCount.getText().toString()+"",uid);
                                //ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                EventBus.getDefault().post(new OwnerPresentEvent());
                                Intent intent=new Intent(context, FallingViewActivity.class);
                                intent.putExtra("fallingFlag",psid-10);
                                context.startActivity(intent);
                                break;
                            case "3006":
                            case "4000":
                            case "4002":
                            case "4003":
                            case "4005":
                            case "4006":
                                ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                break;
                            case "4004":
                                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);
                                dialog.setTitle("??????").setMessage("????????????,????????????????").setNegativeButton("??????", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, MyPurseActivity.class);
                                        context.startActivity(intent);
                                    }
                                }).setPositiveButton("??????", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                                break;
                        }
                    }else{
                        //????????????????????????
                        switch (obj.getString("retcode")) {
                            case "2000":
                                sendPhone(titles2[psid-10]+"",etCount.getText().toString()+"",uid);
                                //ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                EventBus.getDefault().post(new OwnerPresentEvent());
                                Intent intent=new Intent(context, FallingViewActivity.class);
                                intent.putExtra("fallingFlag",psid-10);
                                context.startActivity(intent);
                                break;
                            case "3003":
                            case "3006":
                            case "3007":
                                ToastUtil.show(getContext().getApplicationContext(), obj.getString("msg"));
                                break;
                            case "50001":
                            case "50002":
                                EventBus.getDefault().post(new TokenFailureEvent());
                                break;

                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    private  String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic()+url).addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(),"url_token","")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class DonateRequest {
        String donateObjectStr;
        public DonateRequest(String donateObjectStr) {
            this.donateObjectStr = donateObjectStr;
        }
    }

    public void sendPhone(final String imageName, final String number, String chatUserid){

//        final Message_GeRen_LiWu info =new Message_GeRen_LiWu();
//        info.setImageName(imageName);
//        info.setNumber(number);
        //chatUserid ?????????????????????id   Conversation.ConversationType ???????????????????????????????????????????????????
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.PRIVATE,info);
//
//
//        RongIM.getInstance().sendMessage(message, info.toString(), null, new IRongCallback.ISendMessageCallback() {
//            @Override //????????????????????????????????????
//            public void onAttached(Message message) {
//
//            }
//            @Override//??????????????????
//            public void onSuccess(Message D) {
//                //getxiaohuitiao(uid);
//            }
//            @Override //??????????????????
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });
        List<String> members = new ArrayList<>();
        members.add(MyApp.uid);
        V2TIMManager.getInstance().getUsersInfo(members, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {
                MessageSendHelper.getInstance().sendTipMessage("??????ta??????????????????","ta????????????????????????");
                MessageSendHelper.getInstance().sendGiftMessage(imageName,number,"");
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                String name = v2TIMUserFullInfos.get(0).getNickName();
                MessageSendHelper.getInstance().sendTipMessage("??????ta??????????????????",name + "????????????????????????");
                MessageSendHelper.getInstance().sendGiftMessage(imageName,number,"");
            }
        });

    }


}


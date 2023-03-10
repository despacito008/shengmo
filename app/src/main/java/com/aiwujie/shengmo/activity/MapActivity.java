package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.SearchAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.InputTask;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//????????????

public class MapActivity extends AppCompatActivity implements AMapLocationListener, TextWatcher, AdapterView.OnItemClickListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.mMap_iv_search)
    ImageView mMapIvSearch;
    @BindView(R.id.mMap_et_search)
    EditText mMapEtSearch;
    @BindView(R.id.mMap_iv_cancel)
    TextView mMapIvCancel;
    @BindView(R.id.mMap_et_llSearch)
    PercentLinearLayout mMapEtLlSearch;
    @BindView(R.id.map_rl)
    PercentRelativeLayout mapRl;
    @BindView(R.id.map_listview)
    ListView mapListview;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.mMap_btn_ck)
    Button mMapBtnCk;
    @BindView(R.id.iv_map_return)
    ImageView ivMapReturn;
    private AMap aMap;
    private Marker locationMarker;
    private SearchAdapter mAdapter;
    //    private MarkerOptions markerOption;
    private CameraUpdate mUpdata;
    private double lat = 0;
    private double lng = 0;
    private String areaName;
    private InputMethodManager imm;
    private InputTask inputTask;
    private String province;
    private String city;
    private String detail;
    private Circle circle;
    private String mapflag;
    private String groupname;
    private String introduce;
    Handler handler = new Handler();
    private GeocodeSearch geocoderSearch;
    //??????AMapLocationClientOption??????
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient;
    private String headurl;
    private AlertDialog startDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        headurl = intent.getStringExtra("headurl");
        mapflag = intent.getStringExtra("mapflag");
        if (mapflag.equals("2")) {
            mMapBtnCk.setText("??????");
            groupname = intent.getStringExtra("groupname");
            introduce = intent.getStringExtra("introduce");
        }
        imm = (InputMethodManager)
                getSystemService(
                        Context.INPUT_METHOD_SERVICE);
//???activity??????onCreate?????????mMapView.onCreate(savedInstanceState)?????????????????????????????????
        mMapView.onCreate(savedInstanceState);
        //?????????????????????
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        mMapEtSearch.addTextChangedListener(this);
        mAdapter = new SearchAdapter(this);
        mapListview.setAdapter(mAdapter);
        mapListview.setOnItemClickListener(this);
        //????????????????????????????????????
        aMap.setOnCameraChangeListener(this);
        //????????????????????????
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        ivMapReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.mMap_iv_search, R.id.mMap_iv_cancel, R.id.mMap_btn_ck})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mMap_iv_search:
                mMapIvSearch.setVisibility(View.GONE);
                mMapEtLlSearch.setVisibility(View.VISIBLE);
                mMapEtSearch.setFocusable(true);
                mMapEtSearch.requestFocus();
                imm.showSoftInput(mMapEtSearch, InputMethodManager.RESULT_SHOWN);
                break;
            case R.id.mMap_iv_cancel:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
//                mMapIvSearch.setVisibility(View.VISIBLE);
//                mMapEtLlSearch.setVisibility(View.GONE);
                break;
            case R.id.mMap_btn_ck:
                if (mapflag.equals("1")) {
                    if (lat != 0 && lng != 0) {
                        Log.i("mapmapmap", "onClick: " + lat + "," + lng);
                        //????????????
                        findPeopleByMap();
                    } else {
                        ToastUtil.show(getApplicationContext(), "?????????????????????????????????????????????");
                    }
                } else {
                    if (lat != 0 && lng != 0) {
                        //?????????
                        makeGroup();
                    } else {
                        ToastUtil.show(getApplicationContext(), "?????????????????????????????????????????????");
                    }
                }
                break;
        }
    }

    private void makeGroup() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("groupname", groupname);
        map.put("introduce", introduce);
        map.put("lat", lat + "");
        map.put("lng", lng + "");
        map.put("province", province);
        map.put("city", city);
        map.put("group_pic", headurl);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.MakeGroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post("refreshgroup");
                                    CreateGroupActivity.instance.finish();
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                case 4007:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //??????????????????amapLocation?????????????????????
                province = aMapLocation.getProvince();
                city = aMapLocation.getCity();
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                detail = aMapLocation.getAddress();
                LatLng latLng = new LatLng(lat,
                        lng);//???????????????
                //??????Marker??????????????????
                if (locationMarker == null) {
                    Log.i("mapactivitymap", "onLocationChanged: " + "makernull");
                    //?????????????????????????????????,icon????????????????????????????????????????????????
                    locationMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.biaoji))).position(latLng).draggable(true));
                    locationMarker.showInfoWindow();//????????????indowindow
                    aMap.addText(new TextOptions().position(latLng).text(aMapLocation.getAddress()));
                    //???????????????????????????
                    locationMarker.setPositionByPixels(mMapView.getWidth() / 2, mMapView.getHeight() / 2);
                    // ????????????
                    circle = aMap.addCircle(new CircleOptions().
                            center(latLng).
                            radius(100).
                            fillColor(Color.argb(127, 255, 0, 255)).
                            strokeColor(Color.argb(127, 255, 0, 255)).
                            strokeWidth(2));
                } else {
                    Log.i("mapactivitymap", "onLocationChanged: " + "makers");
                    //???????????????????????????????????????
                    locationMarker.setPosition(latLng);
                }
                //??????????????????????????????,??????animateCamera??????????????????
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));//????????????:1.????????? 2.????????????
            } else {
                //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
        mMapView.onDestroy();
        if (inputTask != null) {
            inputTask = null;
        }
        locationMarker = null;
//        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        mLocationClient.stopLocation();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mapListview.setVisibility(View.VISIBLE);
        inputTask = new InputTask(this, mAdapter);
        inputTask.onSearch(s.toString(), MyApp.city);

    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            mapListview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lat = inputTask.getData().get(position).getLatitude();
        lng = inputTask.getData().get(position).getLongitude();
        province = inputTask.getData().get(position).getProvince();
        city = inputTask.getData().get(position).getCity();
        if (city.equals("")) {
            city = province;
        }
        detail = inputTask.getData().get(position).getText();
        areaName = inputTask.getData().get(position).getTitle();
        //15??????????????????0???????????????30????????????
        mUpdata = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(new LatLng(lat, lng), 15, 0, 30));//???????????????????????????????????????
        aMap.moveCamera(mUpdata);//???????????????
        aMap.clear();
        MarkerOptions markerOption = new MarkerOptions();
        LatLng latLng = new LatLng(inputTask.getData().get(position).getLatitude(), inputTask.getData().get(position).getLongitude());
        markerOption.position(latLng);
        markerOption.title(inputTask.getData().get(position).getTitle());
        markerOption.draggable(true);
        markerOption.icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.mipmap.biaoji)));
        aMap.addMarker(markerOption);
        mapListview.setVisibility(View.GONE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(
                    view.getApplicationWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView.onResume ()?????????????????????????????????
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()?????????????????????????????????
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)?????????????????????????????????
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
        lat = target.latitude;
        lng = target.longitude;
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        Log.i("mapmapmap", "onCameraChange: " + cameraPosition.target.toString());
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        detail = regeocodeResult.getRegeocodeAddress().getProvince() + regeocodeResult.getRegeocodeAddress().getCity() + regeocodeResult.getRegeocodeAddress().getDistrict() + regeocodeResult.getRegeocodeAddress().getCrossroads().get(0).getFirstRoadName();
        province = regeocodeResult.getRegeocodeAddress().getProvince();
        city = regeocodeResult.getRegeocodeAddress().getCity();
        if (city.equals("")) {
            city = province;
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private void findPeopleByMap() {
        String isvip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "");
        if (isvip.equals("1")) {
            Intent intent = new Intent(this, MapFindActivity.class);
            intent.putExtra("lat", lat + "");
            intent.putExtra("lng", lng + "");
            intent.putExtra("addrdetail", detail);
            startActivity(intent);
        } else {
            new VipDialog(this, "??????????????????VIP???????????????~");
        }
    }
}

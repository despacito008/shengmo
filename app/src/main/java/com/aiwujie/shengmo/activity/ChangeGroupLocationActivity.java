package com.aiwujie.shengmo.activity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.kt.util.NormalConstant;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.tim.location.LocationAddressAdapter;
import com.aiwujie.shengmo.utils.InputTask;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
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
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeGroupLocationActivity extends AppCompatActivity {
    @BindView(R.id.map_view_location)
    MapView mMapView;
    @BindView(R.id.rv_location)
    RecyclerView rvLocation;
    @BindView(R.id.tv_location_address)
    TextView tvLocationAddress;
    @BindView(R.id.tv_select_location_cancel)
    TextView tvSelectLocationCancel;
    @BindView(R.id.tv_select_location_confirm)
    TextView tvSelectLocationConfirm;
    @BindView(R.id.tv_select_location_title)
    TextView tvSelectLocationTitle;
    private CameraUpdate mUpdata;
    private double lat = 0;
    private double lng = 0;
    private InputMethodManager imm;
    private InputTask inputTask;
    private String province;
    private String city;
    private String detail;
    private Circle circle;
    private String mapflag;
    private String groupname;
    private String introduce;
    private AMap aMap;
    private Marker locationMarker;
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient;
    private GeocodeSearch geocoderSearch;
    float getZoomB;
    private List<GeocodeAddress> addressList;
    LocationAddressAdapter addressAdapter;
    private String groupId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        StatusBarUtil.showLightStatusBar(this, R.id.ll_select_location_title_bar);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        tvSelectLocationTitle.setText("???????????????");
        groupId = getIntent().getStringExtra("groupId");

        //?????????????????????
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                showCurrentLocation();
            }
        });
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        //mMapEtSearch.addTextChangedListener(this);
//        mAdapter = new SearchAdapter(this);
//        mapListview.setAdapter(mAdapter);
//        mapListview.setOnItemClickListener(this);
        getZoomB = 5f;
        //????????????????????????????????????
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (getZoomB != cameraPosition.zoom) {
                    getZoomB = cameraPosition.zoom;
                } else {
//                    LogUtil.d(cameraPosition.target.latitude);
//                    LogUtil.d(cameraPosition.target.longitude);
                    LatLng target = cameraPosition.target;
                    lat = target.latitude;
                    lng = target.longitude;
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), 200, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                }
            }
        });
        //????????????????????????
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
//                String address = regeocodeAddress.getFormatAddress()
//                        .replace(regeocodeAddress.getProvince(), "")
//                        .replace(regeocodeAddress.getCity(), "")
//                        .replace(regeocodeAddress.getDistrict(), "");
//                if (address.length() > regeocodeAddress.getTownship().length()) {
//                    address = address.replace(regeocodeAddress.getTownship(), "");
//                }
                lat =  regeocodeAddress.getPois().get(0).getLatLonPoint().getLatitude();
                lng = regeocodeAddress.getPois().get(0).getLatLonPoint().getLongitude();
                province = regeocodeAddress.getProvince();
                city = regeocodeAddress.getCity();

                tvLocationAddress.setText(regeocodeAddress.getProvince() + regeocodeAddress.getCity() + regeocodeAddress.getTownship());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {


            }
        });
       // showCurrentLocation();
//        addressList = new ArrayList<>();
//        addressAdapter = new LocationAddressAdapter(SelectLocationActivity.this,addressList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectLocationActivity.this);
//        rvLocation.setLayoutManager(linearLayoutManager);
//        rvLocation.setAdapter(addressAdapter);
        tvSelectLocationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSelectLocationConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(ChangeGroupLocationActivity.this);
                builder4.setMessage("??????????????????????????????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeGroupLocation();
                    }
                }).create().show();
            }
        });
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

    void showCurrentLocation() {
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        lng =Double.parseDouble(getIntent().getStringExtra("lng"));
        detail = province + city;
        LatLng latLng = new LatLng(lat, lng);//???????????????
        //??????Marker??????????????????
        if (locationMarker == null) {
            Log.i("mapactivitymap", "onLocationChanged: " + "makernull");
            //?????????????????????????????????,icon????????????????????????????????????????????????
            locationMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.biaoji))).position(latLng).draggable(true));
            locationMarker.showInfoWindow();//????????????indowindow
            aMap.addText(new TextOptions().position(latLng).text(detail));
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
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));//????????????:1.????????? 2.????????????
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    void changeGroupLocation() {
        HttpHelper.getInstance().changeGroupLocation(groupId, province, city, String.valueOf(lat), String.valueOf(lng), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(ChangeGroupLocationActivity.this)) {
                    return;
                }
                ToastUtil.show(ChangeGroupLocationActivity.this,"?????????????????????");
                setResult(NormalConstant.RESULT_CODE_OK);
                finish();
            }

            @Override
            public void onFail(int code, String msg) {
                if (SafeCheckUtil.isActivityFinish(ChangeGroupLocationActivity.this)) {
                    return;
                }
                ToastUtil.show(ChangeGroupLocationActivity.this,msg);
            }
        });
    }
}

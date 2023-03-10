package com.aiwujie.shengmo.tim.location;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.utils.InputTask;
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
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationInfoActivity extends AppCompatActivity {
    @BindView(R.id.iv_location_info_return)
    ImageView ivLocationInfoReturn;
    @BindView(R.id.map_view_location_info)
    MapView mapViewLocationInfo;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);
        ButterKnife.bind(this);
        lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        lng = Double.parseDouble(getIntent().getStringExtra("lng"));
        String detail = getIntent().getStringExtra("address");
        mapViewLocationInfo.onCreate(savedInstanceState);
        //?????????????????????
        if (aMap == null) {
            aMap = mapViewLocationInfo.getMap();
        }
        mLocationClient = new AMapLocationClient(getApplicationContext());
        LatLng latLng = new LatLng(lat, lng);//???????????????
        Marker locationMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(MyApp.getInstance().getResources(), R.mipmap.biaoji))).position(latLng).draggable(true));
        locationMarker.showInfoWindow();//????????????indowindow
        aMap.addText(new TextOptions().position(latLng).text(detail));
        //???????????????????????????
        locationMarker.setPositionByPixels(mapViewLocationInfo.getWidth() / 2, mapViewLocationInfo.getHeight() / 2);
        locationMarker.setPosition(latLng);
         //????????????
        Circle circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(80).
                fillColor(Color.argb(127, 255, 0, 255)).
                strokeColor(Color.argb(127, 255, 0, 255)).
                strokeWidth(2));

        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

        getZoomB = 15f;
        //????????????????????????????????????
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
            }
        });
        //??????????????????????????????,??????animateCamera??????????????????
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));//????????????:1.????????? 2.????????????
        //????????????????????????
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {


            }
        });
//        addressList = new ArrayList<>();
//        addressAdapter = new LocationAddressAdapter(SelectLocationActivity.this,addressList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectLocationActivity.this);
//        rvLocation.setLayoutManager(linearLayoutManager);
//        rvLocation.setAdapter(addressAdapter);
        ivLocationInfoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

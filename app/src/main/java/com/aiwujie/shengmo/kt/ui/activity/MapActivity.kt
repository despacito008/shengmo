package com.aiwujie.shengmo.kt.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.activity.CreateGroupActivity
import com.aiwujie.shengmo.application.MyApp
import com.aiwujie.shengmo.customview.VipDialog
import com.aiwujie.shengmo.eventbus.TokenFailureEvent
import com.aiwujie.shengmo.http.HttpUrl
import com.aiwujie.shengmo.kt.adapter.SearchAdapter
import com.aiwujie.shengmo.kt.util.showToast
import com.aiwujie.shengmo.net.IRequestCallback
import com.aiwujie.shengmo.net.RequestFactory
import com.aiwujie.shengmo.utils.InputTask
import com.aiwujie.shengmo.utils.SharedPreferencesUtils
import com.aiwujie.shengmo.utils.StatusBarUtil
import com.aiwujie.shengmo.utils.ToastUtil
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdate
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MapActivity : AppCompatActivity(), AMapLocationListener, TextWatcher, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    lateinit var mMapIvSearch: ImageView
    lateinit var mMapEtSearch: EditText
    lateinit var mMapIvCancel: TextView
    lateinit var mMapEtLlSearch: LinearLayout
    lateinit var mapRl: RelativeLayout
    lateinit var mapListview: RecyclerView
    lateinit var mMapView: MapView
    lateinit var mMapBtnCk: Button
    lateinit var ivMapReturn: ImageView
    var aMap: AMap? = null
    var locationMarker: Marker? = null
    var mAdapter: SearchAdapter? = null
    var mUpdata: CameraUpdate? = null
    var lat = 0.0
    var lng = 0.0
    var areaName: String? = null
    var imm: InputMethodManager? = null
    var inputTask: InputTask? = null
    var province: String? = null
    var city: String? = null
    var detail: String? = null
    var circle: Circle? = null
    var mapflag: String? = null
    var groupname: String? = null
    var introduce: String? = null
    val handler = Handler()
    var geocoderSearch: GeocodeSearch? = null

    //??????AMapLocationClientOption??????
    var mLocationOption: AMapLocationClientOption? = null
    var mLocationClient: AMapLocationClient? = null
    var headurl: String? = null
    val startDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        StatusBarUtil.showLightStatusBar(this)
        mMapIvSearch = findViewById(R.id.mMap_iv_search)
        mMapEtSearch = findViewById(R.id.mMap_et_search)
        mMapIvCancel = findViewById(R.id.mMap_iv_cancel)
        mMapEtLlSearch = findViewById(R.id.mMap_et_llSearch)
        mapRl = findViewById(R.id.map_rl)
        mapListview = findViewById(R.id.map_listview)
        mMapView = findViewById(R.id.map_view)
        mMapBtnCk = findViewById(R.id.mMap_btn_ck)
        ivMapReturn = findViewById(R.id.iv_map_return)


        headurl = intent.getStringExtra("headurl")
        mapflag = intent.getStringExtra("mapflag")
        if (mapflag == "2") {
            mMapBtnCk.text = "??????"
            groupname = intent.getStringExtra("groupname")
            introduce = intent.getStringExtra("introduce")
        }
        imm = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        //???activity??????onCreate?????????mMapView.onCreate(savedInstanceState)?????????????????????????????????
        mMapView.onCreate(savedInstanceState)
        //?????????????????????
        if (aMap == null) {
            aMap = mMapView.map
        }
        mLocationClient = AMapLocationClient(applicationContext)
        mLocationClient!!.setLocationListener(this)
        //?????????AMapLocationClientOption??????
        mLocationOption = AMapLocationClientOption()
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        mLocationOption!!.isOnceLocation = true
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption!!.isOnceLocationLatest = true
        mLocationClient!!.setLocationOption(mLocationOption)
        mLocationClient!!.startLocation()
        mMapEtSearch.addTextChangedListener(this)
        mAdapter = SearchAdapter(this)
        with(mapListview){
            layoutManager =LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
           adapter = mAdapter
        }

        mAdapter!!.setOnAddressClick(object :SearchAdapter.OnAddressClick{
            override fun onClick(view: View,position: Int) {

                lat = inputTask!!.getData()[position].latitude
                lng = inputTask!!.getData()[position].longitude
                province = inputTask!!.getData()[position].province
                city = inputTask!!.getData()[position].city
                if (city == "") {
                    city = province
                }
                detail = inputTask!!.getData()[position].text
                areaName = inputTask!!.getData()[position].title
                //15??????????????????0???????????????30????????????
                //15??????????????????0???????????????30????????????
                mUpdata = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(LatLng(lat, lng), 15F, 0F, 30F)) //???????????????????????????????????????

                aMap!!.moveCamera(mUpdata) //???????????????

                aMap!!.clear()
                val markerOption = MarkerOptions()
                val latLng = LatLng(inputTask!!.getData()[position].latitude, inputTask!!.getData()[position].longitude)
                markerOption.position(latLng)
                markerOption.title(inputTask!!.getData()[position].title)
                markerOption.draggable(true)
                markerOption.icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(resources,
                                        R.mipmap.biaoji)))
                aMap!!.addMarker(markerOption)
                mapListview.visibility = View.GONE
                if (imm!!.isActive) {
                    imm!!.hideSoftInputFromWindow(
                            view.getApplicationWindowToken(), 0)
                }
            }
        })

        //????????????????????????????????????
        aMap!!.setOnCameraChangeListener(this)
        //????????????????????????
        geocoderSearch = GeocodeSearch(this)
        geocoderSearch!!.setOnGeocodeSearchListener(this)
        ivMapReturn.setOnClickListener { finish() }
        mMapIvSearch.setOnClickListener {
            mMapIvSearch.visibility = View.GONE
            mMapEtLlSearch.visibility = View.VISIBLE
            mMapEtSearch.isFocusable = true
            mMapEtSearch.requestFocus()
            imm!!.showSoftInput(mMapEtSearch, InputMethodManager.RESULT_SHOWN)
        }
        mMapIvCancel.setOnClickListener {
            imm!!.hideSoftInputFromWindow(it.windowToken, 0)
            finish()
        }
        mMapBtnCk.setOnClickListener {
            if (mapflag == "1") {
                if (lat != 0.0 && lng != 0.0) {
                    Log.i("mapmapmap", "onClick: $lat,$lng")
                    //????????????
                    findPeopleByMap()
                } else {
                    ToastUtil.show(applicationContext, "?????????????????????????????????????????????")
                }
            } else {
                if (lat != 0.0 && lng != 0.0) {
                    //?????????
                    makeGroup()
                } else {
                    ToastUtil.show(applicationContext, "?????????????????????????????????????????????")
                }
            }
        }
    }
    open fun makeGroup() {
        val map: MutableMap<String, String?> = HashMap()
        map["uid"] = MyApp.uid
        map["groupname"] = groupname
        map["introduce"] = introduce
        map["lat"] = lat.toString() + ""
        map["lng"] = lng.toString() + ""
        map["province"] = province
        map["city"] = city
        map["group_pic"] = headurl
        val manager = RequestFactory.getRequestManager()
        manager.post(HttpUrl.MakeGroup, map, object : IRequestCallback {
            override fun onSuccess(response: String) {
                handler.post {
                    try {
                        val obj = JSONObject(response)
                        when (obj.getInt("retcode")) {
                            2000 -> {
                                ToastUtil.show(getApplicationContext(), obj.getString("msg"))
                                EventBus.getDefault().post("refreshgroup")
                                CreateGroupActivity.instance.finish()
                                finish()
                            }
                            4000, 4001, 4003, 4004, 4005, 4006, 4007 -> ToastUtil.show(getApplicationContext(), obj.getString("msg"))
                            50001, 50002 -> EventBus.getDefault().post(TokenFailureEvent())
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(throwable: Throwable) {}
        })
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation != null) {
            if (aMapLocation.errorCode == 0) {
                //??????????????????amapLocation?????????????????????
                province = aMapLocation.province
                city = aMapLocation.city
                lat = aMapLocation.latitude
                lng = aMapLocation.longitude
                detail = aMapLocation.address
                val latLng = LatLng(lat,
                        lng) //???????????????
                //??????Marker??????????????????
                if (locationMarker == null) {
                    Log.i("mapactivitymap", "onLocationChanged: " + "makernull")
                    //?????????????????????????????????,icon????????????????????????????????????????????????
                    locationMarker = aMap!!.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.biaoji))).position(latLng).draggable(true))
                    locationMarker!!.showInfoWindow()  //????????????indowindow
                    aMap!!.addText(TextOptions().position(latLng).text(aMapLocation.address))
                    //???????????????????????????
                    locationMarker!!.setPositionByPixels(mMapView!!.width / 2, mMapView.height / 2)
                    // ????????????
                    circle = aMap!!.addCircle(CircleOptions().center(latLng).radius(100.0).fillColor(Color.argb(127, 255, 0, 255)).strokeColor(Color.argb(127, 255, 0, 255)).strokeWidth(2f))
                } else {
                    Log.i("mapactivitymap", "onLocationChanged: " + "makers")
                    //???????????????????????????????????????
                    locationMarker!!.position = latLng
                }
                //??????????????????????????????,??????animateCamera??????????????????
                aMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f)) //????????????:1.????????? 2.????????????
            } else {
                //???????????????????????????ErrCode????????????????????????????????????????????????errInfo???????????????????????????????????????
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.errorCode + ", errInfo:" + aMapLocation.errorInfo)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
        mMapView!!.onDestroy()
        if (inputTask != null) {
            inputTask = null
        }
        locationMarker = null
        //        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        mLocationClient!!.stopLocation()
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        mapListview!!.visibility = View.VISIBLE
        inputTask = InputTask(this, mAdapter)
        inputTask!!.onSearch(s.toString(), MyApp.city)
    }


    override fun afterTextChanged(s: Editable) {
        if (s.toString().length == 0) {
            mapListview!!.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        //???activity??????onResume?????????mMapView.onResume ()?????????????????????????????????
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        //???activity??????onPause?????????mMapView.onPause ()?????????????????????????????????
        mMapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)?????????????????????????????????
        mMapView!!.onSaveInstanceState(outState)
    }

    override fun onCameraChange(cameraPosition: CameraPosition?) {}

    override fun onCameraChangeFinish(cameraPosition: CameraPosition) {
        val target = cameraPosition.target
        lat = target.latitude
        lng = target.longitude
        val query = RegeocodeQuery(LatLonPoint(lat, lng), 200F, GeocodeSearch.AMAP)
        geocoderSearch!!.getFromLocationAsyn(query)
        Log.i("mapmapmap", "onCameraChange: " + cameraPosition.target.toString())
    }

    override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult, i: Int) {
        detail = regeocodeResult.regeocodeAddress.province + regeocodeResult.regeocodeAddress.city + regeocodeResult.regeocodeAddress.district + regeocodeResult.regeocodeAddress.crossroads[0].firstRoadName
        province = regeocodeResult.regeocodeAddress.province
        city = regeocodeResult.regeocodeAddress.city
        if (city == "") {
            city = province
        }
    }

    override fun onGeocodeSearched(geocodeResult: GeocodeResult?, i: Int) {}

    open fun findPeopleByMap() {
        val isvip = SharedPreferencesUtils.getParam(applicationContext, "vip", "") as String
        if (isvip == "1") {
            val intent = Intent(this, MapFindActivity::class.java)
            intent.putExtra("lat", lat.toString() + "")
            intent.putExtra("lng", lng.toString() + "")
            intent.putExtra("addrdetail", detail)
            startActivity(intent)
        } else {
            VipDialog(this, "??????????????????VIP???????????????~")
        }
    }
}
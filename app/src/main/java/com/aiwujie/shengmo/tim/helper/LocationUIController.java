package com.aiwujie.shengmo.tim.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.LocationInfo;
import com.aiwujie.shengmo.tim.bean.LocationMessageBean;
import com.aiwujie.shengmo.tim.bean.SystemTipsMessageBean;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.location.LocationInfoActivity;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

public class LocationUIController {

    private static final String TAG = LocationUIController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final String data, final MessageInfo info, final int position) {

        // ??????????????????view?????????TUIKit?????????????????????
        View view = LayoutInflater.from(MyApp.instance()).inflate(R.layout.message_tim_location, null, false);
        parent.addMessageContentView(view);
        //parent.addMessageContentView(view);
        LocationMessageBean locationMessageBean = GsonUtil.GsonToBean(data, LocationMessageBean.class);
        final TextView tvLocation = view.findViewById(R.id.tv_message_tim_location);
        MapView mapView = view.findViewById(R.id.map_view_message_tim_location);
        final AMap aMap = mapView.getMap();
        tvLocation.setText(locationMessageBean.getContentDict().getAddressName());

        //??????????????????amapLocation?????????????????????
        final double lat = Double.parseDouble(locationMessageBean.getContentDict().getLatitude());
        final double lng = Double.parseDouble(locationMessageBean.getContentDict().getLongitude());
        String detail = locationMessageBean.getContentDict().getAddressName();
        LatLng latLng = new LatLng(lat, lng);//???????????????

        //?????????????????????????????????,icon????????????????????????????????????????????????
        Marker locationMarker = aMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(MyApp.getInstance().getResources(), R.mipmap.biaoji))).position(latLng).draggable(true));
        locationMarker.showInfoWindow();//????????????indowindow
       // aMap.addText(new TextOptions().position(latLng).text(detail));
        //???????????????????????????
        locationMarker.setPositionByPixels(mapView.getWidth() / 2, mapView.getHeight() / 2);
        locationMarker.setPosition(latLng);
        // ????????????
        Circle circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(30).
                fillColor(Color.argb(127, 255, 0, 255)).
                strokeColor(Color.argb(127, 255, 0, 255)).
                strokeWidth(2));

        //??????????????????????????????,??????animateCamera??????????????????
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));//????????????:1.????????? 2.????????????
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(false);
//        if (ChatContextUtil.getActivity() instanceof Activity) {
//            Bundle bundle = ((ChatActivity) ChatContextUtil.getActivity()).getSaveBundle();
//            mapView.onCreate(bundle);
//        }

        if (FinishActivityManager.getManager().currentActivity() instanceof ChatActivity) {
            Bundle bundle = ((ChatActivity) FinishActivityManager.getManager().currentActivity()).getSaveBundle();
            mapView.onCreate(bundle);
        }

        View fontView = view.findViewById(R.id.view_message_tim_location_font);

        fontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApp.getInstance(), LocationInfoActivity.class);
                intent.putExtra("lat",String.valueOf(lat));
                intent.putExtra("lng",String.valueOf(lng));
                intent.putExtra("address",tvLocation.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstance().startActivity(intent);
            }
        });
        final MessageLayout.OnItemLongClickListener onItemLongClickListener = ((MessageBaseHolder) parent).getOnItemClickListener();

        fontView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null){
                    onItemLongClickListener.onMessageLongClick(v, position, info);
                }
//                if (info.isSelf()) {
//                    if (FinishActivityManager.getManager().currentActivity() instanceof Activity) {
//                        new AlertDialog.Builder(FinishActivityManager.getManager().currentActivity()).setMessage("???????????????????????????")
//                                .setPositiveButton("???", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("???", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                V2TIMManager.getMessageManager().revokeMessage(info.getTimMessage(), new V2TIMCallback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(int code, String desc) {
//                                        ToastUtil.toastShortMessage("????????????" + code + desc);
//                                    }
//                                });
//                            }
//                        }).create().show();
//                    }
//                }
                return true;
            }
        });
    }
}

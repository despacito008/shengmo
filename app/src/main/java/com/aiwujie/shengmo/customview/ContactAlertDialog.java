package com.aiwujie.shengmo.customview;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;

public class ContactAlertDialog {
	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
	public static void openDialog(String title,final String phone,final Context context){
		View view = View.inflate(context, R.layout.item_contact_alertdialog, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.MyDialog).create();
		alertDialog.setView(view);
		alertDialog.show();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		Window window = alertDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = dm.widthPixels * 90 / 100;//宽高可设置具体大小
		alertDialog.getWindow().setAttributes(lp);
		alertDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
		TextView tvTitle=(TextView) window.findViewById(R.id.item_contact_alertdialog_title);
		TextView tvPhone=(TextView) window.findViewById(R.id.item_contact_alertdialog_phone);
		TextView tvConfirm=(TextView) window.findViewById(R.id.item_contact_alertdialog_confirm);
		tvTitle.setText(title);
		tvPhone.setText(phone);
		tvPhone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				// 检查是否获得了权限（Android6.0运行时权限）
				if (ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
					// 没有获得授权，申请授权
					if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
						//如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
						//如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
						//如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
						// 弹窗需要解释为何需要该权限，再次请求授权
						Toast.makeText(context.getApplicationContext(), "请授权！", Toast.LENGTH_LONG).show();
						// 帮跳转到该应用的设置界面，让用户手动授权
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", context.getPackageName(), null);
						intent.setData(uri);
						context.startActivity(intent);
					}else{
						// 不需要解释为何需要该权限，直接请求授权
						ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
					}
				}else {
					// 已经获得授权，可以打电话
					//用intent启动拨打电话  
					if(phone.contains("-")){
						String phones = phone.replace("-", "");
						Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phones));
						context.startActivity(intent);
					}else{
						Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
						context.startActivity(intent);
					}
				}

			}
		});
		tvConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
	}

}

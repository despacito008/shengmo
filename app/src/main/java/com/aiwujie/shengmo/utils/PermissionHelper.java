package com.aiwujie.shengmo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.AppOpsManagerCompat;

import com.aiwujie.shengmo.R;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2019/8/7.
 */

public class PermissionHelper {

    Context context;

    public PermissionHelper(Context context) {
        this.context=context;
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 检查权限列表
     *
     * @param context
     * @param op
     * 这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     * @param opString
     *   如判断定位权限 AppOpsManager.OPSTR_FINE_LOCATION
     * @return  @see 如果返回值 AppOpsManagerCompat.MODE_IGNORED 表示被禁用了

     */
    public static int checkOp(Context context, int op, String opString) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
//            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 23) {
                    return AppOpsManagerCompat.noteOp(context, opString,context.getApplicationInfo().uid,
                            context.getPackageName());
                }

            }
        }
        return -1;
    }

    /**
     * 显示定位服务未开启确认对话框
     */
    public  void showLocServiceDialog(final Context context) {
        new AlertDialog.Builder(context)//.setTitle("手机未开启位置服务")
                .setMessage("请在 设置-位置信息 (将位置服务打开))")
                .setNegativeButton("取消", null)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(getAppDetailSettingIntent());
                    }
                })
                .show();
    }

    /**
     * 显示定位权限被拒绝对话框
     */
    public  void showLocIgnoredDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        //builder.setTitle("手机已关闭位置权限");
        builder.setMessage("请您在手机设置-圣魔-权限中开启位置权限，以便正常使用全部功能（如您想隐藏位置，可在APP我的-设置-隐私中关闭位置）");

        //监听下方button点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                context.startActivity(getAppDetailSettingIntent());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private  Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }
}

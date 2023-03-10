package com.tencent.qcloud.tim.tuikit.live.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public final class UIUtil {
    static Handler mTimeHandler = new Handler();

    private static final String TAG = "UIUtil";

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static boolean isScreenPortrait(Context context) {
        int flag = context.getResources().getConfiguration().orientation;
        return flag == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.widthPixels;
        }
        return 0;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.heightPixels;
        }
        return 0;
    }

    public static int getRealScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(dm);
            return dm.heightPixels;
        }
        return 0;
    }

    public static int getRawScreenHeight(Context context) {
        if (isMeizu()) {
            return getScreenHeight(context) - getSmartBarHeight(context);
        } else {
            return getScreenHeight(context) - getNavigationBarHeight(context);
        }
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealMetrics(metrics);
        } else {
            wm.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }

    public static void setFullscreen(Activity activity, boolean isShowStatusBar, boolean isShowNavigationBar) {
        if(activity != null && activity.getWindow() != null) {
            setFullscreen(activity.getWindow(), isShowStatusBar, isShowNavigationBar);
        }
    }

    public static void setFullscreen(Window window, boolean isShowStatusBar, boolean isShowNavigationBar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        if(window != null) {
            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if(activity != null && activity.getWindow() != null) {
            setStatusBarColor(activity.getWindow(), color);
        }
    }

    public static void setStatusBarColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int height = -1;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }
        if (height <= 0) { // ????????????, ??????Android??????????????????25dp
            height = dp2px(context, 25);
        }
        return height;
    }

    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        //?????????????????????????????????,???????????????????????????huawei,???????????????meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }

    /*public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }*/
    public static boolean isNavigationBarShow(Activity activity, boolean hasNotch) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            int notchY = hasNotch ? size.y + getStatusBarHeight(activity.getApplicationContext()) : size.y;
            return realSize.y != notchY;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        Field f = null;
        Object obj_get = null;

        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f == null) {
                    return;
                }

                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }

                obj_get = f.get(imm);

                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    Log.i(TAG, "v_get.getContext() is " + v_get.getContext());
                    if (v_get.getContext() instanceof MutableContextWrapper) {
                        Context context = ((MutableContextWrapper) v_get.getContext()).getBaseContext();
                        Log.i(TAG, "contextis " + context + ", destContext is " + destContext);

                        if (context == destContext) {
                            // ???InputMethodManager???????????????context????????????????????????
                            f.set(imm, null); // ??????????????????path to gc??????
                        }
                    } else {
                        if (v_get.getContext() == destContext) {
                            // ???InputMethodManager???????????????context????????????????????????
                            f.set(imm, null); // ??????????????????path to gc??????
                        }
                    }

                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void fixAudioManagerLeak(Context ctx) {
        Log.i("AudioManagerLeak", "fixAudioManagerLeak");
        AudioManager am = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        Field field;
        try {
            field = am.getClass().getDeclaredField("mContext");
            field.setAccessible(true);
            field.set(am, null);
        } catch (Throwable e) {
            Log.i("AudioManagerLeak", e.getMessage());
        }
    }

    /**
     * ????????????meizu??????
     */
    private static boolean isMeizu() {
        return Build.BRAND.equals("Meizu");
    }

    /**
     * ??????????????????????????????????????????
     */
    private static int getSmartBarHeight(Context context) {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("mz_action_button_min_height");
            int height = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * ??????????????????????????????????????????????????????view?????????
     * ?????????????????????getNavigationBarHeight(context)?????????
     */
    public static int getContentViewHeight(Context context) {
        return getScreenHeight(context) - getStatusBarHeight(context);
    }

    /**
     * ???????????????????????????
     * ????????????????????????????????????????????????????????????
     * ????????????21???????????????
     * api min is 21 version
     *
     * @return navigationBar????????????
     */
    public static boolean isNavigationBarVisible(Window window) {
        boolean result = false;
        if (window == null) {
            return false;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (attributes != null) {
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            result = (((attributes.systemUiVisibility | decorView.getWindowSystemUiVisibility()) &
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) && (attributes.flags & WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) != 0;
        }
        Object decorViewObj = window.getDecorView();
        Class<?> clazz = decorViewObj.getClass();
        int mLastBottomInset = 0, mLastRightInset = 0, mLastLeftInset = 0;
        try {
            Field mLastBottomInsetField = clazz.getDeclaredField("mLastBottomInset");
            mLastBottomInsetField.setAccessible(true);
            mLastBottomInset = mLastBottomInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field mLastRightInsetField = clazz.getDeclaredField("mLastRightInset");
            mLastRightInsetField.setAccessible(true);
            mLastRightInset = mLastRightInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field mLastLeftInsetField = clazz.getDeclaredField("mLastLeftInset");
            mLastLeftInsetField.setAccessible(true);
            mLastLeftInset = mLastLeftInsetField.getInt(decorViewObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isNavBarToRightEdge = mLastBottomInset == 0 && mLastRightInset > 0;
        int size = isNavBarToRightEdge ? mLastRightInset : (mLastBottomInset == 0 && mLastLeftInset > 0 ? mLastLeftInset : mLastBottomInset);
        result = result && size > 0;
        return result;
    }


    /**
     * ???????????????
     */
    public static String formattedTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        //        if (hs.equals("00")) {
        //            formatTime = ms + ":" + ss;
        //        } else {
        formatTime = hs + ":" + ms + ":" + ss;
        //        }

        return formatTime;
    }

    public static boolean isAppRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcessInfo.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
    public static int getNavigationBarHeightIfRoom(Context context) {
        if(navigationGestureEnabled(context)){
            return 0;
        }
        return getCurrentNavigationBarHeight(((Activity) context));
    }

    /**
     * ??????????????????????????????????????? 0 ??????  1 ?????????
     *
     * @param context
     * @return
     */
    public static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????oppo????????????vivo????????????
     *
     * @return
     */
    public static String getDeviceInfo() {
        String brand = Build.BRAND;
        if(TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI")) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else {
            return "navigationbar_is_min";
        }
    }

    /**
     * ??????????????? ?????????????????????(??????????????????0)
     * @param activity
     * @return
     */
    public static int getCurrentNavigationBarHeight(Activity activity){
        if(isNavigationBarShown(activity)){
            return getNavigationBarHeight(activity);
        } else{
            return 0;
        }
    }

    /**
     * ??????????????? ????????????????????????
     * @param activity
     * @return
     */
    public static boolean isNavigationBarShown(Activity activity){
        //????????????view,???????????????????????????????????????
        View view  = activity.findViewById(android.R.id.navigationBarBackground);
        if(view == null){
            return false;
        }
        int visible = view.getVisibility();
        if(visible == View.GONE || visible == View.INVISIBLE){
            return false ;
        }else{
            return true;
        }
    }

    /**
     * ??????????????? ???????????????(??????????????????)
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height","dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * ????????????????????????
     * @param c
     */
    public static PowerManager.WakeLock setGoToSleep(Context c){
        PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
        wakeLock.acquire();
        return wakeLock;
    }

    /**
     * ????????????????????????
     * @param c
     */
    public static void setGoToWakeLock(final Context c){
        mTimeHandler.postDelayed(new Runnable(){
            public void run(){
                PowerManager.WakeLock powerWakelock =  setGoToSleep(c);
                powerWakelock.release();
            }
        }, 10*1000);
    }
}

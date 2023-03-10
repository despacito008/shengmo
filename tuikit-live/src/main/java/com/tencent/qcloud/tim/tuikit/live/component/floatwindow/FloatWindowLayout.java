package com.tencent.qcloud.tim.tuikit.live.component.floatwindow;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.tencent.liteav.custom.Constents;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Config;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowLayout extends ConstraintLayout {

    private static final String TAG = "FloatWindowLayout";

    public final int OP_SYSTEM_ALERT_WINDOW = 24;                               // 支持TYPE_TOAST悬浮窗的最高API版本

    private static FloatWindowLayout    sInstance;
    private Context                     mContext;
    private View                        mViewRoot;
    private RelativeLayout              mLayoutDisplayContainer;
    private ImageView                   mButtonCloseWindows;
    private WindowManager               mWindowManager;                         // 悬浮窗窗口管理器
    private WindowManager.LayoutParams  mWindowParams;                          // 悬浮窗布局参数
    private IntentParams                mIntentParams;

    public int mWindowMode = Constants.WINDOW_MODE_FULL;

    private FloatWindowLayout.FloatWindowLayoutDelegate mFloatWindowLayoutDelegate;

    private OnTouchFloatWindowListener mOnTouchFloatWindowListener;

    public final static class FloatWindowRect {
        public int x;
        public int y;
        public int width;
        public int height;

        public FloatWindowRect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public final static class IntentParams {
        private Class<?> activityClass;
        public int roomId;
        public String anchorId;
        public boolean useCdn;
        public String cdnURL;
        boolean isNormalFloat = true; //小窗模式下微信支付的bug

        public IntentParams(Class<?> activityClass, int roomId, String anchorId, boolean useCdn, String cdnURL) {
            this.activityClass = activityClass;
            this.roomId = roomId;
            this.anchorId = anchorId;
            this.useCdn = useCdn;
            this.cdnURL = cdnURL;
            isNormalFloat = true;
        }



        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("IntentParams:").append("activityClass=").append(activityClass).append(";roomId=").append(roomId).append(";anchorId=").append(anchorId)
                    .append(";useCdn=").append(useCdn).append(";cdnURL=").append(cdnURL);
            return sb.toString();
        }
    }

    public static synchronized FloatWindowLayout getInstance() {
        if (sInstance == null) {
            sInstance = new FloatWindowLayout(TUIKitLive.getAppContext());
        }
        return sInstance;
    }

    public FloatWindowLayout(Context context) {
        super(context);
        initView(context);
    }

    public void setFloatWindowLayoutDelegate(FloatWindowLayout.FloatWindowLayoutDelegate floatWindowLayoutDelegate) {
        mFloatWindowLayoutDelegate = floatWindowLayoutDelegate;
    }

    public void setOnTouchFloatWindowListener(OnTouchFloatWindowListener mOnTouchFloatWindowListener) {
        this.mOnTouchFloatWindowListener = mOnTouchFloatWindowListener;
    }

    private void initView(Context context) {
        mContext = context;
        mViewRoot = inflate(context, R.layout.live_layout_float_window, this);
        mLayoutDisplayContainer = mViewRoot.findViewById(R.id.rl_display_container);
        mButtonCloseWindows = mViewRoot.findViewById(R.id.btn_close);
        initFloatWindowParams();
    }

    public boolean showFloatWindow(View view, FloatWindowLayout.IntentParams intentParam) {
        if (!Config.enableFloatWindow()) {
            return false;
        }
        if (!requestPermission(mContext, OP_SYSTEM_ALERT_WINDOW)) {
            Toast.makeText(getContext(), "请手动打开悬浮窗口权限", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            mIntentParams = intentParam;
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            mLayoutDisplayContainer.addView(view);
            mWindowManager.addView(mViewRoot, mWindowParams);
            Constents.liveView = view;
            Constents.isShowAudienceFloatWindow = true;
            //延迟设置点击事件
            mViewRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewRoot.setOnTouchListener(new FloatingOnTouchListener());
                }
            },1000);
            mButtonCloseWindows.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeFloatWindow();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "悬浮播放失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        startHeaderReport();
        return true;
    }

    public String getAnchorId() {
        if (mIntentParams != null) {
            return mIntentParams.anchorId;
        } else {
            return null;
        }
    }

    public int getRoomId() {
        if (mIntentParams != null) {
            return mIntentParams.roomId;
        } else {
            return 0;
        }
    }

    public void clearAnchorFloat() {
        if (mIntentParams != null) {
            mIntentParams.isNormalFloat = false;
        }
    }

    public boolean getFloatState() {
        if (mIntentParams != null) {
            return mIntentParams.isNormalFloat;
        } else {
            return false;
        }
    }
    int minX,maxX,minY,maxY;
    public void updateFloatWindowSize(FloatWindowRect rect) {
        if (mWindowManager != null && mWindowParams != null && mViewRoot!=null) {
            minX = -(rect.x / 2);
            maxX = (rect.x / 2);
            int mY = UIUtil.getScreenHeight(mContext) - rect.height;
            minY = -(mY / 2);
            maxY = (mY / 2);

            mWindowParams.x = maxX;
            mWindowParams.y = rect.y;
            mWindowParams.width = rect.width;
            mWindowParams.height = rect.height;

            //Log.d("lololo","gogogo");
            //Log.d("lololo","yyy = " + UIUtil.getScreenHeight(mContext));
            //Log.d("lololo"," x = " + rect.x + " y = " + rect.y + "  w = " + rect.width + "  h = " + rect.height);
            mWindowManager.updateViewLayout(mViewRoot, mWindowParams);
        }
    }

    public boolean closeFloatWindow() {
        if (!Config.enableFloatWindow()) {
            return false;
        }

        if (mViewRoot.isAttachedToWindow()) {
            mLayoutDisplayContainer.removeAllViews();
            mWindowManager.removeView(mViewRoot);
        }

        if (mFloatWindowLayoutDelegate != null) {
            mFloatWindowLayoutDelegate.onClose();
        }
        mWindowMode = Constants.WINDOW_MODE_FULL;
        Constents.isShowAudienceFloatWindow = false;
        mViewRoot.setOnTouchListener(null);
        stopHeaderReport();
        return true;
    }

    public boolean closeFloatWindowWithoutDestory() {
        if (!Config.enableFloatWindow()) {
            return false;
        }

        if (mViewRoot.isAttachedToWindow()) {
            mLayoutDisplayContainer.removeAllViews();
            mWindowManager.removeView(mViewRoot);
        }

        mWindowMode = Constants.WINDOW_MODE_FULL;
        Constents.isShowAudienceFloatWindow = false;
        stopHeaderReport();
        mViewRoot.setOnTouchListener(null);
        return true;
    }

    private void initFloatWindowParams() {
        int screenWidth = UIUtil.getScreenWidth(mContext);
        int screenHeight = UIUtil.getScreenHeight(mContext);
        FloatWindowLayout.FloatWindowRect rect = new FloatWindowLayout.FloatWindowRect(screenWidth - 240, 0, 240, 360);
        minX = -(rect.x / 2);
        maxX = (rect.x / 2);
        int mY = screenHeight - rect.height;
        minY = -(mY / 2);
        maxY = (mY / 2);
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.gravity = Gravity.CENTER;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.x = rect.x;
        mWindowParams.y = rect.y;
        mWindowParams.width = rect.width;
        mWindowParams.height = rect.height;
    }

    /**
     * 检查悬浮窗权限
     * <p>
     * API <18，默认有悬浮窗权限，不需要处理。无法接收无法接收触摸和按键事件，不需要权限和无法接受触摸事件的源码分析
     * API >= 19 ，可以接收触摸和按键事件
     * API >=23，需要在manifest中申请权限，并在每次需要用到权限的时候检查是否已有该权限，因为用户随时可以取消掉。
     * API >25，TYPE_TOAST 已经被谷歌制裁了，会出现自动消失的情况
     */
    private boolean requestPermission(Context context, int op) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 6.0动态申请悬浮窗权限
            if (!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return false;
            }
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                TUILiveLog.e(TAG, Log.getStackTraceString(e));
                return false;
            }
        }
        return true;
    }


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int startX;
        private int startY;
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    startX = x;
                    startY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    if (!(mWindowParams.x < minX && movedX < 0)  && !(mWindowParams.x > maxX && movedX > 0)) {
                        mWindowParams.x = mWindowParams.x + movedX;
                    }
                    if (!(mWindowParams.y < minY && movedY < 0) && !(mWindowParams.y > maxY && movedY > 0) ) {
                        mWindowParams.y = mWindowParams.y + movedY;
                    }
                    //Log.d("lololo","x=" + mWindowParams.x + " y= " + mWindowParams.y);
                    mWindowManager.updateViewLayout(view, mWindowParams);
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) < 5 && Math.abs(y - startY) < 5) {//手指没有滑动视为点击，回到窗口模式
                        //startActivity();
//                        if(mOnTouchFloatWindowListener != null){
//                            mOnTouchFloatWindowListener.onTouchUpFloatWindow();
//                        }
                        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.AUDIENCE_RESUME_LIVING,"",""));
                    }
                    break;
                default:
                    break;
            }
            return true;
        }


    }

    private void startActivity() {
        if (mIntentParams != null) {
            TUILiveLog.d(TAG, "startActivity: " + mIntentParams.toString());
            Intent intent = new Intent(FloatWindowLayout.this.getContext(), mIntentParams.activityClass);
            intent.putExtra(Constants.GROUP_ID, mIntentParams.roomId);
            intent.putExtra(Constants.ANCHOR_ID, mIntentParams.anchorId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public interface FloatWindowLayoutDelegate {
        /**
         * 点击悬浮窗中的关闭按钮等会回调该通知
         */
        void onClose();

    }

    public interface OnTouchFloatWindowListener {
        void onTouchUpFloatWindow();
    }

    Timer mTimer;
    public void startHeaderReport() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("logutil","开始轮询" + mIntentParams.anchorId);
                if (onHeartBeatReport != null) {
                    onHeartBeatReport.reportHeart(mIntentParams.anchorId);
                }
            }
        },100,20000);
    }

    public void stopHeaderReport() {
        Log.d("logutil","结束轮询");
       if (mTimer != null) {
           mTimer.cancel();
       }
    }

    public interface OnHeartBeatReport {
        void reportHeart(String anchorId);
    }

    OnHeartBeatReport onHeartBeatReport;

    public void setOnHeartBeatReport(OnHeartBeatReport onHeartBeatReport) {
        this.onHeartBeatReport = onHeartBeatReport;
    }

    public View getFloatRootView() {
        return mViewRoot;
    }
}

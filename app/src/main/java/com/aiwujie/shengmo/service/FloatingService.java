package com.aiwujie.shengmo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.GlideCircleTransform;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

/**
 * @author:Jack Tony
 * 
 * 重要：注意要申请权限！！！！
 *  <!-- 悬浮窗的权限 -->
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 * 
 * @tips  :思路：
 * 1.获得一个windowManager类
 * 2.通过wmParams设置好windows的各种参数
 * 3.获得一个视图的容器，找到悬浮窗视图的父控件，比如linearLayout
 * 4.将父控件添加到WindowManager中去
 * 5.通过这个父控件找到要显示的悬浮窗图标，并进行拖动或点击事件的设置
 * @date  :2014-9-25
 */
public class FloatingService extends Service{
    /**
     * 定义浮动窗口布局
     */
    RelativeLayout mlayout;
    /**
     * 悬浮窗控件
     */
    ImageView mfloatingIv;
    /**
     * 悬浮窗的布局
     */
    LayoutParams wmParams;
    LayoutInflater inflater;
    /**
     * 创建浮动窗口设置布局参数的对象
     */
    WindowManager mWindowManager;

    //触摸监听器
    GestureDetector mGestureDetector;
    private ImageView mivchatroomicon;
    private TextView tv_chatroomname;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();//设置窗口的参数
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initFloating();//设置悬浮窗图标
        String chatroompic = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "chatroompic", "");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.rc_image_error);
        requestOptions.transform(new GlideCircleTransform());
        Glide.with(getApplicationContext()).load(chatroompic).apply(requestOptions).into(mivchatroomicon);
        String chatroomname = (String) SharedPreferencesUtils.getParam(getApplicationContext().getApplicationContext(), "chatroomname", "");
        tv_chatroomname.setText(""+chatroomname);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mlayout);
        }
    }

    ///////////////////////////////////////////////////////////////////////

    /**
     * 初始化windowManager
     */
    private void initWindow() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = getParams(wmParams);//设置好悬浮窗的参数
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT| Gravity.TOP;
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int i = dp2px(86);
        int i1 = dp2px(120);

        wmParams.x = screenWidth-20-i;
        wmParams.y = screenHeight-50-i1;
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(getApplication());
        // 获取浮动窗口视图所在布局
        mlayout = (RelativeLayout) inflater.inflate(R.layout.view_chat, null);
        ImageView iv_chahca = mlayout.findViewById(R.id.iv_chahca);
        // 添加悬浮窗的视图
        mWindowManager.addView(mlayout, wmParams);
        iv_chahca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("chatroomxiaochuangguan");
                SharedPreferencesUtils.setParam(getApplicationContext(),"chatroombutteryhatch","0");
            }
        });
    }

    /** 对windowManager进行设置
     * @param wmParams
     * @return
     */
    public LayoutParams getParams(LayoutParams wmParams){
        wmParams = new LayoutParams();
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        //wmParams.type = LayoutParams.TYPE_PHONE;
        //wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        //wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
        if (Build.VERSION.SDK_INT > 25) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
       //wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置可以显示在状态栏上
        wmParams.flags =  LayoutParams.FLAG_NOT_FOCUSABLE| LayoutParams.FLAG_NOT_TOUCH_MODAL|
        LayoutParams.FLAG_LAYOUT_IN_SCREEN| LayoutParams.FLAG_LAYOUT_INSET_DECOR|
        LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        //设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        return wmParams;
    }
    
    /**
     * 找到悬浮窗的图标，并且设置事件
     * 设置悬浮窗的点击、滑动事件
     */
    private void initFloating() {
        mfloatingIv = (ImageView) mlayout.findViewById(R.id.floating_imageView);
        mivchatroomicon = mlayout.findViewById(R.id.iv_chatroomicon);
        tv_chatroomname = mlayout.findViewById(R.id.tv_chatroomname);
        // mfloatingIv.getBackground().setAlpha(150);

        mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
        //设置监听器
        mfloatingIv.setOnTouchListener(new FloatingListener());
    }
    
    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX,mTouchStartY,mTouchCurrentX,mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX,mStartY,mStopX,mStopY;
    private boolean isMove;//判断悬浮窗是否移动
    
    /**
     * @author:金凯
     * @tips  :自己写的悬浮窗监听器
     * @date  :2014-3-28
     */
    private class FloatingListener implements OnTouchListener{

        @Override
        public boolean onTouch(View arg0, MotionEvent event) {

            int action = event.getAction();
            switch(action){ 
                case MotionEvent.ACTION_DOWN:
                    isMove = false;
                    mTouchStartX = (int)event.getRawX();
                    mTouchStartY = (int)event.getRawY();
                    mStartX = (int)event.getX();
                    mStartY = (int)event.getY();
                    break; 
                case MotionEvent.ACTION_MOVE:  
                    mTouchCurrentX = (int) event.getRawX();
                    mTouchCurrentY = (int) event.getRawY();
                    wmParams.x += mTouchCurrentX - mTouchStartX;
                    wmParams.y += mTouchCurrentY - mTouchStartY;
                    mWindowManager.updateViewLayout(mlayout, wmParams);
                    
                    mTouchStartX = mTouchCurrentX;
                    mTouchStartY = mTouchCurrentY; 
                    break;
                case MotionEvent.ACTION_UP:
                    mStopX = (int)event.getX();
                    mStopY = (int)event.getY();
                    //System.out.println("|X| = "+ Math.abs(mStartX - mStopX));
                    //System.out.println("|Y| = "+ Math.abs(mStartY - mStopY));
                    if(Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1){
                        isMove = true;
                    }
                    break; 
            }
            return mGestureDetector.onTouchEvent(event);  //此处必须返回false，否则OnClickListener获取不到监听
        }

    }
    
    /**
     * @author:金凯
     * @tips  :自己定义的手势监听类
     * @date  :2014-3-29
     */
    class MyOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!isMove) {
                EventBus.getDefault().post("chatroomdakai");
                System.out.println("onclick");
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    public static int dp2px(float dpValue){
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    
}
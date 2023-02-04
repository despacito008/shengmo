package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.screencapture.screen;

import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.IRotationWatcher;

import java.lang.reflect.Method;

public class WindowUtils {

    public static Class<?>[] getMethodParamTypes(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Log.e("Service", "method = " + method.toGenericString());
            if (methodName.equals(method.getName())) {
                return method.getParameterTypes();
            }
        }
        return null;
    }

    public static int watchRotation(IRotationWatcher watcher) throws Exception {
        //加载得到ServiceManager类，然后得到方法getService。
        Method getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
        //通过getServiceMethod得到ServiceManager的实例（隐藏类，所以使用Object）
        Object ServiceManager = getServiceMethod.invoke(null, new Object[]{"window"});
        //通过反射的到Stub
        Class<?> cStub = Class.forName("android.view.IWindowManager$Stub");
        //得到Stub类的asInterface 方法
        Method asInterface = cStub.getMethod("asInterface", IBinder.class);
        //然后通过类似serviceManager.getIWindowManager的方法获取IWindowManager的实例
        Object iWindowManager = asInterface.invoke(null, ServiceManager);

        int rotation = -1;
        Class<?>[] paramTypes = getMethodParamTypes(iWindowManager.getClass(), "watchRotation");
        Method watchRotation = iWindowManager.getClass().getDeclaredMethod("watchRotation", paramTypes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//这里做个适配
            rotation = (int) watchRotation.invoke(iWindowManager, watcher, 0);
        } else {
            rotation = (int) watchRotation.invoke(iWindowManager, watcher);
        }
        Log.e("Service", "rotation = " + rotation);
        return rotation;
    }
}

package com.longluo.ebookreader.aop;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.content.ContextCompat;

import com.longluo.ebookreader.R;
import com.longluo.ebookreader.manager.ActivityManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import io.github.longluo.util.ToastUtils;

/**
 * 网络检测切面
 */
@Aspect
public class CheckNetAspect {

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.longluo.ebookreader.aop.CheckNet * *(..))")
    public void method() {
    }

    /**
     * 在连接点进行方法替换
     */
    @Around("method() && @annotation(checkNet)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, CheckNet checkNet) throws Throwable {
        Application application = ActivityManager.getInstance().getApplication();
        if (application != null) {
            ConnectivityManager manager = ContextCompat.getSystemService(application, ConnectivityManager.class);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                // 判断网络是否连接
                if (info == null || !info.isConnected()) {
                    ToastUtils.showToast(application, R.string.common_network_hint);
                    return;
                }
            }
        }
        //执行原方法
        joinPoint.proceed();
    }
}
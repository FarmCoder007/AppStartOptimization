package com.zhangwen.coldlaunch;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by zhaojingjing on 2018/2/2.
 */

public class ColdActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private int mFinalCount;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mFinalCount++;
        //如果mFinalCount ==1，说明是从后台到前台
        if (mFinalCount == 1) {
            Log.e("ColdApplication", "---------ColdApplication----ColdActivityLifecycleCallbacks  onActivityStarted");
            // 进入前台 改为热启动
            ColdApplication.isColdLaunch = false;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mFinalCount--;
        //如果mFinalCount ==0，说明是前台到后台
        if (mFinalCount == 0) {
            Log.e("ColdApplication", "---------ColdApplication----ColdActivityLifecycleCallbacks   onActivityStopped");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

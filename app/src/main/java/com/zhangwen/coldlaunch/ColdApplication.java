package com.zhangwen.coldlaunch;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.zhangwen.coldlaunch.task.SystemUtils;
import com.zhangwen.coldlaunch.task.TaskLog;
import com.zhangwen.coldlaunch.task.TaskManager;
import com.zhangwen.coldlaunch.task.launch.LaunchType;

/**
 * author : xu
 * date : 2020/10/14 18:28
 * description :
 */
public class ColdApplication extends Application {
    /**
     * 启动状态临时标识
     */
    public static boolean tempCL;
    /**
     * 是否是冷启动
     */
    public static boolean isColdLaunch;

    private static ColdApplication instance;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Log.e("ColdApplication", "------------------ColdApplication   onCreate");
        tempCL = true;
        // 注册activity 生命周期回调检测
        this.registerActivityLifecycleCallbacks(new ColdActivityLifecycleCallbacks());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//            }
//        }).start();
        init();
    }

    public  Context getContext() {
        return context;
    }

    /**
     * @return 返回context，能够动态获取资源（在任意位置获取应用程序Context）
     */
    public static ColdApplication getInstance() {
        return instance;
    }

    public void init() {
        if (SystemUtils.isMainProcess(context)) {
            long now = System.currentTimeMillis();
            TaskManager taskManager = new TaskManager();
            taskManager
                    .add(LaunchType.SHARED_PREFERENCES, true)
                    .add(LaunchType.DEBUG_ABLE, true)
                    .add(LaunchType.BASIC_INIT, true)
                    .add(LaunchType.COMPARAM_INIT, true)
                    .add(LaunchType.UT_INIT, true)
                    .add(LaunchType.ROUTE_INIT, false)
                    .add(LaunchType.SECURITY_GUARD, false)
                    .add(LaunchType.RIPPLE_INIT, true)
                    .add(LaunchType.PASSPORT_INIT, true)
                    .add(LaunchType.CRASH_INIT, false)
                    .add(LaunchType.PUSH_INIT, true)
                    .add(LaunchType.RECORDER_INIT, false)
                    .start();
            TaskLog.d("execute TaskManager Task cost time : " + (System.currentTimeMillis() - now));
        } else {
            String channelProcessName = getPackageName() + ":channel";
            if (channelProcessName.equals(SystemUtils.getCurProcessName(context))) {
                TaskManager taskManager = new TaskManager();
                taskManager
                        .add(LaunchType.SHARED_PREFERENCES, true)
                        .add(LaunchType.BASIC_INIT, true)
                        .add(LaunchType.UT_INIT, true)
                        .add(LaunchType.PUSH_INIT, true)
                        .add(LaunchType.RIPPLE_INIT, true)
                        .start();
            }
        }
    }
}

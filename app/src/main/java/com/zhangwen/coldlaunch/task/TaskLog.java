package com.zhangwen.coldlaunch.task;

import android.util.Log;

/**
 * 打算把 ocean 所有和播放相关的日志，都通过这个 tag 来打，
 * 所有就新建了这么一个类.
 *
 * @author qisen.tqs@alibaba-inc.com
 */
public class TaskLog {
    private static final String TAG = "TaskMonitor";

    public static void d(String formatMsg, Object... args) {
        Log.d(TAG, String.format(formatMsg, args));
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void e(String formatMsg, Object... args) {
        Log.e(TAG, String.format(formatMsg, args));
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }
}
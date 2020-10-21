package com.zhangwen.coldlaunch.task;

import android.widget.Toast;
import com.zhangwen.coldlaunch.ColdApplication;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 启动时间统计埋点
 * Created by dongming on 2017/7/10.
 */

public class LaunchRunTime {

    private static final String RT_TUDOU = "rt_tudou_total";

    private static LaunchRunTime instance = null;
    private boolean utReport = false;
    private boolean isMainProcess = false;
    private Map<String, Long> runTimes = new HashMap<>();
    private Map<String, Long> beginTimes = new HashMap<>();
    private Map<String, String> specKeys = new HashMap<>();

    private LaunchRunTime() {
    }

    public static LaunchRunTime getInstance() {
        if (instance == null) {
            instance = new LaunchRunTime();
        }

        return instance;
    }

    public void missionStart() {
        markKey("key_missionStart");
        markBegin(RT_TUDOU);
    }

    public void setMainProcess(boolean isMainProcess) {
        this.isMainProcess = isMainProcess;
    }

    public void markBegin(String key) {
        if (!needRecord()) {
            return;
        }
        if (beginTimes.containsKey(key) || runTimes.containsKey(key)) {
            markError();
        } else {
            beginTimes.put(key, System.currentTimeMillis());
        }
    }

    public void markEnd(String key) {
        if (!needRecord()) {
            return;
        }
        if (runTimes.containsKey(key)) {
            markError();
        } else if (!beginTimes.containsKey(key)) {
            markError();
        } else {
            long beginTime = beginTimes.get(key);
            beginTimes.remove(key);
            runTimes.put(key, System.currentTimeMillis() - beginTime);
        }
    }

    public void markBegin(String... keys) {
        for (String key : keys) {
            markBegin(key);
        }
    }

    public void markEnd(String... keys) {
        for (String key : keys) {
            markEnd(key);
        }
    }

    public void markRunTime(String key, long runTime) {
        if (runTimes.containsKey(key)) {
            Toast.makeText(ColdApplication.context, "启动时间统计错误：重复的时间段, key=" + key, Toast.LENGTH_LONG).show();
        } else {
            runTimes.put(key, runTime);
        }
    }
    private boolean needRecord() {
        return isMainProcess && !utReport;
    }

    private void markError() {
        utReport = true;
    }

    public void markKey(String key) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String dateStr = dateformat.format(System.currentTimeMillis());
        specKeys.put(key, dateStr);
    }
}

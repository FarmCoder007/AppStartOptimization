package com.zhangwen.coldlaunch.task.launch;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import com.zhangwen.coldlaunch.task.Task;
import java.util.HashMap;
import java.util.Map;


public class LaunchTaskFactory {

  public static Task newTask(LaunchType taskType) {
    switch (taskType) {
      case DEBUG_ABLE:
        return new Task(taskType.name(), createDebugRunnable());
      case BASIC_INIT:
        return new Task(taskType.name(), createBasicRunnable());
      case SHARED_PREFERENCES:
        return new Task(taskType.name(), createSharedRunnable());
      case UT_INIT:
        return new Task(taskType.name(), createUTInitRunnable());
      case ROUTE_INIT:
        return new Task(taskType.name(), createRouteInitRunnable());
      case CRASH_INIT:
        return new Task(taskType.name(), createCrashRunnable());
      case PUSH_INIT:
        return new Task(taskType.name(), createPushRunnable());
      case COMPARAM_INIT:
        return new Task(taskType.name(), createHttpRunnable());
      case RIPPLE_INIT:
        return new Task(taskType.name(), createRippleRunnable());
      case PASSPORT_INIT:
        return new Task(taskType.name(), createPassportRunnable());
      case RECORDER_INIT:
        return new Task(taskType.name(), createRecorderRunnable());
    }
    return null;
  }

  private static Runnable createCrashRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        CrashManager.getInstance().init();
      }
    };
  }

  private static Runnable createBasicRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        YoucaiService.context = Youcai.context;
//        if (Debuggable.isDebug()) {
//          LeakCanary.install(Youcai.context);
//        }
//        RippleApi.getInstance().init(Youcai.context, "YouCai");
//        Youcai.User_Agent = "Tudou;" + SystemUtils.getVersionName(Youcai.context)
//            + ";Android;" + Build.VERSION.RELEASE + ";" + Build.MODEL;
      }
    };
  }
  private static Runnable createUTInitRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        // UT 的初始化.
//        UTAnalytics.getInstance().setAppApplicationInstance(Youcai.context, new UIApplication());
//        // 关闭自动埋点
//        UTAnalytics.getInstance().turnOffAutoPageTrack();
//        // UT公共参数
//        UTAnalyseGlobalManager.init();
//        GlobalVariableManager.getInstance().setUserAgent(Youcai.User_Agent);
//        Router.init("youcaiRoute");
//        //挂载个人中心Activity注册表
//        Router.register(new UserCenterRouterInitializer());
      }
    };
  }

  private static Runnable createRouteInitRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        Router.init("youcaiRoute");
//        //挂载个人中心Activity注册表
//        Router.register(new UserCenterRouterInitializer());
      }
    };
  }

//  private static class UIApplication implements IUTApplication {
//    @Override
//    public String getUTAppVersion() {
//
//      return SystemUtils.getVersionName(Youcai.context);
//    }
//
//    @Override
//    public String getUTChannel() {
//      // 写死的pid，以后替换
//      return TextUtils.isEmpty(CommonIdManager.getChannelId()) ? "600000" : CommonIdManager.getChannelId();
//    }
//
//    @Override
//    public IUTRequestAuthentication getUTRequestAuthInstance() {
//      String appKeyString;
//      if (!Debuggable.isDebug()) {
//        // 线上环境
//        appKeyString = "24739526";
//      } else {
//        // 日常环境
//        appKeyString = "4272";
//      }
//
//      return new UTSecuritySDKRequestAuthentication(appKeyString);
//    }
//
//    @Override
//    public boolean isUTLogEnable() {
//      return true;
//    }
//
//    @Override
//    public boolean isAliyunOsSystem() {
//      return false;
//    }
//
//    @Override
//    public IUTCrashCaughtListner getUTCrashCraughtListener() {
//      return null;
//    }
//
//    @Override
//    public boolean isUTCrashHandlerDisable() {
//      return true;
//    }
//  }

  private static Runnable createDebugRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        Debuggable.init(Youcai.context);
      }
    };
  }

  private static Runnable createSharedRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        SpManager.init(Youcai.context);
      }
    };
  }

  private static Runnable createPushRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        PushInit.initPush(Youcai.context);
      }
    };
  }

  private static Runnable createHttpRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        HttpConsts.setUserAgent(Youcai.User_Agent);
//        HttpConsts.getStaticCommonInfoMap().clear();
//        HttpConsts.getStaticCommonInfoMap().putAll(HttpRequestParamUtils.getStaticComParamAsMap());
      }
    };
  }

  private static Runnable createPassportRunnable() {
    return new Runnable() {
      @Override
      public void run() {
//        PassportServiceManager.getInstance().initPassportSDK(RippleApi.getInstance().getContext());
      }
    };
  }

  private static Runnable createRippleRunnable() {
    return new Runnable() {
      @Override
      public void run() {

//        RippleApi.getInstance().setUtLog(new IUTLog() {
//
//          @Override
//          public void activityPause(Activity activity) {
//            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
//          }
//
//          @Override
//          public void pageShow(Activity activity, String pageName, String spm, Map<String, String>
//              properties) {
//            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
//            UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(activity, pageName);
//            properties.put("spm-cnt", spm);
//            updatePage(activity, properties);
//          }
//
//          @Override
//          public void updatePage(Activity activity, Map<String, String> properties) {
//            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(activity, properties);
//          }
//
//          @Override
//          public Map<String, String> getPageProperties(Activity activity) {
//            return UTAnalytics.getInstance().getDefaultTracker().getPageProperties(activity);
//          }
//
//          @Override
//          public void updateNextPage(Map<String, String> properties) {
//            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(properties);
//          }
//
//          @Override
//          public void click(String page, String widget, Map<String, String> properties) {
//            UTHitBuilders.UTControlHitBuilder builder = new UTHitBuilders.UTControlHitBuilder(page,
//                widget);
//            builder.setProperties(properties);
//            UTAnalytics.getInstance().getDefaultTracker().send(builder.build());
//            if (properties.containsKey("spm")) {
//              String spm = properties.get("spm");
//              properties = new HashMap<>();
//              properties.put("refer_spm", spm);
//              properties.put("pre_pagename", page);
//              properties.put("spm-url", spm);
//              UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(properties);
//            }
//          }
//
//          @Override
//          public void exposure(String page, String widget, Map<String, String> properties) {
//            UTOriginalCustomHitBuilder builder = new UTOriginalCustomHitBuilder(page
//                , 2201, widget, null, null, properties);
//            UTAnalytics.getInstance().getDefaultTracker().send(builder.build());
//          }
//
//          @Override
//          public void custom(String page, String event, Map<String, String> properties) {
//            UTHitBuilders.UTCustomHitBuilder builder = new UTHitBuilders.UTCustomHitBuilder(event);
//            builder.setEventPage(page);
//            builder.setProperties(properties);
//            UTAnalytics.getInstance().getDefaultTracker().send(builder.build());
//          }
//
//        });
//        RippleApi.getInstance().init(Youcai.context, Youcai.TAG_GLOBAL);
      }
    };
  }

  private static Runnable createRecorderRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
          return;
        }
        System.loadLibrary("aliresample");
        System.loadLibrary("live-openh264");
        System.loadLibrary("QuCore");
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("FaceAREngine");
        System.loadLibrary("AliFaceAREngine");
      }
    };
  }
}

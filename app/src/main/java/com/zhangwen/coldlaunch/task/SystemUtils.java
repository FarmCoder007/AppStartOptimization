package com.zhangwen.coldlaunch.task;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.zhangwen.coldlaunch.ColdApplication;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;


public class SystemUtils {

    private static String versionName = null;
    private static int versionCode = 0;
    private volatile static String currentProcessName;

    private SystemUtils() {
    }

    public static boolean aboveApiLevel(int sdkInt) {
        return getApiLevel() >= sdkInt;
    }

    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static String getSdkVersion() {
        try {
            return Build.VERSION.SDK;
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(getSdkVersionInt());
        }
    }

    public static String getSdkReleaseVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return getSdkVersion();
        }
    }

    /**
     * 获取当前进程名字
     */
    public static String getCurProcessName(Context context) {

        if (currentProcessName != null)
            return currentProcessName;
        synchronized (SystemUtils.class) {
            int pid = android.os.Process.myPid();
            BufferedReader cmdlineReader = null;
            try {
                cmdlineReader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
                int c;
                StringBuilder processName = new StringBuilder();
                while ((c = cmdlineReader.read()) > 0) {
                    processName.append((char) c);
                }
                currentProcessName = processName.toString();
                return currentProcessName;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (cmdlineReader != null) {
                        cmdlineReader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    currentProcessName = appProcess.processName;
                    return currentProcessName;
                }
            }
            return null;
        }
    }

    /**
     * 是否是主进程
     *
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(currentProcessName);
    }

    public static int getSdkVersionInt() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getVersionCode() {
        return getVersionCode(ColdApplication.getInstance().getContext());
    }

    public static int getVersionCode(Context context) {
        if (versionCode != 0) {
            return versionCode;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (NameNotFoundException | RuntimeException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName() {
        return getVersionName(ColdApplication.getInstance().getContext());
    }

    public static String getVersionName(Context context) {
        if (versionName == null && context != null) {
            PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), 0);
            if (packageInfo != null) {
                versionName = packageInfo.versionName;
            } else {
                versionName = "";
            }

        }
        return versionName;
    }

    public static String getFullVersion() {
        return getVersionName() + "." + getVersionCode();
    }

    public static PackageInfo getPackageInfo(Context context, String packageName, int flag) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, flag);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            // In some ROM, there will be a PackageManager has died exception. So we catch it here.
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * get user config locale, if null, return default locale.
     *
     * @param context
     * @return locale
     */
    public static Locale getLocale(Context context) {
        Locale locale = null;
        try {
            Configuration userConfig = new Configuration();
            Settings.System.getConfiguration(context.getContentResolver(), userConfig);
            locale = userConfig.locale;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public static String getSystemDisplayId() {
        if (TextUtils.isEmpty(Build.DISPLAY)) {
            return "";
        } else {
            return Build.DISPLAY;
        }
    }

    public static String getBrand() {
        if (TextUtils.isEmpty(Build.BRAND)) {
            return "";
        } else {
            return Build.BRAND;
        }
    }

    /**
     * get current process name
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        int myPid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
        if (CollectionUtils.isEmpty(processes)) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo process : processes) {
            if (process.pid == myPid) {
                return process.processName;
            }
        }
        return "";
    }

    public static String getImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            return imei == null ? "" : imei;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getOperator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = tm.getSubscriberId();
            if (!TextUtils.isEmpty(imsi)) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    return "CMCC";
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    return "CUCC";
                } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
                    return "CTCC";
                }
            }
        } catch (Throwable th) {
        }
        return "Unknown";
    }

    public static String getNetwork(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = connectivityManager.getActiveNetworkInfo();
            if (nInfo == null) {
                return "Unknown";
            }

            if (nInfo.isConnected()) {
                if (nInfo.getType() == 1) {
                    return "Wi-Fi";
                }

                if (nInfo.getType() == 0) {
                    return getNetworkClass(nInfo.getSubtype());
                }
            }
        } catch (Throwable var4) {
        }

        return "Unknown";
    }

    private static String getNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    public static String getOperatorName(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();

        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return "移动";
            } else if (operator.equals("46001")) {
                return "联通";
            } else if (operator.equals("46003")) {
                return "电信";
            }
        }
        return "未知";
    }

    public static String getOperatorNumber(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();

        if (operator != null) {
            return operator;
        }
        return "未知";
    }

    //获取应用程序名称
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    //将得到的int类型的IP转换为String类型
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getMacAddress() {
        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }
}

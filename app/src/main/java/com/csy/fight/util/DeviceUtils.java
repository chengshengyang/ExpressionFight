package com.csy.fight.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by chengshengyang on 2018/1/31.
 * <p>
 * 系统版本信息类
 *
 * @author chengshengyang
 */
public class DeviceUtils {

    /**
     * >=2.2 8
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * >=2.3 9
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * >=3.0 LEVEL:11
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * >=3.1 12
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * >=4.0 14
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * >= 4.1 16
     *
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * >= 4.2 17
     */
    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * >= 4.3 18
     */
    public static boolean hasJellyBeanMr2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * >=4.4 19
     */
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取当前SDK版本<br>
     * 方 法 名：getSDKVersionInt <br>
     * 创 建 人： <br>
     * 创建时间：2016-6-24 上午10:01:33 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @return int
     */
    public static int getSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前SDK版本<br>
     * 方 法 名：getSDKVersion <br>
     * 创 建 人： <br>
     * 创建时间：2016-6-24 上午10:01:50 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @return String
     */
    @SuppressWarnings("deprecation")
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 获得设备的固件版本号
     */
    public static String getReleaseVersion() {
        return StringUtils.makeSafe(Build.VERSION.RELEASE);
    }

    /**
     * 检测是否是中兴机器
     */
    public static boolean isZte() {
        return getDeviceModel().toLowerCase().indexOf("zte") != -1;
    }

    /**
     * 判断是否是三星的手机
     */
    public static boolean isSamsung() {
        return getManufacturer().toLowerCase().indexOf("samsung") != -1;
    }

    /**
     * 检测是否HTC手机
     */
    public static boolean isHTC() {
        return getManufacturer().toLowerCase().indexOf("htc") != -1;
    }

    /**
     * 检测当前设备是否是特定的设备<br>
     * 方 法 名：isDevice <br>
     * 创 建 人： <br>
     * 创建时间：2016-6-24 上午10:03:42 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @param devices
     * @return boolean
     */
    public static boolean isDevice(String... devices) {
        String model = DeviceUtils.getDeviceModel();
        if (devices != null && model != null) {
            for (String device : devices) {
                if (model.indexOf(device) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return StringUtils.trim(Build.MODEL);
    }

    /**
     * 获取厂商信息
     */
    public static String getManufacturer() {
        return StringUtils.trim(Build.MANUFACTURER);
    }

    /**
     * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）<br>
     * 方 法 名：getScreenPhysicalSize <br>
     * 创 建 人： <br>
     * 创建时间：2016-6-24 上午10:07:37 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @param ctx
     * @return double
     */
    public static double getScreenPhysicalSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }

    public static double getScreenIn(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        // 得到屏幕的宽(像素)
        int screenX = dm.widthPixels;
        // 得到屏幕的高(像素)
        int screenY = dm.heightPixels;
        // 每英寸的像素点
        int dpi = dm.densityDpi;

        // 得到屏幕的宽(英寸)
        float a = screenX / dpi;
        // 得到屏幕的高(英寸)
        float b = screenY / dpi;
        // 勾股定理
        double screenIn = Math.sqrt((a * a) + (b * b));
        return screenIn;

    }

    /**
     * 判断是否是平板电脑
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 检测是否是平板电脑
     *
     * @param context
     * @return
     */
    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static int dipToPX(final Context ctx, float dip) {
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }

    /**
     * 获取CPU的信息
     *
     * @return
     */
    public static String getCpuInfo() {
        String cpuInfo = "";
        try {
            if (new File("/proc/cpuinfo").exists()) {
                FileReader fr = new FileReader("/proc/cpuinfo");
                BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
                cpuInfo = localBufferedReader.readLine();
                localBufferedReader.close();

                if (cpuInfo != null) {
                    cpuInfo = cpuInfo.split(":")[1].trim().split(" ")[0];
                }
            }
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return cpuInfo;
    }

    /**
     * 判断是否支持闪光灯
     */
    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    // 判断设备是否支持闪光灯
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测设备是否支持相机
     */
    public static boolean isSupportCameraHardware(Context context) {
        if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取屏幕宽度<br>
     * 方 法 名：getScreenWidth <br>
     * 创 建 人： <br>
     * 创建时间：2016-6-24 上午10:08:49 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @param context
     * @return int
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕高度<br>
     * 方 法 名：getScreenHeight <br>
     * 创 建 人：<br>
     * 创建时间：2016-6-24 上午10:08:59 <br>
     * 修 改 人： <br>
     * 修改日期： <br>
     *
     * @param context
     * @return int
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getHeight();
    }
}

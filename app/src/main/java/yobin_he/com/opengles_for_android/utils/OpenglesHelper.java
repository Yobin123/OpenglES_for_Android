package yobin_he.com.opengles_for_android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;

/**
 * @author : yobin_he
 * @package: yobin_he.com.opengles_for_android.utils
 * @fileName: OpenglesHelper
 * @Date : 2019/1/18  16:19
 * @describe ： opengles帮助类
 * @org scimall
 * @email he.yibin@scimall.org.cn
 */

public class OpenglesHelper {
    public static final int  OPENGL_VERSION = 0x20000;


    public static boolean isSupportsVersion(Context context,int version){
        final boolean isSupport;
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
//        isSupport = configurationInfo.reqGlEsVersion >= OPENGL_VERSION; //不考虑模拟器等缺陷

        isSupport = configurationInfo.reqGlEsVersion >= version ||    //考虑模拟器的部分缺陷
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown")
                        ||Build.MODEL.contains("google_sdk")|| Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")));

        return isSupport;
    }

}

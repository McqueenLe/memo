package com.xy.memo.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
/**
 *
 * 功能描述：获取系统信息
 *
 * @author ：xy 2017 11 29
 */
public class AppUtil {
    private static final String TAG = AppUtil.class.getSimpleName();

    /**
     * 获取TelephonyManager
     *
     * @param context
     * @return
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telMgr;
    }

    /**
     * 获取"渠道号"等在manifest内定义的meta值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取网络连接类型
     *
     * @param context
     * @return
     */
    public static String getNetWorkType(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String nt = "";
        if (telMgr.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE) {
            nt = "EDGE";
        } else if (telMgr.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS) {
            nt = "GPRS";
        } else if (telMgr.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
            nt = "UMTS";
        } else if (telMgr.getNetworkType() == 4) {
            nt = "HSDPA";
        } else {
        }
        return nt;
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        String imsi = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            LogUtil.d(TAG, "@@@@@ 未获取权限READ_PHONE_STATE @@@@@");
        } else {
            TelephonyManager telMgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telMgr.getSubscriberId() != null && telMgr.getSubscriberId().length() != 0) {
                imsi = telMgr.getSubscriberId();
            }
        }
        return imsi;
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String imei = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            LogUtil.d(TAG, "@@@@@ 未获取权限READ_PHONE_STATE @@@@@");
        } else {
            TelephonyManager telMgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telMgr.getDeviceId() != null && telMgr.getDeviceId().length() != 0) {
                imei = telMgr.getDeviceId();
            }
        }
        return imei;
    }

    /**
     * 获取运营商所在国家
     * 
     * @param context
     * @return
     */
    public static String getCountry(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String country = "";
        country = telMgr.getSimCountryIso();
        return country;
    }
    
    /**
     * 获取手机品牌
     * 
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获得VersionCode，返回值为版本号，例如：5
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "get versionCode Exception(RuntimeException)");
        }
    }

    /**
     * 获得versionName，返回值为版本名称，例如：1.3.1
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "get versionCode Exception(RuntimeException)");
        }
    }

    /**
     * 获取分辨率
     * @param context
     * @return
     */
    public static String getScreenResolution(Context context) {
    	String resolution;
    	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    	((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    	int W = mDisplayMetrics.widthPixels;
    	int H = mDisplayMetrics.heightPixels;
    	resolution = Integer.toString(W) + "*" + Integer.toString(H);
    	return resolution;
    }
    
    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenW(Activity activity) {
    	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    	activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    	int width = mDisplayMetrics.widthPixels;
    	return width;
    }
    
    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenH(Activity activity) {
    	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    	activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
    	int height = mDisplayMetrics.heightPixels;
    	return height;
    }

    /**
     * 获取 uuid
     * 
     * @param context
     * @return
     */
//    public static String getUserUUID(Context context) {
//        if (context == null)
//            return null;
//        String uuid = context.getSharedPreferences(
//                AppConstants.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
//                .getString(AppConstants.SHARED_PREF_KEY_UUID, "");
//        if ("".equals(uuid)) {
//            uuid = java.util.UUID.randomUUID().toString();
//            context.getSharedPreferences(AppConstants.SHARED_PREF_FILE_NAME,
//                    Context.MODE_PRIVATE).edit()
//                    .putString(AppConstants.SHARED_PREF_KEY_UUID, uuid)
//                    .commit();
//        }
//        return uuid;
//    }

    /**
     * 获取手机Mac地址
     * @param ctx
     * @return Mac地址
     */
    public static String getMac(Context ctx) {
        WifiManager manager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }
    /**
     * 安装apk
     */
    public static void installApk(Context ctx, File apkfile) {
        if (apkfile != null && apkfile.exists()) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
            ctx.startActivity(i);
        }
    }
    
    /**
     * dp转px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    }

    /**
     * 判断apk是否安装
     * @param context
     * @param packageName 包名
     * @return 
     */
	public static boolean isInstalled(Context context, String packageName, int verCode) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(packageName, 0);
//			if(verCode >= info.versionCode) {
				return true;
//			} else {
//				return false; //版本过旧,后期可修改为做升级操作
//			}
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 判断是否连接wifi网络
	 * @return
	 */
	public static boolean isConnectWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

    /**
     * 判断是否连接网络
     * @return
     */
    public static boolean isConnectNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取蓝牙设备物理地址
     * @param context
     * @return
     */
    public static String getBTAddress(Context context) {
        String bt_mac = "";
        BluetoothAdapter m_BluetoothAdapter = null;
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (m_BluetoothAdapter != null) {
            bt_mac= m_BluetoothAdapter.getAddress();
        }
        return bt_mac;
    }

    /**
     * 隐藏软键盘
     * @param ctx
     * @param mInput
     */
    public static void hideInputKeyboard(Context ctx, View mInput) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getWindowToken(),0);
    }

    /**
     * 显示软键盘
     * @param ctx
     * @param mInput
     */
    public static void showInputKeyboard(Context ctx, View mInput) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 让状态栏和应用的标题栏颜色一致 （针对miui手机）
     * @param activity
     */
    public static void setStatusBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {            // 系统版本大于19
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if(true){
                extraFlagField.invoke(activity.getWindow(),darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
            }else{
                extraFlagField.invoke(activity.getWindow(), 0, darkModeFlag);//清除黑色字体
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;        // a|=b的意思就是把a和b按位或然后赋值给a   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
        } else {
            winParams.flags &= ~bits;        //&是位运算里面，与运算  a&=b相当于 a = a&b  ~非运算符
        }
        win.setAttributes(winParams);
    }


    // 设置状态栏颜色（针对非miui系统手机）
//    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = activity.getWindow();
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(activity.getResources().getColor(colorResId));
//
//                //底部导航栏
//                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 判断当前线程是否为主线程
     * @return
     */
    public static boolean isInMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        LogUtil.i(TAG, "isInMainThread myLooper=" + myLooper + ";mainLooper=" + mainLooper);
        return myLooper == mainLooper;
    }
}

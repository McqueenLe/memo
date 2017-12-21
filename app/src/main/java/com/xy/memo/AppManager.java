package com.xy.memo;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;

//import com.squareup.leakcanary.LeakCanary;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xy.memo.utils.ImgUtil;

import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * 功能描述：app管理器
 *
 * @author xy 2017 11 29
 */
public class AppManager extends Application {

    public Vibrator mVibrator;

    private HashMap<String, Activity> mActivityMap = new HashMap<String, Activity>();
    private static AppManager instance;
    private int mActivityCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this); // 初始化LeakCanary检测内存溢出

        // 解决安卓7.0相机拍照和选取相册图片卡死的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
//                if (activity.getLocalClassName().equals("activity.SplashActivity")) {
//                    StatisticsDao.saveStatistics(getApplicationContext(), "appUse", "");
//                } else {
//                    //从后台回到前台时,间隔35超过分钟,算增加1次appUse
//                    long lastUseTime = SharedPreferencesInfo.getTagLong(getApplicationContext(),
//                            SharedPreferencesInfo.APP_USE_TIME);
//                    if (System.currentTimeMillis() - lastUseTime > AppConstants.APP_USE_INTERVAL) {
//                        StatisticsDao.saveStatistics(getApplicationContext(), "appUse", "");
//                        Intent intent = new Intent(activity, SplashAdvActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivityCount++;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActivityCount--;
            }

        });
        instance = this;

        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        // 初始化图片加载框架
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                // 添加你的配置需求
                .build();
        ImageLoader.getInstance().init(configuration);
//        ImgUtil.imageLoaderInit(getApplicationContext());
    }

    /**
     * 初始化定位配置
     */
//    public void initLocation(LocationClient mClient){
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//        mClient.setLocOption(option);
//    }

    /**
     * 单例模式中获取唯一的ExitApplication实例
     *
     * @return
     */
    public static AppManager getInstance() {
        return instance;
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityMap.put(activity.getClass().getSimpleName(), activity);
    }

    /**
     * 从容器中删除activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        mActivityMap.remove(activity.getClass().getSimpleName());
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        Iterator<Activity> it = mActivityMap.values().iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
        System.exit(0);
    }

    /**
     * 清空activity容器
     */
    public void clearActivityMap() {
        mActivityMap.clear();
    }

    /**
     * 获取activity容器
     *
     * @return
     */
    public HashMap<String, Activity> getActivityMap() {
        return mActivityMap;
    }

}

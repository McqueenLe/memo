package com.xy.memo.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.xy.memo.AlarmBroadcast;
import com.xy.memo.AppConstants;
import com.xy.memo.activity.MemoDetailActivity;
import com.xy.memo.model.MemoInfo;

import java.util.Calendar;
import java.util.Random;

/**
 * 设置闹钟服务提醒
 * @author xy 2017/12/20.
 */

public class AlarmUtil {
    private static final String TAG = AlarmUtil.class.getSimpleName();
    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h

    public static void setAlarm(Context context, long targetTime, MemoInfo data) {
        LogUtil.d(TAG, DateUtil.formatTime(targetTime, "yyyy-MM-dd hh:mm"));
        Intent dataIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MemoInfo", data);
        dataIntent.putExtras(bundle);
        dataIntent.putExtra("test", "test");
//        dataIntent.setClass(context, MemoDetailActivity.class);
        dataIntent.setAction(AppConstants.BROADCAST_SET_ALART);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetTime, pendingIntent);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetTime, pendingIntent);
        }
        LogUtil.d(TAG, "@@@@@ 创建提醒成功 @@@@@");
    }

    @SuppressLint("NewApi")
    public static void setAlarm1(Context context, long targetTime, MemoInfo data) {
//        Intent dataIntent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("MemoInfo", data);
//        dataIntent.putExtras(bundle);
//        dataIntent.putExtra("test", "test");
//        dataIntent.setClass(context, MemoDetailActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 安卓大于6.0的版本
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 安卓大于4.4的版本
//            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
//        } else {
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), TIME_INTERVAL, pendingIntent);
//        }


        Intent dataIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MemoInfo", data);
        dataIntent.putExtras(bundle);
        dataIntent.putExtra("test", "test");
//        dataIntent.setClass(context, MemoDetailActivity.class);
        dataIntent.setAction(AppConstants.BROADCAST_SET_ALART);
        PendingIntent sender = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), dataIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, targetTime, INTERVAL, sender);
    }
}

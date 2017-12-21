package com.xy.memo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xy.memo.activity.MemoDetailActivity;
import com.xy.memo.model.MemoInfo;
import com.xy.memo.utils.LogUtil;

/**
 * 设置提醒的广播接收器
 * @author xy 2017/12/21.
 */

public class AlarmBroadcast extends BroadcastReceiver {
    private static final String TAG = AlarmBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(AppConstants.BROADCAST_SET_ALART)) {
            String test = intent.getStringExtra("test");
            LogUtil.d(TAG, "@@@@@ " + test);
            Intent jumpIntent = new Intent(context, MemoDetailActivity.class);
            Bundle bundle = new Bundle();
            MemoInfo info = (MemoInfo) intent.getSerializableExtra("MemoInfo");
            if(null != info) {
                bundle.putSerializable("MemoInfo", info);
            } else {
                LogUtil.d(TAG, "@@@@@ 数据为空 @@@@@");
            }
            jumpIntent.putExtras(bundle);
            jumpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(jumpIntent);
        }
    }
}

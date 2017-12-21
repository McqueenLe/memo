package com.xy.memo.utils;

import com.xy.memo.eventbus.EventBusEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus工具类
 * @author xy on 2017/9/13.
 */

public class EventBusUtils {
    /**
     * 注册事件
     * @param subscriber
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 取消注册事件
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        if(EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 发送事件
     * @param event
     */
    public static void sendEvent(EventBusEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件
     * @param event
     */
    public static void sendStickyEvent(EventBusEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 通过code码区分事件类型
     */
    public static final class EventCode {
        // 事件码
        public static final int EVENT_GET_IMG = 1230001; // 获取到图片事件
        public static final int EVENT_SET_MULTI_CHOICE = 1230002; // 更新主界面item多选状态
        public static final int EVENT_CHECK_STATE_CHANGE = 1230003; // 首页多选状态发生变化时的点击事件
        public static final int EVENT_MEMO_ADD = 1230004; // 新增便签通知事件
        public static final int EVENT_ADD_ALARM = 1230005; // 添加便签提醒
    }
}

package com.xy.memo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xy.memo.R;
import com.xy.memo.eventbus.EventBusEvent;
import com.xy.memo.utils.DateUtil;
import com.xy.memo.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 设置提醒dialog
 * @author xy 2017/12/20.
 */

public class DateDialog extends Dialog {
    private Context mContext;
    private TextView tvDate;
    private TextView tvTime;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btOk;
    private Button btCancel;
    private static DateDialog instance;
    private int year = 0, month = 0,day = 0,hour = 0,minute = 0;

    public DateDialog(@NonNull Context context) {
        super(context, R.style.addDevWindowStyle);
        this.mContext = context;
        initLayout(mContext);
    }

    /**
     * 初始化布局
     * @param ctx
     */
    private void initLayout(Context ctx) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_date_select, null);
        Window window = getWindow();
        window.setContentView(view);
        window.setWindowAnimations(R.style.add_dev_animstyle);
        WindowManager windowManager = window.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (displayMetrics.widthPixels * 0.9);
        window.setAttributes(lp);

        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        datePicker = findViewById(R.id.datepicker);
        timePicker = findViewById(R.id.timepicker);
        btOk = findViewById(R.id.bt_alarm_ok);
        btCancel = findViewById(R.id.bt_alarm_cancel);
        Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        year = currentYear;
        int currentMonth = calendar.get(Calendar.MONTH);
        month = currentMonth;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        day = currentDay;
        tvDate.setText(currentMonth + "-" + currentDay);
        String time = DateUtil.formatTime(System.currentTimeMillis(), "hh:mm");
        tvTime.setText(time);
        datePicker.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                // 设置日期
                year = i;
                month = i1;
                day = i2;
                StringBuilder strDate = new StringBuilder();
                strDate.append(datePicker.getYear()).append("-").append(datePicker.getMonth()+1).append("-").append(datePicker.getDayOfMonth());
                tvDate.setText(strDate);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
                // 设置时间
                StringBuilder strTime = new StringBuilder();
                strTime.append(i).append(":").append(i1);
                tvTime.setText(strTime);
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_ADD_ALARM);
                EventBus.getDefault().post(eventBusEvent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 获取选定后的时间
     * @return
     */
    public Long getDateLong() {
        StringBuilder time = new StringBuilder();
        time.append(year).append("-").append(month).append("-").append(day).append(" ").append(hour).append(":").append(minute);
        Date date = null;
        try {
            date = DateUtil.parseTime(time.toString(), "yyyy-MM-dd hh:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}

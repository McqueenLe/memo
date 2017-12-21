package com.xy.memo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xy.memo.R;
import com.xy.memo.utils.AppUtil;

/**
 * 顶部action
 * @author xy 2017/12/8.
 */

public class TopActionDialog extends PopupWindow{
    private Context mContext;
    private TextView tvCancel;
    private TextView tvTitle;
    private TextView tvAll;

    public TopActionDialog(Context ctx) {
        this.mContext = ctx;
        init(mContext);

    }
//    public TopActionDialog(@NonNull Context context) {
//        super(context);
//    }
//
//    public TopActionDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }
//
//    protected TopActionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_top_action);
//        Window window = this.getWindow();
//        window.setGravity(Gravity.TOP);
//        window.setContentView(R.layout.dialog_top_action);
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        setCanceledOnTouchOutside(false);
//        tvCancel = findViewById(R.id.tvCancel);
//        tvTitle = findViewById(R.id.tvTitle);
//        tvAll = findViewById(R.id.tvAll);
//
//    }

    /**
     * 初始化布局
     * @param ctx
     */
    private void init(Context ctx) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_top_action, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(AppUtil.dp2px(ctx, 56));
        this.setOutsideTouchable(false);

        tvCancel = view.findViewById(R.id.tvCancel);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvAll = view.findViewById(R.id.tvAll);
    }

    public void showMenu(){
        // 状态栏的高度
        Rect frame = new Rect();
        Activity activity = (Activity)mContext;
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, mContext.getResources().getDisplayMetrics());
        this.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP | Gravity.CENTER, 0, 0 + frame.top - dp);
    }

    /**
     * 设置取消点击监听
     * @param listener
     */
    public void setCancelListener(View.OnClickListener listener) {
        if(null != tvCancel) {
            tvCancel.setOnClickListener(listener);
        }
    }

    /**
     * 设置全选和反选点击监听
     * @param listener
     */
    public void setAllListener(View.OnClickListener listener) {
        if(null != tvAll) {
            tvAll.setOnClickListener(listener);
        }
    }

    /**
     * 设置全选和反选显示
     * @param text
     */
    public void setAllText(String text) {
         if(null != tvAll) {
             tvAll.setText(text);
         }
    }

    /**
     * 获取当前显示为全选还是反选
     * @return
     */
    public String getAllText() {
        String text = "";
        if(null != tvAll) {
            text = tvAll.getText().toString();
        }
        return text;
    }

    /**
     * 设置标题
     * @param text
     */
    public void setTitle(String text) {
        if(null != tvTitle) {
            tvTitle.setText(text);
        }
    }
}

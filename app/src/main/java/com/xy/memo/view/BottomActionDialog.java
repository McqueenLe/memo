package com.xy.memo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xy.memo.R;

/**
 * 底部操作栏
 * @author xy 2017/12/8.
 */

public class BottomActionDialog extends PopupWindow {
    private Context mContext;
    private TextView tvTop; // 置顶
    private TextView tvDelete; // 删除
    private TextView tvMove; // 移动

    public BottomActionDialog(Context ctx) {
        this.mContext = ctx;
        init(ctx);
    }

//    public BottomActionDialog(@NonNull Context context) {
//        super(context);
//    }
//
//    public BottomActionDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }
//
//    protected BottomActionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.dialog_multi_action);
//        Window window = this.getWindow();
//        // 设置布局
//        window.setContentView(R.layout.dialog_multi_action);
//        // 设置宽高
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        // 设置弹出的动画效果
//        window.setWindowAnimations(R.style.bottom_dialog_animstyle);
//        window.setGravity(Gravity.BOTTOM);
//        setCanceledOnTouchOutside(false);
//        tvTop = findViewById(R.id.tvTop);
//        tvDelete = findViewById(R.id.tvDelete);
//        tvMove = findViewById(R.id.tvMove);
//    }

    /**
     * 初始化布局设置
     * @param ctx
     */
    private void init(Context ctx) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_multi_action, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.bottom_dialog_animstyle);
        tvTop = view.findViewById(R.id.tvTop);
        tvDelete = view.findViewById(R.id.tvDelete);
        tvMove = view.findViewById(R.id.tvMove);
    }

    public void showMenu(){
        // 状态栏的高度
        Rect frame = new Rect();
        Activity activity = (Activity)mContext;
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, mContext.getResources().getDisplayMetrics());
        this.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, frame.top - 2*dp);
    }

    public void setTopListener(View.OnClickListener listener) {
        if(null != tvTop) {
            tvTop.setOnClickListener(listener);
        }
    }

    public void setDeleteListener(View.OnClickListener listener) {
        if(null != tvDelete) {
            tvDelete.setOnClickListener(listener);
        }
    }

    public void setMoveListener(View.OnClickListener listener) {
        if(null != tvMove) {
            tvMove.setOnClickListener(listener);
        }
    }
}

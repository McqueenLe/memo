package com.xy.memo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.memo.R;

/**
 * 自定义TitleBar
 * @author xy 2017/11/30.
 */

public class TitleBar extends LinearLayout {
    ImageView ivLeft;
    TextView tvTitle;
    TextView tvRight;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     * @param ctx
     */
    private void init(Context ctx) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_titlebar,this, true);
        ivLeft = view.findViewById(R.id.ivLeftIcon);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvRight = view.findViewById(R.id.tvRight);
    }

    /**
     * 设置左边点击监听
     * @param onClickListener
     */
    public void setLeftIconClickListener(OnClickListener onClickListener) {
        ivLeft.setOnClickListener(onClickListener);
    }

    /**
     * 设置右边点击监听
     * @param onClickListener
     */
    public void setRightIconClickListener(OnClickListener onClickListener) {
        tvRight.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 获取标题
     * @return
     */
    public String getTitle() {
        return tvTitle.getText().toString();
    }

    /**
     * 设置左边图标
     * @param resId
     */
    public void setLeftImgRes(int resId) {
        ivLeft.setImageResource(resId);
    }

    /**
     * 设置右边图标
     * @param resId
     */
    public void setRightImgRes(int resId) {
        tvRight.setBackgroundResource(resId);
    }

    /**
     * 设置右边文字
     * @param text
     */
    public void setRightText(String text) {
        tvRight.setText(text);
    }

    /**
     * 设置左右是否显示
     * @param isVisible
     */
    public void setLeftVisibility(boolean isVisible) {
        if(isVisible) {
            ivLeft.setVisibility(VISIBLE);
        } else {
            ivLeft.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置右边是否显示
     * @param isVisible
     */
    public void setRightVisibility(boolean isVisible) {
        if(isVisible) {
            tvRight.setVisibility(VISIBLE);
        } else {
            tvRight.setVisibility(INVISIBLE);
        }
    }
}

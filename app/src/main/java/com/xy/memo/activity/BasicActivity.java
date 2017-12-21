package com.xy.memo.activity;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.view.Window;

import com.xy.memo.AppManager;
import com.xy.memo.utils.AppUtil;
import com.xy.memo.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 功能描述：activity基类
 *
 * @author xy 2017 11 29
 */
public abstract class BasicActivity extends AppCompatActivity {
	private static final String TAG = BasicActivity.class.getSimpleName();
	private ProgressDialog mProgressDialog;
	private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
	private OnSoftKeyboardStateChangedListener listener; // 软键盘变化监听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加入Activity容器
        AppManager.getInstance().addActivity(this);
        // 设置样式
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 添加软键盘监听
		mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// 获取当前显示屏幕高度
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int screenHeight = dm.heightPixels;
				LogUtil.i(TAG, " @@@@@ 屏幕高度：" + screenHeight);

				//判断窗口可见区域大小
				Rect r = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

				// 如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
				int heightDifference = screenHeight - (r.bottom - r.top);
				LogUtil.i(TAG, " @@@@@ 键盘高度：" + heightDifference);
				boolean isKeyboardShowing = heightDifference > screenHeight/3;
				if(null != listener) {
					listener.OnSoftKeyboardStateChanged(isKeyboardShowing, heightDifference);
				}
			}
		};
		//注册布局变化监听
		getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);

//        // 初始化请求header
//        RequestHeader.init(this);

//        getIntentParams();
//        initLayout();
//        initVolley();
//        prepareData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		// 移除布局变化监听
		getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        // 从Activity容器中删除该activity
        AppManager.getInstance().removeActivity(this);
    }
    
    /**
	 * 显示加载框
	 * @param message
	 */
	protected void showProgressDialog(CharSequence message) {
		mProgressDialog = ProgressDialog.show(this, null, message);
	}

	/**
	 * 显示加载框
	 * @param messageResid
	 */
	protected void showProgressDialog(int messageResid) {
		showProgressDialog(getString(messageResid));
	}

	/**
	 * 取消加载显示
	 */
	protected void dismissProgressDialog() {
		if(mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * 获取软件变化状态和高度
	 * @param isSoftInputShow
	 * @param inputHeight
	 */
//	protected abstract void onInputChange(boolean isSoftInputShow, int inputHeight);

    /**
     * 初始化volley队列 RequestQueue mQueue = VolleyRequest.getInstance()(context);
     */
//    protected abstract void initVolley();
//
//    /**
//     * 初始化布局
//     */
//    protected abstract void initLayout();
//
//    /**
//     * 获取intent附加数据
//     */
//    protected abstract void getIntentParams();
//
//    /**
//     * 初始化业务数据
//     */
//    protected abstract void prepareData();

	/**
	 * 设置软键盘监听
	 * @param stateChangeListener
	 */
	public void setSoftInputStateChangeListener(OnSoftKeyboardStateChangedListener stateChangeListener) {
		this.listener = stateChangeListener;
	}

	public interface OnSoftKeyboardStateChangedListener {
		void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
	}
}
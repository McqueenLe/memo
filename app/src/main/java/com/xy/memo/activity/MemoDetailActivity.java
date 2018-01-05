package com.xy.memo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xy.memo.AppConstants;
import com.xy.memo.R;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.cell.CameraCell;
import com.xy.memo.cell.TagInfo;
import com.xy.memo.cell.TagsCell;
import com.xy.memo.db.MemoDao;
import com.xy.memo.eventbus.EventBusEvent;
import com.xy.memo.model.MemoInfo;
import com.xy.memo.model.MemoType;
import com.xy.memo.utils.AlarmUtil;
import com.xy.memo.utils.AppUtil;
import com.xy.memo.utils.BitmapUtil;
import com.xy.memo.utils.CameraUtil;
import com.xy.memo.utils.DateUtil;
import com.xy.memo.utils.EventBusUtils;
import com.xy.memo.utils.LogUtil;
import com.xy.memo.utils.SharedPreferencesInfo;
import com.xy.memo.utils.ToastUtil;
import com.xy.memo.view.BottomActionView;
import com.xy.memo.view.DateDialog;
import com.xy.memo.view.PictureAndTextEditorView;
import com.xy.memo.view.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.xy.memo.utils.EventBusUtils.EventCode.EVENT_ADD_ALARM;

/**
 * 备忘录详情页面
 * @author xy 2017 11 29
 */

public class MemoDetailActivity extends BasicActivity implements BottomActionView.ActionListener, BasicActivity.OnSoftKeyboardStateChangedListener {
    private static final String TAG = MemoDetailActivity.class.getSimpleName();
    private final int mRequestCodeFromCamera = 1001; // 拍照
    private final int mRequestCodeCutImage = 1002; // 裁剪
    private Context mContext;
    private TitleBar mTitleBar;
    private PictureAndTextEditorView etContent;
    private MemoInfo memoInfo;
    private BottomActionView bottomActionView;
    private String targetText; // 要保存的便签内容
    private int softInputHeight; // 输入键盘高度
    private boolean isInputShow = false; // 是否显示软键盘
    private boolean isImgAddClick = false; // 是否点击添加图片
    private boolean isTextFromBack = false; // 是否是点击回退操作设置的内容
    private List<String> backList = new ArrayList<>(); // 保存每一次编辑之后的数据，方便回退操作

    private File mGetFile; // 拍照、相册到的图片文件
    private File mCutFile; // 裁剪后的图片文件
    private File mSaveDir; // 保存目录
    private DateDialog dialog; // 时间选择器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        mContext = MemoDetailActivity.this;
        memoInfo = (MemoInfo) getIntent().getSerializableExtra("MemoInfo");
        // 设置缓存目录
        mSaveDir = new File(Environment.getExternalStorageDirectory(), AppConstants.CACHE_DIR);
        if (!mSaveDir.exists()) {
            mSaveDir.mkdirs();
        }

        mTitleBar = findViewById(R.id.titlebar_memo_detail);
        mTitleBar.setRightImgRes(R.drawable.ic_notify);
        bottomActionView = findViewById(R.id.bottomActionView);
        bottomActionView.setListener(this);
        etContent = findViewById(R.id.etContent_memo_detail);

        if(null != memoInfo) {
            mTitleBar.setTitle(DateUtil.formatTime(memoInfo.insertTime, "MM月dd日"));
            String[] strArray = memoInfo.memoContent.replace("\n", "").split(PictureAndTextEditorView.mBitmapTag);
            List<String> contentList = new ArrayList<>();
            for(int i=0;i<strArray.length;i++) {
                contentList.add(strArray[i]);
            }
            etContent.setContentList(contentList);
//            etContent.setSelection(memoInfo.memoContent.length());
        } else {
            mTitleBar.setTitle(DateUtil.formatTime(System.currentTimeMillis(), "MM月dd日"));
        }
        mTitleBar.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBar.setRightIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = etContent.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    if(!isTextFromBack) {
                        backList.add(text);
                    }
                    bottomActionView.setBackEnabled(true);
                } else {
                    bottomActionView.setBackEnabled(false);
//                    backList.clear();
                }
            }
        });
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImgAddClick = false;
            }
        });
        dialog = new DateDialog(mContext);
        setSoftInputStateChangeListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // toolbar返回键监听
                finish();
                break;

                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBack() {
        isTextFromBack = true;
        // 每一次从最后的数据往回回退
        String text = etContent.getText().toString();
//        backList.lastIndexOf(text);
        int lastIndex = backList.lastIndexOf(text) - 1;
        String mBitmapTag = PictureAndTextEditorView.mBitmapTag;
        if(lastIndex >= 0) {
            String content = backList.get(lastIndex);
            String[] array = content.split(PictureAndTextEditorView.mBitmapTag);
            etContent.setText("");
            for(String str : array) {
                if (str.indexOf(mBitmapTag) != -1 || new File(str).exists()) {//判断是否是图片地址
                    String path = str.replace(mBitmapTag, "");//还原地址字符串
                    Bitmap bitmap = BitmapUtil.getSmallBitmap(mContext, path, 480, 800);
                    //插入图片
                    etContent.insertBitmap(path, bitmap);
                } else {
                    //插入文字
                    etContent.append(str);
                }
            }
//            etContent.setText(content);
//            etContent.setSelection(content.length());
//            if(lastIndex + 1 == backList.size()) {
//                backList.remove(backList.size() - 1);
//            }
            backList.remove(text);
        } else {
            etContent.setText("");
        }
        isTextFromBack = false;
    }

    @Override
    public void onForward() {

    }

    @Override
    public void onImgAdd() {
        isImgAddClick  = isInputShow;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onVideoAdd() {

    }

    @Override
    public void onCamera() {
        fromCamera();
    }

    @Override
    public void onSave() {
        // 保存便签
        boolean isNew = false;
        String text = etContent.getText().toString();
        if(TextUtils.isEmpty(text)) {
            return;
        }
        if(null == memoInfo) {
            isNew = true;
            memoInfo = new MemoInfo();
        } else {
            isNew = false;
        }
        if(null == memoInfo.insertTime) {
            memoInfo.insertTime = System.currentTimeMillis();
        }
        memoInfo.memoTitle = "备忘录";
        memoInfo.memoContent = text;
        memoInfo.tag = "所有";
        MemoDao.insertMemo(mContext, memoInfo, isNew);
        bottomActionView.setVisibility(View.GONE);
        AppUtil.hideInputKeyboard(mContext, etContent);
        etContent.clearFocus();

        // 更新标签数量
        String tags = SharedPreferencesInfo.getTagString(mContext, SharedPreferencesInfo.TAGS);
        if(!TextUtils.isEmpty(tags)) {
            Gson gson = new Gson();
            List<TagInfo> tagList = gson.fromJson(tags, new TypeToken<List<TagInfo>>(){}.getType());
            if(null != tagList && tagList.size() > 0) {
                for(TagInfo tagInfo : tagList) {
                    if(tagInfo.tagName.equals(memoInfo.tag)) {
                        tagInfo.memoCount += 1;
                    }
                }
                String jsonTags = gson.toJson(tagList);
                SharedPreferencesInfo.saveTagString(mContext, SharedPreferencesInfo.TAGS, jsonTags);
            }
        }
        ToastUtil.showToast(mContext, "已保存");
        EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_MEMO_ADD);
        EventBus.getDefault().post(eventBusEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent eventBusEvent) {
        int code = eventBusEvent.getCode();
        if(code == EventBusUtils.EventCode.EVENT_GET_IMG) {
            String path = (String) eventBusEvent.getData();
            etContent.insertBitmap(path);
        } else if(code == EVENT_ADD_ALARM) { // 添加便签提醒之后的回调
            if(null != dialog) {
                long targetTime = dialog.getDateLong();
                // 设置闹钟提醒
                AlarmUtil.setAlarm1(mContext, targetTime, memoInfo);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight) {
        isInputShow = isKeyBoardShow;
        if(isKeyBoardShow && bottomActionView.getVisibility() == View.GONE) {
//            softInputHeight = keyboardHeight;
            bottomActionView.setVisibility(View.VISIBLE);
            LogUtil.i(TAG, "键盘高度: " + keyboardHeight);
            return;
        } else if(!isKeyBoardShow && isImgAddClick) {
            return;
        } else if(isKeyBoardShow && bottomActionView.getVisibility() == View.VISIBLE) {
            return;
        } else if(!isKeyBoardShow && bottomActionView.getVisibility() == View.VISIBLE && !isImgAddClick){
            LogUtil.i(TAG, "@@@@@ BottomActionView state hidden @@@@@");
            bottomActionView.setVisibility(View.GONE);
        }
    }

    /**
     * 通过拍照获取图片
     */
    public void fromCamera() {
        mGetFile = new File(mSaveDir, "get-" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mGetFile));
        startActivityForResult(intent, mRequestCodeFromCamera);
    }

    /**
     * 从相册或拍照获取到图片后的下一步处理，裁剪
     * @param file
     */
    private void doImage(File file) {
//        Intent intent = new Intent();
//        intent.putExtra("title", "选择图片");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        // 开始裁剪
//        mCutFile = new File(mSaveDir, "cut-" + (System.currentTimeMillis() + 1) + ".jpg");
//        CameraUtil.startPhotoZoom(MemoDetailActivity.this, intent, mRequestCodeCutImage, file, mCutFile);
        etContent.insertBitmap(file.getAbsolutePath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == mRequestCodeFromCamera) { // 拍照
                // 此处应该结束选择，返回图片链接 mGetFile
                doImage(mGetFile);
            } else if (requestCode == mRequestCodeCutImage) { // 裁剪
                etContent.insertBitmap(mCutFile.getAbsolutePath());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isInputShow) {
            AppUtil.hideInputKeyboard(mContext, etContent);
        } else if(bottomActionView.getVisibility() == View.VISIBLE) {
            bottomActionView.setVisibility(View.GONE);
        } else {
            finish();
        }
    }
}

package com.xy.memo.view;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xy.memo.R;
import com.xy.memo.base.Cell;
import com.xy.memo.base.RVSimpleAdapter;
import com.xy.memo.cell.CameraCell;
import com.xy.memo.cell.ImgCell;
import com.xy.memo.utils.ImgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录添加时的底部菜单
 * @author xy 2017/11/30.
 */

public class BottomActionView extends LinearLayout implements View.OnClickListener{
    private Context mContext;
    private RecyclerView recyclerViewImg;
    private ActionListener actionListener;
    private List<Cell> imgList = new ArrayList<>();
    private RVSimpleAdapter imgAdapter;
    private ImageView ivBack;
    private ImageView ivForward;

    public BottomActionView(Context context) {
        super(context);
        init(context);
    }

    public BottomActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomActionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context ctx) {
        mContext = ctx;
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_bottom_action, this, true);
        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setEnabled(false);
        ivForward = view.findViewById(R.id.ivForward);
        ivForward.setEnabled(false);
        ImageView ivImg = view.findViewById(R.id.ivPicture);
        ImageView ivVideo = view.findViewById(R.id.ivVideo);
        ImageView ivSave = view.findViewById(R.id.ivSave);
        recyclerViewImg = view.findViewById(R.id.recycler_img_list);
        recyclerViewImg.setLayoutManager(new GridLayoutManager(ctx, 4));
        imgAdapter = new RVSimpleAdapter();
//        imgAdapter.setData(imgList);
        recyclerViewImg.setAdapter(imgAdapter);
        getImgList();

        ivBack.setOnClickListener(this);
        ivForward.setOnClickListener(this);
        ivImg.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        ivSave.setOnClickListener(this);
    }

    /**
     * 获取图片List
     */
    private void getImgList() {
        CameraCell cameraCell = new CameraCell(mContext, null);
        cameraCell.setOnClickListener(this);
        imgList.add(cameraCell);
        List<String> paths = ImgUtil.getLatestImagePaths(mContext);
        ImgCell imgCell;
        for(String path : paths) {
            imgCell = new ImgCell(mContext, path);
            imgList.add(imgCell);
        }
        imgAdapter.setData(imgList);
    }

    public void setListener(ActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: // 点击返回
                if(null != actionListener) {
                    actionListener.onBack();
                }
                break;

            case R.id.ivForward: // 点击前进
                if(null != actionListener) {
                    actionListener.onForward();
                }
                break;

            case R.id.ivPicture: // 点击添加图片
                if(null != actionListener) {
                    actionListener.onImgAdd();
                }
                break;

            case R.id.ivVideo: // 点击添加视频
                if(null != actionListener) {
                    actionListener.onVideoAdd();
                }
                break;

            case R.id.ivSave: // 点击保存
                if(null != actionListener) {
                    actionListener.onSave();
                }
                break;

            case R.id.ivCamera: // 点击拍照添加图片
                if(null != actionListener) {
                    actionListener.onCamera();
                }
                break;

                default:
                    break;
        }
    }

    /**
     * 设置回退是否可用
     * @param enabled
     */
    public void setBackEnabled(boolean enabled) {
        ivBack.setEnabled(enabled);
    }

    /**
     * 设置前进是否可用
     * @param enabled
     */
    public void setForwardEnabled(boolean enabled) {
        ivForward.setEnabled(enabled);
    }

    /**
     * 设置图片选择区域是否可见
     * @param visibile
     */
    public void setImgSelVisibility(boolean visibile) {
        if(visibile) {
            recyclerViewImg.setVisibility(VISIBLE);
        } else {
            recyclerViewImg.setVisibility(GONE);
        }
    }

    public interface ActionListener {
        void onBack(); // 退回到前一步操作
        void onForward(); // 跳转到后一步操作
        void onImgAdd(); // 添加图片
        void onVideoAdd(); // 添加视频
        void onSave(); // 保存
        void onCamera(); // 拍照获取图片点击
    }
}

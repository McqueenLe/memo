package com.xy.memo.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xy.memo.R;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVBaseViewHolder;
import com.xy.memo.utils.LogUtil;

/**
 * 点击后拍照
 * @author xy 2017/12/5.
 */

public class CameraCell extends RVBaseCell {
    private ImageView ivCamera;
    private View.OnClickListener onClickListener;

    public CameraCell(Context ctx, Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        ivCamera = (ImageView) holder.getView(R.id.ivCamera);
        ivCamera.setImageResource(R.drawable.ic_camera);
        ivCamera.setOnClickListener(onClickListener);
    }

    /**
     * 设置监听
     * @param onClickListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

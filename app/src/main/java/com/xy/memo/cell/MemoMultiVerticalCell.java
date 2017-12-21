package com.xy.memo.cell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xy.memo.R;
import com.xy.memo.activity.MemoDetailActivity;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVBaseViewHolder;
import com.xy.memo.model.MemoInfo;
import com.xy.memo.model.MemoType;
import com.xy.memo.utils.BitmapUtil;
import com.xy.memo.utils.DateUtil;
import com.xy.memo.view.PictureAndTextEditorView;

import java.io.File;

/**
 * 纵向排版的文字加图片类型
 * @author xy 2017/12/4.
 */

public class MemoMultiVerticalCell extends RVBaseCell<MemoInfo> {
    private Context mContext;

    public MemoMultiVerticalCell(Context ctx, MemoInfo memoInfo) {
        super(memoInfo);
        this.mContext = ctx;
    }

    @Override
    public int getItemType() {
        return MemoType.MEMO_TYPE_TEXT_IMG_VERTICAL;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_memo_text_img_vertical, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        final MemoInfo memoInfo = mData;
        TextView tvTitle = (TextView) holder.getView(R.id.tv_memo_title);
        TextView tvContent = (TextView) holder.getView(R.id.tv_memo_content);
        TextView tvTime = (TextView) holder.getView(R.id.tv_memo_time);
        ImageView ivVertical = (ImageView) holder.getView(R.id.ivVertical);

        tvTime.setText(DateUtil.formatTime(memoInfo.insertTime, "yyyy年MM月dd日 晚上hh:mm"));
        String firstImgPah = "";
        String firstLineText = "";
        String[] contents = memoInfo.memoContent.replace("\n", "").split(PictureAndTextEditorView.mBitmapTag);
        for(int i=0; i<contents.length; i++) {
            if(new File(contents[i]).exists()) {
                firstImgPah = contents[i];
                break;
            } else {
                firstLineText = contents[i];
            }
        }
        if(!TextUtils.isEmpty(firstImgPah)) {
            ivVertical.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapUtil.getSmallBitmap(mContext, firstImgPah, 320, 640);
            if(null != bitmap) {
                ivVertical.setImageBitmap(bitmap);
            }
        } else {
            ivVertical.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(firstLineText)) {
            tvTitle.setText(firstLineText);
            tvContent.setText(firstLineText);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MemoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Memoinfo", memoInfo);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}

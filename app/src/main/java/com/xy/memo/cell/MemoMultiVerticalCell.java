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
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xy.memo.R;
import com.xy.memo.activity.GalleryActivity;
import com.xy.memo.activity.MemoDetailActivity;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVBaseViewHolder;
import com.xy.memo.eventbus.EventBusEvent;
import com.xy.memo.model.MemoInfo;
import com.xy.memo.model.MemoType;
import com.xy.memo.utils.BitmapUtil;
import com.xy.memo.utils.DateUtil;
import com.xy.memo.utils.EventBusUtils;
import com.xy.memo.view.PictureAndTextEditorView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

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
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        final MemoInfo memoInfo = mData;
        TextView tvTitle = (TextView) holder.getView(R.id.tv_memo_title);
        TextView tvContent = (TextView) holder.getView(R.id.tv_memo_content);
        TextView tvTime = (TextView) holder.getView(R.id.tv_memo_time);
        ImageView ivVertical = (ImageView) holder.getView(R.id.ivVertical);
        final CheckedTextView cbSelected = (CheckedTextView) holder.getView(R.id.cbSelected);

        tvTime.setText(DateUtil.formatTime(memoInfo.insertTime, "yyyy年MM月dd日"));
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
        // 是否为多选模式
        if(memoInfo.isMultiMode) {
            cbSelected.setVisibility(View.VISIBLE);
            cbSelected.setChecked(memoInfo.isChecked);
            holder.itemView.setOnClickListener(new View.OnClickListener() { // 多选模式下短按设置是否选中
                @Override
                public void onClick(View view) {
                    cbSelected.toggle();
                    if(cbSelected.isChecked()) {
                        mData.isChecked = true;
                        holder.itemView.setSelected(true);
                    } else {
                        mData.isChecked = false;
                        holder.itemView.setSelected(false);
                    }
                    EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_CHECK_STATE_CHANGE);
                    EventBus.getDefault().post(eventBusEvent);
                }
            });
        } else {
            memoInfo.isChecked = false;
            cbSelected.setChecked(false);
            cbSelected.setVisibility(View.GONE);
            holder.itemView.setSelected(false);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 非多选模式下短按查看详情
                    Intent intent = new Intent(mContext, MemoDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MemoInfo", memoInfo);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) { // 长按进入多选模式
                cbSelected.setVisibility(View.VISIBLE);
                mData.isChecked = true;
                holder.itemView.setSelected(true);

                // 更新主界面UI
                EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_SET_MULTI_CHOICE);
                EventBus.getDefault().post(eventBusEvent);
                return true;
            }
        });
        ivVertical.setOnClickListener(new View.OnClickListener() { // 跳转到图集页面
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GalleryActivity.class);
                ArrayList<String> pathList = new ArrayList<>();
                String[] contents = memoInfo.memoContent.replace("\n", "").trim().split(PictureAndTextEditorView.mBitmapTag);
                for(int i=0; i<contents.length; i++) {
                    if(new File(contents[i]).exists()) {
                        pathList.add(contents[i]);
                    }
                }
                intent.putStringArrayListExtra("PathList", pathList);
                mContext.startActivity(intent);
            }
        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, MemoDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Memoinfo", memoInfo);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//            }
//        });
    }
}

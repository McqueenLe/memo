package com.xy.memo.cell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
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
import com.xy.memo.utils.DateUtil;
import com.xy.memo.utils.EventBusUtils;
import com.xy.memo.view.PictureAndTextEditorView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * 图文memo
 * @author xy 2017/12/4.
 */

public class MemoMultiCell extends RVBaseCell<MemoInfo> {
    private Context mContext;

    public MemoMultiCell(Context ctx, MemoInfo memoInfo) {
        super(memoInfo);
        this.mContext = ctx;
    }

    @Override
    public int getItemType() {
        return MemoType.MEMO_TYPE_TEXT_IMG;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from((Context) parent.getContext()).inflate(R.layout.item_memo_text_img, parent, false));
    }

    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        final MemoInfo memoInfo = mData;
        TextView tvTimeTitle = (TextView) holder.getView(R.id.tv_memo_time_sort);
        TextView tvTitle = (TextView) holder.getView(R.id.tv_memo_title);
        TextView tvContent = (TextView) holder.getView(R.id.tv_memo_content);
        TextView tvTime = (TextView) holder.getView(R.id.tv_memo_time);
        ImageView ivRightImg = (ImageView) holder.getView(R.id.ivRightImg);
        final CheckedTextView cbSelected = (CheckedTextView) holder.getView(R.id.cbSelected);

        tvTimeTitle.setText(DateUtil.formatTime(memoInfo.insertTime, "yyyy年MM月"));
        tvTime.setText(DateUtil.formatTime(memoInfo.insertTime, "yyyy年MM月dd日 晚上hh:mm"));
        String firstImgPah = "";
        String firstLineText = "";
        String[] contents = memoInfo.memoContent.replace("\n", "").trim().split(PictureAndTextEditorView.mBitmapTag);
        for(int i=0; i<contents.length; i++) {
            if(new File(contents[i]).exists()) {
                firstImgPah = contents[i];
                break;
            }
        }
        for(int i=0; i<contents.length; i++) {
            if(!new File(contents[i]).exists() && !TextUtils.isEmpty(contents[i])) {
                firstLineText = contents[i];
                break;
            }
        }

        if(!TextUtils.isEmpty(firstImgPah)) {
            ivRightImg.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(firstImgPah);
            if(null != bitmap) {
                ivRightImg.setImageBitmap(bitmap);
            }
        } else {
            ivRightImg.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(firstLineText)) {
            tvTitle.setText(firstLineText);
            tvContent.setText(firstLineText);
        }
        // 是否为多选模式
        if(memoInfo.isMultiMode) {
            cbSelected.setVisibility(View.VISIBLE);
            cbSelected.setChecked(memoInfo.isChecked);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                public void onClick(View view) {
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
            public boolean onLongClick(View view) {
                cbSelected.setVisibility(View.VISIBLE);
                mData.isChecked = true;
                holder.itemView.setSelected(true);

                // 更新主界面UI
                EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_SET_MULTI_CHOICE);
                EventBus.getDefault().post(eventBusEvent);
                return true;
            }
        });
        ivRightImg.setOnClickListener(new View.OnClickListener() {
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
    }

}

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
import android.widget.TextView;

import com.xy.memo.R;
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

/**
 * 纯文字类型备忘录
 * @author xy 2017/11/29.
 */

public class MemoTextCell extends RVBaseCell<MemoInfo> {

    private Context mContext;

    public MemoTextCell(Context ctx, MemoInfo memoInfo) {
        super(memoInfo);
        this.mContext = ctx;
    }

    @Override
    public int getItemType() {
        return MemoType.MEMO_TYPE_TEXT;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_memo_text, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        final MemoInfo memoInfo = mData;
        TextView tvTimeTitle = (TextView) holder.getView(R.id.tv_memo_time_sort);
        TextView tvTitle = (TextView) holder.getView(R.id.tv_memo_title);
        TextView tvContent = (TextView) holder.getView(R.id.tv_memo_content);
        TextView tvTime = (TextView) holder.getView(R.id.tv_memo_time);
        final CheckedTextView cbSelected  = (CheckedTextView) holder.getView(R.id.cbSelected);

        tvTimeTitle.setText(DateUtil.formatTime(memoInfo.insertTime, "yyyy年MM月"));
        String firstLineText = "";
        String[] contents = memoInfo.memoContent.split(PictureAndTextEditorView.mBitmapTag);
        for(int i=0; i<contents.length; i++) {
            if(!new File(contents[i]).exists() && !contents[i].equals(PictureAndTextEditorView.mBitmapTag)) {
                firstLineText = contents[i];
            }
            break;
        }
        if(!TextUtils.isEmpty(firstLineText)) {
            tvContent.setText(firstLineText);
            tvTitle.setText(firstLineText);
        }
        if(memoInfo.isMultiMode) {
            cbSelected.setVisibility(View.VISIBLE);
            cbSelected.setChecked(memoInfo.isChecked);
        } else {
            cbSelected.setVisibility(View.GONE);
        }
        cbSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                checkedTextView.toggle();
                if(checkedTextView.isChecked()) {
                    mData.isChecked = true;
                } else {
                    mData.isChecked = false;
                }
            }
        });
        tvTime.setText(DateUtil.formatTime(memoInfo.insertTime, "yyyy年MM月dd日 晚上hh:mm"));
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cbSelected.setVisibility(View.VISIBLE);
//                cbSelected.setChecked(true);
                mData.isChecked = true;
                // 更新主界面UI
                EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_SET_MULTI_CHOICE);
                EventBus.getDefault().post(eventBusEvent);
                return true;
            }
        });
    }
}

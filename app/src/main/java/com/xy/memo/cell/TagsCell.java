package com.xy.memo.cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xy.memo.R;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVBaseViewHolder;

/**
 * 便签夹cell
 * @author xy 2017/12/7.
 */

public class TagsCell extends RVBaseCell<TagInfo> {

    public TagsCell(TagInfo tagInfo) {
        super(tagInfo);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        TagInfo tagInfo = mData;
        ImageView imageView = (ImageView) holder.getView(R.id.ivTagsIcon);
        TextView tvTagsName = (TextView) holder.getView(R.id.tvTagsName);
        TextView tvDetail = (TextView) holder.getView(R.id.tvTagsDetail);

        if(tagInfo.tagName.equals("废纸篓")) {
            imageView.setBackgroundResource(R.drawable.ic_trash);
        } else {
            imageView.setBackgroundResource(R.drawable.ic_note_taking);
        }
        tvTagsName.setText(tagInfo.tagName);
        tvDetail.setText(tagInfo.memoCount.toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

package com.xy.memo.cell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xy.memo.R;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVBaseViewHolder;
import com.xy.memo.eventbus.EventBusEvent;
import com.xy.memo.utils.EventBusUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 单个图片展示item
 * @author xy 2017/11/30.
 */

public class ImgCell extends RVBaseCell<String> {

    public ImgCell(Context ctx, String s) {
        super(s);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.getView(R.id.ivImg);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_photo)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage("file://" + mData, imageView, options);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBusEvent eventBusEvent = new EventBusEvent(EventBusUtils.EventCode.EVENT_GET_IMG, mData);
                EventBus.getDefault().post(eventBusEvent);
            }
        });
    }
}

package com.xy.memo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xy.memo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 图集界面
 * @author xy 2017/12/26.
 */

public class GalleryActivity extends BasicActivity {
    private TextView tvBack;
    private TextView tvShare;
    private TextView tvDownload;
    private ViewPager viewPager;
    private List<ImageView> viewList = new ArrayList<>();
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initLayout();
        List<String> pathList = getIntent().getStringArrayListExtra("PathList");
        if(null != pathList && pathList.size() > 0) {
            for(String path : pathList) {
                ImageView imageView = new ImageView(GalleryActivity.this);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if(null != bitmap) {
                    imageView.setImageBitmap(bitmap);
                    viewList.add(imageView);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initLayout() {
        tvBack = findViewById(R.id.tvBack_galleryactivity);
        tvShare = findViewById(R.id.tvShare_galleryactivity);
        tvDownload = findViewById(R.id.tvSave_galleryactivity);
        viewPager = findViewById(R.id.viewpager);
        mAdapter = new ViewPagerAdapter(GalleryActivity.this, viewList);
        viewPager.setAdapter(mAdapter);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private static class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<ImageView> viewList;

        public ViewPagerAdapter(Context ctx, List<ImageView> imgList) {
            this.mContext = ctx;
            this.viewList = imgList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }
}

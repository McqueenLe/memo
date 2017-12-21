package com.xy.memo.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 首页recyclerview的间距
 * @author xy 2017/11/29.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.top = space;
//        outRect.bottom = space;
//        outRect.left = space;
//        outRect.right = space;
        if(parent.getChildPosition(view) != -1)
            outRect.top = space;
    }
}

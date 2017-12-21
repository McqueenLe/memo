package com.xy.memo.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xy.memo.R;
import com.xy.memo.base.RVBaseViewHolder;
import com.xy.memo.base.RVSimpleAdapter;

public class LoadingCell extends RVAbsStateCell {
    public LoadingCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.LOADING_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

    }

    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_loading_layout,null);
    }
}

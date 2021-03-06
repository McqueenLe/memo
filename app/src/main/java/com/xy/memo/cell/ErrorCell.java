package com.xy.memo.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xy.memo.R;
import com.xy.memo.base.RVBaseViewHolder;
import com.xy.memo.base.RVSimpleAdapter;


public class ErrorCell extends RVAbsStateCell {
    public ErrorCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.ERROR_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

    }

    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_error_layout,null);
    }
}

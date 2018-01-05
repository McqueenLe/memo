package com.xy.memo.base;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.xy.memo.R;
import com.xy.memo.helper.ItemTouchHelperAdapter;
import com.xy.memo.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * recyclerview的adapter抽象基类
 * @param <C>
 */
public  abstract class RVBaseAdapter<C extends RVBaseCell>  extends RecyclerView.Adapter<RVBaseViewHolder> implements ItemTouchHelperAdapter {
    public static final String TAG = RVBaseAdapter.class.getSimpleName();
    private OnStartDragListener mDragStartListener; // 拖拽监听
    protected List<C> mData = new ArrayList<>();; // 数据集合
    private boolean mCanDrag = false;

    public RVBaseAdapter(){

    }

    public void setDragable(boolean canDrag) {
        this.mCanDrag = canDrag;
        notifyDataSetChanged();
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for(int i=0;i<getItemCount();i++){
            if(viewType == mData.get(i).getItemType()){
                return mData.get(i).onCreateViewHolder(parent,viewType);
            }
        }
        throw new RuntimeException("wrong viewType");
    }

    @Override
    public void onBindViewHolder(final RVBaseViewHolder holder, int position) {
        mData.get(position).onBindViewHolder(holder,position);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCanDrag && event.getAction() == MotionEvent.ACTION_DOWN && null != mDragStartListener) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false; // onTouch事件中：down事件返回值标记此次事件是否为点击事件（返回false，是点击事件；返回true，不记为点击事件）
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(RVBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.e(TAG,"onViewDetachedFromWindow invoke...");
        //释放资源
        int position = holder.getAdapterPosition();
        //越界检查
        if(position<0 || position>=mData.size()){
            return;
        }
        mData.get(position).releaseResource();
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0:mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 设置拖拽监听
     * @param dragListener
     */
    public void setDragListener(OnStartDragListener dragListener) {
        this.mDragStartListener = dragListener;
    }

    /**
     * 设置list数据
     * @param data
     */
    public void setData(List<C> data) {
        addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 获取List数据
     * @return
     */
    public List<C> getData() {
        return mData;
    }

    /**
     * 添加单个数据
     * @param cell
     */
    public void add(C cell){
         mData.add(cell);
         notifyItemRangeChanged(0, mData.size()-1);
//         notifyDataSetChanged();
    }

    /**
     * 添加数据到指定位置
     * @param index
     * @param cell
     */
    public void add(int index,C cell){
        mData.add(index,cell);
        notifyItemChanged(index);
    }

    /**
     * 移除单个数据
     * @param cell
     */
    public void remove(C cell){
        int indexOfCell = mData.indexOf(cell);
        remove(indexOfCell);
    }

    /**
     * 移除指定位置上的数据
     * @param index
     */
    public void remove(int index){
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * 从指定位置开始，移除指定个数的数据
     * @param start
     * @param count
     */
    public void remove(int start,int count){
        if((start + count) > mData.size()){
            return;
        }
        mData.subList(start,start+count).clear();
        notifyItemRangeRemoved(start,count);
    }

    /**
     * 添加list数据
     * @param cells
     */
    public void addAll(List<C> cells){
        if(cells == null || cells.size() == 0){
            return;
        }
        Log.e(TAG,"addAll cell size:"+cells.size());
        mData.addAll(cells);
        notifyItemRangeChanged(mData.size()-cells.size(),mData.size());
    }

    /**
     * 从指定位置添加list数据
     * @param index
     * @param cells
     */
    public void addAll(int index,List<C> cells){
        if(cells == null || cells.size() == 0){
            return;
        }
        mData.addAll(index,cells);
        notifyItemRangeChanged(index,index+cells.size());
    }

    /**
     * 清除数据
     */
    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 数据变化时触发动画
     * @param recyclerView
     */
    public void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     * @param holder
     * @param position
     */
    protected abstract void onViewHolderBound(RVBaseViewHolder holder, int position);

}

package com.xy.memo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xy.memo.R;
import com.xy.memo.base.Cell;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVSimpleAdapter;
import com.xy.memo.cell.MemoMultiCell;
import com.xy.memo.cell.MemoMultiVerticalCell;
import com.xy.memo.cell.TagInfo;
import com.xy.memo.db.MemoDao;
import com.xy.memo.eventbus.EventBusEvent;
import com.xy.memo.helper.OnStartDragListener;
import com.xy.memo.helper.SimpleItemTouchHelperCallback;
import com.xy.memo.model.MemoInfo;
import com.xy.memo.utils.EventBusUtils;
import com.xy.memo.utils.SharedPreferencesInfo;
import com.xy.memo.view.BottomActionDialog;
import com.xy.memo.view.TopActionDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,OnStartDragListener {
    private Context mContext;
    private RecyclerView mRecyclerview;
    private TopActionDialog topActionDialog;
    private BottomActionDialog bottomActionDialog;
    private RVSimpleAdapter rvSimpleAdapter;
    private ItemTouchHelper mItemTouchHelper; // 处理置顶以及左右滑动
    private TextView tvSort;
    private List<MemoInfo> memoInfoList; // 便签数据
    private boolean isVertical = false; // 是否默认为纵向排版

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        initLayout();
        // 第一次登陆默认保存两个便签
        boolean isFirst = SharedPreferencesInfo.getTagBoolean(mContext, SharedPreferencesInfo.IS_FIRST_USE, true);
        if(isFirst) {
            List<TagInfo> tagList = new ArrayList<>();
            SharedPreferencesInfo.saveTagBoolean(mContext, SharedPreferencesInfo.IS_FIRST_USE, false);
            TagInfo tagTrash = new TagInfo();
            tagTrash.tagName = "废纸篓";
            tagTrash.memoCount = 0;
            tagList.add(tagTrash);

            TagInfo tagAll = new TagInfo();
            tagAll.tagName = "所有";
            tagAll.memoCount = 0;
            tagList.add(tagAll);

            Gson gson = new Gson();
            String gsonListStr = gson.toJson(tagList);
            if(!TextUtils.isEmpty(gsonListStr)) {
                SharedPreferencesInfo.saveTagString(mContext, SharedPreferencesInfo.TAGS, gsonListStr);
            }
        }
        getMemoList(isVertical);
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        // 设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvSort = toolbar.findViewById(R.id.toolbar_sort);
        tvSort.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);
        rvSimpleAdapter = new RVSimpleAdapter();
        rvSimpleAdapter.setDragListener(this);
        // 处理拖拽和侧滑操作
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(rvSimpleAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);

        mRecyclerview.setAdapter(rvSimpleAdapter);
        boolean isVerticalLayout = SharedPreferencesInfo.getTagBoolean(mContext, SharedPreferencesInfo.IS_VERTICAL_LAYOUT, false);
        isVertical = isVerticalLayout;
        if(isVerticalLayout) {
            tvSort.setBackgroundResource(R.drawable.selector_home_sort_grid);
        } else {
            tvSort.setBackgroundResource(R.drawable.selector_home_sort_row);
        }

        topActionDialog = new TopActionDialog(mContext);
        bottomActionDialog = new BottomActionDialog(mContext);
        topActionDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 取消多选模式
                setMode(false);
                rvSimpleAdapter.notifyDataSetChanged();
            }
        });
        topActionDialog.setAllListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = topActionDialog.getAllText();
                if(!TextUtils.isEmpty(text) && text.equals("全选")) {
                    topActionDialog.setAllText("全不选");
                    reverseMulti(true);
                    topActionDialog.setTitle("已选择" + memoInfoList.size() + "项");
                } else {
                    topActionDialog.setAllText("全选");
                    reverseMulti(false);
                    topActionDialog.setTitle("已选择0项");
                }
            }
        });
        bottomActionDialog.setDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击删除
                List<MemoInfo> list = getSelected();
                if(list.size() > 0) {
                    for(MemoInfo info : list) {
                        int index = memoInfoList.indexOf(info);
                        if(index >= 0) {
                            rvSimpleAdapter.remove(index);
                            memoInfoList.remove(info);
                        }
                        MemoDao.removeMemo(mContext, info);
                    }
                    setMode(false);
                }
            }
        });
        bottomActionDialog.setMoveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击移动到
            }
        });
        bottomActionDialog.setTopListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击置顶
                List<MemoInfo> list = getSelected();
                if(list.size() <= 0) {
                    return;
                }
                List<Cell> cells = new ArrayList<>();
                RVBaseCell cell;
                for(MemoInfo info : list) {
                    cell = new MemoMultiCell(mContext, info);
                    cells.add(cell);
                }
                rvSimpleAdapter.addAll(0, cells);
            }
        });
    }

    /**
     * 从数据库获取便签列表
     */
    private void getMemoList(boolean isVerticalLayout) {
        rvSimpleAdapter.clear();
        memoInfoList = MemoDao.getMemoList(mContext);
        RVBaseCell cell = null;
        if(null != memoInfoList && memoInfoList.size() > 0 && !isVerticalLayout) {
            mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            isVertical = false;
            for(MemoInfo memoInfo : memoInfoList) {
//                if(memoInfo.memoType == MemoType.MEMO_TYPE_TEXT) {
//                    cell = new MemoTextCell(mContext, memoInfo);
//                } else if(memoInfo.memoType == MemoType.MEMO_TYPE_TEXT_IMG) {
                    cell = new MemoMultiCell(mContext, memoInfo);
//                }
                rvSimpleAdapter.add(cell);
            }
        } else if(null != memoInfoList && memoInfoList.size() > 0 && isVerticalLayout) {
            mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            isVertical = true;
            for(MemoInfo memoInfo : memoInfoList) {
                cell = new MemoMultiVerticalCell(mContext, memoInfo);
                rvSimpleAdapter.add(cell);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(mContext, TagsActivity.class);
                startActivity(intent);
                break;

                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_sort: // 点击排序
                if(rvSimpleAdapter.getItemCount() == 0) {
                    return;
                }
                if(isVertical) {
                    isVertical = false;
                    tvSort.setBackgroundResource(R.drawable.selector_home_sort_row);
                } else {
                    isVertical = true;
                    tvSort.setBackgroundResource(R.drawable.selector_home_sort_grid);
                }
                SharedPreferencesInfo.saveTagBoolean(mContext, SharedPreferencesInfo.IS_VERTICAL_LAYOUT, isVertical);
                getMemoList(isVertical);
                break;

            case R.id.fab: // 点击添加
                Intent intent = new Intent(mContext, MemoDetailActivity.class);
                startActivity(intent);
                break;

                default:
                    break;
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent event) {
        int code = event.getCode();
        switch (code) {
            case EventBusUtils.EventCode.EVENT_SET_MULTI_CHOICE:
                setMode(true);
                rvSimpleAdapter.notifyDataSetChanged();
                topActionDialog.showMenu();
                bottomActionDialog.showMenu();
                topActionDialog.setTitle("已选择1项");
                topActionDialog.setAllText("全选");
                break;

            case EventBusUtils.EventCode.EVENT_CHECK_STATE_CHANGE:
                List<MemoInfo> list = getSelected();
                if(null != topActionDialog && null != list) {
                    topActionDialog.setTitle("已选择" + list.size() + "项");
                }
                break;

            case EventBusUtils.EventCode.EVENT_MEMO_ADD: // 更新列表
                getMemoList(isVertical);
                break;

            default:
                break;
        }
    }

    /**
     * 修改多选模式
     * @param isChecked
     */
    private void reverseMulti(boolean isChecked) {
        if(null != memoInfoList && memoInfoList.size() > 0) {
            for(MemoInfo info : memoInfoList) {
                info.isChecked = isChecked;
            }
            rvSimpleAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否处于多选状态
     * @param isMulti
     */
    private void setMode(boolean isMulti) {
        if(null != memoInfoList && memoInfoList.size() > 0) {
            for(MemoInfo info : memoInfoList) {
                info.isMultiMode = isMulti;
            }
            rvSimpleAdapter.notifyDataSetChanged();
        }
        if(isMulti) {
            topActionDialog.showMenu();
            bottomActionDialog.showMenu();
        } else {
            topActionDialog.dismiss();
            bottomActionDialog.dismiss();
        }
    }

    /**
     * 获取已选择
     * @return
     */
    private List<MemoInfo> getSelected() {
        List<MemoInfo> list = new ArrayList<>();
        if(memoInfoList.size() > 0) {
            for(MemoInfo info : memoInfoList) {
                if(info.isChecked) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if(topActionDialog.isShowing()) {
            topActionDialog.dismiss();
            bottomActionDialog.dismiss();
            setMode(false);
            rvSimpleAdapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN,null);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}

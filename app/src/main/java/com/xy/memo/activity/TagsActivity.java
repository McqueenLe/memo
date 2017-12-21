package com.xy.memo.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xy.memo.R;
import com.xy.memo.base.Cell;
import com.xy.memo.base.RVBaseCell;
import com.xy.memo.base.RVSimpleAdapter;
import com.xy.memo.cell.TagInfo;
import com.xy.memo.cell.TagsCell;
import com.xy.memo.db.MemoDao;
import com.xy.memo.model.BaseItemDecoration;
import com.xy.memo.model.MemoInfo;
import com.xy.memo.utils.SharedPreferencesInfo;
import com.xy.memo.view.TitleBar;

import java.util.List;

/**
 * 分类标签列表页
 * @author xy 2017/12/4.
 */

public class TagsActivity extends BasicActivity {
    private Context mContext;
    private TitleBar mTitleBar;
    private FloatingActionButton fabAddTag;
    private RecyclerView recyclerView;
    private RVSimpleAdapter rvSimpleAdapter;
    private List<TagInfo> tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        mContext = TagsActivity.this;

        mTitleBar = findViewById(R.id.titlebar_tags);
        mTitleBar.setTitle("便签夹");
        mTitleBar.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fabAddTag = findViewById(R.id.fabAddFolder);
        fabAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(TagsActivity.this);
            }
        });
        recyclerView = findViewById(R.id.recyclerview_tags);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        BaseItemDecoration baseItemDecoration = new BaseItemDecoration(mContext,30);
        baseItemDecoration.setDividerColor(R.color.bg_grey);
        recyclerView.addItemDecoration(baseItemDecoration);
        rvSimpleAdapter = new RVSimpleAdapter();
        recyclerView.setAdapter(rvSimpleAdapter);
        String tags = SharedPreferencesInfo.getTagString(TagsActivity.this, SharedPreferencesInfo.TAGS);
        if(!TextUtils.isEmpty(tags)) {
            Gson gson = new Gson();
            tagList = gson.fromJson(tags, new TypeToken<List<TagInfo>>(){}.getType());
            if(null != tagList && tagList.size() > 0) {
                for(TagInfo tagInfo : tagList) {
                    RVBaseCell cell = new TagsCell(tagInfo);
                    rvSimpleAdapter.add(cell);
                }
            }
        }
    }

    /**
     * 便签夹创建对话框
     * @param ctx
     */
    private void createDialog(final Context ctx) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_tags_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(view);
        final Dialog dialog = builder.create();
        final EditText etTagName = view.findViewById(R.id.etTagName);
        TextView tvOk = view.findViewById(R.id.tvOk);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取标签名
                String tagName = etTagName.getText().toString();
                if(TextUtils.isEmpty(tagName)) {
                    return;
                }
                TagInfo tagInfo = new TagInfo();
                tagInfo.tagName = tagName;
                tagInfo.memoCount = 0;

                // 添加标签,并保存
                String tags = SharedPreferencesInfo.getTagString(TagsActivity.this, SharedPreferencesInfo.TAGS);
                if(!TextUtils.isEmpty(tags)) {
                    Gson gson = new Gson();
                    List<TagInfo> tagList = gson.fromJson(tags, new TypeToken<List<TagInfo>>(){}.getType());
                    if(null != tagList) {
                        tagList.add(tagInfo);
                    }
                    String savedList = gson.toJson(tagList);
                    SharedPreferencesInfo.saveTagString(ctx, SharedPreferencesInfo.TAGS, savedList);
                }
                dialog.dismiss();
                TagsCell cell = new TagsCell(tagInfo);
                rvSimpleAdapter.add(cell);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}

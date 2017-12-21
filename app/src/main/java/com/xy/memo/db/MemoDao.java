package com.xy.memo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xy.memo.model.MemoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 便签的数据库操作类
 * @author xy 2017/11/30.
 */

public class MemoDao {

    /**
     * 向数据库插入一条便签记录
     * @param ctx
     * @param memoInfo
     * @param isNew 是否为新插入数据
     */
    public static void insertMemo(Context ctx, MemoInfo memoInfo, boolean isNew) {
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.MEMO_TITLE, memoInfo.memoTitle);
        values.put(DBHelper.MEMO_TYPE, memoInfo.memoType);
        values.put(DBHelper.MEMO_CONTENT, memoInfo.memoContent);
        values.put(DBHelper.MEMO_INSERT_TIME, Long.toString(System.currentTimeMillis()));
        values.put(DBHelper.TAG, memoInfo.tag);
        if(isNew) {
            sdb.insert(DBHelper.TABLE_MEMO, null , values);
        } else {
            sdb.update(DBHelper.TABLE_MEMO, values, "insertTime=?", new String[]{memoInfo.insertTime.toString()});
        }
        sdb.close();
        dbHelper.close();
    }

    /**
     * 获取便签列表
     * @param ctx
     * @return
     */
    public static List<MemoInfo> getMemoList(Context ctx) {
        List<MemoInfo> list = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        Cursor c = sdb.query(DBHelper.TABLE_MEMO, null, null, null, null, null, null);
        MemoInfo memoInfo;
        while (c.moveToNext()) {
            memoInfo = new MemoInfo();
            memoInfo.id = c.getInt(c.getColumnIndex(DBHelper.MEMO_ID));
            memoInfo.memoTitle = c.getString(c.getColumnIndex(DBHelper.MEMO_TITLE));
            memoInfo.memoType = c.getInt(c.getColumnIndex(DBHelper.MEMO_TYPE));
            memoInfo.memoContent = c.getString(c.getColumnIndex(DBHelper.MEMO_CONTENT));
            memoInfo.insertTime = c.getLong(c.getColumnIndex(DBHelper.MEMO_INSERT_TIME));
            memoInfo.tag = c.getString(c.getColumnIndex(DBHelper.TAG));
            list.add(memoInfo);
        }
        c.close();
        sdb.close();
        dbHelper.close();
        return list;
    }

    /**
     * 删除一条memo记录
     * @param ctx
     * @param memoInfo
     */
    public static void removeMemo(Context ctx, MemoInfo memoInfo) {
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase sdb = dbHelper.getWritableDatabase();
        sdb.delete(DBHelper.TABLE_MEMO, "id=?", new String[]{ memoInfo.id.toString() });
        sdb.close();
        dbHelper.close();
    }
}

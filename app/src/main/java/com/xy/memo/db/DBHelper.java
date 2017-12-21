package com.xy.memo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库工具类
 * @author xy 2017/11/30.
 */

public class DBHelper extends SQLiteOpenHelper {
    // 数据库名
    public static final String DATABASE_NAME = "FuelGas.db";

    // 数据库版本
    private final static int DATABASE_VERSION = 1;

    // 存储便签信息的表名
    public static final String TABLE_MEMO = "table_memo";

    // 便签信息表字段
    public static final String MEMO_ID = "id";
    public static final String MEMO_TITLE = "memoTitle"; // 标题
    public static final String MEMO_TYPE = "memoType"; // 便签模板类型
    public static final String MEMO_CONTENT = "memoContent"; // 内容
    public static final String MEMO_INSERT_TIME = "insertTime"; // 插入时间
    public static final String TAG = "tag"; // 标签名字

    // 创建便签信息表的语句
    public static final String CREATE_TABLE_MEMO = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMO + "("
            + MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + MEMO_TITLE + " TEXT," + MEMO_TYPE + " INTEGER," + MEMO_CONTENT + " TEXT,"
            + MEMO_INSERT_TIME + " INTEGER," + TAG + " TEXT)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        deleteTables(sqLiteDatabase);
    }

    /**
     * 创建表
     * @param db
     */
    private void createTables(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMO);
    }

    /**
     * 先删除表，再重新创建
     * @param db
     */
    private void deleteTables(SQLiteDatabase db) {
        db.execSQL("drop table if exists table_memo");
        createTables(db);
        db.close();
    }
}

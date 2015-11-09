/**
 * Copyright (c) 2015-present, MaxLeapMobile.
 * All rights reserved.
 * ----
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.maxleapmobile.gitmaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MaxLeapGitDB";
    private static final int VERSION = 1;

    private static DBHelper mInstance;
    private int countOpenTimes;
    private SQLiteDatabase mDatabase;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    private DBHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public class RecommendRepoTable {

        public static final String TABLE_NAME = "recommend_repo";

        public static final int DEFAULT_ACTION_VALUE = 0;

        public class Columns {
            public static final String ID = "id";
            public static final String REPO_ID = "filename";
            public static final String IS_STAR = "star";
            public static final String IS_FORK = "fork";
            public static final String IS_SKIP = "skip";
            public static final String CREATE_TIME = "create_time";
        }
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecommendRepoTable.TABLE_NAME + "("
                + RecommendRepoTable.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RecommendRepoTable.Columns.REPO_ID + " BIGINT,"
                + RecommendRepoTable.Columns.IS_STAR + " INTEGER DEFAULT " + RecommendRepoTable.DEFAULT_ACTION_VALUE + ","
                + RecommendRepoTable.Columns.IS_FORK + " INTEGER DEFAULT " + RecommendRepoTable.DEFAULT_ACTION_VALUE + ","
                + RecommendRepoTable.Columns.IS_SKIP + " INTEGER DEFAULT " + RecommendRepoTable.DEFAULT_ACTION_VALUE + ","
                + RecommendRepoTable.Columns.CREATE_TIME + " VARCHAR(16))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBRecRepo getRepoById(long repoId) {
        DBRecRepo dbRecRepo = null;
        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RecommendRepoTable.TABLE_NAME
                        + " WHERE " + RecommendRepoTable.Columns.REPO_ID + " = ?",
                new String[]{String.valueOf(repoId)});
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            dbRecRepo = new DBRecRepo();
            dbRecRepo.setId(cursor.getInt(cursor
                    .getColumnIndex(RecommendRepoTable.Columns.ID)));
            dbRecRepo.setRepo_id(cursor.getLong(cursor
                    .getColumnIndex(RecommendRepoTable.Columns.ID)));
            dbRecRepo.setIsStar(cursor.getInt(cursor
                    .getColumnIndex(RecommendRepoTable.Columns.IS_STAR))!= 0);
            dbRecRepo.setIsFork(cursor.getInt(cursor
                    .getColumnIndex(RecommendRepoTable.Columns.IS_FORK)) != 0);
            dbRecRepo.setIsSkip(cursor.getInt(cursor
                    .getColumnIndex(RecommendRepoTable.Columns.IS_SKIP)) != 0);
            dbRecRepo.setCreateTime(cursor.getLong(cursor
                    .getColumnIndex(RecommendRepoTable.Columns.CREATE_TIME)));
        }
        cursor.close();
        closeDatabase();
        return dbRecRepo;
    }

    public int insertRepo(DBRecRepo dbRecRepo) {
        SQLiteDatabase db = openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RecommendRepoTable.Columns.REPO_ID, dbRecRepo.getRepo_id());
        cv.put(RecommendRepoTable.Columns.IS_STAR, dbRecRepo.isStar());
        cv.put(RecommendRepoTable.Columns.IS_FORK, dbRecRepo.isFork());
        cv.put(RecommendRepoTable.Columns.IS_SKIP, dbRecRepo.isSkip());
        cv.put(RecommendRepoTable.Columns.CREATE_TIME, System.currentTimeMillis());
        int id = (int) db.insert(RecommendRepoTable.TABLE_NAME, null, cv);
        closeDatabase();
        return id;
    }

    public int updateRepo(DBRecRepo dbRecRepo) {
        SQLiteDatabase db = openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(RecommendRepoTable.Columns.IS_STAR, dbRecRepo.isStar());
        cv.put(RecommendRepoTable.Columns.IS_FORK, dbRecRepo.isFork());
        cv.put(RecommendRepoTable.Columns.IS_SKIP, dbRecRepo.isSkip());
        String[] args = {String.valueOf(dbRecRepo.getRepo_id())};
        int id = db.update(RecommendRepoTable.TABLE_NAME, cv,
                RecommendRepoTable.Columns.REPO_ID + "=?", args);
        closeDatabase();
        return id;
    }

    public int delRepo(DBRecRepo dbRecRepo) {
        SQLiteDatabase db = openDatabase();
        String[] args = {String.valueOf(dbRecRepo.getRepo_id())};
        int id = db.delete(RecommendRepoTable.TABLE_NAME,
                RecommendRepoTable.Columns.REPO_ID + "=?", args);
        closeDatabase();
        return id;
    }

    public void delExpiredRepo() {
        SQLiteDatabase db = openDatabase();
        long duration = 14l * 3600l * 24l * 1000l;
        String[] args = {String.valueOf(System.currentTimeMillis() - duration)};
        db.delete(RecommendRepoTable.TABLE_NAME,
                RecommendRepoTable.Columns.CREATE_TIME + "<?", args);
        closeDatabase();
    }

    private synchronized SQLiteDatabase openDatabase() {
        countOpenTimes = countOpenTimes + 1;
        if (countOpenTimes == 1) {
            mDatabase = mInstance.getWritableDatabase();
        }
        return mDatabase;
    }

    private synchronized void closeDatabase() {
        countOpenTimes = countOpenTimes - 1;
        if (countOpenTimes == 0) {
            mDatabase.close();
        }
    }
}

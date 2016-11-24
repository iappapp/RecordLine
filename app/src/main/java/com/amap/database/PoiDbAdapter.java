package com.amap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tree on 16/11/24.
 */
public class PoiDbAdapter {
    private final static String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordPath";
    static final String DATABASE_NAME=DATABASE_PATH+"/"+"record.db";
    private static final int DATABASE_VERSION=1;
    private static final String RECORD_TABLE="poi_record";
    private static final String RECORD_CREATE=
            "create table if not exists poi_record("
                    +"id integer primary key,"
                    +"point STRING,"
                    +"description STRING,"
                    +"date STRING"
                    +");";


    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RECORD_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private Context mCtx = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public PoiDbAdapter(Context mCtx){
        this.mCtx = mCtx;
        dbHelper = new DatabaseHelper(mCtx);
    }

    public PoiDbAdapter open() throws Exception{

        db = dbHelper.getWritableDatabase();
        return this;
    }

    public Cursor getall(){
        return db.rawQuery("select * from " + RECORD_TABLE,null);
    }

    public void close(){
        db.close();
    }

    public static final String KEY_ROWID = "id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";
    public static final String KEY_POINT = "point";

    public boolean createRecord(String description,String time,String point){
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION,description);
        values.put(KEY_DATE,time);
        values.put(KEY_POINT,point);
        return db.insert(RECORD_TABLE,null,values) > 0;
    }

    public Cursor getAllRecord(){
        return db.query(RECORD_TABLE,new String[]{KEY_ROWID,KEY_POINT,KEY_DESCRIPTION,KEY_DATE},null,null,null,null,null);
    }

}

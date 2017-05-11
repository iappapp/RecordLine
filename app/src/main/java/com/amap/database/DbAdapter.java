package com.amap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amap.util.Config;

public class DbAdapter {

    private final static String DATABASE_PATH = Config.DATA_PATH;
    static final String DATABASE_NAME = DATABASE_PATH + "/" + "record.db";
    private static final int DATABASE_VERSION = 3;
    private static final String RECORD_TABLE = "record";
    private static final String RECORD_CREATE =
            "create table if not exists record("
                    + "id integer primary key,"
                    + "stratpoint STRING,"
                    + "endpoint STRING,"
                    + "pathline STRING,"
                    + "distance STRING,"
                    + "duration STRING,"
                    + "averagespeed STRING,"
                    + "date STRING"
                    + ");";

    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RECORD_CREATE);
            db.execSQL(POI_CREATE);
            db.execSQL(MANUAL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private Context mCtx = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    //constructor
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
        dbHelper = new DatabaseHelper(mCtx);
    }

    public DbAdapter open() throws SQLException {

        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getall() {
        return db.rawQuery("SELECT * FROM record", null);
    }

    //remove an entry
    public boolean delete(long rowId) {
        return db.delete(RECORD_TABLE, "id=" + rowId, null) > 0;
    }

    public static final String KEY_ROWID = "id";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_SPEED = "averagespeed";
    public static final String KEY_LINE = "pathline";
    public static final String KEY_STRAT = "stratpoint";
    public static final String KEY_END = "endpoint";
    public static final String KEY_DATE = "date";

    public long createrecord(String distance, String duration, String averagespeed, String pathline, String stratpoint, String endpoint, String date) {
        ContentValues args = new ContentValues();
        args.put("distance", distance);
        args.put("duration", duration);
        args.put("averagespeed", averagespeed);
        args.put("pathline", pathline);
        args.put("stratpoint", stratpoint);
        args.put("endpoint", endpoint);
        args.put("date", date);
        return db.insert(RECORD_TABLE, null, args);
    }

    public Cursor getallrecord() {
        return db.query(RECORD_TABLE, new String[]{KEY_ROWID,KEY_DISTANCE, KEY_DURATION, KEY_SPEED, KEY_LINE, KEY_STRAT, KEY_END, KEY_DATE}, null, null, null, null, null);
    }

    public int getAllRecords(){
        if(getallrecord() != null)
            return getallrecord().getCount();
        else
            return 0;
    }

    private static final String POI_TABLE = "poirecord";
    private static final String POI_CREATE =
            "create table if not exists poirecord("
                    + "id integer primary key,"
                    + "name STRING,"
                    + "point STRING,"
                    + "description STRING,"
                    + "date STRING,"
                    + "address STRING"
                    + ");";

    public Cursor getAllPoi() {
        return db.rawQuery("SELECT * FROM " + POI_TABLE, null);
    }


    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_POINT = "point";
    public static final String KEY_ADDRESS = "address";


    public boolean createRecord(String name,String description, String address, String point,String time) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,name);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DATE, time);
        values.put(KEY_POINT, point);
        values.put(KEY_ADDRESS,address);
        return db.insert(POI_TABLE, null, values) > 0;
    }

    public Cursor getAllPoiRecord() {
        return db.query(POI_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_DESCRIPTION, KEY_POINT, KEY_ADDRESS, KEY_DATE}, null, null, null, null, null);
    }

    public int getAllPoiRecordsCount(){
        if(getAllPoiRecord() != null)
            return getAllPoiRecord().getCount();
        else
            return 0;
    }

    public boolean deletePoiRecord(int id){
        return db.delete(POI_TABLE,"id=" + id,null) > 0;
    }

    private static final String MAN_TABLE = "record_man";
    private static final String MANUAL_CREATE =
            "create table if not exists record_man("
                    + "id integer primary key,"
                    + "stratpoint STRING,"
                    + "endpoint STRING,"
                    + "pathline STRING,"
                    + "distance STRING,"
                    + "description STRING,"
                    + "date STRING"
                    + ");";

    public Cursor getAllManual() {
        return db.rawQuery("SELECT * FROM " + MAN_TABLE, null);
    }

    //remove an entry
    public boolean deleteManualRecord(long rowId) {

        return db.delete(MAN_TABLE, "id=" + rowId, null) > 0;
    }


    public long createManualRecord(String distance, String description, String pathline, String stratpoint, String endpoint, String date) {
        ContentValues args = new ContentValues();
        args.put("distance", distance);
        args.put("description", description);
        args.put("pathline", pathline);
        args.put("stratpoint", stratpoint);
        args.put("endpoint", endpoint);
        args.put("date", date);
        return db.insert(MAN_TABLE, null, args);
    }

    public Cursor getAllManualRecord() {
        return db.query(MAN_TABLE, new String[]{KEY_DISTANCE, KEY_DESCRIPTION, KEY_LINE, KEY_STRAT, KEY_END, KEY_DATE}, null, null, null, null, null);
    }

    public int getAllManualRecords(){
        if(getAllManualRecord() != null)
            return getAllManualRecord().getCount();
        else
            return 0;
    }
}



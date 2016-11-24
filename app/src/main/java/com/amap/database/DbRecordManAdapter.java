package com.amap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbRecordManAdapter {
    private final static String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordPath";
    static final String DATABASE_NAME = DATABASE_PATH + "/" + "record.db";
    private static final int DATABASE_VERSION = 1;
    private static final String RECORD_TABLE = "record_man";
    private static final String RECORD_CREATE =
            "create table if not exists record_man("
                    + "id integer primary key,"
                    + "stratpoint STRING,"
                    + "endpoint STRING,"
                    + "pathline STRING,"
                    + "distance STRING,"
                    + "description STRING,"
                    + "date STRING"
                    + ");";

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

    //constructor
    public DbRecordManAdapter(Context ctx) {
        this.mCtx = ctx;
        dbHelper = new DatabaseHelper(mCtx);
    }

    public DbRecordManAdapter open() throws SQLException {

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
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LINE = "pathline";
    public static final String KEY_STRAT = "stratpoint";
    public static final String KEY_END = "endpoint";
    public static final String KEY_DATE = "date";

    public long createrecord(String distance, String description, String pathline, String stratpoint, String endpoint, String date) {
        ContentValues args = new ContentValues();
        args.put("distance", distance);
        args.put("description", description);
        args.put("pathline", pathline);
        args.put("stratpoint", stratpoint);
        args.put("endpoint", endpoint);
        args.put("date", date);
        return db.insert(RECORD_TABLE, null, args);
    }

    public Cursor getallrecord() {
        return db.query(RECORD_TABLE, new String[]{KEY_DISTANCE, KEY_DESCRIPTION, KEY_LINE, KEY_STRAT, KEY_END, KEY_DATE}, null, null, null, null, null);
    }

}



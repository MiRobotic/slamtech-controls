package com.slamtec.simplecontrol.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


public class AppDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "location";
    private static final int DB_VERSION = 1;

    public AppDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePoint = "CREATE TABLE " + Constants.TABLE_POINT + " ( "
                +Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.NAME + " TEXT, "
                + Constants.X + " REAL, "
                + Constants.Y + " REAL, "
                + Constants.Z + " REAL, "
                + Constants.ROTATION + " REAL)";

        Log.e("DB", "table point > " + tablePoint);

        db.execSQL(tablePoint);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

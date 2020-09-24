package com.slamtec.simplecontrol.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DataProcessor {

    private final String tag = DataProcessor.class.getSimpleName();
    private AppDatabase database;

    public DataProcessor(Context context) {
        database = new AppDatabase(context);
    }

    public void savePoint(MapPoint point) {

        Log.e(tag, "save location "+point.getName());

        ContentValues values = new ContentValues();
        values.put(Constants.NAME, point.getName());
        values.put(Constants.X, point.getX());
        values.put(Constants.Y, point.getY());
        values.put(Constants.Z, point.getZ());
        values.put(Constants.ROTATION, point.getRotation());

        SQLiteDatabase db = database.getWritableDatabase();
        db.insert(Constants.TABLE_POINT, null, values);

    }

    public ArrayList<MapPoint> getSavedPoints() {
        ArrayList<MapPoint> points = new ArrayList<>();

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_POINT, null, null, null, null, null, Constants.NAME+" ASC");

        while (cursor.moveToNext()) {
            points.add(
                    new MapPoint(
                            cursor.getInt(cursor.getColumnIndex(Constants.ID)),
                            cursor.getString(cursor.getColumnIndex(Constants.NAME)),
                            cursor.getFloat(cursor.getColumnIndex(Constants.X)),
                            cursor.getFloat(cursor.getColumnIndex(Constants.Y)),
                            cursor.getFloat(cursor.getColumnIndex(Constants.Z)),
                            cursor.getFloat(cursor.getColumnIndex(Constants.ROTATION))
                    )
            );
        }

        cursor.close();
        db.close();
        Log.e("data", "total: "+points.size());
        return points;

    }

    public void deletePoint(int pointId) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.TABLE_POINT + " WHERE " + Constants.ID + " = '" + pointId + "'");
        db.close();
    }

}

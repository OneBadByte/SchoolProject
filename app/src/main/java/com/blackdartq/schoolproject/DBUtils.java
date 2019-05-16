package com.blackdartq.schoolproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class DBUtils {
    static String testDB(){
       String query = "select sqlite_version() AS sqlite_version";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToNext()){
            return cursor.getString(0);
        }
        throw new RuntimeException("db is fucked up");
    }
}

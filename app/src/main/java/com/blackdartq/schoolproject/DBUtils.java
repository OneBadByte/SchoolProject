package com.blackdartq.schoolproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blackdartq.schoolproject.Utils.Term;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DBUtils{

    SQLiteDatabase db;
    ArrayList<Term> terms;
    private final String DATABASE_PATH = "/data/data/com.blackdartq.schoolproject/files/test.db";

    DBUtils(){
       db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
//       dropTables();
       db.execSQL(
               "CREATE TABLE IF NOT EXISTS term(" +
                       "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                       "name VARCHAR(50)" +
                       ", start_date TEXT" +
                       ", end_date TEXT" +
                       ");");
       terms = getTerms();
    }

    public String getPath(){
        return db.getPath();
    }

    ArrayList<Term> getTerms(){
        ArrayList<Term> terms = new ArrayList<>();
        String[] columns = {"id", "name"};
        Cursor cursor = db.query("term", columns, null, null, null, null, null);
        while (cursor.moveToNext()){
            Term term = new Term();
            term.setId(cursor.getInt(0));
            term.setName(cursor.getString(1));
            terms.add(term);
        }
        return terms;
    }

    int getIdFromIndex(int index){
        return terms.get(index).getId();
    }

    int getIndexFromName(String name){
        int count = 0;
        for(Term term : terms){
            if(term.getName().equals(name)){
                return count;
            }
            count++;
        }
        throw new RuntimeException("couldn't find index by name");
    }

    int getIdFromTermName(String name){
        String[] query = {name};
        Cursor cursor = db.rawQuery("SELECT id FROM term WHERE name = ?", query);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    void addTerm(String name, String startDate, String endDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("start_date", startDate);
        contentValues.put("end_date", endDate);
        db.insert("term", null, contentValues);
        terms.clear();
        terms = getTerms();
    }

    void deleteTermByIndex(int index){
        int id = terms.get(index).getId();
        String[] args = {String.valueOf(id)};
        db.delete("term", "id = ?", args);
        terms.clear();
        terms = getTerms();
    }

    boolean dropTables(){
        try{
            db.execSQL("DROP TABLE term;");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    void dropConnection(){
        db.close();
    }


}

package com.blackdartq.schoolproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blackdartq.schoolproject.Utils.Course;
import com.blackdartq.schoolproject.Utils.Term;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DBUtils{

    SQLiteDatabase db;
    ArrayList<Term> terms;
    ArrayList<Course> courses;
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
       db.execSQL(
               "CREATE TABLE IF NOT EXISTS course(" +
                       "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                       "name VARCHAR(50), " +
                       "start_date TEXT, " +
                       "end_date TEXT, " +
                       "status VARCHAR(25), " +
                       "mentor_name VARCHAR(50), " +
                       "phone_number VARCHAR(11), " +
                       "email VARCHAR(50)" +
                       ");"
       );
       terms = getTerms();
       courses = getCourses();
    }

    public String getPath(){
        return db.getPath();
    }

    ArrayList<Term> getTerms(){
        ArrayList<Term> terms = new ArrayList<>();
        String[] columns = {"id", "name", "start_date", "end_date"};
        Cursor cursor = db.query("term", columns, null, null, null, null, null);
        while (cursor.moveToNext()){
            Term term = new Term();
            term.setId(cursor.getInt(0));
            term.setName(cursor.getString(1));
            term.setStartDate(cursor.getString(2));
            term.setEndDate(cursor.getString(3));
            terms.add(term);
        }
        return terms;
    }

    void updateTermsArray(){
        terms.clear();
        terms = getTerms();
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

    Term getTermFromIndex(int index){
        return terms.get(index);
    }

    void addTerm(String name, String startDate, String endDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("start_date", startDate);
        contentValues.put("end_date", endDate);
        db.insert("term", null, contentValues);
        updateTermsArray();
    }

    void updateTerm(Term term){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", term.getName());
        contentValues.put("start_date", term.getStartDate());
        contentValues.put("end_date", term.getEndDate());
        String[] args = {String.valueOf(term.getId())};
        db.update("term", contentValues, "id = ?", args);
        updateTermsArray();
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
            db.execSQL("DROP TABLE course;");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    void dropConnection(){
        db.close();
    }

    //_____________COURSE DATABASE METHODS________________________

    ArrayList<Course> getCourses(){
        ArrayList<Course> courses = new ArrayList<>();
        String[] columns = {
                "id", "name", "start_date", "end_date", "status", "mentor_name", "phone_number", "email"
        };
        Cursor cursor = db.query("course", columns, null, null, null, null, null);
        while (cursor.moveToNext()){
            Course course = new Course();
            course.setId(cursor.getInt(0));
            course.setName(cursor.getString(1));
            course.setStartDate(cursor.getString(2));
            course.setEndDate(cursor.getString(3));
            course.setStatus(cursor.getString(4));
            course.setMentorNames(cursor.getString(5));
            course.setPhoneNumber(cursor.getString(6));
            course.setEmail(cursor.getString(7));
            courses.add(course);
        }
        return courses;
    }

    void updateCoursesArray(){
        courses.clear();
        courses = getCourses();
    }

    int getCourseIdFromIndex(int index){
        return courses.get(index).getId();
    }

    int getCourseIndexFromName(String name){
        int count = 0;
        for(Course course : courses){
            if(course.getName().equals(name)){
                return count;
            }
            count++;
        }
        throw new RuntimeException("couldn't find index by name");
    }

    int getIdFromCourseName(String name){
        String[] query = {name};
        Cursor cursor = db.rawQuery("SELECT id FROM course WHERE name = ?", query);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    Course getCourseFromIndex(int index){
        return courses.get(index);
    }

    void addCourse(String name, String startDate, String endDate, String status, String mentorName, String phoneNumber, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("start_date", startDate);
        contentValues.put("end_date", endDate);
        contentValues.put("status", status);
        contentValues.put("mentor_name", mentorName);
        contentValues.put("phone_number", phoneNumber);
        contentValues.put("email", email);
        db.insert("course", null, contentValues);
        updateCoursesArray();
    }

    void updateCourse(Course course){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", course.getName());
        contentValues.put("start_date", course.getStartDate());
        contentValues.put("end_date", course.getEndDate());
        contentValues.put("status", course.getStatus());
        contentValues.put("mentor_name", course.getMentorNames());
        contentValues.put("phone_number", course.getPhoneNumber());
        contentValues.put("email", course.getEmail());
        String[] args = {String.valueOf(course.getId())};
        db.update("course", contentValues, "id = ?", args);
        updateCoursesArray();
    }

    void deleteCourseByIndex(int index){
        int id = courses.get(index).getId();
        String[] args = {String.valueOf(id)};
        db.delete("course", "id = ?", args);
        courses.clear();
        courses = getCourses();
    }
}

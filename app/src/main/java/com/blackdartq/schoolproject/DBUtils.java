package com.blackdartq.schoolproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blackdartq.schoolproject.Utils.Assignment;
import com.blackdartq.schoolproject.Utils.Course;
import com.blackdartq.schoolproject.Utils.Term;

import java.util.ArrayList;

public class DBUtils {

    SQLiteDatabase db;
    ArrayList<Term> terms;
    ArrayList<Course> courses;
    ArrayList<Assignment> assignments;
    private final String DATABASE_PATH = "/data/data/com.blackdartq.schoolproject/files/test.db";

    DBUtils() {
        // creates the connection to the database
        db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        terms = new ArrayList<>();
        courses = new ArrayList<>();
        assignments = new ArrayList<>();
//       dropTables();

        // creates the term table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS term(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(50)" +
                        ", start_date TEXT" +
                        ", end_date TEXT" +
                        ");");

        // creates the course table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS course(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(50), " +
                        "start_date TEXT, " +
                        "end_date TEXT, " +
                        "status VARCHAR(25), " +
                        "mentor_name VARCHAR(50), " +
                        "phone_number VARCHAR(11), " +
                        "email VARCHAR(50)," +
                        "term_id INTEGER," +
                        "FOREIGN KEY(term_id) REFERENCES term(id)" +
                        ");"
        );

        // creates the assignment table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS assignment(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name VARCHAR, " +
                        "optional_note VARCHAR," +
                        "due_date VARCHAR," +
                        "course_id INTEGER," +
                        "FOREIGN KEY(course_id) REFERENCES course(id)" +
                        ");"
        );

        // gets all the term/course data from the database and places them in an array
        terms = getTerms();
        courses = getCourses();
        assignments = getAssignments();
        addTestTermsAndCourses();
    }

    /**
     * gets the path to the database
     * @return
     */
    public String getPath() { return db.getPath(); }


    /**
     * gets an arrayList of all the terms in the database
     * @return
     */
    ArrayList<Term> getTerms() {
        ArrayList<Term> terms = new ArrayList<>();
        String[] columns = {"id", "name", "start_date", "end_date"};
        Cursor cursor = db.query("term", columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Term term = new Term();
            term.setId(cursor.getInt(0));
            term.setName(cursor.getString(1));
            term.setStartDate(cursor.getString(2));
            term.setEndDate(cursor.getString(3));
            terms.add(term);
        }
        return terms;
    }

    Integer addTerm(){
        addTerm("", "", "");
        int id = getTermIdFromTermName("");
        System.out.println("Returning id from db: " + id);
        return id;
    }

    /**
     * clears the term array and them reenters the data from the database
     */
    void updateTermsArray() {
        terms.clear();
        terms = getTerms();
    }

    /**
     * gets the term id from the arrayList of term data
     * @param index
     * @return
     */
    int getTermIdFromIndex(int index) {
        return terms.get(index).getId();
    }

    Term getTermById(int id){
        for(Term term: terms){
            if(term.getId() == id){
                return term;
            }
        }
        throw new RuntimeException("Couldn't find term with that term id");
    }

    boolean termHasCourses(int termId){
        return getCourseIdsFromAssociatedTermId(termId).size() > 0;
    }

    ArrayList<Integer> getCourseIdsFromAssociatedTermId(int termId){
        ArrayList<Integer> courseIds = new ArrayList<>();
        for(Course course: courses){
            // this try is to keep the program running even if the course term id is null
            try{
                if(course.getTermId() == termId){
                    courseIds.add(course.getId());
                }
            } catch (Exception e){
            }
        }
        return courseIds;
    }

    /**
     * gets the term index from the term name
     * @param name
     * @return
     */
    int getTermIndexFromName(String name) {
        int count = 0;
        for (Term term : terms) {
            if (term.getName().equals(name)) {
                return count;
            }
            count++;
        }
        throw new RuntimeException("couldn't find index by name");
    }

    /**
     * gets the term id from the database by the term name
     * @param name
     * @return
     */
    Integer getTermIdFromTermName(String name) {
        String[] query = {name};
        Cursor cursor = db.rawQuery("SELECT id FROM term WHERE name = ?", query);
        cursor.moveToFirst();
        try{
            return cursor.getInt(0);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * gets the term data from the arrayList using the term index
     * @param index
     * @return
     */
    Term getTermFromIndex(int index) {
        return terms.get(index);
    }

    /**
     * adds term to the database
     * @param name
     * @param startDate
     * @param endDate
     */
    void addTerm(String name, String startDate, String endDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("start_date", startDate);
        contentValues.put("end_date", endDate);
        db.insert("term", null, contentValues);
        updateTermsArray();
    }

    /**
     * adds three terms and three courses
     * TODO add assignments
     */
    void addTestTermsAndCourses() {
        // Creates Terms
        addTerm("Term 1", "5/19/2019", "6/23/2019");
        addTerm("Term 2", "5/19/2019", "6/23/2019");
        addTerm("Term 3", "5/19/2019", "6/23/2019");
        int term1Id = getTermIdFromIndex(0);
        int term2Id = getTermIdFromIndex(1);
//        int term3Id = getTermIdFromIndex(2);

        // Creates Courses
        addCourse("course 1", "5/29/2019", "5/29/19",
                "Working On", "Dr. Phil", "80183834433",
                "brody.prestwich18@gmail.com", term1Id);
        addCourse("course 2", "5/29/2019", "5/29/19",
                "Working On", "Dr. Phil", "80183834433",
                "brody.prestwich18@gmail.com", term1Id);
        addCourse("course 3", "5/29/2019", "5/29/19",
                "Working On", "Dr. Phil", "80183834433",
                "brody.prestwich18@gmail.com", term2Id);
        int course1Id = getCourseIdFromIndex(0);
        int course2Id = getCourseIdFromIndex(1);

        // Creates Assignments
        addAssignment("Assignment 1", "this shit is whack", "5/29/2019", course1Id);
        addAssignment("Assignment 2", "this shit is whack", "5/29/2019", course2Id);
        addAssignment("Assignment 3", "this shit is whack", "5/29/2019", course2Id);
        addAssignment("Assignment 4", "this shit is whack", "5/29/2019", course2Id);
        addAssignment("Assignment 5", "this shit is whack", "5/29/2019", course1Id);
        addAssignment("Assignment 6", "this shit is whack", "5/29/2019", null);
    }

    int getTermIdFromCourseIndex(int index){
        Course course = getCourseFromIndex(index);
        return course.getTermId();
    }

    void addCourseIdToTermByTermId(int termId, int courseId) {
        ContentValues contentValues = new ContentValues();
//        int courseId = getCourseIdFromTermId(termId);
        String[] args = {String.valueOf(termId)};
        contentValues.put("course_id", courseId);
        db.update("term", contentValues, "id = ?", args);
        updateCoursesArray();
    }


    void updateTerm(Term term) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", term.getName());
        contentValues.put("start_date", term.getStartDate());
        contentValues.put("end_date", term.getEndDate());
//        contentValues.put("course_id", term.getCourseId());
        String[] args = {String.valueOf(term.getId())};
        db.update("term", contentValues, "id = ?", args);
        updateTermsArray();
    }

    void deleteTermByIndex(int index) {
        int id = terms.get(index).getId();
        String[] args = {String.valueOf(id)};
        db.delete("term", "id = ?", args);
        terms.clear();
        terms = getTerms();
    }

    void deleteTermById(int id){
        String[] args = {String.valueOf(id)};
        db.delete("term", "id = ?", args);
        terms.clear();
        terms = getTerms();
    }

    void deleteTermByName(String name){
        String[] args = {name};
        db.delete("term", "name = ?", args);
        terms.clear();
        terms = getTerms();
    }

    boolean dropTables() {
        try {
            db.execSQL("DROP TABLE term;");
            db.execSQL("DROP TABLE course;");
            db.execSQL("DROP TABLE assignment;");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    void dropConnection() {
        db.close();
    }

    //_____________COURSE DATABASE METHODS________________________

    ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String[] columns = {
                "id", "name", "start_date", "end_date", "status", "mentor_name", "phone_number", "email", "term_id"
        };
        Cursor cursor = db.query("course", columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Course course = new Course();
            course.setId(cursor.getInt(0));
            course.setName(cursor.getString(1));
            course.setStartDate(cursor.getString(2));
            course.setEndDate(cursor.getString(3));
            course.setStatus(cursor.getString(4));
            course.setMentorNames(cursor.getString(5));
            course.setPhoneNumber(cursor.getString(6));
            course.setEmail(cursor.getString(7));
            course.setTermId(cursor.getInt(8));
            if(course.getTermId() == 0){
                course.setTermId(null);
            }
            courses.add(course);
        }
        return courses;
    }

    void addTermIdToCourseById(int termId, int courseId){
        int index = 0;
        for(Course course: courses){
            if(course.getId() == courseId){
                break;
            }
            index++;
        }
        addTermIdToCourseByIndex(termId, index);
    }

    void addTermIdToCourseByIndex(int termId, int courseIndex) {
        ContentValues contentValues = new ContentValues();
        int courseId = courses.get(courseIndex).getId();
        String[] args = {String.valueOf(courseId)};
        contentValues.put("term_id", termId);
        db.update("course", contentValues, "id = ?", args);
        updateCoursesArray();
    }

    void removeTermIdByCourseId(int id) {
        ContentValues contentValues = new ContentValues();
        String[] args = {String.valueOf(id)};
        contentValues.put("term_id", (Integer) null);
        db.update("course", contentValues, "id = ?", args);
        updateCoursesArray();
    }

    void updateCoursesArray() {
        courses.clear();
        courses = getCourses();
    }

    int getCourseIdFromIndex(int index) {
        return courses.get(index).getId();
    }

    int getCourseIndexFromName(String name) {
        int count = 0;
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return count;
            }
            count++;
        }
        throw new RuntimeException("couldn't find index by name");
    }

    int getIdFromCourseName(String name) {
        String[] query = {name};
        Cursor cursor = db.rawQuery("SELECT id FROM course WHERE name = ?", query);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    Course getCourseFromIndex(int index) {
        return courses.get(index);
    }

    void addCourse(
            String name, String startDate, String endDate,
            String status, String mentorName, String phoneNumber,
            String email, Integer termId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("start_date", startDate);
        contentValues.put("end_date", endDate);
        contentValues.put("status", status);
        contentValues.put("mentor_name", mentorName);
        contentValues.put("phone_number", phoneNumber);
        contentValues.put("email", email);
        contentValues.put("term_id", termId);
        db.insert("course", null, contentValues);
        updateCoursesArray();
    }

    void updateCourse(Course course) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", course.getName());
        contentValues.put("start_date", course.getStartDate());
        contentValues.put("end_date", course.getEndDate());
        contentValues.put("status", course.getStatus());
        contentValues.put("mentor_name", course.getMentorNames());
        contentValues.put("phone_number", course.getPhoneNumber());
        contentValues.put("email", course.getEmail());
        contentValues.put("term_id", course.getTermId());
        String[] args = {String.valueOf(course.getId())};
        db.update("course", contentValues, "id = ?", args);
        updateCoursesArray();
    }

    void deleteCourseByIndex(int index) {
        int id = courses.get(index).getId();
        String[] args = {String.valueOf(id)};
        db.delete("course", "id = ?", args);
        courses.clear();
        courses = getCourses();
    }

    //_____________ASSIGNMENT DATABASE METHODS________________________

    ArrayList<Assignment> getAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<>();
        String[] columns = {
                "id", "name", "optional_note", "due_date", "course_id"
        };
        Cursor cursor = db.query("assignment", columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Assignment assignment = new Assignment();
            assignment.setId(cursor.getInt(0));
            assignment.setName(cursor.getString(1));
            assignment.setOptionalNote(cursor.getString(2));
            assignment.setDueDate(cursor.getString(3));
            assignment.setCourseId(cursor.getInt(4));
            if(assignment.getId() == 0){
                assignment.setId(null);
            }
            System.out.println("DB: " + assignment.getName());
            assignments.add(assignment);
        }
        return assignments;
    }

    boolean assignmentHasCourses(int assignmentId){
        return getCourseIdsFromAssociatedAssignmentId(assignmentId).size() > 0;
    }

    ArrayList<Integer> getCourseIdsFromAssociatedAssignmentId(int courseId){
        ArrayList<Integer> courseIds = new ArrayList<>();
        for(Assignment assignment: assignments){
            // this try is to keep the program running even if the course term id is null
            try{
                if(assignment.getCourseId() == courseId){
                    courseIds.add(assignment.getId());
                }
            } catch (Exception e){
            }
        }
        return courseIds;
    }

    void addCourseIdToAssignmentById(int courseId, int assignmentId){
        int index = 0;
        for(Assignment assignment: assignments){
            if(assignment.getId() == assignmentId){
                break;
            }
            index++;
        }
        addTermIdToAssignmentByIndex(courseId, index);
    }

    void addTermIdToAssignmentByIndex(int courseId, int assignmentIndex) {
        ContentValues contentValues = new ContentValues();
        int assignmentId = assignments.get(assignmentIndex).getId();
        String[] args = {String.valueOf(assignmentId)};
        contentValues.put("course_id", courseId);
        db.update("assignment", contentValues, "id = ?", args);
        updateAssignmentsArray();
    }

    void removeCourseByAssignmentId(int id) {
        ContentValues contentValues = new ContentValues();
        String[] args = {String.valueOf(id)};
        contentValues.put("course_id", (Integer) null);
        db.update("assignment", contentValues, "id = ?", args);
        updateAssignmentsArray();
    }

    void updateAssignmentsArray() {
        assignments.clear();
        assignments = getAssignments();
    }

    int getAssignmentIdFromIndex(int index) {
        return assignments.get(index).getId();
    }

    int getAssignmentIndexFromName(String name) {
        int count = 0;
        for (Assignment assignment : assignments) {
            if (assignment.getName().equals(name)) {
                return count;
            }
            count++;
        }
        throw new RuntimeException("couldn't find index by name");
    }

    int getIdFromAssignmentName(String name) {
        String[] query = {name};
        Cursor cursor = db.rawQuery("SELECT id FROM assignment WHERE name = ?", query);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    Assignment getAssignmentFromIndex(int index) {
        return assignments.get(index);
    }

    void addAssignment(
            String name, String optionalNote, String dueDate, Integer courseId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("optional_note", optionalNote);
        contentValues.put("due_date", dueDate);
        contentValues.put("course_id", courseId);
        db.insert("assignment", null, contentValues);
        updateAssignmentsArray();
    }

    void updateAssignment(Assignment assignment) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", assignment.getName());
        contentValues.put("optional_note", assignment.getOptionalNote());
        contentValues.put("due_date", assignment.getDueDate());
        contentValues.put("course_id", assignment.getCourseId());
        String[] args = {String.valueOf(assignment.getId())};
        db.update("assignment", contentValues, "id = ?", args);
        updateAssignmentsArray();
    }

    void deleteAssignmentByIndex(int index) {
        int id = assignments.get(index).getId();
        String[] args = {String.valueOf(id)};
        db.delete("assignment", "id = ?", args);
        assignments.clear();
        assignments = getAssignments();
    }
}

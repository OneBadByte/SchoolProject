package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;

import com.blackdartq.schoolproject.Utils.Assignment;
import com.blackdartq.schoolproject.Utils.Course;
import com.blackdartq.schoolproject.Utils.Utils;

import java.util.ArrayList;

public class AddModifyCourse extends AppCompatActivity {

    public String courseNameLoader = "";
    String startDateLoader = "";
    String endDateLoader = "";

    DBUtils dbUtils;

    Button courseSaveButton;
    Button courseCancelButton;
    Button addAssignmentButton;
    Button deleteAssignmentButton;
    Button newAssignmentButton;

    EditText courseNameEditText;
    EditText startDateEditText;
    EditText endDateEditText;
    EditText statusEditText;
    EditText mentorNameEditText;
    EditText phoneNumberEditText;
    EditText emailEditText;
    EditText optionalNoteEditText;

    TextView messageTextView;

    Spinner assignmentSpinner;

    // Linear Layouts
    LinearLayout assignmentLinearLayout;

    Course course;

   ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_course);
        dbUtils = new DBUtils(false);
        final Intent intent = getIntent();

        courseSaveButton = findViewById(R.id.courseSaveButton);
        courseCancelButton = findViewById(R.id.courseCancelButton);
        addAssignmentButton = findViewById(R.id.addAssignmentButton);
        deleteAssignmentButton = findViewById(R.id.deleteAssignmentButton);
        newAssignmentButton = findViewById(R.id.newAssignmentButton);

        courseNameEditText = findViewById(R.id.courseNameEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.dueDateEditText);
        statusEditText = findViewById(R.id.statusEditText);
        mentorNameEditText = findViewById(R.id.mentorNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        emailEditText = findViewById(R.id.emailEditText);
        messageTextView = findViewById(R.id.messageTextView);
        optionalNoteEditText = findViewById(R.id.optionalNotEditText);

        assignmentLinearLayout = findViewById(R.id.assignmentLinearLayout);
        assignmentSpinner = findViewById(R.id.assignmentSpinner);

        Course course = new Course();

        int courseId = intent.getIntExtra("courseIdLoader", 666);
        if(courseId != 666){
            course = dbUtils.getCourseFromId(courseId);
            courseNameEditText.setText(course.getName());
            startDateEditText.setText(course.getStartDate());
            endDateEditText.setText(course.getEndDate());
            statusEditText.setText(course.getStatus());
            mentorNameEditText.setText(course.getMentorNames());
            phoneNumberEditText.setText(course.getPhoneNumber());
            System.out.println("OPTIONAL NOTE: " + course.getOptionalNote());
            optionalNoteEditText.setText(course.getOptionalNote());
            emailEditText.setText(course.getEmail());

        }

        final Course finalCourse = course;
        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int assignmentIndex = assignmentSpinner.getSelectedItemPosition();
                int assignmentId = dbUtils.getAssignmentIdFromIndex(assignmentIndex);
                dbUtils.addCourseIdToAssignmentById(finalCourse.getId(), assignmentId);
                generateAssignmentLinearLayout(finalCourse);
            }
        });

        deleteAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int assignmentIndex = assignmentSpinner.getSelectedItemPosition();
                int assignmentId = dbUtils.getAssignmentIdFromIndex(assignmentIndex);
                dbUtils.removeCourseByAssignmentId(assignmentId);
                generateAssignmentLinearLayout(finalCourse);

            }
        });

        courseSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputFieldsAreValid() ){
                    if(intent.getBooleanExtra("modifying", false)){
                        dbUtils.updateCourse(new Course(
                                finalCourse.getId(),
                                courseNameEditText.getText().toString(),
                                startDateEditText.getText().toString(),
                                endDateEditText.getText().toString(),
                                statusEditText.getText().toString(),
                                mentorNameEditText.getText().toString(),
                                phoneNumberEditText.getText().toString(),
                                emailEditText.getText().toString(),
                                optionalNoteEditText.getText().toString(),
                                0

                        ));

                    }else{
                        dbUtils.addCourse(
                                courseNameEditText.getText().toString(),
                                startDateEditText.getText().toString(),
                                endDateEditText.getText().toString(),
                                statusEditText.getText().toString(),
                                mentorNameEditText.getText().toString(),
                                phoneNumberEditText.getText().toString(),
                                emailEditText.getText().toString(),
                                optionalNoteEditText.getText().toString(),
                                0
                        );
                    }
                    startActivity(new Intent(AddModifyCourse.this, CourseActivity.class));
                }
            }
        });

        newAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddModifyCourse.this, AddModifyAssignment.class);
                startActivity(intent);
            }
        });

        courseCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddModifyCourse.this, CourseActivity.class));
            }
        });

        courseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                   Utils.changeBackgroundColorToRed(courseNameEditText);
                }else {
                    Utils.changeBackgroundColorToWhite(courseNameEditText);
                }
            }
        });

        startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!textIsValidDate(s.toString())){
                    Utils.changeBackgroundColorToRed(startDateEditText);
                } else{
                    sendMessage("");
                    Utils.changeBackgroundColorToWhite(startDateEditText);
                }
            }
        });

        endDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!textIsValidDate(s.toString())){
                    Utils.changeBackgroundColorToRed(endDateEditText);
                } else{
                    sendMessage("");
                    Utils.changeBackgroundColorToWhite(endDateEditText);
                }
            }
        });
        generateAssignmentLinearLayout(course);

    }

    void generateAssignmentLinearLayout(Course course){
        // adds all the assignments to the assignment linear layout
        assignmentLinearLayout.removeAllViewsInLayout();
        ArrayList<Assignment> assignments = dbUtils.getAssignments();
        ArrayList<String> assignmentNames = new ArrayList<>();

        for(Assignment assignment : assignments) {
            assignmentNames.add(assignment.getName());
            if (course.getId() == assignment.getCourseId()) {
                final Button button = new Button(this);
                button.setText(assignment.getName());
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor(getResources().getColor(R.color.blackdartqBlue));
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        int backgroundColor = ((ColorDrawable) button.getBackground()).getColor();
//                        if (backgroundColor == Color.WHITE) {
//                            button.setBackgroundColor(Color.BLUE);
//                        } else {
//                            button.setBackgroundColor(Color.WHITE);
//                        }
//                    }
//                });
                assignmentLinearLayout.addView(button);
            }
        }

        // adds an arrayAdapter with all the assignment names to the spinner
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, assignmentNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assignmentSpinner.setAdapter(arrayAdapter);

    }

    /**
     * @param name
     * @param startDate
     * @param endDate
     */
    public void loadCourseData(String name, String startDate, String endDate){
        System.out.println("loading data");
        courseNameLoader = name;
        startDateLoader = startDate;
        endDateLoader = endDate;
    }

    void sendMessage(String message){
        messageTextView.setText(message);
    }

//    int getIndexOfSpinner(){
//        for(int i = 0; i < arrayAdapter.getCount(); i++){
//            Object testingObject = arrayAdapter.getItem(i);
//            if(i == arrayAdapter.getPosition(testingObject));
//        }
//    }

    boolean textIsValidDate(String date){
        String[] dateBrokenUpBySlashes = date.split("/");
        ArrayList<Integer> dateNumbers = new ArrayList();
        if(dateBrokenUpBySlashes.length != 3){
            sendMessage("date didn't have three fields");
            return false;
        }
        for(String datePiece : dateBrokenUpBySlashes){
           try {
               dateNumbers.add(Integer.parseInt(datePiece));
           }catch (Exception e){
               return false;
           }
        }

        // checks if the months/days are in the correct ranges
        if(dateNumbers.get(0) > 12 && dateNumbers.get(0) <= 0
                || dateNumbers.get(1) > 31 && dateNumbers.get(1) <= 0 ){
            sendMessage("date segment mon/day is over");
            return false;
        }

        // checks if the year is in the right range
        if(dateNumbers.get(2) > 2100 || dateNumbers.get(2) < 2019){
            sendMessage("Please enter a year in the correct range");
            return false;
        }
        return  true;
    }

    boolean checkInputFieldsAreValid(){
        boolean output = true;
        if(courseNameEditText.getText().toString().equals("")){
            Utils.changeBackgroundColorToRed(courseNameEditText);
            output = false;
        }else{
            Utils.changeBackgroundColorToWhite(courseNameEditText);
        }

        if(startDateEditText.getText().toString().equals("")) {
            Utils.changeBackgroundColorToRed(startDateEditText);
            output = false;
        } else{
            Utils.changeBackgroundColorToWhite(startDateEditText);
        }

        if(endDateEditText.getText().toString().equals("")) {
            Utils.changeBackgroundColorToRed(endDateEditText);
            output = false;
        } else{
            Utils.changeBackgroundColorToWhite(endDateEditText);
        }

        return output;
    }

}

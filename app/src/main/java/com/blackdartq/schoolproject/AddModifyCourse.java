package com.blackdartq.schoolproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    EditText courseNameEditText;
    EditText startDateEditText;
    EditText endDateEditText;
    EditText statusEditText;
    EditText mentorNameEditText;
    EditText phoneNumberEditText;
    EditText emailEditText;

    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_course);
        dbUtils = new DBUtils();
        final Intent intent = getIntent();

        courseSaveButton = findViewById(R.id.courseSaveButton);
        courseCancelButton = findViewById(R.id.courseCancelButton);
        courseNameEditText = findViewById(R.id.courseNameEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        statusEditText = findViewById(R.id.statusEditText);
        mentorNameEditText = findViewById(R.id.mentorNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        emailEditText = findViewById(R.id.emailEditText);


        messageTextView = findViewById(R.id.messageTextView);

        courseNameEditText.setText(intent.getStringExtra("courseNameLoader"));
        startDateEditText.setText(intent.getStringExtra("startDateLoader"));
        endDateEditText.setText(intent.getStringExtra("endDateLoader"));

        courseSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputFieldsAreValid() ){
                    if(intent.getBooleanExtra("modifying", false)){
                        int courseId = dbUtils.getIdFromIndex(intent.getIntExtra("courseIndex", 0));
                        dbUtils.updateCourse(new Course(
                                courseId,
                                courseNameEditText.getText().toString(),
                                startDateEditText.getText().toString(),
                                endDateEditText.getText().toString(),
                                statusEditText.getText().toString(),
                                mentorNameEditText.getText().toString(),
                                phoneNumberEditText.getText().toString(),
                                emailEditText.getText().toString()
                        ));

                    }else{
                        dbUtils.addCourse(
                                courseNameEditText.getText().toString(),
                                startDateEditText.getText().toString(),
                                endDateEditText.getText().toString(),
                                statusEditText.getText().toString(),
                                mentorNameEditText.getText().toString(),
                                phoneNumberEditText.getText().toString(),
                                emailEditText.getText().toString()
                        );
                    }
                    startActivity(new Intent(AddModifyCourse.this, CourseActivity.class));
                }
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

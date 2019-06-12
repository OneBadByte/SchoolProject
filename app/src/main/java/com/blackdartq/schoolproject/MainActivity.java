package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.blackdartq.schoolproject.Utils.Course;
import com.blackdartq.schoolproject.Utils.Term;

import java.text.MessageFormat;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class MainActivity extends AppCompatActivity {
    // colors
    String antiFlashWhite = "#f2f6f3";
    String midnightGreen = "#074f57";
    String metallicSeaweed = "#077187";
    DBUtils dbUtils;


    // TextViews

    // Buttons

    // Edit Text
    EditText alertEditText;
    LinearLayout termAndCourseLinearLayout;

    // Layouts


    private void sendMessage(String message){
        alertEditText.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViews

        // Buttons
        Button termButton = findViewById(R.id.termButton);
        Button coursesButton = findViewById(R.id.coursesButton);
        Button assignmentButton = findViewById(R.id.assignmentsButton);


        // Edit Text
        alertEditText = findViewById(R.id.alertEditText);

        // Layouts
        LinearLayout termAndCourseLinearLayout = findViewById(R.id.termAndCourseLinearLayout);

        dbUtils = new DBUtils();
//        dbUtils.addTestTermsAndCourses();
        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TermActivity.class));
            }
        });

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                startActivity(intent);
            }
        });

        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssignmentActivity.class);
                startActivity(intent);
            }
        });

        for(Term term : dbUtils.getTerms()){
            String formattedTerm = MessageFormat.format("{0}   {1} - {2}", term.getName(), term.getStartDate(), term.getEndDate());
            termAndCourseLinearLayout.addView(createTermButton(formattedTerm));
            for(Course course : dbUtils.getCourses()){
                if(course.getTermId() == term.getId()){
                    termAndCourseLinearLayout.addView(createCourseButton(course.getName()));
                }
            }
        }


    }
    Button createTermButton(String text){
        Button button = new Button(this);

        button.setTextColor(Color.WHITE);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.GREEN);
        button.setHeight(200);
        button.setTextSize(24);
        button.setText(text);
        return button;
    }

    Button createCourseButton(String text){
        Button button = new Button(this);

        button.setTextColor(Color.WHITE);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.RED);
        button.setHeight(200);
        button.setTextSize(24);
        button.setText(text);
        return button;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blackdartq.schoolproject.Utils.Assignment;
import com.blackdartq.schoolproject.Utils.Course;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    // views
    ScrollView courseScrollView;
    LinearLayout courseLinearLayout;

    //buttons
    Button addCourseButton;
    Button modifyCourseButton;
    Button deleteCourseButton;
    Button backCourseButton;


    ArrayList<Button> buttonHolder;

    int selectedCourse = 0;

    private static final String TAG = CourseActivity.class.getSimpleName();
    DBUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        buttonHolder = new ArrayList<>();
        dbUtils = new DBUtils(false);
        courseScrollView = findViewById(R.id.courseScrollView);
        courseLinearLayout = findViewById(R.id.courseLinearLayout);
        addCourseButton = findViewById(R.id.addCourseButton);
        modifyCourseButton = findViewById(R.id.modifyCourseButton);
        deleteCourseButton = findViewById(R.id.deleteCourseButton);
        backCourseButton = findViewById(R.id.backCourseButton);

        addCourseButtonsToUI();

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, AddModifyCourse.class);
                intent.putExtra("modifying", false);
                startActivity(intent);
            }
        });

        modifyCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(CourseActivity.this, AddModifyCourse.class);

                    Course courseData = dbUtils.getCourseFromIndex(selectedCourse);
                    intent.putExtra("courseIdLoader", courseData.getId());
                    intent.putExtra("modifying", true);
                    startActivity(intent);
                } catch (Exception e){}
            }
        });

        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtils.deleteCourseByIndex(selectedCourse);
                courseLinearLayout.removeViewAt(selectedCourse);
            }
        });

        backCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseActivity.this, MainActivity.class));
            }
        });


    }

    void addCourseButtonsToUI(){
        ArrayList<Course> courses = dbUtils.getCourses();
        for(Course course : courses){
            createCourseButton(course);
        }
        for(Button button : buttonHolder){
            courseLinearLayout.addView(button);
        }
    }

    void createCourseButton(final Course course) {
        final Button button = new Button(this);
        button.setText(course.getName());
        button.setBackgroundColor(getResources().getColor(R.color.blackdartqBlueGreen));
        button.setTextColor(Color.WHITE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCourse = courseLinearLayout.indexOfChild(button);
            }
        });
        buttonHolder.add(button);
    }
}

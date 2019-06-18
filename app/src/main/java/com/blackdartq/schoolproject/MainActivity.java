package com.blackdartq.schoolproject;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blackdartq.schoolproject.Utils.Assignment;
import com.blackdartq.schoolproject.Utils.Course;
import com.blackdartq.schoolproject.Utils.Term;

import java.text.MessageFormat;
import java.util.ArrayList;

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

    // Layouts
    LinearLayout termAndCourseLinearLayout;

    // used to generate buttons
    ArrayList<Integer> termIndexs;
    ArrayList<Integer> courseIndexs;
    ArrayList<Integer> assignmentIndexs;

    // used by the course buttons that are generated
    final int COURSE_HEIGHT = 150;
    final int COURSE_TEXT_SIZE = 22;

    // used by the assignment buttons that are generated
    final int ASSIGNMENT_HEIGHT = 104;
    final int ASSIGNMENT_TEXT_SIZE = 18;

    Alerts alerts;


    private void sendMessage(String message) {
        alertEditText.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Buttons
        Button termButton = findViewById(R.id.termButton);
        Button coursesButton = findViewById(R.id.coursesButton);
        Button assignmentButton = findViewById(R.id.assignmentsButton);


        // Edit Text
        alertEditText = findViewById(R.id.alertEditText);

        // Layouts
        final LinearLayout termAndCourseLinearLayout = findViewById(R.id.termAndCourseLinearLayout);

//        dbUtils = new DBUtils(true);
        dbUtils = new DBUtils(false);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alerts = new Alerts(this, notificationManager);

        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerts.sendNotification("test", "test");
//                startActivity(new Intent(MainActivity.this, TermActivity.class));
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

        initializeTermCourseAssignmentIndexes();

        sendMessage(getResources().getString(R.string.app_name));

        // generates the buttons
        generateButtons();
    }

    /**
     * Initializes the indexes used to keep track of the buttons being generated
     */
    void initializeTermCourseAssignmentIndexes() {
        termIndexs = new ArrayList<>();
        courseIndexs = new ArrayList<>();
        assignmentIndexs = new ArrayList<>();
    }

    /**
     * Generates the term, course, and assignment buttons
     */
    void generateButtons() {
        // GENERATES TERM BUTTONS
        termAndCourseLinearLayout = findViewById(R.id.termAndCourseLinearLayout);
        for (Term term : dbUtils.getTerms()) {
            String formattedTerm = MessageFormat.format("{0}   {1} - {2}", term.getName(), term.getStartDate(), term.getEndDate());
            createTermButton(formattedTerm);

            // creates the course buttons
            for (Course course : dbUtils.getCourses()) {
                if (course.getTermId() == term.getId()) {
                    createCourseButton(course);

                    // creates the assignment buttons
                    for (Assignment assignment : dbUtils.getAssignments()) {
                        if (assignment.getCourseId() == course.getId()) {
                            createAssignmentButton(assignment);
                        }
                    }
                }
            }
        }
    }

    void createTermButton(String text) {
        final Button button = new Button(this);
        button.setLongClickable(true);
        button.setTextColor(Color.WHITE);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(getResources().getColor(R.color.blackdartqGreen2));
        button.setHeight(300);
        button.setTextSize(24);
        button.setText(text);

        termAndCourseLinearLayout.addView(button);
        termIndexs.add(termAndCourseLinearLayout.indexOfChild(button));

        final int linearLayoutIndex = termAndCourseLinearLayout.indexOfChild(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int termIndex = getTermIndexFromLinearLayoutIndex(linearLayoutIndex);

                // handles buffer overflow of term index
                if(termIndexs.size() == termIndex + 1){
                    return;
                }
                int nextIndex = termIndexs.get(termIndex + 1);
                for (int i = linearLayoutIndex + 1; i < nextIndex; i++) {
                    Button uiButton = (Button) termAndCourseLinearLayout.getChildAt(i);
                    if (uiButton.getVisibility() == View.GONE) {
                        uiButton.setVisibility(View.VISIBLE);
                    } else {
                        uiButton.setVisibility(View.GONE);
                    }
                }
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddModifyTerm.class);
                int termIndex = getTermIndexFromLinearLayoutIndex(linearLayoutIndex);
                intent.putExtra("modifying", true);
                intent.putExtra("termIdLoader", dbUtils.getTermIdFromIndex(termIndex));
                startActivity(intent);
                return true;
            }
        });

    }

    int getTermIndexFromLinearLayoutIndex(int linearLayoutIndex) {
        int count = 0;
        try {
            for (int index : termIndexs) {
                if (index == linearLayoutIndex) {
                    break;
                }
                count++;
            }
        } catch (Exception e){
            throw new RuntimeException("Couldn't find term id from index");
        }
        return count;
    }
    void createCourseButtons(){
    }

    Button createCourseButton(final Course course) {
        final Button button = new Button(this);
        button.setLongClickable(true);
        button.setVisibility(View.GONE);
        button.setTextColor(Color.WHITE);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.parseColor("#20B2AA"));
        button.setHeight(COURSE_HEIGHT);
        button.setTextSize(COURSE_TEXT_SIZE);
        button.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        button.setText(course.getName());

        termAndCourseLinearLayout.addView(button);
        courseIndexs.add(termAndCourseLinearLayout.indexOfChild(button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(button.getHeight());
                int defaultSize = COURSE_TEXT_SIZE + COURSE_HEIGHT;
                if (button.getHeight() > defaultSize) {
                    button.setHeight(defaultSize);
                    button.setText(course.getName());
                } else {
                    button.setHeight(button.getHeight() * 3);
                    String buttonText = MessageFormat.format("{0}   {1} - {2} \n Status: {3}\n Mentor Name: {4}\n Phone Number: {5}\n",
                            course.getName(), course.getStartDate(), course.getEndDate(),
                            course.getStatus(), course.getMentorNames(), course.getPhoneNumber()
                    );
                    button.setText(buttonText);
                }
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddModifyCourse.class);
                intent.putExtra("modifying", true);
                intent.putExtra("courseIdLoader", course.getId());
                startActivity(intent);
                return true;
            }
        });

        return button;
    }

    void createAssignmentButton(final Assignment assignment) {
        final Button button = new Button(this);
        button.setLongClickable(true);
        button.setVisibility(View.GONE);
        button.setTextColor(Color.BLACK);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(Color.parseColor("#FFF8DC"));
        button.setHeight(ASSIGNMENT_HEIGHT);
        button.setTextSize(ASSIGNMENT_TEXT_SIZE);
        button.setText(assignment.getName());

        termAndCourseLinearLayout.addView(button);
        assignmentIndexs.add(termAndCourseLinearLayout.indexOfChild(button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int defaultSize = ASSIGNMENT_TEXT_SIZE + ASSIGNMENT_HEIGHT;
                System.out.println(button.getHeight() + " " + defaultSize);
                if (button.getHeight() > defaultSize) {
                    button.setHeight(defaultSize);
                    System.out.println("HIT RESET");
                    button.setText(assignment.getName());
                } else {
                    System.out.println("HIT EXPAND");
                    button.setHeight(button.getHeight() * 3);
                    String buttonText = MessageFormat.format("{0}   \n Due Date:{1} \n{2}\n",
                            assignment.getName(), assignment.getDueDate(), assignment.getOptionalNote()
                    );
                    button.setText(buttonText);
                }
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddModifyAssignment.class);
                intent.putExtra("modifying", true);
                intent.putExtra("assignmentIdLoader", assignment.getId());
                startActivity(intent);
                return true;
            }
        });
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

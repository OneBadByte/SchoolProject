package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blackdartq.schoolproject.Utils.Assignment;

import java.util.ArrayList;

public class AssignmentActivity extends AppCompatActivity {

    int BLUE = Color.parseColor("#4a97a7");

    // views
    ScrollView assignmentScrollView;
    LinearLayout assignmentLinearLayout;

    //buttons
    Button addAssignmentButton;
    Button modifyAssignmentButton;
    Button deleteAssignmentButton;
    Button backAssignmentButton;

    ArrayList<Button> buttonHolder;

    int selectedAssignment = 0;
    Assignment assignment;
    Toolbar assignmentActivityToolbar;

    private static final String TAG = AssignmentActivity.class.getSimpleName();
    DBUtils dbUtils = new DBUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
//        Toolbar toolbar = findViewById(R.id.assignmentActivityToolbar);
//        setSupportActionBar(toolbar);
        buttonHolder = new ArrayList<>();
        dbUtils = new DBUtils();
        assignmentScrollView = findViewById(R.id.assignmentScrollView);
        assignmentLinearLayout = findViewById(R.id.assignmentLinearLayout);
        addAssignmentButton = findViewById(R.id.addAssignmentButton);
        modifyAssignmentButton = findViewById(R.id.modifyAssignmentButton);
        deleteAssignmentButton = findViewById(R.id.deleteAssignmentButton);
        backAssignmentButton = findViewById(R.id.backAssignmentButton);

        addAssignmentButtonsToUI();

        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignmentActivity.this, AddModifyAssignment.class);
                intent.putExtra("modifying", false);
                startActivity(intent);
            }
        });

        modifyAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignmentActivity.this, AddModifyAssignment.class);
                try{
                    Assignment assignmentData = dbUtils.getAssignmentFromIndex(selectedAssignment);
                    intent.putExtra("assignmentNameLoader", assignmentData.getName());
                    intent.putExtra("optionalNoteLoader", assignmentData.getOptionalNote());
                    intent.putExtra("dueDateLoader", assignmentData.getDueDate());
                    intent.putExtra("modifying", true);
                    int assignmentIdFromIndex = dbUtils.getAssignmentIdFromIndex(selectedAssignment);
                    intent.putExtra("assignmentIdLoader", assignmentIdFromIndex);

                    startActivity(intent);
                } catch (Exception e){
                }
            }
        });

        deleteAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if the assignment has courses and wont delete if it does
                int assignmentId = dbUtils.getAssignmentIdFromIndex(selectedAssignment);
                if(dbUtils.assignmentHasCourses(assignmentId)){
                    sendError("Please remove assignment from course!");
                    return;
                }

                // trys to delete the assignment and removes it from the UI
                try{
                    dbUtils.deleteAssignmentByIndex(selectedAssignment);
                    assignmentLinearLayout.removeViewAt(selectedAssignment);
                } catch (Exception e){
                    throw new RuntimeException("Couldn't delete assignment " + assignmentId);
                }
            }
        });

        backAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AssignmentActivity.this, MainActivity.class));
            }
        });

    }

    void sendMessage(String message){
        assignmentActivityToolbar.setTitle(message);
    }

    void sendError(String message){
        assignmentActivityToolbar.setBackgroundColor(Color.RED);
        sendMessage(message);
    }

    void addAssignmentButtonsToUI(){
        ArrayList<Assignment> assignments = dbUtils.getAssignments();
        for(Assignment assignment : assignments){
            System.out.println("Assignment: " + assignment.getName());
            Button button = createAssignmentButton(assignment);
            assignmentLinearLayout.addView(button);
        }
    }

    Button createAssignmentButton(final Assignment assignment) {
       final Button button = new Button(this);
        button.setText(assignment.getName());
        button.setBackgroundColor(BLUE);
        button.setTextColor(Color.WHITE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAssignment = assignmentLinearLayout.indexOfChild(button);
            }
        });
        return button;
    }
}

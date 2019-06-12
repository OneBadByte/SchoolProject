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

import com.blackdartq.schoolproject.Utils.Assignment;
import com.blackdartq.schoolproject.Utils.Utils;

import java.util.ArrayList;

public class AddModifyAssignment extends AppCompatActivity {

    public String assignmentNameLoader = "";
    String dueDateLoader = "";
    String endDateLoader = "";

    DBUtils dbUtils;

    Button assignmentSaveButton;
    Button assignmentCancelButton;

    EditText assignmentNameEditText;
    EditText dueDateEditText;
    EditText optionalNoteEditText;

    TextView messageTextView;
    Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_assignment);
        dbUtils = new DBUtils();
        final Intent intent = getIntent();

        assignmentSaveButton = findViewById(R.id.assignmentSaveButton);
        assignmentCancelButton = findViewById(R.id.assignmentCancelButton);
        assignmentNameEditText = findViewById(R.id.assignmentNameEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        optionalNoteEditText = findViewById(R.id.optionalNoteEditText);


        messageTextView = findViewById(R.id.messageTextView);

        assignmentNameEditText.setText(intent.getStringExtra("assignmentNameLoader"));
        assignmentNameEditText.setText(intent.getStringExtra("optionalNoteLoader"));
        dueDateEditText.setText(intent.getStringExtra("dueDateLoader"));

        assignmentSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputFieldsAreValid() ){
                    if(intent.getBooleanExtra("modifying", false)){
                        int assignmentId = dbUtils.getAssignmentIdFromIndex(intent.getIntExtra("assignmentIndex", 0));

                        dbUtils.updateAssignment(new Assignment(
                                assignmentId,
                                assignmentNameEditText.getText().toString(),
                                optionalNoteEditText.getText().toString(),
                                dueDateEditText.getText().toString()
                        ));

                    }else{
                        dbUtils.addAssignment(
                                assignmentNameEditText.getText().toString(),
                                optionalNoteEditText.getText().toString(),
                                dueDateEditText.getText().toString(),
                                0
                        );
                    }
                    startActivity(new Intent(AddModifyAssignment.this, AssignmentActivity.class));
                }
            }
        });

        assignmentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddModifyAssignment.this, AssignmentActivity.class));
            }
        });

        assignmentNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                   Utils.changeBackgroundColorToRed(assignmentNameEditText);
                }else {
                    Utils.changeBackgroundColorToWhite(assignmentNameEditText);
                }
            }
        });

        dueDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!textIsValidDate(s.toString())){
                    Utils.changeBackgroundColorToRed(dueDateEditText);
                } else{
                    sendMessage("");
                    Utils.changeBackgroundColorToWhite(dueDateEditText);
                }
            }
        });

//        endDateEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!textIsValidDate(s.toString())){
//                    Utils.changeBackgroundColorToRed(endDateEditText);
//                } else{
//                    sendMessage("");
//                    Utils.changeBackgroundColorToWhite(endDateEditText);
//                }
//            }
//        });
    }

    /**
     * @param name
     * @param dueDate
     * @param endDate
     */
    public void loadAssignmentData(String name, String dueDate, String endDate){
        System.out.println("loading data");
        assignmentNameLoader = name;
        dueDateLoader = dueDate;
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
        if(assignmentNameEditText.getText().toString().equals("")){
            Utils.changeBackgroundColorToRed(assignmentNameEditText);
            output = false;
        }else{
            Utils.changeBackgroundColorToWhite(assignmentNameEditText);
        }

        if(dueDateEditText.getText().toString().equals("")) {
            Utils.changeBackgroundColorToRed(dueDateEditText);
            output = false;
        } else{
            Utils.changeBackgroundColorToWhite(dueDateEditText);
        }

//        if(endDateEditText.getText().toString().equals("")) {
//            Utils.changeBackgroundColorToRed(endDateEditText);
//            output = false;
//        } else{
//            Utils.changeBackgroundColorToWhite(endDateEditText);
//        }

        return output;
    }

}

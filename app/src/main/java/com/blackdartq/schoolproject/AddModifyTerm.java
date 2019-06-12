package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackdartq.schoolproject.Utils.Course;
import com.blackdartq.schoolproject.Utils.Term;
import com.blackdartq.schoolproject.Utils.Utils;

import java.util.ArrayList;

public class AddModifyTerm extends AppCompatActivity {

    public String termNameLoader = "";
    String startDateLoader = "";
    String endDateLoader = "";

    LinearLayout courseSelectorLinearLayout;

    DBUtils dbUtils;

    Button termSaveButton;
    Button termCancelButton;

    EditText termNameEditText;
    EditText startDateEditText;
    EditText endDateEditText;

    TextView messageTextView;
//    int termId = 0;
    int selectedTermIdFromCourse = 0;
    int selectedCourseId = 0;
    Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_term);
        dbUtils = new DBUtils();
        term = new Term();

        termSaveButton = findViewById(R.id.termSaveButton);
        termCancelButton = findViewById(R.id.termCancelButton);
        termNameEditText = findViewById(R.id.termNameEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.dueDateEditText);
        messageTextView = findViewById(R.id.messageTextView);
        courseSelectorLinearLayout = findViewById(R.id.courseSelectorLinearLayout);

        // used to load data from the previous activity
        final Integer defaultTermId = 666;
        final Intent intent = getIntent();
        term.setId(intent.getIntExtra("termIdLoader", 666));
        System.out.println("HIT: " + term.getId());
        if(term.getId().equals(defaultTermId)){
            term.setId(dbUtils.addTerm());
        }

        term = dbUtils.getTermById(term.getId());
        // this adds all the associated course ids to the terms ArrayList
        // used for saving the term ids to courses later on
        term.addCourseIdsToTerm(dbUtils.getCourseIdsFromAssociatedTermId(term.getId()));
        termNameEditText.setText(term.getName());
        startDateEditText.setText(term.getStartDate());
        endDateEditText.setText(term.getEndDate());

        // TERM SAVE BUTTON
        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputFieldsAreValid() ) {
////                    if(intent.getBooleanExtra("modifying", false)){
//                        term.setName(Utils.getTextFromEditText(termNameEditText));
//                        term.setStartDate(Utils.getTextFromEditText(startDateEditText));
//                        term.setEndDate(Utils.getTextFromEditText(endDateEditText));
//                        dbUtils.updateTerm(term);
//                    }else{
//                        dbUtils.addTerm(
//                                termNameEditText.getText().toString(),
//                                startDateEditText.getText().toString(),
//                                endDateEditText.getText().toString()
//                        );
                    term.setName(Utils.getTextFromEditText(termNameEditText));
                    term.setStartDate(Utils.getTextFromEditText(startDateEditText));
                    term.setEndDate(Utils.getTextFromEditText(endDateEditText));
                    dbUtils.updateTerm(term);

                    for(int id: term.getCourseIdsFromTerm()){
                        dbUtils.addTermIdToCourseById(term.getId(), id);
                    }
//                    for(int id: courseIdsMarkedForRemoval){
//                        System.out.println("REMOVING: " + id);
//                        dbUtils.removeTermIdByCourseId(id);
//                    }
//                    dbUtils.addCourseIdToTermByTermId(termId, selectedCourseId);
                    startActivity(new Intent(AddModifyTerm.this, TermActivity.class));
                }
            }
        });

        // TERM CANCEL BUTTON
        termCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deletes all terms with the name ""
                dbUtils.deleteTermByName("");
                startActivity(new Intent(AddModifyTerm.this, TermActivity.class));
            }
        });

        termNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                   Utils.changeBackgroundColorToRed(termNameEditText);
                }else {
                    Utils.changeBackgroundColorToWhite(termNameEditText);
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

        // generates all the courses created so the user can select them
        generateCourseButtons();
    }

    /**
     * generates the course buttons in the LinearLayout
     */
    void generateCourseButtons(){
        for(Course course : dbUtils.getCourses()){
            // checks if the term contains the course id in its ArrayList of course Ids
            if(term.getCourseIdsFromTerm().contains(course.getId())){
                courseSelectorLinearLayout.addView(createCourseButton(course, Color.GREEN, Color.RED, true));
            }else{
                if(course.getTermId() == null){
                    courseSelectorLinearLayout.addView(createCourseButton(course));
                }else{
                    courseSelectorLinearLayout.addView(createCourseButton(course, Color.MAGENTA, Color.MAGENTA, false));
                }
            }
        }
    }

    /**
     * creates a button with all the course information loaded into the text section
     * @param course
     * @return
     */
    Button createCourseButton(final Course course){
        return createCourseButton(course, Color.WHITE, Color.BLUE, true);
    }

    /**
     * creates a button with all the course information loaded into the text section
     * @param course
     * @return
     */
    Button createCourseButton(final Course course, int backgroundColor, final int changeToColor, boolean editable){
        final Button button = new Button(this);
        String message = String.format("%s      %s - %s", course.getName(), course.getStartDate(), course.getEndDate());
        button.setText(message);
        button.setTextColor(Color.BLACK);
        button.setBackgroundColor(backgroundColor);
        button.setClickable(editable);
        if(editable){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.changeBackgroundOnClick(button, Color.WHITE, changeToColor);
                    int backgroundColor = ((ColorDrawable) button.getBackground()).getColor();
                    if(backgroundColor == Color.WHITE){
                        dbUtils.removeTermIdByCourseId(course.getId());
                        term.removeCourseIdFromTermByCourseId(course.getId());
                    }else{
                        dbUtils.addTermIdToCourseById(term.getId(), course.getId());
                        term.appendCourseIdToTerm(course.getId());
                    }
                }
            });
        }
        return button;
    }


    /**
     * @param name
     * @param startDate
     * @param endDate
     */
    public void loadTermData(String name, String startDate, String endDate){
        System.out.println("loading data");
        termNameLoader = name;
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
        if(termNameEditText.getText().toString().equals("")){
            Utils.changeBackgroundColorToRed(termNameEditText);
            output = false;
        }else{
            Utils.changeBackgroundColorToWhite(termNameEditText);
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

    // ______________COURSE Methods______________________________
//    void addTermIdFromCourseIndex(int index){
//        dbUtils.addTermIdToCourseByIndex();
//    }
}

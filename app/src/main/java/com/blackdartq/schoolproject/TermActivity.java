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

import com.blackdartq.schoolproject.Utils.Term;

import java.util.ArrayList;

public class TermActivity extends AppCompatActivity {

    int BLUE = Color.parseColor("#4a97a7");

    // views
    ScrollView termScrollView;
    LinearLayout termLinearLayout;

    //buttons
    Button addTermButton;
    Button modifyTermButton;
    Button deleteTermButton;
    Button backTermButton;

    ArrayList<Button> buttonHolder;

    int selectedTerm = 0;
    Term term;
    Toolbar termActivityToolbar;

    private static final String TAG = TermActivity.class.getSimpleName();
    DBUtils dbUtils = new DBUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
//        Toolbar toolbar = findViewById(R.id.termActivityToolbar);
//        setSupportActionBar(toolbar);
        buttonHolder = new ArrayList<>();
        dbUtils = new DBUtils();
        termScrollView = findViewById(R.id.termScrollView);
        termLinearLayout = findViewById(R.id.termLinearLayout);
        addTermButton = findViewById(R.id.addTermButton);
        modifyTermButton = findViewById(R.id.modifyTermButton);
        deleteTermButton = findViewById(R.id.deleteTermButton);
        backTermButton = findViewById(R.id.backTermButton);
        termActivityToolbar = findViewById(R.id.termActivityToolbar);

        addTermButtonsToUI();

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermActivity.this, AddModifyTerm.class);
                intent.putExtra("modifying", false);
                startActivity(intent);
            }
        });

        modifyTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermActivity.this, AddModifyTerm.class);
                try{
                    Term termData = dbUtils.getTermFromIndex(selectedTerm);
                    intent.putExtra("termNameLoader", termData.getName());
                    intent.putExtra("startDateLoader", termData.getStartDate());
                    intent.putExtra("endDateLoader", termData.getEndDate());
                    intent.putExtra("modifying", true);
                    int termIdFromIndex = dbUtils.getTermIdFromIndex(selectedTerm);
                    intent.putExtra("termIdLoader", termIdFromIndex);

                    startActivity(intent);
                } catch (Exception e){
                }
            }
        });

        deleteTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if the term has courses and wont delete if it does
                int termId = dbUtils.getTermIdFromIndex(selectedTerm);
                if(dbUtils.termHasCourses(termId)){
                    sendError("Please remove courses from term!");
                    return;
                }

                // trys to delete the term and removes it from the UI
                try{
                    dbUtils.deleteTermByIndex(selectedTerm);
                    termLinearLayout.removeViewAt(selectedTerm);
                } catch (Exception e){
                    throw new RuntimeException("Couldn't delete term " + termId);
                }
            }
        });

        backTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermActivity.this, MainActivity.class));
            }
        });

    }

    void sendMessage(String message){
        termActivityToolbar.setTitle(message);
    }

    void sendError(String message){
        termActivityToolbar.setBackgroundColor(Color.RED);
        sendMessage(message);
    }

    void addTermButtonsToUI(){
        ArrayList<Term> terms = dbUtils.getTerms();
        for(Term term : terms){
            createTermButton(term);
        }
        for(Button button : buttonHolder){
            termLinearLayout.addView(button);
        }
    }

    void createTermButton(final Term term) {
        final Button button = new Button(this);
        button.setText(term.getName());
        button.setBackgroundColor(BLUE);
        button.setTextColor(Color.WHITE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTerm = termLinearLayout.indexOfChild(button);
            }
        });
        buttonHolder.add(button);
    }
}

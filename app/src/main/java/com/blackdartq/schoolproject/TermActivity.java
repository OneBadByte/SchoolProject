package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blackdartq.schoolproject.Utils.Term;

import java.util.ArrayList;

public class TermActivity extends AppCompatActivity {
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

    private static final String TAG = TermActivity.class.getSimpleName();
    DBUtils dbUtils = new DBUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonHolder = new ArrayList<>();
        dbUtils = new DBUtils();
        termScrollView = findViewById(R.id.termScrollView);
        termLinearLayout = findViewById(R.id.termLinearLayout);
        addTermButton = findViewById(R.id.addTermButton);
        modifyTermButton = findViewById(R.id.modifyTermButton);
        deleteTermButton = findViewById(R.id.deleteTermButton);
        backTermButton = findViewById(R.id.backTermButton);

        addTermButtonsToUI();

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermActivity.this, AddModifyTerm.class));
            }
        });

        modifyTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermActivity.this, AddModifyTerm.class));
            }
        });

        deleteTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtils.deleteTermByIndex(selectedTerm);
                termLinearLayout.removeViewAt(selectedTerm);
            }
        });

        backTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermActivity.this, MainActivity.class));
            }
        });

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setBackgroundColor(Color.GREEN);
                selectedTerm = termLinearLayout.indexOfChild(button);
            }
        });
        buttonHolder.add(button);
    }
}

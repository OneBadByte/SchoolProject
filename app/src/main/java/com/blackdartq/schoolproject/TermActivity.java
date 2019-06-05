package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
                Intent intent = new Intent(TermActivity.this, AddModifyTerm.class);
                intent.putExtra("modifying", false);
                startActivity(intent);
            }
        });

        modifyTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermActivity.this, AddModifyTerm.class);

                Term termData = dbUtils.getTermFromIndex(selectedTerm);
                intent.putExtra("termNameLoader", termData.getName());
                intent.putExtra("startDateLoader", termData.getStartDate());
                intent.putExtra("endDateLoader", termData.getEndDate());
                intent.putExtra("modifying", true);
                intent.putExtra("termIndex", selectedTerm);

                startActivity(intent);
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

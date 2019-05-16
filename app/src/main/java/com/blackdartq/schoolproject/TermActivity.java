package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class TermActivity extends AppCompatActivity {
    // views
    ScrollView termScrollView;
    LinearLayout termLinearLayout;

    //buttons
    Button addTermButton;
    Button modifyTermButton;
    Button deleteTermButton;
    Button backTermButton;

    private static final String TAG = TermActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        termScrollView = findViewById(R.id.termScrollView);
        termLinearLayout = findViewById(R.id.termLinearLayout);
        addTermButton = findViewById(R.id.addTermButton);
        modifyTermButton = findViewById(R.id.modifyTermButton);
        deleteTermButton = findViewById(R.id.deleteTermButton);
        backTermButton = findViewById(R.id.backTermButton);

        int selectedTerm = 0;

        for(int i = 0; i < 5; i++){
            final Button button = new Button(this);
            button.setText("Term " + i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setBackgroundColor(Color.GREEN);
                }
            });
            termLinearLayout.addView(button);
        }

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
                startActivity(new Intent(TermActivity.this, AddModifyTerm.class));
            }
        });

        backTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermActivity.this, MainActivity.class));
            }
        });

    }

}

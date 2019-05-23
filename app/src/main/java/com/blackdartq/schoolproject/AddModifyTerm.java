package com.blackdartq.schoolproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blackdartq.schoolproject.Utils.Term;

public class AddModifyTerm extends AppCompatActivity {

    DBUtils dbUtils;

    Button termSaveButton;
    Button termCancelButton;

    EditText termNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_term);
        dbUtils = new DBUtils();
        termSaveButton = findViewById(R.id.termSaveButton);
        termCancelButton = findViewById(R.id.termCancelButton);
        termNameEditText = findViewById(R.id.termNameEditText);

        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtils.addTerm(termNameEditText.getText().toString());
                startActivity(new Intent(AddModifyTerm.this, TermActivity.class));
            }
        });

        termCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddModifyTerm.this, TermActivity.class));
            }
        });
    }
}

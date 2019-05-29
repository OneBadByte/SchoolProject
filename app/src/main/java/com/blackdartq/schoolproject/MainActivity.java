package com.blackdartq.schoolproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackdartq.schoolproject.Utils.Term;

import java.util.ArrayList;


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


    private void sendMessage(String message){
        alertEditText.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViews

        // Buttons
        Button termButton = findViewById(R.id.termButton);
        Button coursesButton = findViewById(R.id.coursesButton);

        // Edit Text
        alertEditText = findViewById(R.id.alertEditText);

        // Layouts
        dbUtils = new DBUtils();
        ArrayList<Term> stuff = dbUtils.getTerms();
        dbUtils.addTerm("fuck this shit", "date('now')", "date('now')");
//        sendMessage(stuff.get(dbUtils.getIndexFromName("fuck this shit")).getName());
//        dbUtils.dropTables();

        //sendMessage(DBUtils.testDB());

        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TermActivity.class));
            }
        });

        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("Courses");
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

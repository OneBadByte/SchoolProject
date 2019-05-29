package com.blackdartq.schoolproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blackdartq.schoolproject.Utils.Term;
import com.blackdartq.schoolproject.Utils.Utils;

import java.util.ArrayList;

public class AddModifyTerm extends AppCompatActivity {

    DBUtils dbUtils;

    Button termSaveButton;
    Button termCancelButton;

    EditText termNameEditText;
    EditText startDateEditText;
    EditText endDateEditText;

    TextView messageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_term);
        dbUtils = new DBUtils();
        termSaveButton = findViewById(R.id.termSaveButton);
        termCancelButton = findViewById(R.id.termCancelButton);
        termNameEditText = findViewById(R.id.termNameEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        messageTextView = findViewById(R.id.messageTextView);

        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputFieldsAreValid()){
                    dbUtils.addTerm(
                            termNameEditText.getText().toString(),
                            startDateEditText.getText().toString(),
                            endDateEditText.getText().toString()
                    );
                    startActivity(new Intent(AddModifyTerm.this, TermActivity.class));
                }
            }
        });

        termCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}

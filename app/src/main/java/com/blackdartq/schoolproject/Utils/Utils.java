package com.blackdartq.schoolproject.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.blackdartq.schoolproject.AddModifyAssignment;

public class Utils {

    public static void changeBackgroundColorToRed(View view){
        view.setBackgroundColor(Color.RED);
    }

    public static void changeBackgroundColorToWhite(View view){
        view.setBackgroundColor(Color.WHITE);
    }

    public static void changeBackgroundOnClick(View view, int unclickedColor, int clickedColor){
        int color = ((ColorDrawable) view.getBackground()).getColor();
        System.out.println("Color: " + color + "  -  ClickedColor: "  + unclickedColor );
        if(color == unclickedColor){
            view.setBackgroundColor(clickedColor);
        }else{
            view.setBackgroundColor(unclickedColor);
        }
    }

    public static String getTextFromEditText(EditText editText){
        return editText.getText().toString();
    }
}




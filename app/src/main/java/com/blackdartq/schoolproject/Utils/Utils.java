package com.blackdartq.schoolproject.Utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;

public class Utils {

    public static void changeBackgroundColorToRed(View view){
        view.setBackgroundColor(Color.RED);
    }

    public static void changeBackgroundColorToWhite(View view){
        view.setBackgroundColor(Color.WHITE);
    }

    public static void changeBackgroundOnClick(View view, int unclickedColor, int clickedColor){
        int color = ((ColorDrawable) view.getBackground()).getColor();
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




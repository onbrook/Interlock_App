package com.example.owenslaptop.interlock_app;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

class methods {

    /*
      this is to be used with EditTexts and Spinners to check if they are valid
     */
    static boolean isViewValid(View view){
        if(view instanceof EditText){
            EditText editText = (EditText) view;
            return !editText.getText().toString().isEmpty();
        }else if(view instanceof Spinner){
            Spinner spinner = (Spinner) view;
            return spinner.getSelectedItemPosition() != 0;
        }else{
            System.err.println("ERROR: methods.isViewValid(View view) was called without being handed an EditText or a Spinner.");
            return false;
        }
    }

    /*
    these are to be handed a List of EditTexts and Spinners to check if they are valid
     */
    static List<View> getInvalidViews(View[] views){
        List<View> invalidViews = new ArrayList<>();
        for(View view:views)
            if(!isViewValid(view))
                invalidViews.add(view);
        return invalidViews;
    }

    static List<View> getValidViews(View[] views){
        List<View> validViews = new ArrayList<>();
        for(View view:views)
            if(isViewValid(view))
                validViews.add(view);
        return validViews;
    }

    static boolean areViewsValid(View[] views){
        for(View view:views)
            if(!isViewValid(view))
                return false;
        return true;
    }

    static void updateViewValidity(View[] views){
        System.out.println("updating view validity");
        List<View> invalidViews = getInvalidViews(views);
        List<View> validViews = getValidViews(views);
        for(View view:invalidViews)
            if(view instanceof EditText)
                view.setBackgroundResource(R.drawable.red_outline_edit_text);
            else if(view instanceof Spinner)
                view.setBackgroundResource(R.drawable.red_outline_spinner);
            else
                System.err.println("ERROR: methods.updateViewValidity was called without being handed a Spinner of an EditText");
        for(View view:validViews)
            if(view instanceof EditText)
                view.setBackgroundResource(R.drawable.no_outline_edit_text);
            else if(view instanceof Spinner)
                view.setBackgroundResource(R.drawable.no_outline_spinner);
            else
                System.err.println("ERROR: methods.updateViewValidity was called without being handed a Spinner of an EditText");

    }
}

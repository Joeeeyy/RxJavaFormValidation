package jjoey.com.rxvalidations;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by JosephJoey on 12/19/2017.
 */

public class Validations extends AppCompatActivity {

    public static Context context;

    public static boolean validateNumber(String num){
        return num.length() >= 10;
    }

    public static boolean validateName(String name){
        return name.matches("^[a-zA-Z]+[(?<=\\d\\s]([a-zA-Z]+\\s)*[a-zA-Z]+${5,40}");
    }

    public static boolean validateCollege(String college){
        return college.matches("^[a-zA-Z]+[(?<=\\d\\s]([a-zA-Z]+\\s)*[a-zA-Z]+${8,20}");
    }

    public static boolean validateOccupation(String occupation){
        return occupation.matches("[a-zA-Z]+[(?<=\\d\\s]([a-zA-Z]+\\s)*[a-zA-Z]+${5,20}");
    }

    public static void showNameError(TextInputLayout inputLayout){
        inputLayout.setError("Name is Invalid");
    }

    public static void hideNameError(TextInputLayout inputLayout){
        inputLayout.setError(null);
    }

    public static void showCollegeError(TextInputLayout inputLayout){
        if (inputLayout.getEditText().length() > 25){
            inputLayout.setError("Exceeded Character Limit");
        } else if (inputLayout.getEditText().length() < 25){
            inputLayout.setError("College is Invalid");
        }
    }

    public static void hideCollegeError(TextInputLayout inputLayout){
        inputLayout.setError(null);
    }

    public static void showOccupationError(TextInputLayout inputLayout){
        inputLayout.setError("Occupation is Invalid");
    }

    public static void hideOccupationError(TextInputLayout inputLayout){
        inputLayout.setError(null);
    }

    public static void showPhoneError(TextInputLayout inputLayout){
        inputLayout.setError("Phone is Invalid");
    }

    public static void hidePhoneError(TextInputLayout inputLayout){
        inputLayout.setError(null);
    }

}

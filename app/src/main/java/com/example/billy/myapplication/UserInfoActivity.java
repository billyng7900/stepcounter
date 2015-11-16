package com.example.billy.myapplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * Created by NgChau on 16/11/2015.
 */
public class UserInfoActivity extends Activity {
    EditText userName, heightInput, weightInput;
    TextView ageInput;
    Float height, weight;
    String name, gender;
    int age;
    SharedPreferences settings;
    AlertDialog alertDialog;
    AlertDialog.Builder adb;
    boolean isComplete;
    TextView alertText;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        userName = (EditText)findViewById(R.id.userNameInput);
        heightInput = (EditText)findViewById(R.id.userHeightInput);
        weightInput = (EditText)findViewById(R.id.userWeightInput);
        heightInput = (EditText) findViewById(R.id.userHeightInput);
        weightInput = (EditText) findViewById(R.id.userWeightInput);
        ageInput = (TextView)findViewById(R.id.userAgeInput);
        activity = this;
        alertText = new TextView(this);
        adb = new AlertDialog.Builder(this);
        adb.setView(alertText);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isComplete) {
                    alertText.setText("The information is not complete");
                } else {
                    name = userName.getText().toString();
                    height = Float.valueOf(heightInput.getText().toString());
                    weight = Float.valueOf(weightInput.getText().toString());
                    age = Integer.parseInt(ageInput.getText().toString());
                    settings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("name", name);
                    editor.putFloat("weight", weight);
                    editor.putFloat("height", height);
                    editor.putInt("age", age);
                    editor.putString("gender", gender);
                    editor.commit();
                    activity.finish();
                }
            }
        });
        alertDialog = adb.create();
    }

    public void saveOnClick (View view){

        String nameText = userName.getText().toString().trim();
        String heightText = heightInput.getText().toString().trim();
        String weightText = weightInput.getText().toString().trim();
        if(heightText.equals("")||weightText.equals("")||nameText.equals(""))
        {
            isComplete = false;
            alertText.setText("The information is not completed.");
        }
        else
        {
            isComplete = true;
            alertText.setText("The information is saved");
        }
        alertText.setGravity(Gravity.CENTER_HORIZONTAL);
        alertText.setTextSize(18);

        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public void selectGender(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.famaleRadioButton:
                if(checked){gender = "F";}
                else{gender ="M";}
                break;
            case R.id.maleRadioButton:
                if(checked){gender="M";}
                else{gender="F";}
                break;
        }
    }
}


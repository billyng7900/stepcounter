package com.example.billy.myapplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * Created by NgChau on 16/11/2015.
 */
public class UserInfoActivity extends Activity implements NumberPicker.OnValueChangeListener{
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
        ageInput.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showAge();
            }
        });
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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);

    }

    public void showAge()
    {

        final Dialog d = new Dialog(UserInfoActivity.this);
        d.setTitle("Age");
        d.setContentView(R.layout.ageselector);
        Button set = (Button) d.findViewById(R.id.setButton);
        Button cancel = (Button) d.findViewById(R.id.cancelButton);
        final NumberPicker picker = (NumberPicker) d.findViewById(R.id.agePicker);
        picker.setMaxValue(99);
        picker.setMinValue(12);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(this);
        set.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ageInput.setText(String.valueOf(picker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }
}


package com.example.billy.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ViewActivity extends Activity {

    TextView tv_name;
    TextView tv_age;
    TextView tv_gender;
    TextView tv_height;
    TextView tv_weight;
    Button save;
    float height;
    float weight;
    int age;
    String name;
    String gender;
    Boolean isComplete;
    TextView alertText;
    AlertDialog alertDialog;
    AlertDialog.Builder adb;
    Activity activity;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

       settings = getSharedPreferences("user_info", MODE_PRIVATE);

        name = settings.getString("name", "");
        age = settings.getInt("age", 1);
        height = settings.getFloat("height", 0.00f);
        weight = settings.getFloat("weight", 0.00f);
        gender = settings.getString("gender", "");

        tv_name = (EditText)findViewById(R.id.tv_name);
        tv_age = (EditText)findViewById(R.id.tv_age);
        tv_height = (EditText)findViewById(R.id.tv_height);
        tv_weight = (EditText)findViewById(R.id.tv_weight);
        save = (Button)findViewById(R.id.tv_save);
        RadioGroup rG = (RadioGroup)findViewById(R.id.genderRadioGroup);
        RadioButton maleButton = (RadioButton)findViewById(R.id.maleButton);
        RadioButton femaleButton = (RadioButton)findViewById(R.id.femaleButton);

        if(gender.equals("M"))
            maleButton.setChecked(true);
        else
            femaleButton.setChecked(true);
        tv_name.setText(name);
        tv_age.setText(String.valueOf(age));
        tv_height.setText(String.valueOf(height));
        tv_weight.setText(String.valueOf(weight));

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
                    name = tv_name.getText().toString();
                    age = Integer.parseInt(tv_age.getText().toString());
                    height = Float.valueOf(tv_height.getText().toString());
                    weight = Float.valueOf(tv_weight.getText().toString());

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

    public void onClickSave (View view){

        String nameText = tv_name.getText().toString().trim();
        String heightText = tv_height.getText().toString().trim();
        String weightText = tv_weight.getText().toString().trim();
        if(heightText.equals("")||weightText.equals("")||nameText.equals(""))
        {
            isComplete = false;
            alertText.setText("The information is not completed.");
        }
        else
        {
            isComplete = true;
            alertText.setText("The information is modified successfully");
        }
        alertText.setGravity(Gravity.CENTER_HORIZONTAL);
        alertText.setTextSize(18);

        alertDialog.show();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subclass, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

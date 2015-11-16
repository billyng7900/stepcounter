package com.example.billy.myapplication;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        userName = (EditText)findViewById(R.id.userNameInput);
        heightInput = (EditText)findViewById(R.id.userHeightInput);
        weightInput = (EditText)findViewById(R.id.userWeightInput);
        name = userName.getText().toString();
        heightInput = (EditText) findViewById(R.id.userHeightInput);
        height = Float.valueOf(heightInput.getText().toString());
        weightInput = (EditText) findViewById(R.id.userWeightInput);
        weight = Float.valueOf(weightInput.getText().toString());
        ageInput = (TextView)findViewById(R.id.userAgeInput);
        age = Integer.parseInt(ageInput.getText().toString());


    }

    public void saveOnClick (View view){
        settings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("name", name);
        editor.putFloat("weight", weight);
        editor.putFloat("height", height);
        editor.putInt("age",age);
        editor.putString("gender",gender);
        editor.commit();
        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
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


package com.example.billy.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class ViewActivity extends Activity {

    TextView tv_name, tv_age, tv_height, tv_weight, alertText;
    EditText et_alert;
    Switch s_status;
    Button save,alertButtonOk,alertButtonCancel;
    float height, weight;
    int age;
    String name, gender;
    Boolean isComplete, isStatusBar;
    AlertDialog alertDialog,alertDialogEdit;
    AlertDialog.Builder adb,adbEdit;
    Activity activity;
    SharedPreferences settings;
    int updateType;
    Button dialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences("user_info", MODE_PRIVATE);

        name = settings.getString("name", "");
        age = settings.getInt("age", 1);
        height = settings.getFloat("height", 0.00f);
        weight = settings.getFloat("weight", 0.00f);
        gender = settings.getString("gender", "");
        isStatusBar = settings.getBoolean("stautsBar", true);

        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_age = (TextView)findViewById(R.id.tv_age);
        tv_height = (TextView)findViewById(R.id.tv_height);
        tv_weight = (TextView)findViewById(R.id.tv_weight);
        s_status = (Switch)findViewById(R.id.switch_notic);
        save = (Button)findViewById(R.id.tv_save);
        RadioGroup rG = (RadioGroup)findViewById(R.id.genderRadioGroup);
        RadioButton maleButton = (RadioButton)findViewById(R.id.maleButton);
        RadioButton femaleButton = (RadioButton)findViewById(R.id.femaleButton);
        activity = this;
        if(gender.equals("M"))
            maleButton.setChecked(true);
        else
            femaleButton.setChecked(true);
        if(isStatusBar)
            s_status.setChecked(true);
        else
            s_status.setChecked(false);
        tv_name.setText(name);
        tv_age.setText(String.valueOf(age));
        tv_height.setText(String.valueOf(height));
        tv_weight.setText(String.valueOf(weight));
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit(v);
            }
        });
        tv_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit(v);
            }
        });
        tv_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit(v);
            }
        });

        tv_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAge();
            }
        });
        s_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isStatusBar = true;
                }else{
                    isStatusBar = false;
                }
            }
        });
        alertText = new TextView(this);
        adb = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_notify,null);
        adb.setView(dialogView);
        alertText = (TextView)dialogView.findViewById(R.id.text_message);
        dialogButton = (Button)dialogView.findViewById(R.id.button_dialog_confirm);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isComplete) {
                    name = tv_name.getText().toString();
                    height = Float.valueOf(tv_height.getText().toString());
                    weight = Float.valueOf(tv_weight.getText().toString());
                    age = Integer.parseInt(tv_age.getText().toString());
                    settings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("name", name);
                    editor.putFloat("weight", weight);
                    editor.putFloat("height", height);
                    editor.putInt("age", age);
                    editor.putString("gender", gender);
                    editor.putBoolean("stautsBar", isStatusBar);
                    editor.commit();
                    if(isStatusBar==false)
                    {
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.cancel(0);
                    }

                    SharedPreferences s = getSharedPreferences("fitness_plan", MODE_PRIVATE);
                    if (s.getFloat("height", 0.00f) != height || s.getFloat("weight", 0.00f) !=weight){
                        SharedPreferences.Editor e = s.edit();
                        FitnessPlan f = new FitnessPlan(height, weight);
                        if(s.getBoolean("planStarted", false) && f.getBMI()>21) {
                            e.putBoolean("planStarted", true);
                        }else if(s.getBoolean("planStarted", false) && f.getBMI()<=21) {
                            e.putFloat("height", 0.00f);
                            e.putFloat("weight", 0.00f);
                            e.putInt("targetTotal", 0);
                            e.putInt("targetStepDay", 6000);
                            e.putString("startDate", "Null");
                            e.putBoolean("planStarted", false);
                        }else {
                            e.putBoolean("planStarted", false);
                        }
                        e.commit();
                    }

                    alertDialog.dismiss();
                    activity.finish();
                } else {
                    alertDialog.dismiss();
                }
            }
        });
        adbEdit = new AlertDialog.Builder(this);
        LayoutInflater inflater1 = this.getLayoutInflater();
        View dialogView1 = inflater.inflate(R.layout.alertdialog_edittext, null);
        et_alert = (EditText)dialogView1.findViewById(R.id.text_edit);
        alertButtonOk = (Button)dialogView1.findViewById(R.id.button_ok);
        alertButtonCancel = (Button)dialogView1.findViewById(R.id.button_cancel);
        alertButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateType==0)
                    tv_name.setText(et_alert.getText().toString());
                else if(updateType == 1)
                    tv_height.setText(et_alert.getText().toString());
                else
                    tv_weight.setText(et_alert.getText().toString());
                alertDialogEdit.dismiss();
            }
        });
        alertButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogEdit.dismiss();
            }
        });
        adbEdit.setView(dialogView1);
        alertDialog = adb.create();
        alertDialogEdit = adbEdit.create();
    }
    public void onEdit(View v)
    {
        if(v.getId()==tv_name.getId())
        {
            updateType = 0;
            et_alert.setText(tv_name.getText().toString());
            et_alert.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            if(v.getId()==tv_height.getId()) {
                updateType = 1;
                et_alert.setText(tv_height.getText().toString());
            }
            else {
                updateType = 2;
                et_alert.setText(tv_weight.getText().toString());
                et_alert.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            }
        }
        et_alert.setSelection(et_alert.getText().length());
        alertDialogEdit.show();
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

    public void showAge()
    {

        final Dialog d = new Dialog(ViewActivity.this);
        d.setTitle("Edit Age");
        d.setContentView(R.layout.ageselector);
        Button set = (Button) d.findViewById(R.id.setButton);
        Button cancel = (Button) d.findViewById(R.id.cancelButton);
        final NumberPicker picker = (NumberPicker) d.findViewById(R.id.agePicker);
        picker.setValue(Integer.parseInt(tv_age.getText().toString()));
        picker.setMaxValue(99);
        picker.setMinValue(12);
        picker.setWrapSelectorWheel(false);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_age.setText(String.valueOf(picker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
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

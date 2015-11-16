package com.example.billy.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class ViewActivity extends Activity {

    TextView name;
    TextView age;
    TextView gender;
    TextView height;
    TextView weight;
    Button edit;
    ViewDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        name = (TextView)findViewById(R.id.tv_name);
        age = (TextView)findViewById(R.id.tv_age);
        gender = (TextView)findViewById(R.id.tv_gender);
        height = (TextView)findViewById(R.id.tv_height);
        weight = (TextView)findViewById(R.id.tv_weight);
        edit = (Button)findViewById(R.id.tv_edit);

        dbHelper = new ViewDBHelper(this);
        name.setText(dbHelper.getDBName(this));
        age.setText(String.valueOf(dbHelper.getDBAge(this)));
        gender.setText(dbHelper.getDBGender(this));
        height.setText(String.valueOf(dbHelper.getDBHeight(this)));
        weight.setText(String.valueOf(dbHelper.getDBWeight(this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

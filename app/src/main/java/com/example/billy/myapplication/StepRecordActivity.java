package com.example.billy.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class StepRecordActivity extends Activity {
    ArrayList<Step> stepList;
    StepDbHelper dbHelper;
    StepListAdapter adapter;
    ListView listView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.menu_step_record,menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = minfo.position;
        switch (id) {
            case R.id.action_view_detail:
                Step stepRecord = stepList.get(pos);
                Intent i = new Intent(this,ViewStepDetail.class);
                i.putExtra("stepDate", stepRecord.getDate());
                i.putExtra("stepRecord",stepRecord.getStep());
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_record);
        ActionBar ab = getActionBar();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        stepList = new ArrayList<>();
        dbHelper = new StepDbHelper(this);
        stepList = dbHelper.getAllStepRecord(this);
        Collections.sort(stepList, new Comparator<Step>() {
            @Override
            public int compare(Step lhs, Step rhs) {
                int result =  (lhs).getConvertToDate().compareTo(rhs.getConvertToDate());
                if(result == 1)
                    result = -1;
                else if(result == -1)
                    result = 1;
                return result;
            }
        });
        adapter = new StepListAdapter(this,stepList);
        listView = (ListView) findViewById(R.id.list_step);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
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

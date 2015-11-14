package com.example.billy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Billy on 15/11/2015.
 */
public class StepListAdapter extends BaseAdapter {
    ArrayList<Step> stepList;
    LayoutInflater layoutInflater;

    private static class ViewHolder
    {
        TextView date;
        TextView step;
    }

    public StepListAdapter(Context context,ArrayList<Step> stepList)
    {
        this.stepList = stepList;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return stepList.size();
    }

    @Override
    public Object getItem(int position) {
        return stepList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView = layoutInflater.inflate(R.layout.step_row,null);
            holder = new ViewHolder();
            holder.date = (TextView)convertView.findViewById(R.id.text_date);
            holder.step = (TextView)convertView.findViewById(R.id.text_step);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.date.setText(stepList.get(position).getDate());
        holder.step.setText(String.valueOf(stepList.get(position).getStep()));
        return convertView;
    }
}

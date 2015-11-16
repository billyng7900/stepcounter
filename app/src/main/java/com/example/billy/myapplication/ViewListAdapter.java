package com.example.billy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewListAdapter extends BaseAdapter {

    ArrayList<ViewUserData> userData;
    LayoutInflater layoutInflater;
    static class ViewHolder{
        TextView name;
        TextView age;
        RadioGroup gender;
        TextView height;
        TextView weight;
    }
    public ViewListAdapter(Context context,ArrayList<ViewUserData> userData){
        this.userData = userData;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userData.size();
    }

    @Override
    public Object getItem(int position) {
        return userData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.activity_view,null);
            holder=new ViewHolder();
            holder.name=(TextView)convertView.findViewById(R.id.tv_name);
            holder.age=(TextView)convertView.findViewById(R.id.tv_age);
            holder.gender=(RadioGroup)convertView.findViewById(R.id.genderRadioGroup);
            holder.height=(TextView)convertView.findViewById(R.id.tv_height);
            holder.weight=(TextView)convertView.findViewById(R.id.tv_weight);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.name.setText(userData.get(position).getName());
        holder.age.setText(String.valueOf(userData.get(position).getAge()));
        holder.height.setText(String.valueOf(userData.get(position).getHeight()));
        holder.weight.setText(String.valueOf(userData.get(position).getWeight()));
        return convertView;
    }
}

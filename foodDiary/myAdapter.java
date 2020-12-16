package com.simple.foodDiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;


public class myAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List list;

    public myAdapter(@NonNull Context context, List list) {
    this.context=context;
    this.list=list;
    }

    /*public myAdapter(Context c,List list){
        context = c;
        this.list = list;
    }*/


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)convertView = inflater.inflate(R.layout.adapter_item,null);

        TextView textView = convertView.findViewById(R.id.textview_adapterItem);

        textView.setText((String)list.get(position));

        return convertView;
    }
}

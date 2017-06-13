package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marcoli on 27.04.2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

   public NewsAdapter(Context context, ArrayList<News> objects){
        super(context,0,objects);
    }

    @NonNull
    @Override
    public View getView (int postion, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        News currentAdapter = getItem(postion);

        //get Header
        TextView header = (TextView) convertView.findViewById(R.id.header);
        header.setText(currentAdapter.getmHeader());
        //get text
        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(currentAdapter.getmText());



        return convertView;
    }

}

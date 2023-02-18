package com.example.sports_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.entities.Thread;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ThreadListAdapter extends ArrayAdapter<Thread> implements View.OnClickListener {

    private ArrayList<Thread> threadList;
    Context mContext;

    private static class ViewHolder {
        TextView txtSport;
        TextView txtHeader;
        TextView txtUsername;
        TextView txtDate;
    }

    public ThreadListAdapter(ArrayList<Thread> threads, @NonNull Context context) {
        super(context, R.layout.thread_list_row_item, threads);
        this.threadList = threads;
        this.mContext = context;
    }

    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        Thread thread = (Thread)obj;

        switch (v.getId())
        {
            case R.id.thread_header:
                Snackbar.make(v, "Þú smelltir á þráð!", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    public View getView(int position, View convertView, ViewGroup container) {
        Thread thread = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater i = LayoutInflater.from(getContext());
            convertView = i.inflate(R.layout.thread_list_row_item, container, false);
            viewHolder.txtSport = (TextView) convertView.findViewById(R.id.thread_sport);
            viewHolder.txtHeader = (TextView) convertView.findViewById(R.id.thread_header);
            viewHolder.txtUsername = (TextView) convertView.findViewById(R.id.thread_username);
            viewHolder.txtDate = (TextView) convertView.findViewById((R.id.thread_date));

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtSport.setText(thread.getmSport());
        viewHolder.txtHeader.setText(thread.getmHeader());
        viewHolder.txtUsername.setText(thread.getmUsername());
        viewHolder.txtHeader.setOnClickListener(this);

        // TODO: Útfæra localDate, skoða Listener + fullt af öðru sem þarf að skoða hér.
        viewHolder.txtHeader.setTag(position);
        return convertView;
    }
}

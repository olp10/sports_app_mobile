package com.example.sports_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.entities.Thread;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * ThreadListAdapter retrieves a collection of Thread entities and creates a custom view for
 * each Thread in the collection. To do this ThreadListAdapter extends the ArrayAdapter class
 * and overrides its getView method.
 */
public class ThreadListAdapter extends ArrayAdapter<Thread> implements View.OnClickListener {

    private ArrayList<Thread> threadList;
    Context mContext;
    private int lastPosition = -1;

    // The content of the view to be displayed
    private static class ViewHolder {
        TextView txtSport;
        TextView txtHeader;
        TextView txtUsername;
        TextView txtDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        Thread thread = getItem(position);
        ViewHolder viewHolder;
        final View result;

        // Check whether a view object can be reused to improve performance.
        // If not, generate a new view object at this position.
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

        viewHolder.txtSport.setText(thread.getSport());
        viewHolder.txtHeader.setText(thread.getHeader());
        viewHolder.txtUsername.setText(thread.getUsername());
        viewHolder.txtHeader.setOnClickListener(this);

        // TODO: ??tf??ra localDate, sko??a Listener + fullt af ????ru sem ??arf a?? sko??a h??r.
        viewHolder.txtHeader.setTag(position);
        return convertView;
    }

    public ThreadListAdapter(ArrayList<Thread> threads, @NonNull Context context) {
        super(context, R.layout.thread_list_row_item, threads);
        this.threadList = threads;
        this.mContext = context;
    }

    // ??ESSI "EVENT HANDLER" ??TF??RIR S??RSTAKT VI??BRAG?? VI?? A?? KLIKKA ?? ??KVE??INN HLUTA AF ??R????INUM
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        Thread thread = (Thread)obj;

        switch (v.getId())
        {
            // TODO: ??nnur case ef mismunandi vi??brag?? vi?? a?? klikka ?? date e??a username t.d.
            case R.id.thread_header:
                Snackbar.make(v, "???? smelltir ?? ??r????!", Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

}

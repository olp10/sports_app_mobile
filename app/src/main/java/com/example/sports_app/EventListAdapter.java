package com.example.sports_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.entities.Event;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> implements View.OnClickListener {
    private ArrayList<Event> eventList;
    Context mContext;
    private int lastPosition = -1;

    // The content of the view to be displayed
    private static class ViewHolder {
        TextView txtSport;
        TextView txtName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        Event event = getItem(position);
        EventListAdapter.ViewHolder viewHolder;
        final View result;

        // Check whether a view object can be reused to improve performance.
        // If not, generate a new view object at this position.
        if (convertView == null) {
            viewHolder = new EventListAdapter.ViewHolder();
            LayoutInflater i = LayoutInflater.from(getContext());
            convertView = i.inflate(R.layout.events_list_row_item, container, false);
            viewHolder.txtSport = (TextView) convertView.findViewById(R.id.event_sport);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.event_name);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EventListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtSport.setText(event.getSport());
        viewHolder.txtName.setText(event.getEventName());

        viewHolder.txtName.setTag(position);
        return convertView;
    }

    public EventListAdapter(ArrayList<Event> events, @NonNull Context context) {
        super(context, R.layout.events_list_row_item, events);
        this.eventList = events;
        this.mContext = context;
    }

    // ??ESSI "EVENT HANDLER" ??TF??RIR S??RSTAKT VI??BRAG?? VI?? A?? KLIKKA ?? ??KVE??INN HLUTA AF ??R????INUM
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        Event event = (Event)obj;
    }
}

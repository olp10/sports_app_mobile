package com.example.sports_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.R;
import com.example.sports_app.entities.Event;
import com.example.sports_app.util.HelperMethods;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> implements View.OnClickListener {
    private ArrayList<Event> eventList;
    Context mContext;
    private int lastPosition = -1;

    // The content of the view to be displayed
    private static class ViewHolder {
        TextView txtSport;
        TextView txtName;
        TextView txtDate;
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
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.event_shortDate);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EventListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtSport.setText(HelperMethods.firstLetterUppercase(event.getSport()));
        viewHolder.txtName.setText(HelperMethods.firstLetterUppercase(event.getEventName()));
        viewHolder.txtDate.setText(event.getFormattedDate());

        viewHolder.txtName.setTag(position);
        return convertView;
    }

    public EventListAdapter(ArrayList<Event> events, @NonNull Context context) {
        super(context, R.layout.events_list_row_item, events);
        this.eventList = events;
        this.mContext = context;
    }

    // ÞESSI "EVENT HANDLER" ÚTFÆRIR SÉRSTAKT VIÐBRAGÐ VIÐ AÐ KLIKKA Á ÁKVEÐINN HLUTA AF ÞRÆÐINUM
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        Event event = (Event)obj;
    }
}

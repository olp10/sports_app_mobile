package com.example.sports_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.R;

import java.util.ArrayList;

public class SportListAdapter extends ArrayAdapter<String> implements View.OnClickListener {
    private ArrayList<String> sportList;
    Context mContext;
    private int lastPosition = -1;

    // The content of the view to be displayed
    private static class ViewHolder {
        TextView txtSport;
        TextView txtName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        String sport = getItem(position);
        SportListAdapter.ViewHolder viewHolder;
        final View result;

        // Check whether a view object can be reused to improve performance.
        // If not, generate a new view object at this position.
        if (convertView == null) {
            viewHolder = new SportListAdapter.ViewHolder();
            LayoutInflater i = LayoutInflater.from(getContext());
            convertView = i.inflate(R.layout.sports_list_row_item, container, false);
            viewHolder.txtSport = (TextView) convertView.findViewById(R.id.sport_name);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SportListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtSport.setText(sport);
        return convertView;
    }

    public SportListAdapter(ArrayList<String> sports, @NonNull Context context) {
        super(context, R.layout.sports_list_row_item, sports);
        this.sportList = sports;
        this.mContext = context;
    }

    // ÞESSI "EVENT HANDLER" ÚTFÆRIR SÉRSTAKT VIÐBRAGÐ VIÐ AÐ KLIKKA Á ÁKVEÐINN HLUTA AF ÞRÆÐINUM
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        String sport = (String)obj;
    }
}

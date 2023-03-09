package com.example.sports_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.entities.Club;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ClubListAdapter extends ArrayAdapter<Club> implements View.OnClickListener {
    private ArrayList<Club> clubList;
    Context mContext;
    private int lastPosition = -1;

    // The content of the view to be displayed
    private static class ViewHolder {
        TextView txtSport;
        TextView txtName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        Club club = getItem(position);
        ClubListAdapter.ViewHolder viewHolder;
        final View result;

        // Check whether a view object can be reused to improve performance.
        // If not, generate a new view object at this position.
        if (convertView == null) {
            viewHolder = new ClubListAdapter.ViewHolder();
            LayoutInflater i = LayoutInflater.from(getContext());
            convertView = i.inflate(R.layout.clubs_list_row_item, container, false);
            viewHolder.txtSport = (TextView) convertView.findViewById(R.id.club_sport);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.club_name);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ClubListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtSport.setText(club.getmSport());
        viewHolder.txtName.setText(club.getmClubName());

        viewHolder.txtName.setTag(position);
        return convertView;
    }

    public ClubListAdapter(ArrayList<Club> clubs, @NonNull Context context) {
        super(context, R.layout.clubs_list_row_item, clubs);
        this.clubList = clubs;
        this.mContext = context;
    }

    // ÞESSI "EVENT HANDLER" ÚTFÆRIR SÉRSTAKT VIÐBRAGÐ VIÐ AÐ KLIKKA Á ÁKVEÐINN HLUTA AF ÞRÆÐINUM
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        Club club = (Club) obj;
    }
}

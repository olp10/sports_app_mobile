package com.example.sports_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.entities.Comment;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * CommentListAdapter retrieves a collection of Comment entities and creates a custom view for
 * each Comment in the collection. To do this CommentListAdapter extends the ArrayAdapter class
 * and overrides its getView method.
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {

    private ArrayList<Comment> mComments;
    Context mContext;
    private int lastPosition = -1;

    public CommentListAdapter(@NonNull Context context, @NonNull ArrayList<Comment> comments) {
        super(context, R.layout.comment_list_row_item, comments);
        this.mComments = comments;
        this.mContext = context;
    }

    private static class ViewHolder {
        TextView txtUsername;
        TextView txtDate;
        TextView txtBody;
    }

    public View getView(int position, View convertView, ViewGroup container) {
        Comment comment = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater i = LayoutInflater.from(getContext());
            convertView = i.inflate(R.layout.comment_list_row_item, container, false);
            viewHolder.txtUsername = (TextView) convertView.findViewById(R.id.comment_username);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.comment_date);
            viewHolder.txtBody = (TextView) convertView.findViewById(R.id.comment_body);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        if (comment != null) {
            Log.d("CommentListAdapter", comment.getTimeCommented());
            viewHolder.txtUsername.setText(comment.getUser());
            viewHolder.txtBody.setText(comment.getComment());
            viewHolder.txtDate.setText(comment.getFormattedDate());
        }

        // TODO: Útfæra localDate, skoða Listener + fullt af öðru sem þarf að skoða hér.
        viewHolder.txtBody.setTag(position);
        return convertView;
    }
}

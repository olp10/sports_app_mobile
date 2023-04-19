package com.example.sports_app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sports_app.R;
import com.example.sports_app.entities.Comment;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.google.android.material.snackbar.Snackbar;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * CommentListAdapter retrieves a collection of Comment entities and creates a custom view for
 * each Comment in the collection. To do this CommentListAdapter extends the ArrayAdapter class
 * and overrides its getView method.
 */
public class CommentListAdapter extends ArrayAdapter<Comment> implements View.OnClickListener {
    private static final String EXTRA_USER = "com.example.sports_app.username";
    private ArrayList<Comment> mComments;
    Context mContext;
    private int lastPosition = -1;
    boolean isAdmin;

    private String loggedInUser;

    public CommentListAdapter(@NonNull Context context, @NonNull ArrayList<Comment> comments, boolean isAdmin) {
        super(context, R.layout.comment_list_row_item, comments);
        this.mComments = comments;
        this.mContext = context;
        this.isAdmin = isAdmin;
    }

    private static class ViewHolder {
        TextView txtUsername;
        TextView txtDate;
        TextView txtBody;
        Button mBtnDeleteComment;
    }

    private void deleteComment(long id) {
        mComments.remove(id);
        notifyDataSetChanged();
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
            viewHolder.mBtnDeleteComment = (Button) convertView.findViewById(R.id.button_delete_comment);
            if (!isAdmin) {
                viewHolder.mBtnDeleteComment.setVisibility(View.GONE);
            }
            viewHolder.mBtnDeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetworkManager networkManager = NetworkManager.getInstance(getContext());
                    networkManager.deleteComment(comment.getId(), new NetworkCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            //deleteComment(comment.getId());
                            System.out.println("On success: " + comment.getId());
                            deleteComment(comment.getId());
                            // FIXME: Reload UI - Er búinn að reyna eitthvað smá en lendi alltaf á vegg
                        }

                        @Override
                        public void onFailure(String errorString) {
                            System.out.println("Failed to delete comment with id: " + comment.getId());
                            Snackbar.make(v, errorString, Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            });

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


    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object obj = getItem(position);
        ViewHolder viewHolder = (ViewHolder) v.getTag();


    }
}

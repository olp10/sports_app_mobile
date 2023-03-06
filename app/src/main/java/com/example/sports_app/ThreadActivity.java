package com.example.sports_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ThreadActivity extends AppCompatActivity {
    private final String TAG = "ThreadActivity";
    private Thread mThread;
    private ArrayList<Comment> mComments;
    private ThreadService mThreadService;
    private static CommentListAdapter sCommentListAdapter;
    ListView mCommentList;
    TextView mThreadCreator;
    TextView mThreadSport;
    TextView mThreadHeader;
    TextView mThreadBody;
    EditText mNewCommentText;
    Button mNewCommentButton;

    private static final String EXTRA_THREAD_ID = "com.example.sports_app.thread_id";
    private static final String EXTRA_THREAD_SPORT = "com.example.sports_app.thread_sport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        // Sækja þráð gegnum ThreadService og commentin
        mThreadService = new ThreadService();
        long thisThreadId = getIntent().getLongExtra(EXTRA_THREAD_ID, 0);
        String thisThreadSport = getIntent().getStringExtra(EXTRA_THREAD_SPORT);

        NetworkManager sNetworkManager = NetworkManager.getInstance(ThreadActivity.this);
        sNetworkManager.getThreadById(thisThreadId, thisThreadSport, new NetworkCallback() {

            @Override
            public void onSuccess(Object result) {
                mThread = (Thread) result;
                // Setja inn texta (og önnur gögn) þráðar
                mThreadCreator = (TextView) findViewById(R.id.thread_creator);
                mThreadSport = (TextView) findViewById(R.id.thread_section);
                mThreadHeader = (TextView) findViewById(R.id.thread_head);
                mThreadBody = (TextView) findViewById(R.id.thread_body);
                mThreadCreator.setText(mThread.getUsername());
                mThreadSport.setText(mThread.getSport());
                mThreadHeader.setText(mThread.getHeader());
                mThreadBody.setText(mThread.getBody());

                addCommentsToThreadList(thisThreadId, thisThreadSport);
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, errorString);
            }
        });

        // Sækja texta og búa til nýtt comment
        // TODO: User þarf að vera logged. Búa til nýtt comment og adda í þráð!
        mNewCommentText = (EditText) findViewById(R.id.newComment_text);
        mNewCommentButton = (Button) findViewById(R.id.newComment_button);
        mNewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCommentBody = String.valueOf(mNewCommentText.getText());
            }
        });
    }

    public void addCommentsToThreadList(long threadId, String sport) {
        NetworkManager sNetworkManager = NetworkManager.getInstance(ThreadActivity.this);
        sNetworkManager.getAllCommentsForThread(threadId, sport, new NetworkCallback<ArrayList<Comment>>() {
            @Override
            public void onSuccess(ArrayList<Comment> comments) {
                mComments = comments;
                sCommentListAdapter = new CommentListAdapter(ThreadActivity.this, mComments);
                mCommentList = findViewById(R.id.commentList);
                mCommentList.setAdapter(sCommentListAdapter);
            }

            @Override
            public void onFailure(String errorString) {
                System.out.println("Failed to get comments");
            }
        });
    }

    public static Intent newIntent(Context packageContext, long threadId, String sport) {
        Intent i = new Intent(packageContext, ThreadActivity.class);
        i.putExtra(EXTRA_THREAD_ID, threadId);
        i.putExtra(EXTRA_THREAD_SPORT, sport);
        return i;
    }
}
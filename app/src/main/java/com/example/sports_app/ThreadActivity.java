package com.example.sports_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.services.ThreadService;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ThreadActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        // Sækja þráð gegnum ThreadService og commentin
        mThreadService = new ThreadService();
        long thisThreadId = getIntent().getLongExtra(EXTRA_THREAD_ID, 0);
        mThread = mThreadService.findThreadById(thisThreadId);
        mComments = mThread.getComments();

        // Setja inn texta (og önnur gögn) þráðar
        mThreadCreator = (TextView) findViewById(R.id.thread_creator);
        mThreadSport = (TextView) findViewById(R.id.thread_section);
        mThreadHeader = (TextView) findViewById(R.id.thread_head);
        mThreadBody = (TextView) findViewById(R.id.thread_body);
        mThreadCreator.setText(mThread.getUsername());
        mThreadSport.setText(mThread.getSport());
        mThreadHeader.setText(mThread.getHeader());
        mThreadBody.setText(mThread.getBody());

        // Smíða layout element fyrir comments
        sCommentListAdapter = new CommentListAdapter(getApplicationContext(), mComments);
        mCommentList = (ListView) findViewById(R.id.commentList);
        mCommentList.setAdapter(sCommentListAdapter);

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

    public static Intent newIntent(Context packageContext, long threadId) {
        Intent i = new Intent(packageContext, ThreadActivity.class);
        i.putExtra(EXTRA_THREAD_ID, threadId);
        return i;
    }
}
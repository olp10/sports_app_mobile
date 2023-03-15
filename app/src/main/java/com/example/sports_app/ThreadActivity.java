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
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = "ThreadActivity";
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

        mThreadCreator = (TextView) findViewById(R.id.thread_creator);
        mThreadSport = (TextView) findViewById(R.id.thread_section);
        mThreadHeader = (TextView) findViewById(R.id.thread_head);
        mThreadBody = (TextView) findViewById(R.id.thread_body);

        // TODO: User þarf að vera logged til að commenta!
        mNewCommentText = (EditText) findViewById(R.id.newComment_text);
        mNewCommentButton = (Button) findViewById(R.id.newComment_button);

        boolean loggedIn = getIntent().getExtras().getBoolean("com.example.sports_app.loggedIn");

        if (!loggedIn) {
            mNewCommentButton.setVisibility(View.GONE);
            mNewCommentText.setVisibility(View.GONE);
        }


        // Sækja þráð gegnum ThreadService og commentin
//        mThreadService = new ThreadService();
        long thisThreadId = getIntent().getLongExtra(EXTRA_THREAD_ID, 0);
        getThreadById(thisThreadId);

        mNewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                User fakeUser = new User("FakeCommentUser", "abc", false);
                String newCommentBody = String.valueOf(mNewCommentText.getText());
//                newComment newComment = new newComment(fakeUser, newCommentBody, mThread);
                NetworkManager networkManager = NetworkManager.getInstance(ThreadActivity.this);
                networkManager.postNewComment(1L, newCommentBody, mThread.getId(), new NetworkCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        mNewCommentText.setText("");
                    }

                    @Override
                    public void onFailure(String errorString) {
                        Log.e(TAG, "POST request failed.");
                    }
                });
            }
        });
    }

//    private class newComment {
//        public User mUser;
//        public String commentBody;
//        public Thread mThread;
//
//        public newComment(User user, String body, Thread thread) {
//            mUser = user;
//            commentBody = body;
//            mThread = thread;
//        }
//    }

    private void getThreadById(Long id) {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getThreadById(id, new NetworkCallback<Thread>() {
            @Override
            public void onSuccess(Thread result) {
                mThread = result;
                if (mThread != null) {
                    mComments = mThread.getComments();
                    populateUI();
                } else {
                    mComments = new ArrayList<Comment>();
                }
            }
            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get thread by id via REST");
            }
        });
    }

    private void populateUI() {
        // Setja inn texta (og önnur gögn) þráðar
        mThreadCreator.setText(mThread.getUsername());
        mThreadSport.setText(mThread.getSport());
        mThreadHeader.setText(mThread.getHeader());
        mThreadBody.setText(mThread.getBody());

        // Athuga hvort user sé loggaður inn/admin og senda inn í commentlistadapter til að gefa
        // sýna admin takka/actions.
        boolean isAdmin;
        try {
            isAdmin = getIntent().getExtras().getBoolean("com.example.sports_app.isAdmin");
        } catch (Exception e) {
            isAdmin = false;
        }

        // Smíða layout element fyrir comments
        sCommentListAdapter = new CommentListAdapter(getApplicationContext(), mComments, isAdmin);
        mCommentList = (ListView) findViewById(R.id.commentList);
        mCommentList.setAdapter(sCommentListAdapter);
    }

    public static Intent newIntent(Context packageContext, long threadId) {
        Intent i = new Intent(packageContext, ThreadActivity.class);
        i.putExtra(EXTRA_THREAD_ID, threadId);
        return i;
    }
}
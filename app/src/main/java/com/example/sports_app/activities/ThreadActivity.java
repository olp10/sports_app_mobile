package com.example.sports_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sports_app.R;
import com.example.sports_app.adapters.CommentListAdapter;
import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = "ThreadActivity";
    private static final String EXTRA_USER = "com.example.sports_app.username";
    private Thread mThread;
    private ArrayList<Comment> mComments;
    private ThreadService mThreadService;
    private static CommentListAdapter sCommentListAdapter;
    ListView mCommentList;
    TextView mThreadCreator;
    TextView mThreadSport;
    TextView mThreadHeader;
    TextView mThreadBody;
    private String loggedInUser;
    Button mNewCommentButton;
    String loggedIn;
    boolean isAdmin;
    private TextInputLayout mNewCommentTextLayout;
    EditText mNewCommentText;
    long userId;

    private static final String EXTRA_THREAD_ID = "com.example.sports_app.thread_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        mThreadCreator = (TextView) findViewById(R.id.thread_creator);
        mThreadSport = (TextView) findViewById(R.id.thread_section);
        mThreadHeader = (TextView) findViewById(R.id.thread_head);
        mThreadBody = (TextView) findViewById(R.id.thread_body);

        addCommentSection();
    }

    public void addCommentSection() {
        // TODO: User þarf að vera logged til að commenta!
        mNewCommentText = (EditText) findViewById(R.id.newComment_text);
        mNewCommentTextLayout = (TextInputLayout) findViewById(R.id.new_comment_input_layout);
        mNewCommentTextLayout.getEditText().addTextChangedListener(commentTextHandler());

        mNewCommentButton = (Button) findViewById(R.id.newComment_button);


        loggedIn = getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getString("logged_in_user", null);

        if (loggedIn == null || loggedIn == "") {
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
                String newCommentBody = String.valueOf(mNewCommentText.getText());
                NetworkManager networkManager = NetworkManager.getInstance(ThreadActivity.this);

                networkManager.postNewComment(loggedIn, newCommentBody, mThread.getId(), new NetworkCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        finish();
                        startActivity(getIntent().putExtra(EXTRA_THREAD_ID, mThread.getId()));
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

        // Athuga hvort user sé loggaður inn/admin og senda inn í commentlistadapter til að
        // sýna admin takka/actions.

        try {
            loggedInUser = getIntent().getStringExtra(EXTRA_USER);
        } catch (Exception e) {
            loggedInUser = "";
        }

        try {
            isAdmin = getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getBoolean("isAdmin", false);
        } catch (Exception e) {
            isAdmin = false;
        }
        mThreadCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThreadActivity.this, UserProfileActivity.class);
                intent.putExtra("com.example.sports_app.userClicked", mThread.getUsername());
                intent.putExtra("com.example.sports_app.loggedIn", loggedIn);
                intent.putExtra("com.example.sports_app.isAdmin", isAdmin);
                startActivity(intent);
            }
        });

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

    private TextWatcher commentTextHandler() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mNewCommentTextLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}
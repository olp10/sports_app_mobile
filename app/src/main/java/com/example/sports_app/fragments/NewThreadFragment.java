package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.sports_app.R;
import com.example.sports_app.activities.SportActivity;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

public class NewThreadFragment extends Fragment {
    private static final String TAG = "NewThreadFragment";
    private Button mSubmitNewThreadButton;
    private EditText mNewThreadTitleText;
    private EditText mNewThreadBodyText;
    private NetworkManager mNetworkManager = NetworkManager.getInstance(getContext());
    private static final String EXTRA_SPORT_NAME = "com.example.sports_app.sport_name";
    private static final String EXTRA_USERNAME = "com.example.sports_app.username";
    private static NewThreadFragment sNewThreadFragment;

    public static NewThreadFragment newInstance(String param1, String param2) {
        NewThreadFragment fragment = new NewThreadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_thread, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mNewThreadTitleText = (EditText) view.findViewById(R.id.new_thread_title);
        mNewThreadBodyText = (EditText) view.findViewById(R.id.new_thread_body);

        String user = getActivity().getSharedPreferences("com.example.sports_app", 0).getString("logged_in_user", "");

        mSubmitNewThreadButton = (Button) view.findViewById(R.id.new_thread_submit_button);
        mSubmitNewThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mNewThreadTitleText.getText().toString();
                String body = mNewThreadBodyText.getText().toString();
                Thread thread = new Thread();
                thread.setBody(body);
                thread.setHeader(title);
                thread.setUsername(user);
                thread.setSport(getActivity()
                        .getIntent()
                        .getExtras()
                        .getString(EXTRA_SPORT_NAME));
                saveThread(thread);
                Intent i = new Intent(getActivity(), SportActivity.class);
                i.putExtra(EXTRA_SPORT_NAME, getActivity().getIntent().getExtras().getString(EXTRA_SPORT_NAME));
                i.putExtra(EXTRA_USERNAME, getActivity().getIntent().getExtras().getString(EXTRA_USERNAME));
                i.putExtra("com.example.sports_app.loggedIn"
                        ,getActivity()
                        .getIntent()
                        .getExtras()
                        .getBoolean("com.example.sports_app.loggedIn"));
                startActivity(i);
            }
        });
    }

    private void saveThread(Thread thread) {
        mNetworkManager.saveThread(thread, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG, "Thread saved successfully");
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to save thread: " + errorString);
            }
        });
    }
}

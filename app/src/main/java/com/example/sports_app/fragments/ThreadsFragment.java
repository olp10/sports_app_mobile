package com.example.sports_app.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.sports_app.R;
import com.example.sports_app.activities.ThreadActivity;
import com.example.sports_app.adapters.ThreadListAdapter;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.entities.Thread;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThreadsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThreadsFragment extends Fragment {
    private static final String EXTRA_SPORT_NAME = "com.example.sports_app.sport_name";
    private static final String EXTRA_USERNAME = "com.example.sports_app.username";
    private final String TAG = "ThreadsFragment";
    private static ThreadListAdapter sThreadListAdapter;
    private ListView mListView;
    private List<Thread> mThreads;
    private Button mNewThreadButton;
    private FragmentContainerView mFragmentContainerView;
    String username;

    public ThreadsFragment() {
        // Required empty public constructor
    }

    public static ThreadsFragment newInstance(String param1, String param2) {
        ThreadsFragment fragment = new ThreadsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_threads, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_of_threads);
        getAllThreadsBySport();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long threadToOpenId = mThreads.get(i).getId();
                boolean loggedIn = getActivity().getIntent().getExtras().getBoolean("com.example.sports_app.loggedIn");
                String sport = mThreads.get(i).getSport();
                Intent intent = ThreadActivity.newIntent(getActivity(), threadToOpenId);
                intent.putExtra("com.example.sports_app.loggedIn", loggedIn);
                intent.putExtra(EXTRA_USERNAME, getActivity().getIntent().getExtras().getString(EXTRA_USERNAME));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mFragmentContainerView = (FragmentContainerView) getActivity().findViewById(R.id.fragmentContainerView);
        mNewThreadButton = (Button) view.findViewById(R.id.new_thread_button);
        String loggedInUser = getActivity().getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getString("logged_in_user", null);
        if (loggedInUser != null) {
            mNewThreadButton.setVisibility(View.VISIBLE);
        }
        mNewThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentContainerView.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, NewThreadFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
    }

    private void getAllThreadsBySport() {

        NetworkManager sNetworkManager = NetworkManager.getInstance(getContext());
        String sport = getActivity().getIntent().getExtras().getString(EXTRA_SPORT_NAME);
        sNetworkManager.getAllThreadsForSport(sport, new NetworkCallback<ArrayList<Thread>>() {
            @Override
            public void onSuccess(ArrayList<Thread> result) {
                ArrayList<Thread> threads = result;
                mThreads = threads;

                if (threads != null) {
                    sThreadListAdapter = new ThreadListAdapter(threads, getActivity().getApplicationContext());
                    mListView.setAdapter(sThreadListAdapter);
                }
            }

            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "onFailure: " + errorString);
            }
        });
    }
}
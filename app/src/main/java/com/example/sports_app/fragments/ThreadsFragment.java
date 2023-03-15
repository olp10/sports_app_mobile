package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sports_app.EventListAdapter;
import com.example.sports_app.MainActivity;
import com.example.sports_app.R;
import com.example.sports_app.ThreadActivity;
import com.example.sports_app.ThreadListAdapter;
import com.example.sports_app.entities.Event;
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
    private final String TAG = "ThreadsFragment";
    private static ThreadListAdapter sThreadListAdapter;
    private ListView mListView;

    private List<Thread> mThreads;

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
                System.out.println(sport);
                Intent intent = ThreadActivity.newIntent(getActivity(), threadToOpenId);
                intent.putExtra("com.example.sports_app.loggedIn", loggedIn);
                startActivity(intent);
            }
        });
        return rootView;
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
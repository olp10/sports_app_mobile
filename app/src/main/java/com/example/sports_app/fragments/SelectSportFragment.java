package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.sports_app.R;
import com.example.sports_app.activities.SportActivity;
import com.example.sports_app.adapters.SportListAdapter;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.util.ArrayList;

public class SelectSportFragment extends Fragment {
    private final String TAG = "SelectSportFragment";
    private static SportListAdapter sSportListAdapter;
    private ListView mListView;

    public SelectSportFragment() {
        // Required empty public constructor
    }

    public static SelectSportFragment newInstance(String param1, String param2) {
        SelectSportFragment fragment = new SelectSportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllSports();
    }

    private void getAllSports() {
        NetworkManager sNetworkManager = NetworkManager.getInstance(getActivity());
        sNetworkManager.getAllSports(new NetworkCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                ArrayList<String> sports = result;
                sSportListAdapter = new SportListAdapter(sports, getActivity());
                mListView.setAdapter(sSportListAdapter);
                sSportListAdapter = new SportListAdapter(sports, getActivity().getApplicationContext());
                mListView.setAdapter(sSportListAdapter);
            }

            @Override
            public void onFailure(String errorString) {
                System.err.println("ERROR AÐ SÆKJA SPORT");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_sport, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_of_sports);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                boolean loggedIn = false;

                try {
                    if (getActivity().getIntent().getExtras().getBoolean("com.example.sports_app.loggedIn")) {
                        loggedIn = getActivity().getIntent().getExtras().getBoolean("com.example.sports_app.loggedIn");
                    }
                } catch (Exception e) {
                    loggedIn = false;
                }

                String sport = (String) mListView.getItemAtPosition(position);
                String username;
                try {
                    username = getActivity().getIntent().getExtras().getString("com.example.sports_app.username");
                } catch (Exception e) {
                    username = "";
                }
                Intent j = new Intent(getActivity(), SportActivity.class);
                j.putExtra("com.example.sports_app.loggedIn", loggedIn);
                j.putExtra("com.example.sports_app.sport_name", sport);
                j.putExtra("com.example.sports_app.username", username);
                startActivity(j);
            }
        });

        return rootView;
    }


}

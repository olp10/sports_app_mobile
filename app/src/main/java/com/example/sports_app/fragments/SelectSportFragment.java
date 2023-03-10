package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.sports_app.MainActivity;
import com.example.sports_app.R;
import com.example.sports_app.SportActivity;
import com.example.sports_app.SportListAdapter;
import com.example.sports_app.ThreadActivity;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.google.android.material.snackbar.Snackbar;

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
                for(String s : sports) {
                    System.out.println(s);
                }
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
                    if (getActivity().getIntent().getExtras().getBoolean("com.example.sports_app.isLoggedIn")) {
                        loggedIn = getActivity().getIntent().getExtras().getBoolean("com.example.sports_app.isLoggedIn");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String sport = (String) mListView.getItemAtPosition(position);

                Intent j = new Intent(getActivity(), SportActivity.class);
                j.putExtra("com.example.sports_app.isLoggedIn", loggedIn);
                j.putExtra("com.example.sports_app.sport_name", sport);
                startActivity(j);
            }
        });

        return rootView;
    }


}

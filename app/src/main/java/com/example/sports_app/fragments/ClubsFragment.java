package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sports_app.activities.ClubActivity;
import com.example.sports_app.adapters.ClubListAdapter;
import com.example.sports_app.R;
import com.example.sports_app.entities.Club;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClubsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClubsFragment extends Fragment {
    private static final String EXTRA_SPORT_NAME = "com.example.sports_app.sport_name";
    private final String TAG = "ClubsFragment";
    private static ClubListAdapter sClubListAdapter;
    private ListView mListView;
    private ArrayList<Club> mClubs = new ArrayList<>();

    public ClubsFragment() {
        // Required empty public constructor
    }
    public static ClubsFragment newInstance(String param1, String param2) {
        ClubsFragment fragment = new ClubsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllClubsBySport();
    }

    private void getAllClubsBySport() {
        String sport = getActivity().getIntent().getExtras().getString(EXTRA_SPORT_NAME);
        NetworkManager sNetworkManager = NetworkManager.getInstance(ClubsFragment.this.getContext());

        sNetworkManager.getAllClubsForSport(sport, new NetworkCallback<ArrayList<Club>>() {
            @Override
            public void onSuccess(ArrayList<Club> result) {
                mClubs = result;
                if (mClubs != null) {
                    sClubListAdapter = new ClubListAdapter(mClubs, getActivity().getApplicationContext());
                    mListView.setAdapter(sClubListAdapter);
                }
                sClubListAdapter = new ClubListAdapter(mClubs, getActivity().getApplicationContext());
                mListView.setAdapter(sClubListAdapter);
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_clubs, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_of_clubs);
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            long clubToOpenId = mClubs.get(i).getmId();
            Intent intent = ClubActivity.newIntent(getActivity(), clubToOpenId);
            startActivity(intent);
        });
        return rootView;
    }

}
package com.example.sports_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sports_app.R;
import com.example.sports_app.entities.Club;
import com.example.sports_app.entities.Event;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClubsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClubsFragment extends Fragment {
    private static final String EXTRA_SPORT_NAME = "com.example.sports_app.sport_name";

    public ClubsFragment() {
        // Required empty public constructor
    }
    public static ClubsFragment newInstance(String param1, String param2) {
        ClubsFragment fragment = new ClubsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sport = getActivity().getIntent().getExtras().getString(EXTRA_SPORT_NAME);
        NetworkManager sNetworkManager = NetworkManager.getInstance(ClubsFragment.this.getContext());

        // TODO: Laga svo að sport sé ekki harðkóðað
        sNetworkManager.getAllClubsForSport(sport, new NetworkCallback<ArrayList<Club>>() {
            @Override
            public void onSuccess(ArrayList<Club> result) {
                // VIRKAR - Þarf bara að setja þetta inn í lista núna
                ArrayList<Club> clubs = result;
                for (Club club : clubs) {
                    System.out.println(club.getmId());
                    System.out.println(club.getmClubName());
                    System.out.println(club.getmClubEmail());
                    System.out.println(club.getmClubLocation());
                    System.out.println(club.getmClubUrl());
                    System.out.println(club.getmDescription());
                    System.out.println(club.getmSport());
                }
                // VIRKAR - Þarf bara að setja þetta inn í lista núna

            }

            @Override
            public void onFailure(String error) {
                System.out.println(error);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clubs, container, false);
    }
}
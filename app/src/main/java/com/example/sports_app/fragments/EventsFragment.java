package com.example.sports_app.fragments;

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

import com.example.sports_app.activities.EventActivity;
import com.example.sports_app.adapters.EventListAdapter;
import com.example.sports_app.R;
import com.example.sports_app.entities.Event;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    private static final String EXTRA_SPORT_NAME = "com.example.sports_app.sport_name";
    private static final String EXTRA_USERNAME = "com.example.sports_app.username";
    private static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    private final String TAG = "EventsFragment";
    private static EventListAdapter sEventListAdapter;
    private ListView mListView;
    private List<Event> mEvents;
    private Button mNewEventButton;
    private FragmentContainerView mFragmentContainerView;
    boolean moderatesSport;
    NetworkManager sNetworkManager;
    String username;
    boolean loggedIn;
    boolean userIsAdmin;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sNetworkManager = NetworkManager.getInstance(getActivity());
        getAllEventsBySport();

        try {
            userIsAdmin = getActivity().getIntent().getExtras().getBoolean(EXTRA_IS_ADMIN);
        } catch (Exception e) {
            userIsAdmin = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_of_events);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long eventToOpenId = mEvents.get(i).getId();
                Intent intent = EventActivity.newIntent(getActivity(), eventToOpenId);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mFragmentContainerView = (FragmentContainerView) getActivity().findViewById(R.id.fragmentContainerView);
        mNewEventButton = (Button) view.findViewById(R.id.new_event_button);
        String sport = getActivity().getIntent().getExtras().getString(EXTRA_SPORT_NAME);

        try {
            loggedIn = getActivity().getIntent().getExtras().getBoolean("com.example.sports_app.loggedIn");
        } catch (Exception e) {
            loggedIn = false;
        }

        try {
            username = getActivity().getIntent().getStringExtra(EXTRA_USERNAME);
            System.out.println("Username in EventsFragment: " + username);
        } catch (Exception e) {
            username = "";
            System.out.println("Error: Username in EventsFragment: " + username);
        }

        if (username != null && !username.equals("")) {
            sNetworkManager.userModeratesSport(sport, username, new NetworkCallback() {
                @Override
                public void onSuccess(Object object) {
                    moderatesSport = (boolean) object;
                    if (loggedIn && moderatesSport || loggedIn && userIsAdmin) {
                        mNewEventButton.setVisibility(View.VISIBLE);
                    }
                    mNewEventButton.setOnClickListener(view1 -> {
                        // Intents fyrir createEventFragment: engin?
                        getActivity().getIntent().putExtra(EXTRA_SPORT_NAME, sport);
                        mFragmentContainerView.setVisibility(View.VISIBLE);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, NewEventFragment.class, null)
                                .setReorderingAllowed(true)
                                .commit();
                    });
                }

                @Override
                public void onFailure(String errorString) {
                    Log.d(TAG, "onFailure: " + errorString);
                }
            });
        }
    }

    private void getAllEventsBySport() {

        NetworkManager sNetworkManager = NetworkManager.getInstance(getContext());
        String sport = getActivity().getIntent().getExtras().getString(EXTRA_SPORT_NAME);
        sNetworkManager.getAllEventsForSport(sport, new NetworkCallback<ArrayList<Event>>() {
            @Override
            public void onSuccess(ArrayList<Event> result) {
                ArrayList<Event> events = result;
                mEvents = result;
                if (events != null) {
                    sEventListAdapter = new EventListAdapter(events, getActivity().getApplicationContext());
                    mListView.setAdapter(sEventListAdapter);
                }

            }

            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "onFailure: " + errorString);
            }
        });
    }
}
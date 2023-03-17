package com.example.sports_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sports_app.R;
import com.example.sports_app.entities.Event;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

public class EventActivity extends AppCompatActivity {

    private static final String TAG = "EventActivity";
    private static final String EXTRA_EVENT_ID = "com.example.sports_app.event_id";
    private Event mEvent;

    TextView mEventName;
    TextView mEventDate;
    TextView mEventDescription;
    Button mSubscribeToEvent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mEventName = (TextView) findViewById(R.id.event_title);
        mEventDate = (TextView) findViewById(R.id.event_date);
        mEventDescription = (TextView)  findViewById(R.id.event_description);
        mSubscribeToEvent = (Button) findViewById(R.id.event_subscribe_button);

        long thisEventId = getIntent().getLongExtra(EXTRA_EVENT_ID, 0);
        getEventById(thisEventId);

        mSubscribeToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkManager networkManager = NetworkManager.getInstance(EventActivity.this);
                networkManager.subscribeToEvent(1L, mEvent.getId(), new NetworkCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                    }

                    @Override
                    public void onFailure(String errorString) {

                    }
                });
            }
        });
    }

    private void getEventById(Long id) {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getEventById(id, new NetworkCallback<Event>() {
            @Override
            public void onSuccess(Event result) {
                mEvent = result;
                populateUI();
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get event by id via REST");
            }
        });
    }

    private void populateUI() {
        mEventName.setText(mEvent.getEventName());
        mEventDate.setText(mEvent.getFormattedDate());
        mEventDescription.setText(mEvent.getEventDescription());
    }

    public static Intent newIntent(Context packageContext, long eventId) {
        Intent i = new Intent(packageContext, EventActivity.class);
        i.putExtra(EXTRA_EVENT_ID, eventId);
        return i;
    }
}
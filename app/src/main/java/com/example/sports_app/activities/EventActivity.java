package com.example.sports_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sports_app.R;
import com.example.sports_app.entities.Event;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.squareup.picasso.Picasso;

public class EventActivity extends Activity {

    private static final String TAG = "EventActivity";
    private static final String EXTRA_EVENT_ID = "com.example.sports_app.event_id";
    private static final String EXTRA_USER = "com.example.sports_app.username";
    private Event mEvent;

    TextView mEventName;
    TextView mEventDate;
    TextView mEventDescription;
    Button mSubscribeToEvent;

    ImageView mEventImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NetworkManager networkManager = NetworkManager.getInstance(EventActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mEventImage = (ImageView) findViewById(R.id.event_image);
        mEventName = (TextView) findViewById(R.id.event_title);
        mEventDate = (TextView) findViewById(R.id.event_date);
        mEventDescription = (TextView)  findViewById(R.id.event_description);
        mSubscribeToEvent = (Button) findViewById(R.id.event_subscribe_button);

        // Get image from url
        long thisEventId = getIntent().getLongExtra(EXTRA_EVENT_ID, 0);
        getEventById(thisEventId);


        String username = getIntent().getExtras().getString(EXTRA_USER);

        mSubscribeToEvent.setOnClickListener(new View.OnClickListener() {
            Long userId;

            @Override
            public void onClick(View view) {
//                System.out.println("Username: " + username);
                try {
                    networkManager.getUserByUsername(username, new NetworkCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            if (user != null) {
                                userId = user.getmId();

                                networkManager.subscribeToEvent(thisEventId, userId, new NetworkCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Log.d(TAG, result);
                                    }

                                    @Override
                                    public void onFailure(String errorString) {

                                    }
                                });
                            } else {
                                Toast.makeText(EventActivity.this, "Not logged in", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String errorString) {
                            Log.d(TAG, "onFailure: User not logged in");
                        }
                    });

                } catch (Exception e) {
                    Log.d(TAG, "onClick: Failed getting user info");
                }
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

        if (mEvent.getmEventImage() != null) {
            Picasso.get().load(mEvent.getmEventImage()).resize(500, 1000).into(mEventImage);
        }
    }

    public static Intent newIntent(Context packageContext, long eventId) {
        Intent i = new Intent(packageContext, EventActivity.class);
        i.putExtra(EXTRA_EVENT_ID, eventId);
        return i;
    }
}
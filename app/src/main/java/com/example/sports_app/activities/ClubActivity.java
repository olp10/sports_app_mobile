package com.example.sports_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.sports_app.R;
import com.example.sports_app.entities.Club;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import org.w3c.dom.Text;

public class ClubActivity extends Activity {

    private static final String TAG ="ClubActivity";
    private static final String EXTRA_CLUB_ID = "com.example.sports_app.club_id";

    private Club mClub;

    TextView mClubName;
    TextView mClubURL;
    TextView mClubEmail;
    TextView mClubLocation;
    TextView mClubDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        mClubName = findViewById(R.id.club_name);
        mClubURL = findViewById(R.id.club_url);
        mClubDescription = findViewById(R.id.club_description);
        mClubEmail = findViewById(R.id.club_email);
        mClubLocation = findViewById(R.id.club_location);

        long thisClubId = getIntent().getLongExtra(EXTRA_CLUB_ID, 0);
        getClubById(thisClubId);
    }

    private void getClubById(long id) {
        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getClubById(id, new NetworkCallback<Club>() {
            @Override
            public void onSuccess(Club result) {
                mClub = result;
                populateUI();
            }
            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to get club by id via REST");
            }
        });
    }

    private void populateUI() {
        mClubName.setText(mClub.getmClubName());
        mClubDescription.setText(mClub.getmDescription());
        mClubEmail.setText(mClub.getmClubEmail());
        mClubLocation.setText(mClub.getmClubLocation());
        mClubURL.setText(mClub.getmClubUrl());
    }

    public static Intent newIntent(Context packageContext, long clubId) {
        Intent i = new Intent(packageContext, ClubActivity.class);
        i.putExtra(EXTRA_CLUB_ID, clubId);
        return i;
    }
}

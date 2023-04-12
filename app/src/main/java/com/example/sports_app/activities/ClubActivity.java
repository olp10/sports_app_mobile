package com.example.sports_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sports_app.R;
import com.example.sports_app.entities.Club;

public class ClubActivity extends AppCompatActivity {

    private static final String TAG ="ClubActivity";
    private static final String EXTRA_USER = "com.example.sports_app.username";

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
    }
}
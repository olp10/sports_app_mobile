package com.example.sports_app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentContainerView;

import com.example.sports_app.R;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

public class UserProfileActivity extends Activity {
    private static final String TAG = "UserProfileActivity";
    private static final String EXTRA_USER = "com.example.sports_app.userClicked";
    private static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    TextView txtUsername;
    TextView userEmail;
    Button banUserButton;
    boolean isAdmin;
    boolean userIsBanned;
    NetworkManager sNetworkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        txtUsername = (TextView) findViewById(R.id.username);
        banUserButton = (Button) findViewById(R.id.ban_user_button);
        String user = getIntent().getStringExtra(EXTRA_USER);

        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
        mFragmentContainerView.setVisibility(ViewGroup.GONE);

        NetworkManager sNetworkManager = NetworkManager.getInstance(this);
        sNetworkManager.getUserByUsername(user, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                txtUsername.setText(result.getmUsername());
            }

            @Override
            public void onFailure(String errorString) {
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            }
        });

        try {
            isAdmin = getIntent().getExtras().getBoolean("com.example.sports_app.isAdmin");
        } catch (Exception e) {
            isAdmin = false;
        }
        if (isAdmin) {
            sNetworkManager.getUserBanned(user, new NetworkCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    userIsBanned = result;
                    if (userIsBanned) {
                        banUserButton.setText("Unban user");
                    } else {
                        banUserButton.setText("Ban user");
                    }
                }

                @Override
                public void onFailure(String errorString) {
                    Log.d(TAG, "onFailure: " + errorString);
                }
            });

            banUserButton.setVisibility(View.VISIBLE);
            banUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (userIsBanned) {
                        sNetworkManager.unbanUser(user, new NetworkCallback<User>() {
                            @Override
                            public void onSuccess(User result) {
                                Toast.makeText(getApplicationContext(), result.getmUsername() + " has been unbanned" , Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onFailure(String errorString) {
                                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        sNetworkManager.banUser(user, new NetworkCallback<User>() {

                            @Override
                            public void onSuccess(User result) {
                                Toast.makeText(getApplicationContext(), result.getmUsername() + " has been banned", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onFailure(String errorString) {
                                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                }
            });
        }
    }
}

package com.example.sports_app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentContainerView;

import com.example.sports_app.R;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class UserProfileActivity extends Activity {
    private static final String TAG = "UserProfileActivity";
    private static final String EXTRA_USER = "com.example.sports_app.userClicked";
    private static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    private static final String EXTRA_LOGGED_IN_USER = "com.example.sports_app.loggedInUser";
    private TextView txtUsername;
    private EditText mUserFullName;
    private EditText mUserEmail;
    private TextInputLayout mUserFullNameLabel;
    private TextInputLayout mUserEmailLabel;
    private Button mUpdateUserProfile;
    private Button banUserButton;
    boolean isAdmin;
    boolean userIsBanned;
    NetworkManager sNetworkManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String user = getIntent().getStringExtra(EXTRA_USER);
        String loggedInUser = getIntent().getStringExtra(EXTRA_LOGGED_IN_USER);

        txtUsername = (TextView) findViewById(R.id.username);
        banUserButton = (Button) findViewById(R.id.ban_user_button);
        mUserFullName = (EditText) findViewById(R.id.user_real_name);
        mUserEmail = (EditText) findViewById(R.id.user_email_address);
        mUserFullNameLabel = (TextInputLayout) findViewById(R.id.user_full_name_input_layout);
        mUserEmailLabel = (TextInputLayout) findViewById(R.id.user_email_address_input_layout);

        // User info text listeners
        mUserFullNameLabel.getEditText().addTextChangedListener(editProfileTextHandler());
        mUserEmailLabel.getEditText().addTextChangedListener(editProfileTextHandler());
        mUpdateUserProfile = (Button) findViewById(R.id.update_user_profile_button);

        // Sér til þess að aðrir notendur geti ekki breytt upplýsingunum
        if (loggedInUser.equals(user) == false) {
            mUserFullName.setKeyListener(null);
            mUserEmail.setKeyListener(null);
        }

//        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
//        mFragmentContainerView.setVisibility(ViewGroup.GONE);

        sNetworkManager = NetworkManager.getInstance(this);

        mUpdateUserProfile.setOnClickListener(view -> {
            String fullName = mUserFullName.getText().toString();
            String email = mUserEmail.getText().toString();

            if (!validateEmail(email)) {
                mUserEmailLabel.setError("Tölvupóstfang verður að vera á réttu formi");
            } else if (!validateFullName(fullName)) {
                mUserFullNameLabel.setError("Fullt nafn verður að vera á réttu formi");
            } else {
                sNetworkManager.updateUserInfo(user, fullName, email, new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        if (result != null) {
                            mUserFullName.setHint(result.getUserFullName());
                            mUserEmail.setHint(result.getUserEmailAddress());
                        }
                    }

                    @Override
                    public void onFailure(String errorString) {
                        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        sNetworkManager.getUserByUsername(user, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    txtUsername.setText(result.getmUsername());
                    if (result.getUserFullName() != null && result.getUserFullName().length() > 0) {
                        mUserFullName.setHint(result.getUserFullName());
                    }
                    if (result.getUserEmailAddress() != null && result.getUserEmailAddress().length() > 0) {
                        mUserEmail.setHint(result.getUserEmailAddress());
                    }
                } else {
                    txtUsername.setText("User does not exist");
                }
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

    private TextWatcher editProfileTextHandler() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUpdateUserProfile.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private static boolean validateEmail(String emailAdress) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern)
                .matcher(emailAdress)
                .matches();
    }

    private static boolean validateFullName(String fullName) {
        String regexPattern = "^[A-Z](?=.{1,29}$)[A-Za-z]*(?:\\h+[A-Z][A-Za-z]*)*$";
        return Pattern.compile(regexPattern)
                .matcher(fullName)
                .matches();
    }
}

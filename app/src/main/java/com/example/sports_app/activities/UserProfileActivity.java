package com.example.sports_app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentContainerView;

import com.example.sports_app.R;
import com.example.sports_app.adapters.EventListAdapter;
import com.example.sports_app.entities.Event;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserProfileActivity extends Activity {
    private static final String TAG = "UserProfileActivity";
    private static final String EXTRA_USER_CLICKED = "com.example.sports_app.userClicked";
    private static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    private static final String EXTRA_LOGGED_IN_USER = "com.example.sports_app.loggedInUser";
    private TextView txtUsername;
    private TextView mUserFullName;
    private TextView mUserEmail;
    private EditText mUserFullNameEdit;
    private EditText mUserEmailEdit;
    private TextInputLayout mUserFullNameLabel;
    private TextInputLayout mUserEmailLabel;
    private Button mChangeFullNameButton;
    private Button mChangeEmailButton;
    private Button mUpdateUserProfile;
    private Button banUserButton;
    private Button mShowEventsButton;
    private boolean showEvents;
    private LinearLayout mEventsLayout;
    private TextView mUserEventsText;
    private ListView mSubscribedEvents;
    private EventListAdapter mEventListAdapter;
    private ArrayList<Event> subscribedEventsList = new ArrayList<>();
    boolean isAdmin;
    boolean userIsBanned;
    NetworkManager sNetworkManager;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        sharedPreferences = getSharedPreferences("com.example.sports_app", MODE_PRIVATE);

        String user = getIntent().getStringExtra(EXTRA_USER_CLICKED);
        String loggedInUser = sharedPreferences.getString("logged_in_user", null);

        // User info
        txtUsername = (TextView) findViewById(R.id.username);
        mUserFullName = (TextView) findViewById(R.id.user_fullName);
        mUserEmail = (TextView) findViewById(R.id.user_email);
        mChangeFullNameButton = (Button) findViewById(R.id.change_fullName_button);
        mChangeEmailButton = (Button) findViewById(R.id.change_email_button);
        banUserButton = (Button) findViewById(R.id.ban_user_button);
        mUserFullNameEdit = (EditText) findViewById(R.id.user_real_name_edit);
        mUserEmailEdit = (EditText) findViewById(R.id.user_email_address_edit);
        mUserFullNameLabel = (TextInputLayout) findViewById(R.id.user_full_name_input_layout);
        mUserEmailLabel = (TextInputLayout) findViewById(R.id.user_email_address_input_layout);

        // User info text listeners
        mUserFullNameLabel.getEditText().addTextChangedListener(editProfileNameHandler());
        mUserEmailLabel.getEditText().addTextChangedListener(editProfileNameHandler());
        mUpdateUserProfile = (Button) findViewById(R.id.update_user_profile_button);

        // Sér til þess að aðrir notendur geti ekki breytt upplýsingunum


        if (!loggedInUser.equals(user)) {
            mChangeFullNameButton.setVisibility(View.GONE);
            mChangeEmailButton.setVisibility(View.GONE);
            mUserFullName.setKeyListener(null);
            mUserEmail.setKeyListener(null);
        }
//        else {
//            mUpdateUserProfile.setVisibility(View.VISIBLE);
//        }

        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
        mFragmentContainerView.setVisibility(ViewGroup.GONE);
        sNetworkManager = NetworkManager.getInstance(this);


        // OnClickListeners fyrir buttons
        mChangeFullNameButton.setOnClickListener(view -> mUserFullNameEdit.setVisibility(View.VISIBLE));
        mChangeEmailButton.setOnClickListener(view -> mUserEmailEdit.setVisibility(View.VISIBLE));


        mUpdateUserProfile.setOnClickListener(view -> {
            String fullName = mUserFullNameEdit.getText().toString();
            String email = mUserEmailEdit.getText().toString();

            if (!validateEmail(email)) {
                mUserEmailLabel.setError("Tölvupóstfang verður að vera á réttu formi eða tómt!");
            } else if (!validateFullName(fullName)) {
                mUserFullNameLabel.setError("Fullt nafn verður að vera á réttu formi eða tómt!");
            } else {
                mUserEmailLabel.setErrorEnabled(false);
                mUserFullNameLabel.setErrorEnabled(false);
                sNetworkManager.updateUserInfo(user, fullName, email, new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        if (result != null) {
                            mUserFullName.setText(mUserFullName.getText() + result.getUserFullName());
                            mUserEmail.setText(mUserEmail.getText() + result.getUserEmailAddress());
                            mUserFullNameEdit.setVisibility(View.GONE);
                            mUserEmailEdit.setVisibility(View.GONE);
                            Toast.makeText(UserProfileActivity.this, "Upplýsingum breytt", Toast.LENGTH_SHORT).show();
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
                    sNetworkManager.getMySubscriptions(result.getmId(), new NetworkCallback<ArrayList<Event>>() {


                        @Override
                        public void onSuccess(ArrayList<Event> result) {
                            UserProfileActivity.this.subscribedEventsList = result;
                            UserProfileActivity.this.subscribedEventsSetup(result);
                            for (Event e : result) {
                                System.out.println(e.getmEventName());
                                e.getSubscribers().forEach(u -> System.out.println("subId: " + String.valueOf(u.getmUsername())));
                            }
                        }

                        @Override
                        public void onFailure(String errorString) {

                        }
                    });
                    txtUsername.setText(result.getmUsername());
                    if (result.getUserFullName() != null && result.getUserFullName().length() > 0) {
                        mUserFullName.setText(mUserFullName.getText() + result.getUserFullName());
                    }
                    if (result.getUserEmailAddress() != null && result.getUserEmailAddress().length() > 0) {
                        mUserEmail.setText(mUserEmail.getText() + result.getUserEmailAddress());
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

        isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        if (isAdmin && !user.equals(loggedInUser)) {
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

            if (!loggedInUser.equals(user)) {
                banUserButton.setVisibility(View.VISIBLE);
            }
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

    private void subscribedEventsSetup(ArrayList<Event> events) {
        if (events.size() > 0) {
            mEventsLayout = (LinearLayout) findViewById(R.id.user_events_layout);
            mUserEventsText = (TextView) findViewById(R.id.user_events_text);
            mSubscribedEvents = (ListView) findViewById(R.id.user_events);

            mUserEventsText.setText(mUserEventsText.getText() + " " + String.valueOf(events.size()));
            mEventsLayout.setVisibility(View.VISIBLE);
            mEventListAdapter = new EventListAdapter(events, UserProfileActivity.this);
            mSubscribedEvents.setAdapter(mEventListAdapter);

            showEvents = false;
            mShowEventsButton = (Button) findViewById(R.id.show_events_button);
            mShowEventsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEvents = !showEvents;
                    if (showEvents) {
                        mSubscribedEvents.setVisibility(View.VISIBLE);
                    } else {
                        mSubscribedEvents.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private TextWatcher editProfileNameHandler() {
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
    private TextWatcher editProfileEmailHandler() {
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

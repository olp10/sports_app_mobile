package com.example.sports_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.sports_app.R;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.example.sports_app.fragments.SignupFragment;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class LoginActivity extends Activity {
    boolean doubleBackToExitPressedOnce = false;
    private final String TAG = "LoginActivity";
    ActionMenuView mActionMenuView;
    NetworkManager mNetworkManager;
    EditText mUsernameTextField;
    EditText mPasswordTextField;
    Button mLoginButton;
    TextView mNewAccountLink;
    MenuItem mMenuLogin;

    ThreadService threadService;

    private List<Thread> mThreadBank;
    private FragmentContainerView mSignupFragmentContainerView;

    private TextInputLayout mUsernameLabel;

    /**
     * Instantiate all UI variables to keep onCreate less crowded
     */
    private void InstantiateUIElements() {
        // Action bar
        //mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);

        // Network tengt
        mNetworkManager = NetworkManager.getInstance(LoginActivity.this);

        // Tengt login
        mUsernameTextField = (EditText) findViewById(R.id.login_username);
        mPasswordTextField = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mNewAccountLink = (TextView) findViewById(R.id.new_account_link);
        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
        mSignupFragmentContainerView = (FragmentContainerView) findViewById(R.id.sign_up_fragment_container_view);
        createLoginButton();

        mUsernameLabel = (TextInputLayout) findViewById(R.id.login_username_input_layout);
        mUsernameLabel.getEditText().addTextChangedListener(usernameHandler());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InstantiateUIElements();


        // Listener fyrir "Stofna nýjan aðgang" hlekk á login skjá
        mNewAccountLink.setOnClickListener(v -> {
            if (mSignupFragmentContainerView.getVisibility() == View.GONE) {
                mSignupFragmentContainerView.setVisibility(View.VISIBLE);
                Fragment fragment = new SignupFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sign_up_fragment_container_view, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                mSignupFragmentContainerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    public void createLoginButton() {

        // Login virkar á móti bakenda
        mLoginButton.setOnClickListener(v -> {
            mNetworkManager.login(mUsernameTextField.getText().toString(), mPasswordTextField.getText().toString(), new NetworkCallback() {
                @Override
                public void onSuccess(Object result) {
                    Log.d(TAG, "onSuccess: " + result);
                    User user = (User) result;
                    Toast t = new Toast(getApplicationContext());
                    t.setDuration(Toast.LENGTH_SHORT);
                    if (user != null && user.loggedIn()) {
                        t.setText("Login successful");
                        t.show();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        if (user.ismIsAdmin()) {
                            i.putExtra("com.example.sports_app.isAdmin", true);
                        } else {
                            i.putExtra("com.example.sports_app.isAdmin", false);
                        }
                        i.putExtra("com.example.sports_app.loggedIn", true);
                        i.putExtra("com.example.sports_app.username", user.getmUsername());
                        i.putExtra("com.example.sports_app.password", user.getmUserPassword());

                        startActivity(i);
                        finish();
                    } else {
                        t.setText("Notendanafn og lykilorð passa ekki");
                        t.show();
                        mUsernameTextField.setText("");
                        mPasswordTextField.setText("");
                    }
                }

                @Override
                public void onFailure(String errorString) {
                    Log.d(TAG, "onFailure: " + "Failed to connect to backend");
                }
            });
        });
    }

    private TextWatcher usernameHandler() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mUsernameLabel.setErrorEnabled(false);
                }
                if (charSequence.length() > 15) {
                    mUsernameLabel.setError("Username too long");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}

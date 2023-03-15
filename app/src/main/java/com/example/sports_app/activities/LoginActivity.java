package com.example.sports_app.activities;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class LoginActivity extends Activity {
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
                        i.putExtra("com.example.sports_app.password", user.getmUserPassword());
                        startActivity(i);
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
}

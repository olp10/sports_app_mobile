package com.example.sports_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
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

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InstantiateUIElements();
        createLoginButton();

        // Listener fyrir "Stofna nýjan aðgang" hlekk á login skjá
        mNewAccountLink.setOnClickListener(v -> {
            // TODO búa til signup layout/fragment og láta þetta fara þangað
            Toast t = new Toast(getApplicationContext());
            t.setDuration(Toast.LENGTH_SHORT);
            t.setText("Nýr aðgangur");
            t.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

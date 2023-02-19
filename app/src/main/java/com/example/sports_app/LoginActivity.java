package com.example.sports_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sports_app.networking.NetworkManager;

public class LoginActivity extends Activity {
    ActionMenuView mActionMenuView;
    NetworkManager mNetworkManager;
    EditText mUsernameTextField;
    EditText mPasswordTextField;
    Button mLoginButton;
    TextView mNewAccountLink;


    /**
     * Method for instantiating all UI variables to keep onCreate less crowded
     */
    private void InstantiateUIElements() {
        // Action bar
        mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);

        // Network tengt
        mNetworkManager = NetworkManager.getInstance(this);

        // Tengt login
        mUsernameTextField = (EditText) findViewById(R.id.login_username);
        mPasswordTextField = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mNewAccountLink = (TextView) findViewById(R.id.new_account_link);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InstantiateUIElements();

        // Listener fyrir "Stofna nýjan aðgang" hlekk á login skjá
        mNewAccountLink.setOnClickListener(v -> {
            // TODO búa til signup layout/fragment og láta þetta fara þangað
            Toast t = new Toast(getApplicationContext());
            t.setDuration(Toast.LENGTH_SHORT);
            t.setText("Nýr aðgangur");
            t.show();
        });

        // Feik login með dummy gögnum - user: admin, pass: admin
        mLoginButton.setOnClickListener(v -> {
            // TODO sækja notendaupplýsingar í gegnum REST þjónustu og verify-a
            if (mUsernameTextField.getText().toString().equals("admin") && mPasswordTextField.getText().toString().equals("admin")) {
                Toast t = new Toast(getApplicationContext());
                t.setDuration(Toast.LENGTH_SHORT);
                t.setText("Innskráning tókst");
                t.show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
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
        }
        return super.onOptionsItemSelected(item);
    }
}

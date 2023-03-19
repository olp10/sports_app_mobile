package com.example.sports_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.sports_app.R;
import com.example.sports_app.fragments.SelectSportFragment;
import com.example.sports_app.fragments.UserSettingsFragment;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.util.Objects;


/**
 * Superclass that all Activities (the ones that want to have the menu bar on top) extend from.
 * Each activity must initialize the mFragmentContainerView variable in their onCreate method.
 */
public abstract class Activity extends AppCompatActivity {
    protected static final String EXTRA_USERNAME = "com.example.sports_app.username";
    protected static final String EXTRA_PASSWORD = "com.example.sports_app.password";
    protected static final String EXTRA_LOGGED_IN = "com.example.sports_app.loggedIn";
    protected static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    private static final String TAG = "Activity";
    protected FragmentContainerView mFragmentContainerView;
    boolean userIsAdmin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            userIsAdmin = getIntent().getExtras().getBoolean(EXTRA_IS_ADMIN);
        } catch (Exception e) {
            userIsAdmin = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionview, menu);
        MenuItem mMenuItemLogin = menu.findItem(R.id.menu_login);
        MenuItem mMenuItemLogout = menu.findItem(R.id.menu_logout);

        try {
            if (getIntent().getExtras().getBoolean(EXTRA_LOGGED_IN)) {
                mMenuItemLogin.setVisible(false);
                mMenuItemLogout.setVisible(true);
            } else {
                mMenuItemLogin.setVisible(true);
                mMenuItemLogout.setVisible(false);
            }
        } catch (Exception e) {
            Log.d(TAG, "onCreateOptionsMenu: " + e.getMessage());
        }
        return true;
    }
    public void logout() {
        if (getIntent().getExtras().getString(EXTRA_USERNAME) != null &&
                getIntent().getExtras().getString(EXTRA_PASSWORD) != null &&
                !Objects.equals(getIntent().getExtras().getString(EXTRA_USERNAME), "") &&
                !Objects.equals(getIntent().getExtras().getString(EXTRA_PASSWORD), "")) {

            NetworkManager sNetworkManager = NetworkManager.getInstance(this);
            String username = getIntent().getExtras().getString(EXTRA_USERNAME);
            String password = getIntent().getExtras().getString(EXTRA_PASSWORD);
            sNetworkManager.logout(username, password, new NetworkCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(Activity.this, LoginActivity.class);
                    intent.putExtra(EXTRA_LOGGED_IN, false);
                    intent.putExtra(EXTRA_IS_ADMIN, false);
                    intent.putExtra(EXTRA_USERNAME, "");
                    intent.putExtra(EXTRA_PASSWORD, "");
                    startActivity(intent);
                }

                @Override
                public void onFailure(String errorString) {
                    Log.e("Threadservice", "Failed to logout via REST");
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        boolean loggedIn;
        try {
            loggedIn = getIntent().getExtras().getBoolean(EXTRA_LOGGED_IN);
        } catch (Exception e) {
            loggedIn = false;
        }
        String username;
        try {
            username = getIntent().getExtras().getString(EXTRA_USERNAME);
        } catch (Exception e) {
            username = "";
        }
        System.out.println("Username í Activity klasa: " + username);
        switch (item.getItemId()) {
            case R.id.menu_login:
                i = new Intent(Activity.this, LoginActivity.class);
                i.putExtra(EXTRA_LOGGED_IN, loggedIn);
                i.putExtra(EXTRA_USERNAME, username);
                startActivity(new Intent(Activity.this, LoginActivity.class));
                break;
            case R.id.menu_logout:
                i = new Intent(Activity.this, MainActivity.class);
                logout();
                i.putExtra(EXTRA_LOGGED_IN, false);
                i.removeExtra(EXTRA_USERNAME);
                startActivity(i);
                break;
            case R.id.menu_home:
                i = new Intent(Activity.this, MainActivity.class);
                i.putExtra(EXTRA_LOGGED_IN, loggedIn);
                i.putExtra(EXTRA_USERNAME, username);
                startActivity(i);
                break;
            case R.id.menu_settings:
                if (mFragmentContainerView.getVisibility() == View.GONE) {
                    mFragmentContainerView.setVisibility(View.VISIBLE);
                    Fragment fragment = new UserSettingsFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null)
                            .commit();
                } else mFragmentContainerView.setVisibility(View.GONE);
                break;
            case R.id.menu_sport:
                if (mFragmentContainerView.getVisibility() == View.GONE) {
                    mFragmentContainerView.setVisibility(View.VISIBLE);
                    Fragment fragment = new SelectSportFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null)
                            .commit();
                } else mFragmentContainerView.setVisibility(View.GONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

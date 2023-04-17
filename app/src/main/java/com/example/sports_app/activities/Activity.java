package com.example.sports_app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;

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
    protected static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    private static final String TAG = "Activity";
    protected FragmentContainerView mFragmentContainerView;
    boolean userIsAdmin;

    protected ActionMenuView bottomBar;
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

        // Show login option if not logged in, and logout option if logged in
        if (getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getString("logged_in_user", null) != null) {
            mMenuItemLogin.setVisible(false);
            mMenuItemLogout.setVisible(true);
        } else {
            mMenuItemLogin.setVisible(true);
            mMenuItemLogout.setVisible(false);
        }
        return true;
    }

    public void logout() {
        if (getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getString("logged_in_user", null) != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.sports_app", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.menu_login:
                i = new Intent(Activity.this, LoginActivity.class);
                startActivity(new Intent(Activity.this, LoginActivity.class));
                break;
            case R.id.menu_logout:
                i = new Intent(Activity.this, MainActivity.class);
                logout();
                startActivity(i);
                break;
            case R.id.menu_home:
                i = new Intent(Activity.this, MainActivity.class);
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

package com.example.sports_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;

import com.example.sports_app.entities.Thread;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Breytur fyrir aðalvalmynd //
    ActionMenuView mActionMenuView;
    MenuItem mMenuItemLogin;
    MenuItem mMenuItemLogout;
    // ------------------------ //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);

        /*
        NetworkManager networkManager = NetworkManager.getInstance(this);

        // Prufa að tengjast við vefþjónustuna - Má eyða
        networkManager.getAllThreads(new NetworkCallback<List<Thread>>() {
            @Override
            public void onSuccess(List<Thread> result) {
                threads = result;
                for (Thread t : threads) {
                    Log.d(TAG, t.getmHeader());
                }
            }
            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "onFailure: Error");
            }
        });
        Log.d(TAG, "end");
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionview, menu);
        mMenuItemLogin = menu.findItem(R.id.menu_login);
        mMenuItemLogout = menu.findItem(R.id.menu_logout);

        try {
            if (getIntent().getExtras().getBoolean("com.example.sports_app.isLoggedIn") == true) {
                mMenuItemLogin.setVisible(false);
                mMenuItemLogout.setVisible(true);
            } else {
                mMenuItemLogin.setVisible(true);
                mMenuItemLogout.setVisible(false);
            }
        } catch (Exception e) {
            Log.d(TAG, "onCreateOptionsMenu: " + e.toString());
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.menu_logout:
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.putExtra("com.example.sports_app.isLoggedIn", false);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login() {

    }
}
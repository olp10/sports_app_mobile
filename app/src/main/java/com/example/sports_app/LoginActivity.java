package com.example.sports_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.MailTo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sports_app.networking.NetworkManager;

public class LoginActivity extends Activity {
    ActionMenuView mActionMenuView;
    NetworkManager networkManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActionMenuView = findViewById(R.id.toolbar_bottom);
        networkManager = NetworkManager.getInstance(this);
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

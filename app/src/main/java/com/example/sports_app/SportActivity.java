package com.example.sports_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import java.util.UUID;

/**
 *
 */
public class SportActivity extends FragmentActivity {
    private final String EXTRA_SPORT_ID = "com.example.sports_app.sport_id";
    TabLayout tabLayout;
    ActionMenuView mActionMenuView;
    private static final String TAG = "MainActivity";

    public void InstantiateUIElements() {
        mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);

        // Adding tabs (Threads/Events/Clubs) for sport
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabLayout.Tab threadsTab = tabLayout.newTab();
        threadsTab.setText("Threads");
        TabLayout.Tab eventsTab = tabLayout.newTab();
        eventsTab.setText("Events");
        TabLayout.Tab clubsTab = tabLayout.newTab();
        clubsTab.setText("Clubs");

        tabLayout.addTab(threadsTab);
        tabLayout.addTab(eventsTab);
        tabLayout.addTab(clubsTab);

    }


    @LayoutRes
    public int getLayoutResID() {
        return R.layout.fragment_events;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            case R.id.menu_login:
                startActivity(new Intent(SportActivity.this, LoginActivity.class));
                break;
            case R.id.menu_home:
                startActivity(new Intent(SportActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

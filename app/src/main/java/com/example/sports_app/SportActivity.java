package com.example.sports_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sports_app.entities.Event;
import com.example.sports_app.fragments.ClubsFragment;
import com.example.sports_app.fragments.EventsFragment;
import com.example.sports_app.fragments.ThreadsFragment;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 *
 */
public class SportActivity extends AppCompatActivity {
    private final String EXTRA_SPORT_ID = "com.example.sports_app.sport_name";

    // Breytur fyrir aðalvalmynd //
    private ActionMenuView mActionMenuView;
    private ActionMenuView mSportMenuView;
    private MenuItem mMenuItemLogin;
    private MenuItem mMenuItemLogout;
    private MenuItem mMenuItemSport;
    private ListView mListView;
    TabLayout tabLayout;

    private static final String TAG = "SportActivity";

    public void InstantiateUIElements() {


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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (tab.getPosition()) {
                    case 0:
                        System.out.println("Clicked threads tab");
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.fragmentContainerView, ThreadsFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();
                        break;
                    case 1:
                        System.out.println("Clicked events tab");
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.fragmentContainerView, EventsFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();
                        break;
                    case 2:
                        System.out.println("Clicked clubs tab");
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.fragmentContainerView, ClubsFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        InstantiateUIElements();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionview, menu);
        Log.d(TAG, "onCreateOptionsMenu: asdf");
        try {
            if (getIntent().getExtras().getBoolean("com.example.sports_app.isLoggedIn")) {
                mMenuItemLogin.setVisible(false);
                mMenuItemLogout.setVisible(true);
            } else {
                mMenuItemLogin.setVisible(true);
                mMenuItemLogout.setVisible(false);
            }
        } catch (Exception e) {
            Log.d(TAG, "onCreateOptionsMenu: " + e.toString());
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(SportActivity.this, LoginActivity.class));
                break;
            case R.id.menu_logout:
                Intent i = new Intent(SportActivity.this, MainActivity.class);
                i.putExtra("com.example.sports_app.isLoggedIn", false);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

// FIXME: Prufa að pusha aftur, dno

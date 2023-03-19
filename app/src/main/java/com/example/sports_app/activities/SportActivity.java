package com.example.sports_app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.sports_app.R;
import com.example.sports_app.fragments.ClubsFragment;
import com.example.sports_app.fragments.EventsFragment;
import com.example.sports_app.fragments.ThreadsFragment;
import com.google.android.material.tabs.TabLayout;

/**
 *
 */
public class SportActivity extends Activity {
    private final String EXTRA_SPORT_ID = "com.example.sports_app.sport_name";
    private static final String EXTRA_IS_ADMIN = "com.example.sports_app.isAdmin";
    private final String EXTRA_USERNAME = "com.example.sports_app.username";

    // Breytur fyrir aðalvalmynd //
    private ActionMenuView mActionMenuView;
    private ActionMenuView mSportMenuView;
    private MenuItem mMenuItemLogin;
    private MenuItem mMenuItemLogout;
    private MenuItem mMenuItemSport;
    private MenuItem mAdmin;
    private ListView mListView;

    // private FragmentContainerView mFragmentContainerView;
    TabLayout tabLayout;

    private static final String TAG = "SportActivity";
    private String currentUsername;

    FragmentManager fragmentManager;

    public void InstantiateUIElements() {

        fragmentManager = getSupportFragmentManager();
        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
        mFragmentContainerView.setVisibility(ViewGroup.GONE);

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
        currentUsername = getIntent().getExtras().getString(EXTRA_USERNAME);

        this.setTitle(getIntent().getExtras().getString(EXTRA_SPORT_ID));

        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.tabsFragmentContainerView, ThreadsFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        System.out.println("Clicked threads tab");
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.tabsFragmentContainerView, ThreadsFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();
                        break;
                    case 1:
                        System.out.println("Clicked events tab");
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.tabsFragmentContainerView, EventsFragment.class, null)
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();
                        break;
                    case 2:
                        System.out.println("Clicked clubs tab");
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                                .replace(R.id.tabsFragmentContainerView, ClubsFragment.class, null)
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
}

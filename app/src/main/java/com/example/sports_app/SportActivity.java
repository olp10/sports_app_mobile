package com.example.sports_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ActionMenuView;
import android.widget.ListView;

import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.sports_app.fragments.ClubsFragment;
import com.example.sports_app.fragments.EventsFragment;
import com.example.sports_app.fragments.ThreadsFragment;
import com.google.android.material.tabs.TabLayout;

/**
 *
 */
public class SportActivity extends Activity {
    private final String EXTRA_SPORT_ID = "com.example.sports_app.sport_name";

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
        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);
        InstantiateUIElements();
    }
}

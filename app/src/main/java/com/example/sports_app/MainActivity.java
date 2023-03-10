package com.example.sports_app;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.fragments.SelectSportFragment;
import com.example.sports_app.networking.LoginManagement;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Breytur fyrir aðalvalmynd //
    private ActionMenuView mActionMenuView;
    private MenuItem mMenuItemLogin;
    private MenuItem mMenuItemLogout;
    private MenuItem mMenuItemSport;
    // ------------------------ //
    private static final int REQUEST_THREAD_OPEN = 0;
    private ArrayList<Thread> threads;
    ListView mThreadList;
    private static ThreadListAdapter sThreadListAdapter;
    private ThreadService mThreadService;

    private FragmentContainerView mFragmentContainerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mThreadList = (ListView) findViewById(R.id.threadList);
        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);


        // TODO: Sækja og geyma þræði í ThreadService. EÐA: Geyma hér og vinna með þá í ThreadService?
        getAllThreads();

        // TODO: laga að getComments() á Dummy þræði frá bakenda valda NullPointerException
        mThreadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long threadToOpenId = threads.get(i).getId();
                String sport = threads.get(i).getSport();
                System.out.println(sport);
                Intent intent = ThreadActivity.newIntent(MainActivity.this, threadToOpenId);
                startActivityForResult(intent, REQUEST_THREAD_OPEN);
            }
        });

        createNotificationChannel();
    }

    private void getAllThreads() {
        NetworkManager sNetworkManager = NetworkManager.getInstance(this);
        mThreadService = new ThreadService();
        sNetworkManager.getAllTheThreads(new NetworkCallback<ArrayList<Thread>>() {
            @Override
            public void onSuccess(ArrayList<Thread> result) {
                threads = result;

                for (Thread t : threads) {
                    for (Comment c : t.getComments()) {
                        System.out.println(c.getComment());
                    }
                }
                sThreadListAdapter = new ThreadListAdapter(threads, getApplicationContext());
                mThreadList.setAdapter(sThreadListAdapter);
            }

            @Override
            public void onFailure(String errorString) {
                Log.e("Threadservice", "Failed to get threads dvia REST");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionview, menu);
        mMenuItemLogin = menu.findItem(R.id.menu_login);
        mMenuItemLogout = menu.findItem(R.id.menu_logout);
        mMenuItemSport = menu.findItem(R.id.menu_sport);

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
        //mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);
        //mActionMenuView.setBackgroundColor(304032);
        return true;
    }



    // TODO: Spurnig ef header er gerður að fragmenti, þá þarf ekki að implementa þetta
    // TODO: í öllum activities.
    public void logout() {
        if (getIntent().getExtras().getString("com.example.sports_app.username") != null &&
                getIntent().getExtras().getString("com.example.sports_app.password") != null &&
                getIntent().getExtras().getString("com.example.sports_app.username") != "" &&
                getIntent().getExtras().getString("com.example.sports_app.password") != "") {

            NetworkManager sNetworkManager = NetworkManager.getInstance(this);
            String username = getIntent().getExtras().getString("com.example.sports_app.username");
            String password = getIntent().getExtras().getString("com.example.sports_app.password");
            sNetworkManager.logout(username, password, new NetworkCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("com.example.sports_app.isLoggedIn", false);
                    intent.putExtra("com.example.sports_app.username", "");
                    intent.putExtra("com.example.sports_app.password", "");
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
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.menu_logout:
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                LoginManagement loginManagement = LoginManagement.getInstance(MainActivity.this);
                logout();
                i.putExtra("com.example.sports_app.isLoggedIn", false);
                startActivity(i);
                break;
            case R.id.menu_sport:
                if (mFragmentContainerView.getVisibility() == View.GONE) {
                    mFragmentContainerView.setVisibility(View.VISIBLE);
                    Fragment fragment = new SelectSportFragment();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.fragmentContainerView, fragment)
                            .addToBackStack(null)
                            .commit();
                } else mFragmentContainerView.setVisibility(View.GONE);

                notifyClickedSport(); // TODO: Eyða þessu - bara til að prufa notifications
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Þetta er bara til að prufa að búa til notification
    private void notifyClickedSport() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Það activity sem á að opnast þegar ýtt er á notificationið
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "myChannel");
            builder.setContentTitle("Sport!");
            builder.setContentText("You clicked Sport!");
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setContentIntent(pendingIntent); // Opnar pendingIntent þegar klikkað á notification
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(1, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CharSequence name"; // TODO: Bæta þessu við í resources -> getString(R.string.channel_name);
            String description = "Description"; // TODO: Bæta þessu líka við í resources -> getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("myChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
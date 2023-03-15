package com.example.sports_app.activities;

import static java.lang.Thread.sleep;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentContainerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sports_app.R;
import com.example.sports_app.adapters.ThreadListAdapter;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    // Breytur fyrir aðalvalmynd //

    // ------------------------ //
    private static final int REQUEST_THREAD_OPEN = 0;
    private ArrayList<Thread> threads;
    ListView mThreadList;
    private static ThreadListAdapter sThreadListAdapter;
    private ThreadService mThreadService;
    private boolean isAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);

        try {
            System.out.println("Mod: " + getIntent().getExtras().getBoolean("com.example.sports_app.isModerator"));
        } catch (Exception e) {
            System.out.println("Catch - Mod: " + false);
        }

        try {
            isAdmin = getIntent().getExtras().getBoolean("com.example.sports_app.isAdmin");
        } catch (Exception e) {
            isAdmin = false;
        }

        mThreadList = (ListView) findViewById(R.id.threadList);
        // TODO: Sækja og geyma þræði í ThreadService. EÐA: Geyma hér og vinna með þá í ThreadService?
        getAllThreads();

        mThreadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long threadToOpenId = threads.get(i).getId();
                String sport = threads.get(i).getSport();
                boolean loggedIn;
                try {
                    loggedIn = getIntent().getExtras().getBoolean("com.example.sports_app.loggedIn");
                } catch (Exception e) {
                    loggedIn = false;
                }



                Intent intent = ThreadActivity.newIntent(MainActivity.this, threadToOpenId);
                intent.putExtra("com.example.sports_app.loggedIn", loggedIn);
                intent.putExtra("com.example.sports_app.isAdmin", isAdmin);
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
                sThreadListAdapter = new ThreadListAdapter(threads, getApplicationContext());
                mThreadList.setAdapter(sThreadListAdapter);
            }

            @Override
            public void onFailure(String errorString) {
                Log.e("Threadservice", "Failed to get threads dvia REST");
            }
        });
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

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
}
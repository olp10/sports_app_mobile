package com.example.sports_app.activities;

import static java.lang.Thread.sleep;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sports_app.R;
import com.example.sports_app.adapters.ThreadListAdapter;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.example.sports_app.services.ThreadService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

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

    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(this, 10000);
        }
    };
    public void getToken() {
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);

        handler.post(runnableCode);

        getToken();
        // Breytur fyrir aðalvalmynd //
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

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }


}
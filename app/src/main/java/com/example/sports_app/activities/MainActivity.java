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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sports_app.R;
import com.example.sports_app.adapters.ThreadListAdapter;
import com.example.sports_app.entities.Event;
import com.example.sports_app.entities.Message;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String EXTRA_USER = "com.example.sports_app.username";

    // Breytur fyrir aðalvalmynd //
    private static final int REQUEST_THREAD_OPEN = 0;
    private ArrayList<Thread> threads;
    ListView mThreadList;
    private static ThreadListAdapter sThreadListAdapter;
    private boolean isAdmin;

    TextView mThreadCreator;
    TextView loggedInAsTextview;
    TextView userProfileLink;
    private String loggedInUser;

    ArrayList<Message> mMessages;
    NetworkManager sNetworkManager;

    ImageView mUserProfileIcon;

    boolean notificationRead = false;

    RelativeLayout mBottomBarContainer;

    Handler handler = new Handler();
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if (loggedInUser != null && !loggedInUser.equals("")) {
                sNetworkManager.getMessages(loggedInUser, new NetworkCallback<ArrayList<Message>>() {
                    @Override
                    public void onSuccess(ArrayList<Message> messages) {
                        if (messages != null) {
                            for (Message message : messages) {
                                if (!message.isRead() && message.getThreadCreator().equals(loggedInUser)) {
                                    notifyNewMessage(message);

                                    // TODO: Eyða skilaboðum úr gagnagrunni frekar en að merkja sem lesin?
                                    // TODO: Er einhver ástæða fyrir að geyma notification skilaboð?
                                    // Þarf að merkja skilaboðin sem lesin til þess að fá þau ekki í hvert skipti sem lúppa keyrir
                                    // Ekki nóg að gera það í minni, þar sem skilaboð eru alltaf sótt í gagnagrunn í hverri lúppu
                                    sNetworkManager.setMessageRead(loggedInUser, message.getmId(), new NetworkCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.d(TAG, "onSuccess: Message marked as read");
                                        }

                                        @Override
                                        public void onFailure(String errorString) {
                                            Log.e(TAG, "Failed to mark message as read: " + errorString);
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorString) {
                        Toast.makeText(MainActivity.this, errorString, Toast.LENGTH_SHORT).show();
                    }
                });

                sNetworkManager.getAllEvents(new NetworkCallback<ArrayList<Event>>() {
                    @Override
                    public void onSuccess(ArrayList<Event> result) {
                        for (Event event : result) {
                            if (event.isInLessThan24Hours() && !notificationRead) {
                                notifyNewEvent(event);
                                notificationRead = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorString) {
                        System.out.println(errorString);
                    }
                });
            }
            handler.postDelayed(this, 10000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sNetworkManager = NetworkManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sports_app", MODE_PRIVATE);
        String user = sharedPreferences.getString("logged_in_user", "");

        mUserProfileIcon = (ImageView) findViewById(R.id.user_profile_icon);
        mUserProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        loggedInAsTextview = (TextView) findViewById(R.id.logged_in_as_textview);
        //userProfileLink = (TextView) findViewById(R.id.link_to_user_profile);
        ImageView userProfileIcon = (ImageView) findViewById(R.id.user_profile_icon);
        userProfileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            intent.putExtra("com.example.sports_app.userClicked", loggedInUser);
            intent.putExtra("com.example.sports_app.loggedInUser", loggedInUser);
            intent.putExtra("com.example.sports_app.username", loggedInUser);
            startActivity(intent);
        });

        mBottomBarContainer = (RelativeLayout) findViewById(R.id.bottom_bar_container);

        bottomBar = (ActionMenuView) findViewById(R.id.menu_bottom_menu);
        Menu bottomMenu = bottomBar.getMenu();
        getMenuInflater().inflate(R.menu.menu_bottom_menu, bottomMenu);

        mFragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);

        // Byrjar lúppu sem sækir skilaboð til notanda
        handler.post(runnableCode);

        checkUserAndPermissions();
        createThreadList();
        createNotificationChannel();


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserAndPermissions();
        loggedInUser = getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getString("logged_in_user", "");
        Log.d(TAG, "onResume - user: " + loggedInUser);
        if (loggedInUser != null) {
            loggedInAsTextview.setText("Logged in as: " + loggedInUser);
            // userProfileLink.setVisibility(View.VISIBLE);
        }
    }


    /**
     *  Sets variables for user and admin/moderator status
     */
    private void checkUserAndPermissions() {
        try {
            loggedInUser = getSharedPreferences("com.example.sports_app", MODE_PRIVATE).getString("logged_in_user", "");
            if (loggedInUser != null && !loggedInUser.equals("")) {
                mBottomBarContainer.setVisibility(View.VISIBLE);
                loggedInAsTextview.setText("Logged in as: " + loggedInUser);
            } else {
                mBottomBarContainer.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            loggedInUser = "";
        }
    }

    /**
     *  Initializes the UI for the main Thread list
     */
    private void createThreadList() {
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
                intent.putExtra(EXTRA_USER, loggedInUser);
                startActivityForResult(intent, REQUEST_THREAD_OPEN);
            }
        });
    }


    /**
     * returns all threads from REST service
     */
    private void getAllThreads() {
        NetworkManager sNetworkManager = NetworkManager.getInstance(this);
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


    /**
     * Creates a new notification when user receives a new message
     * @param message
     */
    private void notifyNewMessage(Message message) {

        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Það activity sem á að opnast þegar ýtt er á notificationið
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "myChannel");
            builder.setContentTitle(message.getMessage());
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setContentIntent(pendingIntent); // Opnar pendingIntent þegar klikkað á notification
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(1, builder.build());
        }
    }

    private void notifyNewEvent(Event event) {

        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Það activity sem á að opnast þegar ýtt er á notificationið
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "myChannel");
            builder.setContentTitle(event.getEventName() + " happening in less than 24 hours!");
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setContentIntent(pendingIntent); // Opnar pendingIntent þegar klikkað á notification
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            managerCompat.notify(1, builder.build());
        }
    }

    private void createNotificationChannel() {
        askNotificationPermission();
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
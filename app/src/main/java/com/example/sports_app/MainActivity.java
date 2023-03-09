package com.example.sports_app;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
    // ------------------------ //
    private static final int REQUEST_THREAD_OPEN = 0;
    private ArrayList<Thread> threads;
    ListView mThreadList;
    private static ThreadListAdapter sThreadListAdapter;
    private ThreadService mThreadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);
        mThreadList = (ListView) findViewById(R.id.threadList);

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
                Log.e("Threadservice", "Failed to get threads via REST");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionview, menu);
        mMenuItemLogin = menu.findItem(R.id.menu_login);
        mMenuItemLogout = menu.findItem(R.id.menu_logout);

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
}
package com.example.sports_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sports_app.entities.Thread;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<Thread> threads;
    ActionMenuView mActionMenuView;
    ListView mThreadList;
    private static ThreadListAdapter threadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionMenuView = (ActionMenuView) findViewById(R.id.toolbar_bottom);
        mThreadList = (ListView) findViewById(R.id.threadList);

        // DUMMY GÖGN FYRIR Þráða listann
        threads = new ArrayList<Thread>();
        for (int i = 0; i < 20; i++) {
            threads.add(new Thread(i, "someUser", false, "Dummy thread " + String.valueOf(i), "Blabla", "Badminton"));
        }

        // Smíða Layout element fyrir þræði
        threadListAdapter = new ThreadListAdapter(threads, getApplicationContext());
        mThreadList.setAdapter(threadListAdapter);

        // TODO: Opna þráð sem clickað er á
        mThreadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Thread thread = threads.get(i);

                Snackbar.make(view, thread.getmHeader(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        /*
        NetworkManager networkManager = NetworkManager.getInstance(this);

        // Prufa að tengjast við vefþjónustuna - Má eyða
        networkManager.getAllThreads(new NetworkCallback<List<Thread>>() {
            @Override
            public void onSuccess(List<Thread> result) {
                threads = result;
                for (Thread t : threads) {
                    Log.d(TAG, t.getmHeader());
                }
            }
            @Override
            public void onFailure(String errorString) {
                Log.d(TAG, "onFailure: Error");
            }
        });
        Log.d(TAG, "end");
        */
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void login() {

    }
}
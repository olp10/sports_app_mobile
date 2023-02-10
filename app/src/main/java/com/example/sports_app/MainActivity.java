package com.example.sports_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.sports_app.entities.Thread;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<Thread> threads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkManager networkManager = NetworkManager.getInstance(this);
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
    }
}
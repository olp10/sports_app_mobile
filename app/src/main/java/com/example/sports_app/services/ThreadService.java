package com.example.sports_app.services;

import android.content.Context;
import android.util.Log;

import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ThreadService {

    private ArrayList<Thread> threads;;
    private NetworkManager sNetworkManager;

    public ThreadService() {

        // Búa til dummy gögn
//        for (int i = 0; i < 20; i++) {
//            threads.add(new Thread(i, "someUser", false,
//                    "Dummy thread " + i, "Blabla", "Badminton"));
//        }
//
//        // Setja comments inn í dummy þráð
//        Thread testThread = new Thread(50, "Jakob", false, "Dummy thread " + 50,
//                "Blabla og já blalbalblalabalba. oOOOOOooooooadakdaodkaosdkaodkaosdkasodaodkaosdkaosdkaodadasivjaidvaoivdasoiv", "Polo");
//        User testUser = new User("TestUser", "test");
//        LocalDate date = LocalDate.now();
//        String c = "Blablablablablablablablablablablabal";
//        long l = 0;
//        Comment comment = new Comment(l, testUser, date, date, c, testThread);
//        Comment comment2 = new Comment(l+1, testUser, date, date, c, testThread);
//        testThread.addComment(comment);
//        testThread.addComment(comment2);
//        threads.add(testThread);
    }

    // Test: kalla á RESTFUL bakendann
    public void getAllThreads(Context context) {
        sNetworkManager = NetworkManager.getInstance(context);
        sNetworkManager.getAllTheThreads(new NetworkCallback<ArrayList<Thread>>() {
            @Override
            public void onSuccess(ArrayList<Thread> result) {
                Log.d("Threadservice","I got the threads!");
                setThreads(result);
            }

            @Override
            public void onFailure(String errorString) {
                Log.e("Threadservice", "Failed to get threads via REST");
            }
        });
    }

    // ÞETTA ER ALLT FYRIR DUMMY GÖGNIN
    // TODO: Láta föll kalla á NetworkManager
    public void addComment(Comment comment, Thread thread) {

    }

    public void setThreads(ArrayList<Thread> threads) {
        this.threads = threads;
    }

    public Thread findThreadById(long id) {
        for (Thread t : threads) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    private String S = "blabla";
    public String getString(){
        return S;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }
}

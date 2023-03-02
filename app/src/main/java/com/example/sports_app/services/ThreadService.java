package com.example.sports_app.services;

import android.util.Log;

import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThreadService {

    private ArrayList<Thread> threads;
    NetworkManager mNetworkManager;
    private List<Thread> mThreadBank;

    public ThreadService() {

        // Búa til dummy gögn
        threads = new ArrayList<Thread>();
        for (int i = 0; i < 20; i++) {
            threads.add(new Thread(i, "someUser", false,
                    "Dummy thread " + i, "Blabla", "Badminton"));
        }

        // Setja comments inn í dummy þráð
        Thread testThread = new Thread(50, "Jakob", false, "Dummy thread " + 50,
                "Blabla og já blalbalblalabalba. oOOOOOooooooadakdaodkaosdkaodkaosdkasodaodkaosdkaosdkaodadasivjaidvaoivdasoiv", "Polo");
        User testUser = new User("TestUser", "test");
        LocalDate date = LocalDate.now();
        String c = "Blablablablablablablablablablablabal";
        long l = 0;
        Comment comment = new Comment(l, testUser, date, date, c, testThread);
        Comment comment2 = new Comment(l+1, testUser, date, date, c, testThread);
        testThread.addComment(comment);
        testThread.addComment(comment2);
        threads.add(testThread);
    }

    // ÞETTA ER ALLT FYRIR DUMMY GÖGNIN
    // TODO: Láta föll kalla á NetworkManager
    public void addComment(Comment comment, Thread thread) {

    }

    public Thread findThreadById(long id) {
        for (Thread t : threads) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }


    public void getAllThreads() {
        mNetworkManager.getAllThreads(new NetworkCallback<List<Thread>>() {
            @Override
            public void onSuccess(List<Thread> result) {
                mThreadBank = result;
            }

            @Override
            public void onFailure(String errorString) {
                Log.d("ThreadService", errorString);
            }
        });
    }

    public void updateThreads(List<Thread> res) {

    }

}

package com.example.sports_app.services;

import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Thread;

import java.util.ArrayList;

public class ThreadService {

    private ArrayList<Thread> threads;

    public ThreadService() {

        // Búa til dummy gögn
        threads = new ArrayList<Thread>();
        threads.add(new Thread(50, "Jakob", false, "Dummy thread " + 50,
                "Blabla og já blalbalblalabalba. oOOOOOooooooadakdaodkaosdkaodkaosdkasodaodkaosdkaosdkaodadasivjaidvaoivdasoiv", "Polo"));
        for (int i = 0; i < 20; i++) {
            threads.add(new Thread(i, "someUser", false,
                    "Dummy thread " + i, "Blabla", "Badminton"));
        }
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
}

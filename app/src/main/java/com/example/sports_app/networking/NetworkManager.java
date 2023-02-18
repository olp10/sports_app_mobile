package com.example.sports_app.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sports_app.entities.Comment;
import com.example.sports_app.entities.Event;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Superclass fyrir network köll
 */
public class NetworkManager {

    private static final String TAG = "NetworkManager";
    // Þessi IP tala er til þess að appið noti localhost á tölvunni, ekki á símanum/emulatornum
    // Breyta þessu þegar verið að nota á live (Railway) þjón
    private static final String BASE_URL = "https://hugbunadarverkefni2-production.up.railway.app/";

    private static NetworkManager mInstance;
    private static RequestQueue mQueue;
    private Context mContext;

    public static synchronized NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    private NetworkManager(Context context) {
        mContext = context;
        mQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mQueue;
    }

    public void getAllThreads(NetworkCallback<List<Thread>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "allThreads", response -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Thread>>(){}.getType();
                    List<Thread> threads = gson.fromJson(response, listType);
                    callback.onSuccess(threads);
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        );
        mQueue.add(request);
    }

    /***         GET REQUESTUR hér fyrir neðan          ***/

    public void getThreadById(NetworkCallback<Thread> callback) {
        // TODO
    }

    public void getThreadsBySport(NetworkCallback<List<Thread>> callback) {
        // TODO
    }

    public void getThreadsByUser(NetworkCallback<List<Thread>> callback) {
        // TODO
    }

    public void getCommentsByThreadId(NetworkCallback<List<Comment>> callback) {
        // TODO
    }

    public void getAllEvents(NetworkCallback<List<Event>> callback) {
        // TODO
    }

    public void getEventsBySport(NetworkCallback<List<Event>> callback) {
        // TODO
    }

    public void getEventById(NetworkCallback<Event> callback) {
        // TODO
    }

    public void getTodaysEvents(NetworkCallback<List<Event>> callback) {
        // TODO
    }

    /****     POST REQUESTUR hér fyrir neðan      ****/

    public void postUserSignUp(User user, NetworkCallback<User> callback) {
        // TODO
    }
}

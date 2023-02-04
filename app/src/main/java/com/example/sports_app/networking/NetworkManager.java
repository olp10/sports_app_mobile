package com.example.sports_app.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sports_app.Thread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Superclass fyrir network köll
 */
public class NetworkManager {

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
                Request.Method.GET, BASE_URL + "allThreads", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Thread>>(){}.getType();
                List<Thread> threads = gson.fromJson(response, listType);
                callback.onSuccess(threads);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        );
        mQueue.add(request);
    }
}

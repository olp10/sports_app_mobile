package com.example.sports_app.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sports_app.entities.Thread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Superclass fyrir network köll
 */
public class NetworkManager {

    private static final String TAG = "NetworkManager";
    // Þessi IP tala er til þess að appið noti localhost á tölvunni, ekki á símanum/emulatornum
    // Breyta þessu þegar verið að nota á live (Railway) þjón
//    private static final String BASE_URL = "https://hugbunadarverkefni2-production.up.railway.app/";

    private static final String BASE_URL = "http://10.0.2.2:8080";

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

    public void getAllTheThreads(final NetworkCallback<ArrayList<Thread>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/allThreads", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type arrayListType = new TypeToken<ArrayList<Thread>>() {
                }.getType();
                ArrayList<Thread> threads = gson.fromJson(response, arrayListType);
                callback.onSuccess(threads);
            }
        }, error -> callback.onFailure(error.toString())
        );
        mQueue.add(request);
    }

    public ArrayList<Thread> getAllThreads(NetworkCallback<ArrayList<Thread>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/allThreads", response -> {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Thread>>(){}.getType();
                    ArrayList<Thread> threads = gson.fromJson(response, listType);
                    callback.onSuccess(threads);
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        );
        mQueue.add(request);
        return null;
    }

    public void login(String username, String password, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/login", response ->
                callback.onSuccess(response), error ->
                callback.onFailure(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        mQueue.add(request);
    }

}

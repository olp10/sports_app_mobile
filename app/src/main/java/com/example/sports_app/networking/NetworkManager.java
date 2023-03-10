package com.example.sports_app.networking;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sports_app.entities.Club;
import com.example.sports_app.entities.Event;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void getAllSports(final NetworkCallback<ArrayList<String>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/sports", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type arrayListType = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> sports = gson.fromJson(response, arrayListType);
                System.out.println(sports);
                callback.onSuccess(sports);
            }
        }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
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
                System.out.println(threads);
                callback.onSuccess(threads);
            }
        }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getAllEventsForSport(String sport, final NetworkCallback<ArrayList<Event>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/home/" + sport + "/events", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type arrayListType = new TypeToken<ArrayList<Event>>() {
                }.getType();
                ArrayList<Event> events = gson.fromJson(response, arrayListType);
                callback.onSuccess(events);
            }
        }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getAllClubsForSport(String sport, final NetworkCallback<ArrayList<Club>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/home/" + sport + "/clubs", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type arrayListType = new TypeToken<ArrayList<Club>>() {
                }.getType();
                ArrayList<Club> clubs = gson.fromJson(response, arrayListType);
                callback.onSuccess(clubs);
            }
        }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getAllThreadsForSport(String sport, final NetworkCallback<ArrayList<Thread>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/home/" + sport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type arrayListType = new TypeToken<ArrayList<Thread>>() {
                }.getType();
                ArrayList<Thread> threads = gson.fromJson(response, arrayListType);
                callback.onSuccess(threads);
            }
        }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getThreadById(Long id, final NetworkCallback<Thread> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("thread")
                .appendPath(String.valueOf(id))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Thread thread = gson.fromJson(response, Thread.class);
                callback.onSuccess(thread);
            }
        }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void postNewComment(
            Long userId, String commentBody, Long threadId, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/newComment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, error -> callback.onFailure(error.toString())){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String,String>();
                params.put("userId", userId.toString());
                params.put("commentBody", commentBody);
                params.put("threadId", threadId.toString());

                return params;
            }
        };
        mQueue.add(request);
    }

    public void logout(String username, String password, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/logout", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, error -> callback.onFailure(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", password);
                params.put("username", username);
                return params;
            }
        };
        mQueue.add(request);
    }

    public void login(String username, String password, final NetworkCallback<User> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    callback.onSuccess(user);
                }
            }, error -> callback.onFailure(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        mQueue.add(request);
    }


    // TODO: Þetta virkar, er að stússast í öðru
    /*
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

     */

    // Líklega ekki þörf fyrir þetta lengur en gæti mögulega komið að notum í öðru samhengi?
//    private Gson getLocalDateTimeAdapter() {
//        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
//            @Override
//            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSS"));
//            }
//        }).create();
//        return gson;
//    }
}

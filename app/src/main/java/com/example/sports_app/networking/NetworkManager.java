package com.example.sports_app.networking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sports_app.entities.Club;
import com.example.sports_app.entities.Event;
import com.example.sports_app.entities.Message;
import com.example.sports_app.entities.Thread;
import com.example.sports_app.entities.User;
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

    //Nota fyrir localhost
    private static final String BASE_URL = "http://10.0.2.2:8080";

    // Nota þennan fyrir live (Railway) þjón
    //private static final String BASE_URL = "https://hugbunadarverkefni2-production.up.railway.app";

    private static NetworkManager mInstance;
    private static RequestQueue mQueue;
    private final Context mContext;

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

    public void deleteComment(Long id, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.DELETE, BASE_URL + "/comments/"+id+"/delete", callback::onSuccess, error -> callback.onFailure(error.toString())){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("id", id.toString());
                System.out.println("Params: " + params.get("commentId"));
                Log.d(TAG, "getParams: " + params.get("commentId"));
                return params;
            }
        };
        mQueue.add(request);
    }

    public void getAllSports(final NetworkCallback<ArrayList<String>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/sports", response -> {
                    Gson gson = new Gson();
                    Type arrayListType = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    ArrayList<String> sports = gson.fromJson(response, arrayListType);
                    System.out.println(sports);
                    callback.onSuccess(sports);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getUserByUsername(String username, final NetworkCallback<User> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/userInfo/"+username, response -> {
                    Gson gson = new Gson();
                    Type userType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(response, userType);
                    callback.onSuccess(user);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getUserBanned(String username, final NetworkCallback<Boolean> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/userInfo/"+username+"/isBanned", response -> {
                    Gson gson = new Gson();
                    Boolean banned = gson.fromJson(response, Boolean.class);
                    callback.onSuccess(banned);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getMessages(String username, final NetworkCallback<ArrayList<Message>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/userInfo/"+username+"/messages", response -> {
                    Gson gson = new Gson();
                    Type arrayListType = new TypeToken<ArrayList<Message>>() {
                    }.getType();
                    ArrayList<Message> messages = gson.fromJson(response, arrayListType);
                    callback.onSuccess(messages);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void setMessageRead(String username, long messageId, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.PUT, BASE_URL + "/userInfo/"+username+"/messages/"+messageId, response -> {
                    callback.onSuccess(response);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void userModeratesSport(String sport, String username, final NetworkCallback<Boolean> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/userInfo/"+username+"/moderates/"+sport, response -> {
                    Gson gson = new Gson();
                    Boolean moderated = gson.fromJson(response, Boolean.class);
                    callback.onSuccess(moderated);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void banUser(String username, final NetworkCallback<User> callback) {
        StringRequest request = new StringRequest(
                Request.Method.PUT, BASE_URL + "/banUser/"+username, response -> {
                    Gson gson = new Gson();
                    Type userType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(response, userType);
                    callback.onSuccess(user);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void unbanUser(String username, final NetworkCallback<User> callback) {
        StringRequest request = new StringRequest(
                Request.Method.PUT, BASE_URL + "/unbanUser/"+username, response -> {
                    Gson gson = new Gson();
                    Type userType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(response, userType);
                    callback.onSuccess(user);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getAllTheThreads(final NetworkCallback<ArrayList<Thread>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/allThreads", response -> {
                    Gson gson = new Gson();
                    Type arrayListType = new TypeToken<ArrayList<Thread>>() {
                    }.getType();
                    ArrayList<Thread> threads = gson.fromJson(response, arrayListType);
                    for (Thread t : threads) {
                        System.out.println("Username: " + t.getUsername());
                    }
                    callback.onSuccess(threads);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getAllEventsForSport(String sport, final NetworkCallback<ArrayList<Event>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/home/" + sport + "/events", response -> {
                    Gson gson = new Gson();
                    Type arrayListType = new TypeToken<ArrayList<Event>>() {
                    }.getType();
                    ArrayList<Event> events = gson.fromJson(response, arrayListType);
                    callback.onSuccess(events);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getAllClubsForSport(String sport, final NetworkCallback<ArrayList<Club>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/home/" + sport + "/clubs", response -> {
                    Gson gson = new Gson();
                    Type arrayListType = new TypeToken<ArrayList<Club>>() {
                    }.getType();
                    ArrayList<Club> clubs = gson.fromJson(response, arrayListType);
                    callback.onSuccess(clubs);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }



    public void getAllThreadsForSport(String sport, final NetworkCallback<ArrayList<Thread>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "/home/" + sport, response -> {
                    Gson gson = new Gson();
                    Type arrayListType = new TypeToken<ArrayList<Thread>>() {
                    }.getType();
                    ArrayList<Thread> threads = gson.fromJson(response, arrayListType);
                    callback.onSuccess(threads);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void saveThread(Thread thread, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/saveThread", callback::onSuccess, error -> callback.onFailure(error.toString())){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("title", thread.getHeader());
                params.put("body", thread.getBody());
                params.put("sport", thread.getSport());
                params.put("user", thread.getUsername());
                return params;
            }
        };
        mQueue.add(request);
    }

    public void saveEvent(Event event, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/saveEvent", callback::onSuccess, error -> callback.onFailure(error.toString())){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("title", event.getEventName());
                params.put("description", event.getEventDescription());
                params.put("sport", event.getSport());
                params.put("startingDate", event.getEventStartDate());
                return params;
            }
        };
        mQueue.add(request);
    }

    public void getThreadById(Long id, final NetworkCallback<Thread> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("thread")
                .appendPath(String.valueOf(id))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, response -> {
                    Gson gson = new Gson();
                    Thread thread = gson.fromJson(response, Thread.class);
                    callback.onSuccess(thread);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void getEventById(Long id, final NetworkCallback<Event> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("event")
                .appendPath(String.valueOf(id))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, response -> {
                    Gson gson = new Gson();
                    Event event = gson.fromJson(response, Event.class);
                    callback.onSuccess(event);
                }, error -> callback.onFailure(error.toString()));
        mQueue.add(request);
    }

    public void subscribeToEvent(Long eventId, Long userId, final NetworkCallback<String> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("event")
                .appendPath(String.valueOf(eventId))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.POST, url, callback::onSuccess, error -> callback.onFailure(error.toString())){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("userId", userId.toString());

                return params;
            }
        };
        mQueue.add(request);
    }


    public void postNewComment(
            String username, String commentBody, Long threadId, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/newComment", callback::onSuccess, error -> callback.onFailure(error.toString())){
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("commentBody", commentBody);
                params.put("threadId", threadId.toString());

                return params;
            }
        };
        mQueue.add(request);
    }

    public void logout(String username, String password, final NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/logout", callback::onSuccess, error -> callback.onFailure(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("username", username);
                return params;
            }
        };
        mQueue.add(request);
    }

    public void login(String username, String password, final NetworkCallback<User> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/login", response -> {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    callback.onSuccess(user);
                }, error -> callback.onFailure(error.toString())) {
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

    public void createUser(String username, String password, final NetworkCallback<User> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/register", response -> {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    callback.onSuccess(user);
                }, error -> callback.onFailure(error.toString())) {
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


    public void checkIfUsernameTaken(String username, final NetworkCallback<Boolean> callback) {
        StringRequest request = new StringRequest(
                Request.Method.POST, BASE_URL + "/checkUsername", response -> {
                    Gson gson = new Gson();
                    Boolean isTaken = gson.fromJson(response, Boolean.class);
                    callback.onSuccess(isTaken);
                }, error -> callback.onFailure(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        mQueue.add(request);
    }

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

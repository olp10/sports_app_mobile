package com.example.sports_app.networking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.sports_app.LoginActivity;
import com.example.sports_app.MainActivity;


// Tilraun til að aðskilja login/logout ferlið - gekk ekki nógu vel

public class LoginManagement {

    private static LoginManagement mInstance;
    private Context mContext;

    public static synchronized LoginManagement getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LoginManagement(context);
        }
        return mInstance;
    }

    private LoginManagement(Context context) {
        mContext = context;
    }
    public void logout(Intent i, Context context) {
        if (i.getExtras().getString("com.example.sports_app.username") != null &&
                i.getExtras().getString("com.example.sports_app.password") != null &&
                i.getExtras().getString("com.example.sports_app.username") != "" &&
                i.getExtras().getString("com.example.sports_app.password") != "") {

            NetworkManager sNetworkManager = NetworkManager.getInstance(context);
            String username = i.getExtras().getString("com.example.sports_app.username");
            String password = i.getExtras().getString("com.example.sports_app.password");
            sNetworkManager.logout(username, password, new NetworkCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("com.example.sports_app.loggedIn", false);
                    intent.putExtra("com.example.sports_app.username", "");
                    intent.putExtra("com.example.sports_app.password", "");
                    // startActivity(intent); // FIXME plz
                }

                @Override
                public void onFailure(String errorString) {
                    Log.e("Threadservice", "Failed to logout via REST");
                }
            });
        }
    }
}

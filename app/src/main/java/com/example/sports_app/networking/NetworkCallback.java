package com.example.sports_app.networking;

public interface NetworkCallback<T> {

    void onSuccess(T result);
    void onFailure(String errorString);
}

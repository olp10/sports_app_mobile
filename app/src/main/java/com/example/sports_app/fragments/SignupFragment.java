package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sports_app.activities.MainActivity;
import com.example.sports_app.R;
import com.example.sports_app.entities.User;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;

public class SignupFragment extends Fragment {
    private final String TAG = "SignupFragment";
    private static final String EXTRA_USERNAME = "com.example.sports_app.username";
    private EditText txtUsername;
    private String mUsername = "";
    private EditText txtPassword;
    private String mPassword = "";
    private EditText txtConfirmPassword;
    private Button mSignUpButton;
    NetworkManager sNetworkManager = NetworkManager.getInstance(getActivity());

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtUsername = (EditText) getActivity().findViewById(R.id.txt_username);
        txtPassword = (EditText) getActivity().findViewById(R.id.txt_password);
        txtConfirmPassword = (EditText) getActivity().findViewById(R.id.txt_confirm_password);
        mSignUpButton = (Button) getActivity().findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = txtUsername.getText().toString();
                checkIfUsernameTaken(mUsername);
            }
        });
    }

    private boolean checkIfUsernameTaken(String username) {
        sNetworkManager.checkIfUsernameTaken(username, new NetworkCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    makeShortToast("Username already taken");
                } else {
                    mPassword = txtPassword.getText().toString();
                    if (mUsername.length() < 5) makeShortToast("Username must be at least 5 characters");
                    else if (mPassword.length() < 5) makeShortToast("Password must be at least 5 characters");
                    else if (!mPassword.equals(txtConfirmPassword.getText().toString())) makeShortToast("Passwords do not match");
                    else {
                        signUp(mUsername, mPassword);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("com.example.sports_app.loggedIn", true);
                        intent.putExtra(EXTRA_USERNAME, mUsername);
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onFailure(String errorString) {
                System.err.println("Villa við að athuga hvort notendanafn sé tekið");
            }
        });
        return false;
    }

    private void signUp(String username, String password) {
        sNetworkManager.createUser(username, password, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                makeShortToast("User successfully created!");
            }

            @Override
            public void onFailure(String errorString) {
                System.err.println("Could not register user");
            }
        });
    }

    private void makeShortToast(String message) {
        Toast t = new Toast(getContext().getApplicationContext());
        t.setDuration(Toast.LENGTH_SHORT);
        t.setText(message);
        t.show();
    }
}

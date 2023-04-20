package com.example.sports_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.sports_app.R;

public class ContactUsFragment extends Fragment {
    private static final String EXTRA_USERNAME = "com.example.sports_app.username";
    private static final String EXTRA_PASSWORD = "com.example.sports_app.password";
    private static final String EXTRA_LOGGED_IN = "com.example.sports_app.loggedIn";
    private static final String TAG = "UserSettingsFragment";

    public ContactUsFragment() {
        // Required empty public constructor
    }

    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentContainerView mFragmentContainerView;
                mFragmentContainerView = getActivity().findViewById(R.id.fragmentContainerView);
                mFragmentContainerView.setVisibility(View.GONE);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }
}

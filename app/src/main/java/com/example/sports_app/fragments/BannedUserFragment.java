package com.example.sports_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sports_app.R;

public class BannedUserFragment extends Fragment {

    Button buttonReasonForUnban;
    public BannedUserFragment() {
        // Required empty public constructor
    }

    public static BannedUserFragment newInstance() {
        BannedUserFragment fragment = new BannedUserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banned_user, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonReasonForUnban = view.findViewById(R.id.button_reason_for_unban);
        buttonReasonForUnban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Send message to admin - Búa til sameiginlegt inbox fyrir alla admin?
                Toast t = Toast.makeText(getContext(), "Message sent! We will consider if your reasoning is valid", Toast.LENGTH_SHORT);
                t.show();
            }
        });
    }
}

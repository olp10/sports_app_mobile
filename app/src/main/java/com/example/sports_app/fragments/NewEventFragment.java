package com.example.sports_app.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sports_app.R;
import com.example.sports_app.entities.Event;
import com.example.sports_app.networking.NetworkCallback;
import com.example.sports_app.networking.NetworkManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment {

    private static final String TAG = "NewEventFragment";

    private static final String EXTRA_SPORT_NAME = "com.example.sports_app.sport_name";

    private Button mSubmitNewEventButton;
    private TextInputLayout mNewEventTitleLabel;
    private EditText mNewEventTitleText;
    private TextInputLayout mNewEventDescriptionLabel;
    private EditText mNewEventDescriptionText;
    private TextInputLayout mNewEventDateLabel;
    private EditText mNewEventDate;
    private TextInputLayout mNewEventTimeLabel;
    private EditText mNewEventTime;
    private String mNewEventDateTimeFormatted;

    private NetworkManager mNetworkManager = NetworkManager.getInstance(getContext());



    public NewEventFragment() {
        // Required empty public constructor
    }


    public static NewEventFragment newInstance() {
        NewEventFragment fragment = new NewEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mNewEventTitleLabel = (TextInputLayout) view.findViewById(R.id.new_event_title_input_layout);
        mNewEventTitleText = (EditText) view.findViewById(R.id.new_event_title);

        mNewEventDescriptionLabel = (TextInputLayout) view.findViewById(R.id.new_event_description_input_layout);
        mNewEventDescriptionText = (EditText) view.findViewById(R.id.new_event_description);

        mNewEventDateLabel = (TextInputLayout) view.findViewById(R.id.new_event_date_input_layout);
        mNewEventDate = (EditText) view.findViewById(R.id.new_event_date);

        mNewEventTimeLabel = (TextInputLayout) view.findViewById(R.id.new_event_time_input_layout);
        mNewEventTime = (EditText) view.findViewById(R.id.new_event_time);

        // Title and Description text listeners
        mNewEventTitleLabel.getEditText().addTextChangedListener(titleTextHandler());
        mNewEventDescriptionLabel.getEditText().addTextChangedListener(descriptionTextHandler());

        // Date and Time listeners
        mNewEventDate.setOnClickListener(showDatePicker());
        mNewEventTime.setOnClickListener(showTimePicker());

        mSubmitNewEventButton = (Button) view.findViewById(R.id.new_event_submit_button);
        mSubmitNewEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = mNewEventTitleText.getText().toString();
                String description = mNewEventDescriptionText.getText().toString();
                String startingDate = mNewEventDate.getText().toString() + mNewEventTime.getText().toString();

                if (title.isEmpty()) {
                    mNewEventTitleLabel.setError(getString(R.string.event_title_required));
                } else if (description.isEmpty()) {
                    mNewEventDescriptionLabel.setError(getString(R.string.event_description_required));
                } else {
                    Event event = new Event();
                    event.setEventName(title);
                    event.setEventDescription(description);
                    event.setEventStartDate(startingDate);
                    event.setEventDate(startingDate);
                    event.setSport(getActivity()
                            .getIntent()
                            .getExtras()
                            .getString(EXTRA_SPORT_NAME));
                    saveEvent(event);
                }

            }
        });
    }

    private void saveEvent(Event event) {
        mNetworkManager.saveEvent(event, new NetworkCallback() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG, "Event saved successfully");
            }

            @Override
            public void onFailure(String errorString) {
                Log.e(TAG, "Failed to save event: " + errorString);
            }
        });
    }

    public View.OnClickListener showDatePicker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Format á bakenda: yyyy MM dd HH:mm
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        (datePicker, year1, monthOfYear, dayOfMonth) ->
                                mNewEventDate.setText(year1 + " " +
                                        ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + " "
                                        + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " " ),
                        year, month, day);
                datePickerDialog.show();
            }
        };
    }

    public View.OnClickListener showTimePicker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        (timePicker, hourOfDay, minute1) ->
                                mNewEventTime.setText(
                                        (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay)
                                                + ":" + (minute1 < 10 ? "0" + minute1 : minute1)),
                        hour, minute, true);
                timePickerDialog.show();
            }
        };
    }

    // TODO: Prófa að láta EditText hint breytast dýnamískt eftir skorðum?
    private TextWatcher titleTextHandler() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mNewEventTitleLabel.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private TextWatcher descriptionTextHandler() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mNewEventDescriptionLabel.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}
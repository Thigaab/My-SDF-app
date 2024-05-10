package com.example.mysdfapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;

public class CreateAnnouncementFragment extends Fragment {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button buttonEndDate;
    private Button buttonCoordinates;
    private Button buttonCreate;

    private Calendar endDateCalendar; // To store the end date chosen by the user
    private double latitude; // To store the latitude chosen by the user
    private double longitude; // To store the longitude chosen by the user

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.creat_post, container, false);

        // Initialize views
        titleEditText = rootView.findViewById(R.id.editTextTitle);
        descriptionEditText = rootView.findViewById(R.id.editTextDescription);
        buttonEndDate = rootView.findViewById(R.id.buttonEndDate);
        buttonCoordinates = rootView.findViewById(R.id.buttonCoordinates);
        buttonCreate = rootView.findViewById(R.id.buttonCreate);

        // Initialize end date calendar
        endDateCalendar = Calendar.getInstance();

        // Set click listener for "Choose date" button
        buttonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a DatePickerDialog to allow the user to choose a date
                // and update endDateCalendar variable with the selected date
            }
        });

        // Set click listener for "Choose coordinates" button
        buttonCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a UI to allow the user to choose coordinates
                // and update latitude and longitude variables with the selected values
            }
        });

        // Set click listener for "Create announcement" button
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get title and description of the announcement
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                // Create an announcement with the data entered by the user
                Announcement announcement = new Announcement();
                announcement.Title = title;
                announcement.Description = description;
                announcement.End = new Timestamp(endDateCalendar.getTime());
                announcement.Coordinates = new GeoPoint(latitude, longitude);

            }
        });

        return rootView;
    }
}
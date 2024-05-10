package com.example.mysdfapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class CreateAnnouncementFragment extends Fragment {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText endDateEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button createButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.creat_post, container, false);


        titleEditText = rootView.findViewById(R.id.editTextTitle);
        descriptionEditText = rootView.findViewById(R.id.editTextDescription);
        endDateEditText = rootView.findViewById(R.id.editTextEndDate);
        latitudeEditText = rootView.findViewById(R.id.editTextLatitude);
        longitudeEditText = rootView.findViewById(R.id.editTextLongitude);
        createButton = rootView.findViewById(R.id.buttonCreate);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();
                String latitude = latitudeEditText.getText().toString();
                String longitude = longitudeEditText.getText().toString();


                Date endDateDate = new Date(Long.parseLong(endDate));
                Timestamp endTimestamp = new Timestamp(endDateDate);


                Announcement announcement = new Announcement();
                announcement.Title = title;
                announcement.Description = description;
                announcement.End = endTimestamp;
                announcement.Coordinates = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            }
        });

        return rootView;
    }
}
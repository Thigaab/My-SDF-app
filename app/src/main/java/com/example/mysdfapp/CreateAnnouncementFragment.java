package com.example.mysdfapp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class CreateAnnouncementFragment extends Fragment {

    private MainActivity _mainActivity;
    private DatabaseManager _databaseManager;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText endDateEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button createButton;
    private FirebaseAuth mAuth;
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i("CreateAnnouncementFragment","Fragment's on attach");
        if (context instanceof MainActivity){
            _mainActivity = (MainActivity) context;
            _databaseManager = _mainActivity.getDatabaseManager();
            mAuth.getCurrentUser();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.creat_post, container, false);


        titleEditText = rootView.findViewById(R.id.editTextTitle);
        descriptionEditText = rootView.findViewById(R.id.editTextDescription);
        endDateEditText = rootView.findViewById(R.id.endDate);
        latitudeEditText = rootView.findViewById(R.id.editTextLatitude);
        longitudeEditText = rootView.findViewById(R.id.editTextLongitude);
        createButton = rootView.findViewById(R.id.buttonCreate);

        latitudeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    String replacement = "";
                    try{
                        Double value = Double.parseDouble(s.toString());

                        if (value < -90){
                            replacement = "-90";
                        }
                        else if (value > 90){
                            replacement = "90";
                        }
                        if (!replacement.equals("")){
                            latitudeEditText.setError("Latitude must be between -90 and 90.");
                            latitudeEditText.setText(replacement);
                        }
                    }
                    catch (NumberFormatException e){
                        latitudeEditText.setError("Latitude must be a valid number.");
                    }

                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();
                String latitude = latitudeEditText.getText().toString();
                String longitude = longitudeEditText.getText().toString();

                if (title.equals("")
                || description.equals("")
                || latitude.equals("")
                || longitude.equals("")){
                    Toast.makeText(_mainActivity, "Please fill out everything before creating a post (end date is optionnal).", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        Timestamp endTimestamp = null;
                        if (!endDate.equals("")) {
                            Date endDateDate = new Date(Long.parseLong(endDate));
                            endTimestamp = new Timestamp(endDateDate);
                        }

                        Double latitudeValue = Double.parseDouble(latitude);
                        Double longitudeValue = Double.parseDouble(longitude);
                        if (latitudeValue < -90.0){
                            latitudeValue = -90.0;
                        }
                        else if (latitudeValue > 90.0){
                            latitudeValue = 90.0;
                        }
                        if (longitudeValue < -90.0){
                            longitudeValue = -90.0;
                        }
                        else if (longitudeValue > 90.0){
                            longitudeValue = 90.0;
                        }

                        Announcement announcement = new Announcement();
                        announcement.Title = title;
                        announcement.Description = description;
                        announcement.Category = null; //TODO
                        announcement.Photo = "";
                        announcement.Number_of_likes = 2L;
                        announcement.UserID = mAuth.getUid();
                        announcement.End = endTimestamp;
                        announcement.Coordinates = new GeoPoint(latitudeValue, longitudeValue);

                        addAnnouncementToDatabase(announcement);
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(_mainActivity, "Invalid number format used.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        _mainActivity.setToolbarTitle("Create a new post");
        _mainActivity.setBackButtonEnabled(true);
        _mainActivity.showFloatingButton(false);

        return rootView;
    }

    public void addAnnouncementToDatabase(Announcement announcement){
        _databaseManager.addAnnouncement(announcement);
    }
}
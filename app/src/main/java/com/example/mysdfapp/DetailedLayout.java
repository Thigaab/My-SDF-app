package com.example.mysdfapp;

import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailedLayout extends AppCompatActivity {
    Announcement Announce;

    DetailedLayout(Announcement announce){
        Announce = announce;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);

        TextView titleTextView = findViewById(R.id.details_title);
        TextView descriptionTextView = findViewById(R.id.details_description);
        TextView likesTextView = findViewById(R.id.details_likes);
        TextView endDateTextView = findViewById(R.id.details_endDate);


        titleTextView.setText(Announce.Title);
        descriptionTextView.setText(Announce.Description);
        likesTextView.setText(Math.toIntExact(Announce.Number_of_likes));
        endDateTextView.setText(Announce.End.toString());

        MapsFragment mapsFragment = new MapsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapFragment, mapsFragment)
                .commit();

        mapsFragment.setDestination(Announce.Coordinates.getLatitude(), Announce.Coordinates.getLongitude(), Announce.Title);
    }
}

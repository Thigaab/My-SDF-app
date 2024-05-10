package com.example.mysdfapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailedLayout extends Fragment {
    private Announcement Announce;

    public DetailedLayout(Announcement announce) {
        Announce = announce;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.details_layout, container, false);

        TextView titleTextView = rootView.findViewById(R.id.details_title);
        TextView descriptionTextView = rootView.findViewById(R.id.details_description);
        TextView likesTextView = rootView.findViewById(R.id.details_likes);
        TextView endDateTextView = rootView.findViewById(R.id.details_endDate);

        titleTextView.setText(Announce.Title);
        descriptionTextView.setText(Announce.Description);
        likesTextView.setText(String.valueOf(Announce.Number_of_likes));
        endDateTextView.setText(Announce.End.toString());

        MapsFragment mapsFragment = new MapsFragment();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.mapFragment, mapsFragment)
                .commit();

        mapsFragment.setDestination(Announce.Coordinates.getLatitude(), Announce.Coordinates.getLongitude(), Announce.Title);

        return rootView;
    }
}

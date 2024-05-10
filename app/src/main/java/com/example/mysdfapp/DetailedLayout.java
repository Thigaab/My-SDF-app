package com.example.mysdfapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailedLayout extends Fragment {
    private Announcement Announce;
    private MainActivity _mainActivity;

    public DetailedLayout(Announcement announce) {
        Announce = announce;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i("MyPostListFragment","Fragment's on attach");
        if (context instanceof MainActivity){
            _mainActivity = (MainActivity) context;
        }
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
        LinearLayout endDateLayout = rootView.findViewById(R.id.details_end);

        titleTextView.setText(Announce.Title);
        descriptionTextView.setText(Announce.Description);
        likesTextView.setText(String.valueOf(Announce.Number_of_likes));
        if (Announce.End != null){
            endDateLayout.setVisibility(View.VISIBLE);
            endDateTextView.setText(Announce.End.toString());
        }
        else{
            endDateLayout.setVisibility(View.GONE);
        }

        MapsFragment mapsFragment = new MapsFragment();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.mapFragment, mapsFragment)
                .commit();

        mapsFragment.setDestination(Announce.Coordinates.getLatitude(), Announce.Coordinates.getLongitude(), Announce.Title);

        _mainActivity.setToolbarTitle("More details about the post");
        _mainActivity.setBackButtonEnabled(true);
        _mainActivity.showFloatingButton(false);

        return rootView;
    }
}

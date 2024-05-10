package com.example.mysdfapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyPostListFragment extends Fragment {

    private MainActivity _mainActivity;
    private DatabaseManager _databaseManager;
    private RecyclerView _recyclerView;
    private MyPostViewAdapter _myPostAdapter;
    private FloatingActionButton _loadButton;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i("MyPostListFragment","Fragment's on attach");
        if (context instanceof MainActivity){
            _mainActivity = (MainActivity) context;
            _databaseManager = _mainActivity.getDatabaseManager();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_list, container, false);
        _recyclerView = view.findViewById(R.id.post_list_recyclerView);
        _loadButton = view.findViewById(R.id.post_list_loadMore);

        _myPostAdapter = new MyPostViewAdapter(new MyPostViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Expend fragment with the given post info
            }
        });
        _myPostAdapter.AnnouncementList = new ArrayList<>();
        _myPostAdapter.DatabaseManager = _databaseManager;

        _recyclerView.setLayoutManager(new LinearLayoutManager(_mainActivity));
        _recyclerView.setAdapter(_myPostAdapter);

        _loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch additional elements from user in database
            }
        });

        Log.i("MyPostListFragment","end of fragment's create view");
        _mainActivity.setToolbarTitle("Your posts");
        _mainActivity.setBackButtonEnabled(true);

        return view;
    }
}

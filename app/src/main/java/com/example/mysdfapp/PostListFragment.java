package com.example.mysdfapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PostListFragment extends Fragment {

    private RecyclerView _recyclerView;
    private PostViewAdapter _postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_list, container, false);
        _recyclerView = view.findViewById(R.id.post_list_recyclerView);

        _postAdapter = new PostViewAdapter();

        _recyclerView.setAdapter(_postAdapter);

        return view;
    }
}

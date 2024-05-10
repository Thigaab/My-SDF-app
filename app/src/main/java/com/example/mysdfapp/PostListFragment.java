package com.example.mysdfapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class PostListFragment extends Fragment {

    private MainActivity _mainActivity;
    private RecyclerView _recyclerView;
    private PostViewAdapter _postAdapter;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i("PostListFragment","Fragment's on attach");
        if (context instanceof MainActivity){
            _mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_list, container, false);
        _recyclerView = view.findViewById(R.id.post_list_recyclerView);

        _postAdapter = new PostViewAdapter(new PostViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Expend fragment with the given post info
            }
        });

        _recyclerView.setAdapter(_postAdapter);

        Log.i("PostListFragment","end of fragment's create view");
        _mainActivity.setToolbarTitle("Discover new discounts");
        _mainActivity.setBackButtonEnabled(false);

        return view;
    }
}

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
import java.util.List;

public class PostListFragment extends Fragment {

    private MainActivity _mainActivity;
    private DatabaseManager _databaseManager;
    private RecyclerView _recyclerView;
    private PostViewAdapter _postAdapter;
    private FloatingActionButton _loadButton;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.i("PostListFragment","Fragment's on attach");
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

        _postAdapter = new PostViewAdapter(new PostViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Expend fragment with the given post info
                DetailedLayout newFragment = new DetailedLayout(_postAdapter.AnnouncementList.get(position));
                _mainActivity.commitFragment(newFragment, null);
            }
        });
        _postAdapter.AnnouncementList = new ArrayList<>();
        _postAdapter.DatabaseManager = _databaseManager;

        _recyclerView.setLayoutManager(new LinearLayoutManager(_mainActivity));
        _recyclerView.setAdapter(_postAdapter);

        _loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mainActivity.fetchInDatabase(new DatabaseManager.ExecuteToListAfterQueryAction() {
                    @Override
                    public void applyToAnnouncements(List<Announcement> announcementList) {
                        if (announcementList.size() > 0){
                            int startPosition = _postAdapter.getItemCount();
                            for (Announcement announcement : announcementList){
                                _postAdapter.AnnouncementList.add(announcement);
                            }
                            _postAdapter.notifyItemRangeInserted(startPosition, announcementList.size());
                        }
                    }
                });
            }
        });

        Log.i("PostListFragment","end of fragment's create view");
        _mainActivity.setToolbarTitle("Discover new discounts");
        _mainActivity.setBackButtonEnabled(false);
        _mainActivity.showFloatingButton(true);
        _mainActivity.FloatingButtonSwitchToMySpaceMode();
        _mainActivity.setFloatingButtonIcon(android.R.drawable.ic_menu_myplaces);

        return view;
    }
}

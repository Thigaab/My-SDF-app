package com.example.mysdfapp;

import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {

    // Variable for post list

    public PostViewAdapter(){
        //Initialize things
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gets the data from the post at the position
        String title = "Title";
        String likeNumber = "Number";

        // Update the view holder with the information found at the position
        holder.PostTitle.setText(title);
        holder.LikeNumber.setText(likeNumber);
        holder.UpVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add upvote to the post at the position
            }
        });
        holder.DownVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add downvote to the post at the position
            }
        });
    }

    @Override
    public int getItemCount() {
        // Returns the size of the post list
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView PostTitle;
        public ImageButton UpVoteButton;
        public ImageButton DownVoteButton;
        public TextView LikeNumber;
        public ViewHolder(View itemView){
            super(itemView);
            PostTitle = itemView.findViewById(R.id.post_title);
            UpVoteButton = itemView.findViewById(R.id.post_upvoteButton);
            DownVoteButton = itemView.findViewById(R.id.post_downvoteButton);
            LikeNumber = itemView.findViewById(R.id.post_likeNumber);
        }
    }
}

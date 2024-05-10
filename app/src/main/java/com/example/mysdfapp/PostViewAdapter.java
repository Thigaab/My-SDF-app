package com.example.mysdfapp;

import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {

    // Variable for post list
    public List<Announcement> AnnouncementList;
    public DatabaseManager DatabaseManager;

    public PostViewAdapter(OnItemClickListener clickListener){
        //Initialize things
        ClickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener ClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(itemView, ClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gets the data from the post at the position
        String title = AnnouncementList.get(position).Title;
        String likeNumber = AnnouncementList.get(position).Number_of_likes.toString();

        // Update the view holder with the information found at the position
        holder.PostTitle.setText(title);
        holder.LikeNumber.setText(likeNumber);
        holder.UpVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add upvote to the post at the position
                DatabaseManager.LikesAnnouncement(AnnouncementList.get(holder.getPosition()).ID, 1);
            }
        });
        holder.DownVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add downvote to the post at the position
                DatabaseManager.LikesAnnouncement(AnnouncementList.get(holder.getPosition()).ID, -1);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Returns the size of the post list
        return AnnouncementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public OnItemClickListener ClickListener;
        public TextView PostTitle;
        public ImageButton UpVoteButton;
        public ImageButton DownVoteButton;
        public TextView LikeNumber;
        public ViewHolder(View itemView, OnItemClickListener clickListener){
            super(itemView);
            PostTitle = itemView.findViewById(R.id.post_title);
            UpVoteButton = itemView.findViewById(R.id.post_upvoteButton);
            DownVoteButton = itemView.findViewById(R.id.post_downvoteButton);
            LikeNumber = itemView.findViewById(R.id.post_likeNumber);
            ClickListener = clickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            ClickListener.onItemClick(v,position);
                        }
                    }
                }
            });
        }
    }
}

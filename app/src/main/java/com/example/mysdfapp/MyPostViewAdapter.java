package com.example.mysdfapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyPostViewAdapter extends RecyclerView.Adapter<MyPostViewAdapter.ViewHolder> {

    // Variable for post list
    public List<Announcement> AnnouncementList;
    public DatabaseManager DatabaseManager;

    public MyPostViewAdapter(OnItemClickListener clickListener){
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypost_layout, parent, false);
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
        holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the post at the position
                int positionAnnouncement = holder.getPosition();
                DatabaseManager.deleteAnnouncement(AnnouncementList.get(positionAnnouncement).ID);
                AnnouncementList.remove(positionAnnouncement);
                notifyItemRemoved(positionAnnouncement);
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
        public TextView LikeNumber;
        public Button DeleteButton;
        public ViewHolder(View itemView, OnItemClickListener clickListener){
            super(itemView);
            PostTitle = itemView.findViewById(R.id.mypost_title);
            LikeNumber = itemView.findViewById(R.id.mypost_likeNumber);
            DeleteButton = itemView.findViewById(R.id.mypost_deleteButton);
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

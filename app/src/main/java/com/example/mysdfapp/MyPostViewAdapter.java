package com.example.mysdfapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostViewAdapter extends RecyclerView.Adapter<MyPostViewAdapter.ViewHolder> {

    // Variable for post list

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
        String title = "Title";
        String likeNumber = "Number";

        // Update the view holder with the information found at the position
        holder.PostTitle.setText(title);
        holder.LikeNumber.setText(likeNumber);
        holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the post at the position
                //notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Returns the size of the post list
        return 0;
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

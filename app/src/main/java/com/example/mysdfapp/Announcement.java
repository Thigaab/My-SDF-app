package com.example.mysdfapp;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Announcement {
    public String ID;
    public String Title;
    public String Description;
    public List<String> Category;
    public String Photo;

    public Long Number_of_likes;

    public String UserID;

    public Timestamp Creation;

    public Timestamp  End;

    public GeoPoint Coordinates;
    public double Distance;
}
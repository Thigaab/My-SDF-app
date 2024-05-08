package com.example.mysdfapp;


import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private FirebaseFirestore db;
    private CollectionReference announcementsCollection;
    public CollectionReference categoryCollection;
    private CollectionReference likesCollection;

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        announcementsCollection = db.collection("announcements");
        categoryCollection = db.collection("Category");

    }
    public void addCategory(String[] category) {
        Map<String, Object> Category = new HashMap<>();
        Category.put("Category_Champ", category);

        categoryCollection.add(Category)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Announcement added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding announcement", e);
                });
    }
    public List<String> retrieveCategory() {
        List<String> categoryChamps = new ArrayList<>();
        categoryCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String categoryChamp = document.getString("Category_Champ");
                        if (categoryChamp != null) {
                            categoryChamps.add(categoryChamp);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error retrieving categories", e);
                });
        return categoryChamps;

    }

    public void addAnnouncement(String[] category, GeoPoint point, Timestamp end, String title, String description, String photoUrl, String userId) {
        Map<String, Object> announcement = new HashMap<>();
        announcement.put("Title", title);
        announcement.put("Description", description);
        announcement.put("Category", category);
        announcement.put("Photo", photoUrl);
        announcement.put("Number_of_likes", 50);
        announcement.put("UserID", userId);
        announcement.put("Creation", Timestamp.now());
        announcement.put("End", end);
        announcement.put("Coordinates", point);

        announcementsCollection.add(announcement)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Announcement added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding announcement", e);
                });
    }

    public void retrieveAnnouncements() {
        announcementsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Processing retrieved announcements
                        // You can, for example, display them in the user interface
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error retrieving announcements", e);
                });
    }

    public void increaseLikesAnnouncement(String announcementId) {
        DocumentReference announcementRef = announcementsCollection.document(announcementId);

        announcementRef.update("Number_of_likes", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Number_of_likes of announcement increased successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error increasing Number_of_likes of announcement", e);
                });
    }

    public void deleteAnnouncement(String announcementId) {
        DocumentReference announcementRef = announcementsCollection.document(announcementId);

        announcementRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Announcement deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error deleting announcement", e);
                });
    }

    public void updateCategories(String announcementId, String[] newCategories) {
        DocumentReference announcementRef = announcementsCollection.document(announcementId);

        announcementRef.update("Category", newCategories)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Categories of announcement updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating Categories of announcement", e);
                });
    }
    public void searchAnnouncementsByCategory(String category, OnSuccessListener<List<Announcement>> onSuccessListener, OnFailureListener onFailureListener) {
        Query query = announcementsCollection.whereArrayContains("Category", category);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Announcement> idsAnnouncements = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Announcement newAnnouncement = new Announcement();
                        newAnnouncement.ID = document.getId();
                        newAnnouncement.Title = document.getString("Title");
                        newAnnouncement.Description = document.getString("Description");
                        newAnnouncement.Category = (List<String>) document.get("Category");
                        newAnnouncement.Photo = document.getString("Photo");
                        newAnnouncement.Number_of_likes = document.getLong("Number_of_likes");
                        newAnnouncement.UserID = document.getString("UserID");
                        newAnnouncement.Creation = document.getTimestamp("Creation");
                        newAnnouncement.End = document.getTimestamp("End");
                        newAnnouncement.Coordinates = document.getGeoPoint("Coordinates");
                        idsAnnouncements.add(newAnnouncement);
                    }
                    onSuccessListener.onSuccess(idsAnnouncements);
                })
                .addOnFailureListener(onFailureListener);
    }
}

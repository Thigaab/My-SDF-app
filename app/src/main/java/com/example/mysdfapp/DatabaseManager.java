package com.example.mysdfapp;


import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private FirebaseFirestore db;
    private CollectionReference announcementsCollection;
    public CollectionReference categoryCollection;
    private CollectionReference updateCollection;

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        announcementsCollection = db.collection("announcements");
        categoryCollection = db.collection("Category");
        updateCollection = db.collection("uptdate");
    }
    public void Update() {
        // Get the reference of the document containing the last update
        DocumentReference lastUpdateDocRef = updateCollection.document("last_update");

        lastUpdateDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Timestamp lastUpdateTimeStamp = documentSnapshot.getTimestamp("timestamp");
                        if (lastUpdateTimeStamp != null) {
                            // Get the current timestamp
                            Timestamp currentTimestamp = Timestamp.now();

                            // Calculate the difference between the current timestamp and the timestamp of the last update
                            long differenceInMillis = currentTimestamp.getSeconds() * 1000 - lastUpdateTimeStamp.getSeconds() * 1000;

                            // Check if the difference is greater than 24 hours (in milliseconds)
                            if (differenceInMillis > 24 * 60 * 60 * 1000) {
                                // Update the document with the new timestamp
                                updatelike();
                                lastUpdateDocRef.update("timestamp", currentTimestamp)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Last update timestamp updated successfully");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w(TAG, "Error updating last update timestamp", e);
                                        });
                            }
                        }
                    } else {
                        // The document of the last update does not exist, we need to create it with the current timestamp
                        Timestamp currentTimestamp = Timestamp.now();
                        lastUpdateDocRef.set(new HashMap<String, Object>() {{
                                    put("timestamp", currentTimestamp);
                                }})
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Last update timestamp created successfully");
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error creating last update timestamp", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting last update timestamp", e);
                });
    }

    public void updatelike() {
        announcementsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Timestamp endTimestamp = document.getTimestamp("End");
                        // Check if the end date is in the past
                        if (endTimestamp != null && endTimestamp.toDate().before(Timestamp.now().toDate())) {
                            deleteAnnouncement(document.getId());
                        } else {
                            // Increment the number of likes
                            document.getReference().update("Number_of_likes", FieldValue.increment(1))
                                    .addOnSuccessListener(aVoid -> {
                                        // Check if the number of likes became zero after incrementing
                                        Long numberOfLikes = document.getLong("Number_of_likes");
                                        if (numberOfLikes != null && numberOfLikes == 0) {
                                            deleteAnnouncement(document.getId());
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error updating likes", e);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error retrieving announcements", e);
                });
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

    public void LikesAnnouncement(String announcementId,int value) {
        DocumentReference announcementRef = announcementsCollection.document(announcementId);

        announcementRef.update("Number_of_likes", FieldValue.increment(value))
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
    public Announcement createAnnouncementFromDocument(QueryDocumentSnapshot document){
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
        return newAnnouncement;
    }
    public void searchAnnouncementsByCategory(String category, OnSuccessListener<List<Announcement>> onSuccessListener, OnFailureListener onFailureListener) {
        Query query = announcementsCollection.whereArrayContains("Category", category);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Announcement> idsAnnouncements = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Announcement newAnnouncement = createAnnouncementFromDocument(document);
                        idsAnnouncements.add(newAnnouncement);
                    }
                    onSuccessListener.onSuccess(idsAnnouncements);
                })
                .addOnFailureListener(onFailureListener);
    }
    public void searchAnnouncementsOrderByLikes(OnSuccessListener<List<Announcement>> onSuccessListener, OnFailureListener onFailureListener) {
        // Create a query to retrieve announcements ordered by number of likes in descending order
        Query query = announcementsCollection.orderBy("Number_of_likes", Query.Direction.DESCENDING);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Announcement> announcements = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Create an Announcement object from document data
                        Announcement newAnnouncement = createAnnouncementFromDocument(document);
                        // Add the announcement to the list
                        announcements.add(newAnnouncement);
                    }
                    // Send the sorted list to the success listener
                    onSuccessListener.onSuccess(announcements);
                })
                .addOnFailureListener(onFailureListener);
    }
    public void searchAnnouncementsOrderByProximity(OnSuccessListener<List<Announcement>> onSuccessListener, OnFailureListener onFailureListener, GeoPoint point) {
        // Create a query to retrieve all announcements
        announcementsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Announcement> announcements = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Create an Announcement object from document data
                        Announcement newAnnouncement = createAnnouncementFromDocument(document);
                        // Calculate the distance between the specified point and the announcement's point
                        double distance = calculateDistance(point, newAnnouncement.Coordinates);
                        // Set the distance in the announcement
                        newAnnouncement.Distance = distance;
                        // Add the announcement to the list
                        announcements.add(newAnnouncement);
                    }
                    // Sort the list of announcements by proximity
                    sortAnnouncementsByDistance(announcements);
                    // Send the sorted list to the success listener
                    onSuccessListener.onSuccess(announcements);
                })
                .addOnFailureListener(onFailureListener);
    }
    public void sortAnnouncementsByDistance(List<Announcement> announcements) {
        // Implementation of Bubble Sort to sort announcements by distance
        int n = announcements.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                Announcement current = announcements.get(i);
                Announcement previous = announcements.get(i - 1);
                if (current.Distance < previous.Distance) {
                    // Swap the announcements if they are not in the correct order
                    announcements.set(i, previous);
                    announcements.set(i - 1, current);
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
    }

    private double calculateDistance(GeoPoint point1, GeoPoint point2) {
        double dx = point1.getLatitude() - point2.getLatitude();
        double dy = point1.getLongitude() - point2.getLongitude();
        return Math.sqrt(dx * dx + dy * dy);
    }
}

package com.example.mysdfapp;


import android.util.Log;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public long LimitPerBatch = 10;

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        announcementsCollection = db.collection("announcements");
        categoryCollection = db.collection("Category");
        updateCollection = db.collection("update");
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

                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_MONTH, - 1);
                            java.util.Date yesterday = calendar.getTime();


                            // Check if the difference is greater than 24 hours (in milliseconds)
                            if (lastUpdateTimeStamp.toDate().before(yesterday)) {
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
                            // Decrement the number of likes
                            document.getReference().update("Number_of_likes", FieldValue.increment(-1))
                                    .addOnSuccessListener(aVoid -> {
                                        // Check if the number of likes became zero after incrementing
                                        Long numberOfLikes = document.getLong("Number_of_likes");
                                        if (numberOfLikes != null && numberOfLikes <= 0) {
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

    public void addCategory(String category) {
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

    public interface ExecuteAfterCategoryQuery{
        void applyToCategories(List<String> categories);
    }

    public void retrieveCategory(ExecuteAfterCategoryQuery action) {
        categoryCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> categories = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String categoryChamp = document.getString("Category_Champ");
                        if (categoryChamp != null) {
                            categories.add(categoryChamp);
                        }
                    }

                    action.applyToCategories(categories);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error retrieving categories", e);
                });

    }

    public void addAnnouncement(Announcement announcement) {
        Map<String, Object> announcementHash = new HashMap<>();
        announcementHash.put("Title", announcement.Title);
        announcementHash.put("Description", announcement.Description);
        announcementHash.put("Category", announcement.Category.toArray());
        announcementHash.put("Photo", announcement.Photo);
        announcementHash.put("Number_of_likes", announcement.Number_of_likes);
        announcementHash.put("UserID", announcement.UserID);
        announcementHash.put("Creation", Timestamp.now());
        announcementHash.put("End", announcement.End);
        announcementHash.put("Coordinates", announcement.Coordinates);

        announcementsCollection.add(announcementHash)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Announcement added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding announcement", e);
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

    public interface ExecuteToListAfterQueryAction {
        void applyToAnnouncements(List<Announcement> announcementList);
    }

    private DocumentSnapshot lastDocument = null;

    public void ResetLastDocument(){
        lastDocument = null;
    }

    public void searchAnnouncementsByCategory(String category, ExecuteToListAfterQueryAction action) {
        Query query = announcementsCollection.whereArrayContains("Category", category)
                .limit(LimitPerBatch);

        if (lastDocument != null){
            query.startAfter(lastDocument);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Announcement> announcementList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Announcement newAnnouncement = createAnnouncementFromDocument(document);
                        announcementList.add(newAnnouncement);
                    }
                    if (queryDocumentSnapshots.getDocuments().size() > 0) {
                        lastDocument = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.getDocuments().size() - 1);
                    }

                    action.applyToAnnouncements(announcementList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Couldn't get the announcements with the given category.");
                });
    }

    public void searchAnnouncementsOrderByLikes(ExecuteToListAfterQueryAction action) {
        // Create a query to retrieve announcements ordered by number of likes in descending order
        Query query = announcementsCollection.orderBy("Number_of_likes", Query.Direction.DESCENDING)
                .limit(LimitPerBatch);

        if (lastDocument != null){
            query.startAfter(lastDocument);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Announcement> announcementList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Announcement newAnnouncement = createAnnouncementFromDocument(document);
                        announcementList.add(newAnnouncement);
                    }
                    if (queryDocumentSnapshots.getDocuments().size() > 0) {
                        lastDocument = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.getDocuments().size() - 1);
                    }

                    action.applyToAnnouncements(announcementList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Couldn't get the announcements.");
                });
    }
    public void searchAnnouncementsOrderByProximity(ExecuteToListAfterQueryAction action, GeoPoint point) {
        // Create a query to retrieve all announcements
        Query query = announcementsCollection.limit(LimitPerBatch);

        if (lastDocument != null){
            query.startAfter(lastDocument);
        }

        query.get()
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
                    if (queryDocumentSnapshots.getDocuments().size() > 0) {
                        lastDocument = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.getDocuments().size() - 1);
                    }

                    // Sort the list of announcements by proximity
                    sortAnnouncementsByDistance(announcements);

                    action.applyToAnnouncements(announcements);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Couldn't get the announcements.");
                });
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

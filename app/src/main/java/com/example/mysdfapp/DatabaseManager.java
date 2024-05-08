package com.example.mysdfapp;


import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
/*
    private static final String TAG = "DatabaseManager";

    private FirebaseFirestore db;
    private CollectionReference annoncesCollection;
    private CollectionReference likesCollection;

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        annoncesCollection = db.collection("annonces");
        likesCollection = db.collection("likes");
    }

    public void ajouterAnnonce(String titre, String description, double prix, String photoUrl, String utilisateurId) {
        Map<String, Object> annonce = new HashMap<>();
        annonce.put("titre", titre);
        annonce.put("description", description);
        annonce.put("prix", prix);
        annonce.put("photo", photoUrl);
        annonce.put("likes", 0);
        annonce.put("utilisateur_id", utilisateurId);
        annonce.put("timestamp", System.currentTimeMillis());

        annoncesCollection.add(annonce)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Annonce ajoutée avec ID : " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erreur lors de l'ajout de l'annonce", e);
                });
    }

    public void recupererAnnonces() {
        annoncesCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Traitement des annonces récupérées
                        // Vous pouvez par exemple les afficher dans l'interface utilisateur
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erreur lors de la récupération des annonces", e);
                });
    }

    // Ajouter d'autres méthodes pour ajouter, supprimer des likes, etc.*/
}
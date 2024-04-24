package com.example.mysdfapp;


import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private CollectionReference annoncesCollection;
    private CollectionReference likesCollection;

    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
        annoncesCollection = db.collection("annonces");
    }

    public void ajouterAnnonce(String[] cathegorie, GeoPoint point ,String fin, String titre, String description, String photoUrl, String utilisateurId) {
        Map<String, Object> annonce = new HashMap<>();
        annonce.put("Titre", titre);
        annonce.put("Déscription", description);
        annonce.put("Catégorie", cathegorie);
        annonce.put("Photo", photoUrl);
        annonce.put("Nombre de likes", 50);
        annonce.put("UserID", utilisateurId);
        annonce.put("Création", System.currentTimeMillis());
        annonce.put("Fin",fin);
        annonce.put("Coordonnée", point);

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

    public void augmenterLikesAnnonce(String idAnnonce) {
        // Référence au document de l'annonce dans la collection "annonces"
        DocumentReference annonceRef = annoncesCollection.document(idAnnonce);

        // Mettre à jour le nombre de likes en incrémentant la valeur actuelle de 1
        annonceRef.update("likes", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Nombre de likes de l'annonce augmenté avec succès");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erreur lors de l'augmentation du nombre de likes de l'annonce", e);
                });
    }

    public void supprimerAnnonce(String idAnnonce) {
        // Référence au document de l'annonce dans la collection "annonces"
        DocumentReference annonceRef = annoncesCollection.document(idAnnonce);

        // Supprimer le document de l'annonce
        annonceRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Annonce supprimée avec succès");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erreur lors de la suppression de l'annonce", e);
                });
    }
    public void mettreAJourCategories(String idAnnonce, String[] nouvellesCategories) {
        DocumentReference annonceRef = annoncesCollection.document(idAnnonce);

        annonceRef.update("categories", nouvellesCategories)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Catégories de l'annonce mises à jour avec succès");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Erreur lors de la mise à jour des catégories de l'annonce", e);
                });
    }
    public void rechercherAnnoncesParCategorie(String categorie, OnSuccessListener<List<String>> onSuccessListener, OnFailureListener onFailureListener) {
        // Créer une requête pour rechercher les annonces ayant la catégorie donnée
        Query query = annoncesCollection.whereArrayContains("categories", categorie);

        // Exécuter la requête
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> idsAnnonces = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Ajouter l'identifiant de l'annonce à la liste
                        idsAnnonces.add(document.getId());
                    }
                    // Appeler onSuccessListener avec la liste des identifiants des annonces correspondantes
                    onSuccessListener.onSuccess(idsAnnonces);
                })
                .addOnFailureListener(onFailureListener);
    }
}
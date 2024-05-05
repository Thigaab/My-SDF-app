package com.example.mysdfapp;

import android.graphics.Point;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Annonce {
    public String ID;
    public String Titre;
    public String Déscription;
    public List<String> Catégorie;
    public String Photo;

    public Long Nombre_de_likes;

    public String UserID;

    public Timestamp Création;

    public Timestamp  Fin;

    public GeoPoint Coordonnée;
}


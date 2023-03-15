package com.example.go4lunch.data;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public interface UserRepositoryContract {
    //TODO : task -> utilisation des callback
    //TODO : FirebaseUser -> User perso
    FirebaseUser getCurrentUser();
    String getCurrentUserUID();
    void createUser();
    Task<Void> signOut(Context context);
    Task<Void> deleteUser(Context context);
    Task<DocumentSnapshot> getUserData();
    Task<Void> updateUsername(String username);
    void updateLunchChoice(String lunchCoice);
    void deleteUserFromFirestore();
}

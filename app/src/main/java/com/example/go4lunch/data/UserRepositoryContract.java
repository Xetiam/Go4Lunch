package com.example.go4lunch.data;

import android.content.Context;

import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.EvaluationCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface UserRepositoryContract {
    //TODO : task -> utilisation des callback
    //TODO : FirebaseUser -> User perso
    UserEntity getCurrentUser();
    String getCurrentUserUID();
    void createUser();
    Task<Void> signOut(Context context);
    Task<Void> deleteUser(Context context);
    Task<DocumentSnapshot> getUserData();
    Task<Void> updateUsername(String username);
    void updateLunchChoice(String lunchCoice);
    void deleteUserFromFirestore();
    void addOrRemoveRestaurantEvaluation(String restaurantID, boolean contains);
    void getCurrentUserEvaluations(EvaluationCallback callback);
}
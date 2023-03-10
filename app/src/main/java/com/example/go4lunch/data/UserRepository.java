package com.example.go4lunch.data;

import android.content.Context;

import com.example.go4lunch.model.UserEntity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository implements UserRepositoryContract{
    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String LUNCH_CHOICE = "lunchchoice";
    private static volatile UserRepository instance;

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();

            UserEntity userToCreate = new UserEntity(uid, username, urlPicture);

            Task<DocumentSnapshot> userData = getUserData();
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(LUNCH_CHOICE)) {
                    userToCreate.setLunchChoice((String) documentSnapshot.get(LUNCH_CHOICE));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }

    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    public Task<Void> updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).update(USERNAME_FIELD, username);
        } else {
            return null;
        }
    }

    public void updateLunchChoice(String lunchCoice) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(LUNCH_CHOICE, lunchCoice);
        }
    }

    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }
}

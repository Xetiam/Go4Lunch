package com.example.go4lunch.data;

import android.content.Context;
import android.net.Uri;

import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.EvaluationCallback;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository implements UserRepositoryContract {
    //TODO: utiliser mon UserEntity et ma base de données
    private static final String COLLECTION_NAME = "users";
    private static final String USERNAME_FIELD = "username";
    private static final String USER_ID_FIELD = "uid";
    private static final String USER_PICTURE_FIELD = "urlPicture";
    private static final String LUNCH_CHOICE_FIELD = "lunchchoice";
    private static final String EVALUATIONS_FIELD = "evaluations";

    public UserEntity getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return new UserEntity(user.getUid(), user.getDisplayName(), user.getPhotoUrl(), "", new ArrayList<>());
        } else {
            return null;
        }
    }

    public String getCurrentUserUID() {
        UserEntity user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public void createUser() {
        UserEntity user = getCurrentUser();
        if (user != null) {
            Uri urlPicture = (user.getUrlPicture() != null) ? user.getUrlPicture() : null;
            String username = user.getUsername();
            String uid = user.getUid();

            UserEntity userToCreate = new UserEntity(uid, username, urlPicture, "", new ArrayList<>());
            Task<DocumentSnapshot> userData = getUserData();
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(LUNCH_CHOICE_FIELD)) {
                    userToCreate.setLunchChoice((String) documentSnapshot.get(LUNCH_CHOICE_FIELD));
                }
                this.getUsersCollection().document(uid).get().addOnSuccessListener(userDocument -> {
                            if(userDocument == null){
                                this.getUsersCollection().document(uid).set(userToCreate);
                            }
                        });
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

    public void updateLunchChoice(String lunchChoice) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).update(LUNCH_CHOICE_FIELD, lunchChoice);
        }
    }

    public void deleteUserFromFirestore() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getUsersCollection().document(uid).delete();
        }
    }

    @Override
    public void addOrRemoveRestaurantEvaluation(String restaurantId, boolean contains) {
        Map<String, Object> data = new HashMap<>();
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.document(getCurrentUserUID()).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            ArrayList<String> evaluations = new ArrayList<>();
            if (document != null) {
                if (document.get(EVALUATIONS_FIELD) != null) {
                    evaluations = (ArrayList<String>) document.get(EVALUATIONS_FIELD);
                    if (contains) {
                        evaluations.remove(restaurantId);
                    } else {
                        evaluations.add(restaurantId);
                    }
                } else {
                    evaluations.add(restaurantId);
                }
                data.put(EVALUATIONS_FIELD, evaluations);
                reference.document(restaurantId).update(data);
            }
        });
    }

    @Override
    public void getCurrentUserEvaluations(EvaluationCallback callback) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.document(getCurrentUserUID()).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                if (document.get(EVALUATIONS_FIELD) != null) {
                    callback.evaluationsCallback((ArrayList<String>) document.get(EVALUATIONS_FIELD));
                } else {
                    callback.evaluationsCallback(new ArrayList<>());
                }
            }
        });
    }
}

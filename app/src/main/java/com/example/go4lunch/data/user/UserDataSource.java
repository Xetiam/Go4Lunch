package com.example.go4lunch.data.user;

import static com.example.go4lunch.data.restaurant.RestaurantDataSource.RESTAURANT_COLLECTION_NAME;
import static com.example.go4lunch.data.restaurant.RestaurantRepository.RESTAURANT_LUNCHERS_FIELD;
import static com.example.go4lunch.data.user.UserRepository.USER_EVALUATIONS_FIELD;
import static com.example.go4lunch.data.user.UserRepository.USER_ID_FIELD;
import static com.example.go4lunch.data.user.UserRepository.USER_LUNCH_CHOICE_FIELD;
import static com.example.go4lunch.data.user.UserRepository.USER_NAME_FIELD;
import static com.example.go4lunch.data.user.UserRepository.USER_PICTURE_FIELD;

import android.net.Uri;

import com.example.go4lunch.data.callback.CallbackUserDataSource;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.SignOutCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserDataSource {
    public static final String USER_COLLECTION_NAME = "users";
    private final FirebaseFirestore reference = FirebaseFirestore.getInstance();

    public UserEntity getCurrentUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return new UserEntity(user.getUid(), user.getDisplayName(), user.getPhotoUrl(), "", new ArrayList<>());
        } else {
            return null;
        }
    }

    public void updateUsernameData(String username, String uid) {
        if (uid != null) {
            reference.collection(USER_COLLECTION_NAME).document(uid).update(USER_NAME_FIELD, username);
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();
            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(request);
        }
    }

    public void createUserData(UserEntity user) {
        reference.collection(USER_COLLECTION_NAME).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            boolean shouldCreateUser = true;
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    if (Objects.equals(document.get(USER_ID_FIELD), user.getUid())) {
                        shouldCreateUser = false;
                    }
                }
            }
            if (shouldCreateUser) {
                Uri urlPicture = (user.getUrlPicture() != null) ? user.getUrlPicture() : null;
                String username = user.getUsername();
                String uid = user.getUid();
                Map<String, Object> data = new HashMap<>();
                data.put(USER_ID_FIELD, uid);
                data.put(USER_NAME_FIELD, username);
                data.put(USER_PICTURE_FIELD, urlPicture);
                reference.collection(USER_COLLECTION_NAME).document(uid).set(data);
            }
        });
    }

    public void deleteUserData(SignOutCallback callback) {
        String uid = getCurrentUserData().getUid();
        reference.collection(USER_COLLECTION_NAME).document(uid).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                if (document.get(USER_LUNCH_CHOICE_FIELD) != null &&
                        !Objects.equals(document.get(USER_LUNCH_CHOICE_FIELD), "")) {
                    String lunchChoiceToDelete = (String) document.get(USER_LUNCH_CHOICE_FIELD);
                    DocumentReference restaurantRef = FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION_NAME).document(lunchChoiceToDelete);
                    restaurantRef.get().addOnSuccessListener(restaurantDocument -> {
                        ArrayList<String> actualLunchers;
                        Map<String, Object> restaurantDocumentData = restaurantDocument.getData();
                        if (restaurantDocumentData.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                            actualLunchers = (ArrayList<String>) restaurantDocumentData.get(RESTAURANT_LUNCHERS_FIELD);
                            if (actualLunchers != null && actualLunchers.contains(uid)) {
                                actualLunchers.remove(uid);
                                restaurantRef.update(RESTAURANT_LUNCHERS_FIELD, actualLunchers);
                            }
                        }
                        deleteAndSignOut(uid, callback);
                    });
                } else {
                    deleteAndSignOut(uid, callback);
                }
            }
        });
    }

    private void deleteAndSignOut(String uid, SignOutCallback callback) {
        reference.collection(USER_COLLECTION_NAME).document(uid).delete();
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete();
        FirebaseAuth.getInstance().signOut();
        callback.signout();
    }

    public String getCurrentUserMailData() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }

    public void getUSersLuncherByIdsData(ArrayList<String> lunchers, DetailCallback callback, CallbackUserDataSource callbackUserDataSource) {
        reference.collection(USER_COLLECTION_NAME).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            ArrayList<Map<String, Object>> userDocuments = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    String uid = (String) document.get(USER_ID_FIELD);
                    if (uid != null && lunchers.contains(uid)) {
                        if (!uid.equals(getCurrentUserData().getUid())) {
                            userDocuments.add(document);
                        }
                    }
                }
            }
            callbackUserDataSource.usersData(userDocuments, callback);
        });
    }

    public void getCurrentUserEvaluationsData(String restaurantId, DetailCallback callback) {
        reference.collection(USER_COLLECTION_NAME).document(getCurrentUserData().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                if (document.get(USER_EVALUATIONS_FIELD) != null) {
                    ArrayList<String> evals = (ArrayList<String>) document.get(USER_EVALUATIONS_FIELD);
                    callback.evaluationsCallback(evals.contains(restaurantId));
                } else {
                    callback.evaluationsCallback(false);
                }
            }
        });
    }

    public void getCurrentUserLunchData(RestaurantCallback callback) {
        reference.collection(USER_COLLECTION_NAME).document(getCurrentUserData().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            String lunchChoice = (String) documentSnapshot.getData().get(USER_LUNCH_CHOICE_FIELD);
            callback.userCallback(lunchChoice);
        });
    }

    public void getUsersAndRestaurantsData(CoworkerCallback callback, CallbackUserDataSource callbackDataSource) {
        reference.collection(USER_COLLECTION_NAME).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documentsSnapshots = queryDocumentSnapshots.getDocuments();
            ArrayList<Map<String, Object>> userDocuments = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentsSnapshots) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    if (!Objects.equals(document.get(USER_ID_FIELD), getCurrentUserData().getUid())) {
                        userDocuments.add(document);
                    }
                }
            }
            reference.collection(RESTAURANT_COLLECTION_NAME).get()
                    .addOnSuccessListener(restaurantQueryDocumentSnapshots -> {
                        ArrayList<Map<String, Object>> restaurantDocuments = new ArrayList<>();
                        List<DocumentSnapshot> restaurantDocumentsSnapshot = restaurantQueryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : restaurantDocumentsSnapshot) {
                            Map<String, Object> document = documentSnapshot.getData();
                            if (document != null) {
                                restaurantDocuments.add(document);
                            }
                            callbackDataSource.usersAndRestaurantsData(userDocuments, restaurantDocuments, callback);
                        }
                    });
        });
    }


    public void updateLunchChoiceData(String restaurantId, DetailCallback callback) {
        String uid = getCurrentUserData().getUid();
        if (uid != null) {
            WriteBatch batch = reference.batch();
            reference.collection(USER_COLLECTION_NAME).document(uid).get().addOnSuccessListener(documentSnapshot -> {
                Map<String, Object> document = documentSnapshot.getData();
                String previousLunchoice = "";
                if (document != null) {
                    if (document.get(USER_LUNCH_CHOICE_FIELD) != null &&
                            !Objects.equals(document.get(USER_LUNCH_CHOICE_FIELD), "")) {
                        previousLunchoice = (String) document.get(USER_LUNCH_CHOICE_FIELD);
                    }
                    if (Objects.equals(previousLunchoice, restaurantId)) {
                        document.put(USER_LUNCH_CHOICE_FIELD, "");
                        batch.update(reference.collection(USER_COLLECTION_NAME).document(uid), document);
                    } else {
                        document.put(USER_LUNCH_CHOICE_FIELD, restaurantId);
                        batch.set(reference.collection(USER_COLLECTION_NAME).document(uid), document);
                    }
                }
                updateLunchChoiceOnRestaurant(previousLunchoice, restaurantId, uid, callback, batch);
            });
        }
    }

    private void updateLunchChoiceOnRestaurant(String previousLunchChoice, String newLunchChoice, String uid, DetailCallback callback, WriteBatch batch) {
        if (!Objects.equals(previousLunchChoice, "")) {
            reference.collection(RESTAURANT_COLLECTION_NAME).document(previousLunchChoice).get().addOnSuccessListener(restaurantDocumentSnapshot -> {
                ArrayList<String> lunchers;
                Map<String, Object> document = restaurantDocumentSnapshot.getData();
                if (document != null) {
                    if (document.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                        lunchers = (ArrayList<String>) document.get(RESTAURANT_LUNCHERS_FIELD);
                        lunchers.remove(uid);
                        document.put(RESTAURANT_LUNCHERS_FIELD, lunchers);
                        DocumentReference restaurantRef = reference.collection(RESTAURANT_COLLECTION_NAME).document(previousLunchChoice);
                        batch.update(restaurantRef, document);
                    }
                    if (!Objects.equals(previousLunchChoice, newLunchChoice)) {
                        setNewLunchChoice(newLunchChoice, callback, uid, batch);
                    } else {
                        batch.commit().addOnCompleteListener(task -> callback.isLuncherCallback(false));
                    }
                }
            });
        } else {
            setNewLunchChoice(newLunchChoice, callback, uid, batch);
        }

    }

    private void setNewLunchChoice(String newLunchChoice, DetailCallback callback, String uid, WriteBatch batch) {

        reference.collection(RESTAURANT_COLLECTION_NAME).document(newLunchChoice).get().addOnSuccessListener(newRestaurantDocumentSnapshot -> {
            ArrayList<String> newLunchers = new ArrayList<>();
            Map<String, Object> newDocument = newRestaurantDocumentSnapshot.getData();
            if (newDocument != null) {
                if (newDocument.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                    newLunchers = (ArrayList<String>) newDocument.get(RESTAURANT_LUNCHERS_FIELD);
                }
                newLunchers.add(uid);
                newDocument.put(RESTAURANT_LUNCHERS_FIELD, newLunchers);
                DocumentReference restaurantRef = reference.collection(RESTAURANT_COLLECTION_NAME).document(newLunchChoice);
                batch.update(restaurantRef, newDocument);
                batch.commit().addOnCompleteListener(task -> callback.isLuncherCallback(true));
            }
        });
    }
}

package com.example.go4lunch.data;

import android.net.Uri;
import android.util.Log;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.SignOutCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserRepository implements UserRepositoryContract {
    private static final String COLLECTION_NAME = "users";
    private static final String USER_NAME_FIELD = "username";
    private static final String USER_ID_FIELD = "uid";
    private static final String USER_PICTURE_FIELD = "urlpicture";
    private static final String USER_LUNCH_CHOICE_FIELD = "lunchchoice";
    private static final String USER_EVALUATIONS_FIELD = "evaluations";

    private static final String RESTAURANT_COLLECTION_NAME = "restaurants";
    private static final String RESTAURANT_LUNCHERS_FIELD = "restaurantluncher";


    private final CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);


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

    public String getCurrentUserMail() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }

    public void createUser() {
        UserEntity user = getCurrentUser();
        if (user != null) {
            reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                boolean shouldCreateUser = true;
                for (DocumentSnapshot documentSnapshot : documents) {
                    Map<String, Object> document = documentSnapshot.getData();
                    if (document != null) {
                        if (Objects.equals(getUserEntity(document).getUid(), getCurrentUserUID())) {
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
                    reference.document(uid).set(data);
                }
            });

        }
    }

    @Override
    public void updateUsername(String username) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            reference.document(uid).update(USER_NAME_FIELD, username);
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();
            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(request);
        }
    }

    @Override
    public void updateLunchChoiceOnUserAndPreviousRestaurant(String restaurantId, DetailCallback callback) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            FirebaseFirestore batchReference = FirebaseFirestore.getInstance();
            WriteBatch batch = batchReference.batch();
            reference.document(uid).get().addOnSuccessListener(documentSnapshot -> {
                Map<String, Object> document = documentSnapshot.getData();
                String previousLunchoice = "";
                if (document != null) {
                    DocumentReference userBatchRef = batchReference.collection(COLLECTION_NAME).document(uid);
                    if (document.get(USER_LUNCH_CHOICE_FIELD) != null &&
                            !Objects.equals(document.get(USER_LUNCH_CHOICE_FIELD), "")) {
                        previousLunchoice = (String) document.get(USER_LUNCH_CHOICE_FIELD);
                    }
                    if (Objects.equals(previousLunchoice, restaurantId)) {
                        document.put(USER_LUNCH_CHOICE_FIELD, "");
                        batch.update(userBatchRef, document);
                    } else {
                        document.put(USER_LUNCH_CHOICE_FIELD, restaurantId);
                        batch.set(userBatchRef, document);
                    }
                }
                updateLunchChoiceOnRestaurant(previousLunchoice, restaurantId, callback, batch);
            });
        }
    }

    private void updateLunchChoiceOnRestaurant(String previousLunchChoice, String newLunchChoice, DetailCallback callback, WriteBatch batch) {
        FirebaseFirestore batchReference = FirebaseFirestore.getInstance();
        if (!Objects.equals(previousLunchChoice, "")) {
            batchReference.collection(RESTAURANT_COLLECTION_NAME).document(previousLunchChoice).get().addOnSuccessListener(restaurantDocumentSnapshot -> {
                ArrayList<String> lunchers;
                Map<String, Object> document = restaurantDocumentSnapshot.getData();
                if (document != null) {
                    if (document.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                        lunchers = (ArrayList<String>) document.get(RESTAURANT_LUNCHERS_FIELD);
                        lunchers.remove(getCurrentUserUID());
                        document.put(RESTAURANT_LUNCHERS_FIELD, lunchers);
                        DocumentReference restaurantRef = batchReference.collection(RESTAURANT_COLLECTION_NAME).document(previousLunchChoice);
                        batch.update(restaurantRef, document);
                    }
                    if (!Objects.equals(previousLunchChoice, newLunchChoice)) {
                        setNewLunchChoice(newLunchChoice, callback, batch);
                    } else {
                        batch.commit().addOnCompleteListener(task -> callback.isLuncherCallback(false));
                    }
                }
            });
        } else {
            setNewLunchChoice(newLunchChoice, callback, batch);
        }

    }

    private void setNewLunchChoice(String newLunchChoice, DetailCallback callback, WriteBatch batch) {
        FirebaseFirestore batchReference = FirebaseFirestore.getInstance();
        batchReference.collection(RESTAURANT_COLLECTION_NAME).document(newLunchChoice).get().addOnSuccessListener(newRestaurantDocumentSnapshot -> {
            ArrayList<String> newLunchers = new ArrayList<>();
            Map<String, Object> newDocument = newRestaurantDocumentSnapshot.getData();
            if (newDocument != null) {
                if (newDocument.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                    newLunchers = (ArrayList<String>) newDocument.get(RESTAURANT_LUNCHERS_FIELD);
                }
                newLunchers.add(getCurrentUserUID());
                newDocument.put(RESTAURANT_LUNCHERS_FIELD, newLunchers);
                DocumentReference restaurantRef = batchReference.collection(RESTAURANT_COLLECTION_NAME).document(newLunchChoice);
                batch.update(restaurantRef, newDocument);
                batch.commit().addOnCompleteListener(task -> callback.isLuncherCallback(true));
            }
        });
    }

    @Override
    public void deleteUserFromFirestore(SignOutCallback callback) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            reference.document(uid).get().addOnSuccessListener(documentSnapshot -> {
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
                                if (actualLunchers != null && actualLunchers.contains(getCurrentUserUID())) {
                                    actualLunchers.remove(getCurrentUserUID());
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
    }

    private void deleteAndSignOut(String uid, SignOutCallback callback) {
        reference.document(uid).delete();
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete();
        FirebaseAuth.getInstance().signOut();
        callback.signout();
    }

    @Override
    public void getCurrentUserEvaluations(DetailCallback callback, String restaurantId) {
        reference.document(getCurrentUserUID()).get().addOnSuccessListener(documentSnapshot -> {
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

    @Override
    public void getUsersLuncherByIds(ArrayList<String> lunchers, DetailCallback callback) {
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            ArrayList<UserEntity> entities = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    UserEntity user = getUserEntity(document);
                    if (user.getUid() != null && lunchers.contains(user.getUid())) {
                        if (!user.getUid().equals(getCurrentUserUID())) {
                            entities.add(getUserEntity(document));
                        }
                    }
                }
            }
            callback.userCallback(entities);
        }).addOnFailureListener(throwable -> {
            Log.e("getUsers :", "on failure", throwable);
        });
    }

    @Override
    public void getAllUserAndRestaurants(CoworkerCallback callback) {
        CollectionReference restaurantReference = FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION_NAME);
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            ArrayList<UserEntity> entities = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    if (!Objects.equals(getUserEntity(document).getUid(), getCurrentUserUID())) {
                        entities.add(getUserEntity(document));
                    }
                }
            }
            restaurantReference.get()
                    .addOnSuccessListener(restaurantQueryDocumentSnapshots -> {
                        ArrayList<RestaurantEntity> restaurantEntities = new ArrayList<>();
                        List<DocumentSnapshot> restaurantDocuments = restaurantQueryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : restaurantDocuments) {
                            Map<String, Object> document = documentSnapshot.getData();
                            if (document != null) {
                                restaurantEntities.add(getRestaurant(document));
                            }
                            callback.coworkerCallback(entities, restaurantEntities);
                        }
                    });
        }).addOnFailureListener(throwable -> {
            Log.e("getUsers :", "on failure", throwable);
        });
    }

    public void getCurrentUserLunch(RestaurantCallback callback) {
        reference.document(getCurrentUserUID()).get().addOnSuccessListener(documentSnapshot -> {
            UserEntity user = getUserEntity(documentSnapshot.getData());
            callback.userCallback(user.getLunchChoice());
        });

    }

    private UserEntity getUserEntity(Map<String, Object> document) {
        String id = "";
        String name = "";
        Uri picture = Uri.parse("");
        String lunchChoice = "";
        ArrayList<String> evals = new ArrayList<>();
        if (document != null) {
            id = (String) document.get(USER_ID_FIELD);
            name = (String) document.get(USER_NAME_FIELD);
            if (document.get(USER_PICTURE_FIELD) != null) {
                picture = Uri.parse((String) document.get(USER_PICTURE_FIELD));
            }
            evals = (ArrayList<String>) document.get(USER_EVALUATIONS_FIELD);
            lunchChoice = (String) document.get(USER_LUNCH_CHOICE_FIELD);
        }
        return new UserEntity(id, name, picture, lunchChoice, evals);
    }

    private RestaurantEntity getRestaurant(Map<String, Object> document) {
        String id = "";
        Long eval = 0L;
        String description = "";
        String name = "";
        GeoPoint position = null;
        String openingHour = "";
        String picture = "";
        ArrayList<String> lunchers = new ArrayList<>();
        if (document != null) {
            id = (String) document.get("restaurantid");
            eval = (Long) document.get("restaurantevaluations");
            description = (String) document.get("restaurantdescription");
            name = (String) document.get("restaurantname");
            position = (GeoPoint) document.get("restaurantposition");
            openingHour = (String) document.get("restaurantopeninghours");
            picture = (String) document.get("restaurantpictureurl");
            lunchers = (ArrayList<String>) document.get(RESTAURANT_LUNCHERS_FIELD);
        }
        return new RestaurantEntity(id, name, description, openingHour, position, eval, picture, lunchers);
    }
}

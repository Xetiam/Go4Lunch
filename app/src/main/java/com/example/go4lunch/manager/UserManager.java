package com.example.go4lunch.manager;

import android.content.Context;

import com.example.go4lunch.data.UserRepository;
import com.example.go4lunch.model.UserEntity;
import com.google.android.gms.tasks.Task;

public class UserManager {
   private static volatile UserManager instance;
   private final UserRepository userRepository;

   private UserManager(){ userRepository = UserRepository.getInstance(); }

    public static UserManager getInstance() {
       UserManager result = instance;
        if (result == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public Boolean isCurrentUserLogged() { return (userRepository.getCurrentUser() != null); }
    public Task<Void> signOut(Context context) { return userRepository.signOut(context); }
    public void createUser(){
        userRepository.createUser();
    }

    public Task<UserEntity> getUserData(){
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(UserEntity.class)) ;
    }

    public Task<Void> updateUsername(String username){
        return userRepository.updateUsername(username);
    }

    public void updateLunchChoice(String lunchChoice){
        userRepository.updateLunchChoice(lunchChoice);
    }

    public Task<Void> deleteUser(Context context){
        // Delete the user account from the Auth
        return userRepository.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            userRepository.deleteUserFromFirestore();
        });
    }
}

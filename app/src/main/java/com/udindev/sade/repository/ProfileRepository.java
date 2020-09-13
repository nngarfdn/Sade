package com.udindev.sade.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udindev.sade.model.Profile;

import java.util.HashMap;
import java.util.Map;

public class ProfileRepository {
    private final String TAG = getClass().getSimpleName();
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private MutableLiveData<Profile> resultData = new MutableLiveData<>();
    public LiveData<Profile> getData(){
        return resultData;
    }

    public void query(String userId){
        database.collection("profile").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            Profile profile = new Profile(
                                    document.getString("address"),
                                    document.getString("phoneNumber"),
                                    document.getString("waNumber"));
                            resultData.postValue(profile);
                            Log.d(TAG, "Document was queried");
                        } else Log.w(TAG, "Error querying document", task.getException());
                    }
                });
    }

    public void insert(String userId, Profile profile){
        database.collection("profile").document(userId)
                .set(hashMapProfile(profile))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.d(TAG, "Document was added");
                        else Log.w(TAG, "Error adding document", task.getException());
                    }
                });
    }

    public void update(String userId, Profile profile){
        database.collection("profile").document(userId)
                .update(hashMapProfile(profile))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                        else Log.w(TAG, "Error updating document", task.getException());
                    }
                });
    }

    private Map<String, Object> hashMapProfile(Profile profile){
        Map<String, Object> document = new HashMap<>();
        document.put("address", profile.getAddress());
        document.put("phoneNumber", profile.getPhoneNumber());
        document.put("waNumber", profile.getWaNumber());
        return document;
    }
}

package com.udindev.sade.cobacoba;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRepository {
    private final String TAG = getClass().getSimpleName();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final MutableLiveData<Favorite> resultData = new MutableLiveData<>();
    public LiveData<Favorite> getData(){
        return resultData;
    }

    public void query(String userId){
        database.collection("favorite").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Favorite favorite = task.getResult().toObject(Favorite.class);

                            // Pengguna yang belum pernah tambah favorit
                            if (favorite == null) {
                                favorite = new Favorite(new ArrayList<>());
                                insert(userId, favorite);  // Sekalian bikin dokumennya
                            }

                            resultData.postValue(favorite);
                        } else Log.w(TAG, "Error querying document", task.getException());
                    }
                });
    }

    private void insert(String userId, Favorite favorite){
        database.collection("favorite").document(userId)
                .set(favorite)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.d(TAG, "Document was added");
                        else Log.w(TAG, "Error adding document", task.getException());
                    }
                });
    }

    public void add(String userId, String productId){
        database.collection("favorite").document(userId)
                .update("listProductId", FieldValue.arrayUnion(productId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                        else Log.w(TAG, "Error updating document", task.getException());
                    }
                });
    }

    public void remove(String userId, String productId){
        database.collection("favorite").document(userId)
                .update("listProductId", FieldValue.arrayRemove(productId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.d(TAG, "Document was updated");
                        else Log.w(TAG, "Error updating document", task.getException());
                    }
                });
    }
}

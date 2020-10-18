package com.udindev.sade.cobacoba;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FavoriteViewModel extends ViewModel {
    private final FavoriteRepository repository = new FavoriteRepository();

    public LiveData<Favorite> getData(){
        return repository.getData();
    }

    public void loadData(String userId){
        repository.query(userId);
    }

    public void add(String userId, String productId){
        repository.add(userId, productId);
    }

    public void remove(String userId, String productId){
        repository.remove(userId, productId);
    }
}

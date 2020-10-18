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

    public void insert(String userId, Favorite favorite){
        repository.insert(userId, favorite);
    }

    public void update(String userId, Favorite favorite){
        repository.update(userId, favorite);
    }
}

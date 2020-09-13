package com.udindev.sade.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udindev.sade.model.Profile;
import com.udindev.sade.repository.ProfileRepository;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository repository = new ProfileRepository();

    public LiveData<Profile> getData(){
        return repository.getData();
    }

    public void loadData(String userId){
        repository.query(userId);
    }

    public void insert(String userId, Profile profile){
        repository.insert(userId, profile);
    }

    public void update(String userId, Profile profile){
        repository.update(userId, profile);
    }
}

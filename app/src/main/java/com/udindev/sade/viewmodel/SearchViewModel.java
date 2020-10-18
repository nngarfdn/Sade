package com.udindev.sade.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udindev.sade.model.Filter;
import com.udindev.sade.model.Produk;
import com.udindev.sade.repository.SearchRepository;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private final SearchRepository repository = new SearchRepository();

    public LiveData<ArrayList<Produk>> getData(){
        return repository.getData();
    }

    public void loadData(Filter filter){
        repository.query(filter);
    }
}

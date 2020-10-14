package com.udindev.sade.cobacoba;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udindev.sade.model.Produk;

import java.util.ArrayList;

public class ProductViewModel extends ViewModel {
    private ProductRepository repository = new ProductRepository();

    public LiveData<ArrayList<Produk>> getData(){
        return repository.getData();
    }

    public void loadData(Filter filter){
        repository.query(filter);
    }
}

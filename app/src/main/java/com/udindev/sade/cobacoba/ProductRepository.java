package com.udindev.sade.cobacoba;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.udindev.sade.model.Produk;

import java.util.ArrayList;

public class ProductRepository {
    private final String TAG = getClass().getSimpleName();
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final MutableLiveData<ArrayList<Produk>> resultData = new MutableLiveData<>();
    public LiveData<ArrayList<Produk>> getData(){
        return resultData;
    }

    public void query(Filter filter){
        Query query = database.collection("produk");

        // Filter lokasi
        if (filter.isProvinsi()) query = query.whereEqualTo("provinsi", filter.getNamaProvinsi());
        if (filter.isKabupaten()) query = query.whereEqualTo("kabupaten", filter.getNamaKabupaten());
        if (filter.isKecamatan()) query = query.whereEqualTo("kecamatan", filter.getNamaKecamatan());

        // Sorting harga
        if (filter.isMurah()) query = query.orderBy("harga", Query.Direction.ASCENDING);
        else query = query.orderBy("harga", Query.Direction.DESCENDING);

        // Mulai kueri
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<Produk> listProduct = new ArrayList<>();
                            QuerySnapshot querySnapshot = task.getResult();

                            for (DocumentSnapshot snapshot : querySnapshot){
                                Produk produk = snapshot.toObject(Produk.class);
                                Log.d(TAG, "Nama item: " + produk.getNama());

                                if (filter.isProduk()){
                                    if (produk.getKategori().equals("Produk")){
                                        listProduct.add(produk);
                                        continue;
                                    }
                                }

                                if (filter.isJasa()){
                                    if (produk.getKategori().equals("Jasa")){
                                        listProduct.add(produk);
                                        continue;
                                    }
                                }

                                if (filter.isUsaha()){
                                    if (produk.getKategori().equals("Usaha")){
                                        listProduct.add(produk);
                                        continue;
                                    }
                                }

                                if (filter.isLainnya()){
                                    if (produk.getKategori().equals("Lainnya")){
                                        listProduct.add(produk);
                                    }
                                }
                            }

                            resultData.postValue(listProduct);
                            Log.d(TAG, "Document was queried");
                        } else Log.w(TAG, "Error querying document", task.getException());
                    }
                });
    }

    /*private Map<String, Object> hashMapProduct(Produk product){
        Map<String, Object> document = new HashMap<>();
        document.put("alamat", product.getAlamat());
        document.put("deskripsi", product.getDeskripsi());
        document.put("email", product.getEmail());
        document.put("harga", product.getHarga());
        document.put("isFavorite", product.isFavorite());
        document.put("kabupaten", product.getKabupaten());
        document.put("kategori", product.getKategori());
        document.put("kecamatan", product.getKecamatan());
        document.put("nama", product.getNama());
        document.put("photo", product.getPhoto());
        document.put("provinsi", product.getProvinsi());
        document.put("wa", product.getWa());
        return document;
    }*/
}

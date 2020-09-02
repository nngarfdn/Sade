package com.udindev.sade.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.udindev.sade.R;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.ProdukViewModel;


public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    ProdukViewModel produkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);

        Produk keyboard = new Produk(null, "Keyboard", "produk", "kukap", "Srandakan",
                "Bantul", "Yogyakarta", "081234567890", 100000, "Keyboard terbaik", null);

        Produk mouse = new Produk(null, "Mouse", "produk", "Koripan", "Srandakan",
                "Sleman", "Yogyakarta", "081234567890", 50000, "Keyboard terbaik", null);

        Produk potonRambut = new Produk(null, "Postong Rambut", "jasa", "Jopaten", "Sanden",
                "Bantul", "Jakarta", "081234567890", 7000, "Keyboard terbaik", null);

        Produk updateMouse = new Produk("COFgOY4NO5LIsWilHy49", "Mouse Logitech", "produk", "Koripan", "Srandakan",
                "Sleman", "Yogyakarta", "081234567890", 50000, "Mouse terbaik", null);

        String search = "Keyboard";
        String kategoriSearch = "jasa";
        String provinsiSearch = "Yogyakarta";
        String kabupatenSearch = "o";
        String kecamatanSearch = "Srandakan";

//        insertProduk(keyboard);
//        insertProduk(mouse);
//        insertProduk(potonRambut);

//        updateProduk(updateMouse);
        getAllResult();
        getResultTinggiKeRendah();
        getResultRendahKeTinggi();
        getResultSearch(search);
        getResultKategori(kategoriSearch);
        getResultProvinsi(provinsiSearch);
        getResultKabupaten(kabupatenSearch);
        getResultKecamatan(kecamatanSearch);

        tesKotlin();
        tesLoginRegister();
        tesDataClass();
    }

    private void insertProduk(Produk produk) {
        produkViewModel.insertProduk(produk);
    }

    private void deleteProduk(Produk produk) {
        produkViewModel.deteteProduk(produk);
    }

    private void updateProduk(Produk produk) {
        produkViewModel.updateProduk(produk);
    }

    private void getAllResult() {
        produkViewModel.loadResult();
        produkViewModel.getResult().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getAllResult " + produk.component2());
            }
        });
    }

    private void deleteAll() {
        produkViewModel.loadResult();
        produkViewModel.getResult().observe(this, result -> {
            for (Produk produk : result) {
                deleteProduk(produk);
            }
        });
    }

    private void getResultTinggiKeRendah() {
        produkViewModel.loadResultTinggiKeRendah();
        produkViewModel.getHargaTinggiKeRendah().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: tinggi ke rendah " + produk.component2() + " harganya " + produk.component9());
            }
        });
    }

    private void getResultRendahKeTinggi() {
        produkViewModel.loadResultRendahKeTinggi();
        produkViewModel.getHargaRendahKeTinggi().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: rendah ke tinggi" + produk.component2() + " harganya " + produk.component9());
            }
        });
    }

    private void getResultProvinsi(String provinsi) {
        produkViewModel.loadResultProvinsi(provinsi);
        produkViewModel.getResultProvinsi().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultProvinsi" + produk.component2() + " provinsi " + produk.component7());
            }
        });
    }

    private void getResultKabupaten(String kabupaten) {
        produkViewModel.loadResultKabupaten(kabupaten);
        produkViewModel.getResultKabupaten().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKabupaten" + produk.component2() + " kabupaten " + produk.component6());
            }
        });
    }

    private void getResultKecamatan(String kecamatan) {
        produkViewModel.loadResultKecamatan(kecamatan);
        produkViewModel.getResultKecamatan().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKecamatan" + produk.component2() + " kecamatan " + produk.component5());
            }
        });
    }

    private void getResultSearch(String nama) {
        produkViewModel.loadResultSearch(nama);
        produkViewModel.getResultSearch().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultSearch" + produk.component2() );
            }
        });
    }

    private void getResultKategori(String kategori) {
        produkViewModel.loadResultByKategory(kategori);
        produkViewModel.getResultByKategori().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKategori" + produk.component2() + " kategori " + produk.component3());
            }
        });
    }

    private void tesDataClass() {
        Produk produk = new Produk("1", "Java", null, null, null, null, null, null, null, null, null);
        Log.d(TAG, "onCreate: " + produk.toString());
        Log.d(TAG, "onCreate: " + produk.component2());
    }

    private void tesLoginRegister() {
        Button btnLogin = findViewById(R.id.tesLogin);
        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void tesKotlin() {
        Button nanangTest = findViewById(R.id.tesDataNanang);
        nanangTest.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, KotlinTest.class);
            startActivity(intent);
        });
    }
}
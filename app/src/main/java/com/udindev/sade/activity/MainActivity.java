package com.udindev.sade.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.udindev.sade.R;
import com.udindev.sade.model.Produk;
import com.udindev.sade.pageradapter.MainPagerAdapter;
import com.udindev.sade.viewmodel.ProdukViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    ProdukViewModel produkViewModel;
    List<Produk> listRendahKeTinggi = new ArrayList<Produk>();
    List<Produk> listKecamatan = new ArrayList<Produk>();
    List<Produk> listShow = new ArrayList<Produk>();
    private Toast exitToast;
    private FragmentManager fragmentManager;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        setBottomNavigationView();

        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);
        exitToast = Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed(){
        if (fragmentManager.getBackStackEntryCount() > 0) fragmentManager.popBackStack();
        else {
            if (exitToast.getView().isShown()) super.onBackPressed();
            else exitToast.show();
        }
    }

    private void setBottomNavigationView() {
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(fragmentManager);
        ViewPager viewPager = findViewById(R.id.vp_main);
        viewPager.setAdapter(pagerAdapter);

        BottomNavigationView navigationView = findViewById(R.id.bn_main);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard_menu:
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.favorite_menu:
                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.profile_menu:
                        viewPager.setCurrentItem(2);
                        break;
                }
                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) fragmentManager.popBackStack();
                return true;
            }
        });

        // Biar warna berubah saat ganti fragment dengan drag
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                navigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void insertProduk(Produk produk) {
        produkViewModel.insertProduk(produk);
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
                listRendahKeTinggi.add(produk);
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
                listKecamatan.add(produk);
                Log.d(TAG, "onCreate: getResultKecamatan" + produk.component2() + " kecamatan " + produk.component5());
            }
        });
    }

    private void getResultSearch(String nama) {
        produkViewModel.loadResultSearch(nama);
        produkViewModel.getResultSearch().observe(this, result -> {
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultSearch" + produk.component2());
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

//    private void tesLoginRegister() {
//        Button btnLogin = findViewById(R.id.tesLogin);
//        btnLogin.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    private void tesKotlin() {
//        Button nanangTest = findViewById(R.id.tesDataNanang);
//        nanangTest.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, KotlinTest.class);
//            startActivity(intent);
//        });
//    }
}
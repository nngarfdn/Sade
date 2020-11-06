package com.udindev.sade.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.udindev.sade.R;
import com.udindev.sade.pageradapter.MainPagerAdapter;
import com.udindev.sade.viewmodel.ProdukViewModel;

public class MainActivity extends AppCompatActivity {
    ProdukViewModel produkViewModel;
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
}
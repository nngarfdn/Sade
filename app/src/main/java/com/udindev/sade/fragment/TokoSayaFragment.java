package com.udindev.sade.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udindev.sade.R;
import com.udindev.sade.adapter.TokoSayaAdapter;
import com.udindev.sade.callback.OnProductAddCallback;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.ProdukViewModel;

public class TokoSayaFragment extends Fragment {
    Button btnTambah;
    ImageView imgIlustrasi;
    TextView txtProdukKosong;
    RecyclerView rvTokoSaya ;
    ProdukViewModel produkViewModel;
    Button fabTambahProduk ;
    private FirebaseUser firebaseUser;
    private static final String TAG = "TokoSayaFragment";
    private final OnProductAddCallback callback;

    public TokoSayaFragment(OnProductAddCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toko_saya, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTambah = view.findViewById(R.id.btn_tambah_produk);
        imgIlustrasi = view.findViewById(R.id.img_produk_kosong);
        txtProdukKosong = view.findViewById(R.id.txt_produk_kosong);
        rvTokoSaya = view.findViewById(R.id.rv_toko_saya);
        fabTambahProduk = view.findViewById(R.id.fab_add);

        btnTambah.setOnClickListener(v -> loadFragment(new TambahProdukFragment(callback)));
        fabTambahProduk.setOnClickListener(v -> loadFragment(new TambahProdukFragment(callback)));

        produkViewModel.getDataEmail().observe(this, result -> {
            if (result.isEmpty()){
                btnTambah.setVisibility(View.VISIBLE);
                txtProdukKosong.setVisibility(View.VISIBLE);
                imgIlustrasi.setVisibility(View.VISIBLE);
                rvTokoSaya.setVisibility(View.INVISIBLE); // List tidak mau refresh setelah produk terakhir dihapus
            }else {
                btnTambah.setVisibility(View.INVISIBLE);
                txtProdukKosong.setVisibility(View.INVISIBLE);
                imgIlustrasi.setVisibility(View.INVISIBLE);
                rvTokoSaya.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rvTokoSaya.setLayoutManager(layoutManager);
                TokoSayaAdapter adapter = new TokoSayaAdapter(result);
                rvTokoSaya.setAdapter(adapter);
                for (Produk produk : result) {
                    Log.d(TAG, "onCreate: getResultKategori" + produk.component2() + " kategori " + produk.component3());
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        produkViewModel.loadResultDataEmail(firebaseUser.getEmail());
    }
}
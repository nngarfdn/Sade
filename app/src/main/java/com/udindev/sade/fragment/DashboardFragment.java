package com.udindev.sade.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.udindev.sade.R;
import com.udindev.sade.adapter.JasaMenuAdapter;
import com.udindev.sade.adapter.ProdukMenuAdapter;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.ProdukViewModel;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    EditDrawableText search;
    ShimmerFrameLayout shimmerFrameLayoutProduk, shimmerFrameLayoutJasa;
    private RecyclerView rvProdukMenu, rvJasaMenu;
    ProdukViewModel produkViewModel;

    public DashboardFragment() {

    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvProdukMenu = view.findViewById(R.id.rv_produk_dasbor);
        rvJasaMenu = view.findViewById(R.id.rv_jasa_dasbor);
        shimmerFrameLayoutProduk = view.findViewById(R.id.shimmerFrameLayoutProduk);
        shimmerFrameLayoutJasa = view.findViewById(R.id.shimmerFrameLayoutJasa);
        search = view.findViewById(R.id.search);
        search.setDrawableClickListener(target -> {
            if (target == DrawablePosition.RIGHT) {
                String text = search.getText().toString();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        shimmerFrameLayoutProduk.startShimmerAnimation();
        shimmerFrameLayoutJasa.startShimmerAnimation();
        getResultProduk("produk");
        getAllResult();

    }

    private void getResultProduk(String kategori) {
        produkViewModel.loadResultByKategory(kategori);
        produkViewModel.getResultByKategori().observe(this, result -> {
            shimmerFrameLayoutProduk.stopShimmerAnimation();
            shimmerFrameLayoutProduk.setVisibility(View.INVISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            rvProdukMenu.setLayoutManager(layoutManager);
            ProdukMenuAdapter adapter = new ProdukMenuAdapter(result);
            rvProdukMenu.setAdapter(adapter);
            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKategori" + produk.component2() + " kategori " + produk.component3());
            }
        });
    }

    private void getAllResult() {
        produkViewModel.loadResult();
        produkViewModel.getResult().observe(this, result -> {
            shimmerFrameLayoutJasa.stopShimmerAnimation();
            shimmerFrameLayoutJasa.setVisibility(View.INVISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvJasaMenu.setLayoutManager(layoutManager);
            JasaMenuAdapter adapter = new JasaMenuAdapter(result);
            rvJasaMenu.setAdapter(adapter);

            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKategori" + produk.component2() + " kategori " + produk.component3());
            }
        });
    }

}
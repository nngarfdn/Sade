package com.udindev.sade.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.udindev.sade.R;
import com.udindev.sade.adapter.JasaMenuAdapter;
import com.udindev.sade.adapter.ProdukMenuAdapter;
import com.udindev.sade.callback.OnProductAddCallback;
import com.udindev.sade.model.Filter;
import com.udindev.sade.activity.SearchActivity;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.ProdukViewModel;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

import static com.udindev.sade.utils.AppUtils.getDefaultFilter;

public class DashboardFragment extends Fragment implements OnProductAddCallback {

    private static final String TAG = "DashboardFragment";
    private EditDrawableText search;
    private ShimmerFrameLayout shimmerFrameLayoutProduk, shimmerFrameLayoutJasa;
    private RecyclerView rvProdukMenu, rvJasaMenu;
    private ProdukViewModel produkViewModel;
    private Button btnProdukSelengkapnya , btnSemuaSelengkapnya ;
    private ImageButton imgbtnSemua, imgBtnTokoSaya, imgBtnUsaha,  imgBtnJasa, imgBtnProduk, imgBtnLainnya;

    public DashboardFragment() {}

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
        rvProdukMenu = view.findViewById(R.id.rv_semua_item);
        rvJasaMenu = view.findViewById(R.id.rv_jasa_dasbor);

        btnProdukSelengkapnya = view.findViewById(R.id.btn_produk_selengkapnya);
        btnSemuaSelengkapnya = view.findViewById(R.id.btn_semua_selengkapnya);

        imgbtnSemua = view.findViewById(R.id.imgbtn_semua);
        imgBtnJasa = view.findViewById(R.id.imgbtn_jasa);
        imgBtnProduk = view.findViewById(R.id.imgbtn_produk);
        imgBtnLainnya = view.findViewById(R.id.imgbtn_lainnya);
        imgBtnUsaha = view.findViewById(R.id.imgbtn_usaha);
        imgBtnTokoSaya = view.findViewById(R.id.btn_tokosaya);

        imgBtnTokoSaya.setOnClickListener(v -> loadFragment(new TokoSayaFragment(this)));
        imgbtnSemua.setOnClickListener(v -> performSearch("Semua"));
        imgBtnUsaha.setOnClickListener(v -> performSearch("Usaha"));
        imgBtnJasa.setOnClickListener(v -> performSearch("Jasa"));
        imgBtnProduk.setOnClickListener(v -> performSearch("Produk"));
        imgBtnLainnya.setOnClickListener(v -> performSearch("Lainnya"));

        btnProdukSelengkapnya.setOnClickListener(v -> performSearch("Produk"));
        btnSemuaSelengkapnya.setOnClickListener(v -> performSearch("Semua"));

        shimmerFrameLayoutProduk = view.findViewById(R.id.shimmerFrameLayoutProduk);
        shimmerFrameLayoutJasa = view.findViewById(R.id.shimmerFrameLayoutJasa);
        search = view.findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch("Kata kunci");
                    return true;
                }
                return false;
            }
        });

        shimmerFrameLayoutProduk.startShimmerAnimation();
        shimmerFrameLayoutJasa.startShimmerAnimation();

        getResultProduk();
        getAllResult();

        // Tambah
        showShowcaseView();

        ImageButton btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch("Kata kunci");
            }
        });
    }

    private void showShowcaseView() {
        new MaterialIntroView.Builder(getActivity())
                .enableDotAnimation(false)
                .enableIcon(true)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(false)
                .setTargetPadding(32)
                .setInfoText("Ketuk ikon ini untuk menuju tokomu")
                .setShape(ShapeType.CIRCLE)
                .setTarget(getView().findViewById(R.id.btn_tokosaya))
                .setUsageId("btn_tokosaya")
                .show();
    }

    private void performSearch(String kategori) {
        Filter filter = getDefaultFilter();

        switch (kategori) {
            case "Produk":
                filter.setProduk(true);
                filter.setJasa(false);
                filter.setUsaha(false);
                filter.setLainnya(false);
                break;
            case "Jasa":
                filter.setProduk(false);
                filter.setJasa(true);
                filter.setUsaha(false);
                filter.setLainnya(false);
                break;
            case "Usaha":
                filter.setProduk(false);
                filter.setJasa(false);
                filter.setUsaha(true);
                filter.setLainnya(false);
                break;
            case "Lainnya":
                filter.setProduk(false);
                filter.setJasa(false);
                filter.setUsaha(false);
                filter.setLainnya(true);
                break;
            case "Kata kunci":
                filter.setKataKunci(search.getText().toString());
        }

        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("extra_filter", filter);
        startActivity(intent);
    }

    private void getResultProduk() {
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

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        produkViewModel.loadResultByKategory("Produk");
        produkViewModel.loadResult();
    }

    @Override
    public void onAdd() {
        onResume();
    }
}
package com.udindev.sade.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.udindev.sade.R;
import com.udindev.sade.adapter.ProdukMenuAdapter;
import com.udindev.sade.dialogfragment.FilterDialog;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.ProdukViewModel;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();

    private ShimmerFrameLayout shimmerFrameLayoutSemuaItem;
    private RecyclerView rvSemuaItem;
    private EditDrawableText search;
    private ProdukViewModel produkViewModel;

    private FilterDialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel.class);

        shimmerFrameLayoutSemuaItem = findViewById(R.id.shimmerFrameLayoutSemuaItem);
        rvSemuaItem = findViewById(R.id.rv_semua_item);
        search = findViewById(R.id.search);
        search.setDrawableClickListener(target -> {
            if (target == DrawablePosition.RIGHT) {
                String text = search.getText().toString();
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        });

        shimmerFrameLayoutSemuaItem.startShimmerAnimation();
        getAllResult();

        filterDialog = new FilterDialog(this);
        ImageButton btnFilter = findViewById(R.id.imgbtn_filter_semua);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.show(getSupportFragmentManager(), FilterDialog.class.getSimpleName());
            }
        });
    }

    private void getAllResult() {
        produkViewModel.loadResult();
        produkViewModel.getResult().observe(this, result -> {
            shimmerFrameLayoutSemuaItem.stopShimmerAnimation();
            shimmerFrameLayoutSemuaItem.setVisibility(View.INVISIBLE);
            LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
            rvSemuaItem.setLayoutManager(layoutManager);
            ProdukMenuAdapter adapter = new ProdukMenuAdapter(result);
            rvSemuaItem.setAdapter(adapter);

            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKategori" + produk.component2() + " kategori " + produk.component3());
            }
        });
    }
}

package com.udindev.sade.cobacoba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.udindev.sade.R;
import com.udindev.sade.dialogfragment.FilterDialog;
import com.udindev.sade.model.Produk;

public class SearchActivity extends AppCompatActivity implements FilterDialog.FilterDialogListener {
    private static final String TAG = SearchActivity.class.getSimpleName();

    private ShimmerFrameLayout shimmer;
    private EditDrawableText edtSearch;
    private ProductViewModel productViewModel;
    private Filter filter;
    private SearchAdapter adapter;
    private FilterDialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ambil konfigurasi filter
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) filter = bundle.getParcelable("extra_filter");
        else throw new ClassCastException(TAG + ": harus ada filter");

        // Initialize view
        RecyclerView recyclerView = findViewById(R.id.rv_semua_item);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        adapter = new SearchAdapter(this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        shimmer = findViewById(R.id.shimmerFrameLayoutSemuaItem);
        shimmer.startShimmerAnimation();

        edtSearch = findViewById(R.id.search);
        edtSearch.setText(filter.getKataKunci());
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        ImageButton btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        // Muat data
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.loadData(filter);
        productViewModel.getData().observe(this, result -> {
            shimmer.stopShimmerAnimation();
            shimmer.setVisibility(View.INVISIBLE);

            adapter.setData(result);
            performSearch();
        });

        // Filter dialog
        filterDialog = new FilterDialog(this);
        ImageButton btnFilter = findViewById(R.id.imgbtn_filter_semua);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("extra_filter", filter);
                filterDialog.setArguments(bundle);
                filterDialog.show(getSupportFragmentManager(), FilterDialog.class.getSimpleName());
            }
        });
    }

    private void performSearch() {
        String keyword = edtSearch.getText().toString();
        adapter.getFilter().filter(keyword);
    }

    @Override
    public void receiveFilter(Filter filter) {
        this.filter = filter;
        productViewModel.loadData(filter);
    }

    public static Filter getDefaultFilter(){
        return new Filter("",
                true, true, true, true,
                false, false, false,
                true,
                "", "", "",
                0, 0, 0);
    }
}

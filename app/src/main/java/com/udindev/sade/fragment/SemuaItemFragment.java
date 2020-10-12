package com.udindev.sade.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.EditDrawableText;
import com.udindev.sade.R;
import com.udindev.sade.adapter.ProdukMenuAdapter;
import com.udindev.sade.dialogfragment.FilterDialog;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.ProdukViewModel;


public class SemuaItemFragment extends Fragment {

    private static final String TAG = "SemuaItemFragment";

    private ShimmerFrameLayout shimmerFrameLayoutSemuaItem;
    private RecyclerView rvSemuaItem;
    private EditDrawableText search;
    private ProdukViewModel produkViewModel;

    private FilterDialog filterDialog;

    public SemuaItemFragment() {
        // Required empty public constructor
    }

    public static SemuaItemFragment newInstance(String param1, String param2) {
        SemuaItemFragment fragment = new SemuaItemFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmerFrameLayoutSemuaItem = view.findViewById(R.id.shimmerFrameLayoutSemuaItem);
        rvSemuaItem = view.findViewById(R.id.rv_semua_item);
        search = view.findViewById(R.id.search);
        search.setDrawableClickListener(target -> {
            if (target == DrawablePosition.RIGHT) {
                String text = search.getText().toString();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        shimmerFrameLayoutSemuaItem.startShimmerAnimation();
        getAllResult();

        filterDialog = new FilterDialog(getActivity());
        ImageButton btnFilter = view.findViewById(R.id.imgbtn_filter_semua);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.show(getActivity().getSupportFragmentManager(), FilterDialog.class.getSimpleName());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_semua_item, container, false);
    }

    private void getAllResult() {
        produkViewModel.loadResult();
        produkViewModel.getResult().observe(this, result -> {
            shimmerFrameLayoutSemuaItem.stopShimmerAnimation();
            shimmerFrameLayoutSemuaItem.setVisibility(View.INVISIBLE);
            LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
            rvSemuaItem.setLayoutManager(layoutManager);
            ProdukMenuAdapter adapter = new ProdukMenuAdapter(result);
            rvSemuaItem.setAdapter(adapter);

            for (Produk produk : result) {
                Log.d(TAG, "onCreate: getResultKategori" + produk.component2() + " kategori " + produk.component3());
            }
        });
    }

}
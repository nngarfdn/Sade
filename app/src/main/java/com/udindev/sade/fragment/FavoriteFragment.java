package com.udindev.sade.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udindev.sade.R;
import com.udindev.sade.adapter.FavoriteAdapter;
import com.udindev.sade.model.Favorite;
import com.udindev.sade.model.Produk;
import com.udindev.sade.viewmodel.FavoriteViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FavoriteFragment extends Fragment implements View.OnClickListener, FavoriteCallback {
    private FirebaseUser firebaseUser;
    private FavoriteViewModel favoriteViewModel;
    private FavoriteAdapter adapter;
    private FirebaseFirestore database;
    private LinearLayout layoutEmpty;
    private Spinner spinnerFilter;
    private List<String> listProductId = new ArrayList<>();

    public FavoriteFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rv_favorite_saya);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoriteAdapter(getActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        layoutEmpty = view.findViewById(R.id.layout_empty_favorite);

        ImageButton btnShop = view.findViewById(R.id.btn_shop_favorite);
        btnShop.setOnClickListener(this);

        Button btnFilter = view.findViewById(R.id.btn_filter_favorite);
        btnFilter.setOnClickListener(this);


        spinnerFilter = view.findViewById(R.id.spinner_filter_favorite);

        // Dapatkan daftar id produk yang difavoritkan
        favoriteViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Favorite>() {
            @Override
            public void onChanged(Favorite favorite) {
                // Kalo daftar favorit masih sama, jangan dimuat lagi
                if (!listProductId.equals(favorite.getListProductId())){
                    listProductId = favorite.getListProductId();
                    // Muat semua produk menurut id yang difavoritkan
                    loadProductById();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        favoriteViewModel.loadData(firebaseUser.getUid());
    }

    private void loadProductById(){
        ArrayList<Produk> listItem = new ArrayList<>();
        Iterator<String> iterator = listProductId.iterator();
        while (iterator.hasNext()){
            database.collection("produk").document(iterator.next())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Produk product = task.getResult().toObject(Produk.class);
                                if (product != null) listItem.add(product);
                                if (!iterator.hasNext()) onFinish(listItem); // Pemuatan id terakhir
                            }
                        }
                    });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_shop_favorite:
                loadFragment(new TokoSayaFragment());
                break;

            case R.id.btn_filter_favorite:
                performSearch();
                break;
        }
    }

    private void performSearch() {
        adapter.getFilter().filter(spinnerFilter.getSelectedItem().toString());
    }

    private void loadFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFinish(ArrayList<Produk> listItem) {
        adapter.setData(listItem);
        if (adapter.getItemCount() > 0) layoutEmpty.setVisibility(View.INVISIBLE);
        else layoutEmpty.setVisibility(View.VISIBLE);
        spinnerFilter.setSelection(0);
        performSearch();
    }
}

interface FavoriteCallback{
    void onFinish(ArrayList<Produk> listItem);
}
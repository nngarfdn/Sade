package com.udindev.sade.cobacoba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udindev.sade.R;
import com.udindev.sade.model.Produk;

import static com.udindev.sade.activity.DetailActivity.EXTRA_PRODUK;

public class DetailTestActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private FirebaseUser firebaseUser;
    private FavoriteViewModel favoriteViewModel;
    private ImageButton btnFavorite;
    private Produk produk;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_test);

        produk = getIntent().getParcelableExtra(EXTRA_PRODUK);
        Log.d(TAG, produk.getNama() + " " + produk.getId());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        btnFavorite = findViewById(R.id.btn_favorite_detail);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = firebaseUser.getUid();
                if (isFavorite){
                    favoriteViewModel.remove(userId, produk.getId());
                    btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                } else {
                    favoriteViewModel.add(userId, produk.getId());
                    btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                }
                isFavorite = !isFavorite;
            }
        });

        favoriteViewModel.getData().observe(this, new Observer<Favorite>() {
            @Override
            public void onChanged(Favorite favorite) {
                isFavorite = favorite.getListProductId().contains(produk.getId());
                if (isFavorite) btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                else btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        favoriteViewModel.loadData(firebaseUser.getUid());
    }
}
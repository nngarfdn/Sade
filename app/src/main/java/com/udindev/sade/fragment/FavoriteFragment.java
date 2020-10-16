package com.udindev.sade.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udindev.sade.R;
import com.udindev.sade.viewmodel.FavoriteViewModel;
import com.udindev.sade.viewmodel.ProfileViewModel;


public class FavoriteFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProfileViewModel profileViewModel;
    private FavoriteViewModel favoriteViewModel;
    private RecyclerView rvFavoriteSaya;
    private static final String TAG = "FavoriteFragment";

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        String email = firebaseUser.getEmail();

        favoriteViewModel.loadResultMyFavorite(email);
        favoriteViewModel.getDataMyFavorite().observe(this, result -> {

            Log.d(TAG, "onViewCreated: "+ result);
//            if (result.isEmpty()){
//                Toast.makeText(getContext(), "Favorite Kosong" , Toast.LENGTH_SHORT).show();
//            }else {
//                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                rvFavoriteSaya.setLayoutManager(layoutManager);
//                JasaMenuAdapter adapter = new JasaMenuAdapter(result);
//
//                rvFavoriteSaya.setAdapter(adapter);
//            }

        });

        super.onViewCreated(view, savedInstanceState);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
}
package com.udindev.sade.fragment;

import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udindev.sade.R;
import com.udindev.sade.activity.LoginActivity;
import com.udindev.sade.activity.PusatBantuanActivity;
import com.udindev.sade.model.Profile;
import com.udindev.sade.viewmodel.ProfileViewModel;

import static com.udindev.sade.utils.AppUtils.loadImageFromUrl;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView imgPhoto;
    private TextView tvName, tvEmail, tvAddress, tvPhoneNumber, tvWaNumber;
    private ProfileViewModel profileViewModel;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d(getClass().getSimpleName(), "userId: " + firebaseUser.getUid());

        imgPhoto = view.findViewById(R.id.img_photo_profile);
        tvName = view.findViewById(R.id.tv_name_profile);
        tvEmail = view.findViewById(R.id.tv_email_profile);
        tvAddress = view.findViewById(R.id.tv_address_profile);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number_profile);
        tvWaNumber = view.findViewById(R.id.tv_wa_number_profile);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel.class);
        profileViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                loadImageFromUrl(getContext(), imgPhoto, firebaseUser.getPhotoUrl().toString());
                tvName.setText(firebaseUser.getDisplayName());
                tvEmail.setText(firebaseUser.getEmail());
                tvAddress.setText(profile.getAddress());
                tvPhoneNumber.setText(profile.getPhoneNumber());
                tvWaNumber.setText(profile.getWaNumber());
            }
        });

        Button btnLogoutTest = view.findViewById(R.id.btn_logout_test_profile);
        btnLogoutTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignIn.getClient(getActivity(), gso).signOut();
                firebaseAuth.signOut();

                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        Button btnPusatBantuan;
        btnPusatBantuan = view.findViewById(R.id.btn_pusat_bantuan);
        btnPusatBantuan.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PusatBantuanActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        profileViewModel.loadData(firebaseUser.getUid());
    }
}
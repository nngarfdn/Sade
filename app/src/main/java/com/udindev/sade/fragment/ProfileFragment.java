package com.udindev.sade.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.rishabhharit.roundedimageview.RoundedImageView;
import com.udindev.sade.R;
import com.udindev.sade.activity.LoginActivity;
import com.udindev.sade.activity.PusatBantuanActivity;
import com.udindev.sade.activity.TentangAplikasi;
import com.udindev.sade.callback.OnProfileUpdateCallback;
import com.udindev.sade.model.Profile;
import com.udindev.sade.viewmodel.ProfileViewModel;

import static com.udindev.sade.utils.AppUtils.loadImageFromUrl;

public class ProfileFragment extends Fragment implements View.OnClickListener, OnProfileUpdateCallback {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RoundedImageView imgPhoto;
    private TextView tvName, tvAddress, tvFullname, tvEmail, tvFulladdress, tvPhoneNumber, tvWaNumber;
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

        ImageButton btnShop = view.findViewById(R.id.btn_shop_profile);
        btnShop.setOnClickListener(this);

        imgPhoto = view.findViewById(R.id.img_photo_profile);
        tvName = view.findViewById(R.id.tv_name_profile);
        tvAddress = view.findViewById(R.id.tv_address_profile);
        tvFullname = view.findViewById(R.id.tv_fullname_profile);
        tvEmail = view.findViewById(R.id.tv_email_profile);
        tvFulladdress = view.findViewById(R.id.tv_fulladdress_profile);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number_profile);
        tvWaNumber = view.findViewById(R.id.tv_wa_number_profile);

        TextView tvAbout = view.findViewById(R.id.tv_about_profile);
        TextView tvHelpCenter = view.findViewById(R.id.tv_help_center_profile);
        TextView tvLogout = view.findViewById(R.id.tv_logout_profile);
        tvAbout.setOnClickListener(this);
        tvHelpCenter.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        ImageButton btnAbout = view.findViewById(R.id.btn_about_profile);
        ImageButton btnHelpCenter = view.findViewById(R.id.btn_help_center_profile);
        ImageButton btnLogout = view.findViewById(R.id.btn_logout_profile);
        btnAbout.setOnClickListener(this);
        btnHelpCenter.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        Button btnEdit = view.findViewById(R.id.btn_edit_profile);
        btnEdit.setOnClickListener(this);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel.class);
        profileViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                // Daftar pakai email+pass tidak ada foto
                Uri photoUrl = firebaseUser.getPhotoUrl();
                if (photoUrl != null) loadImageFromUrl(imgPhoto, photoUrl.toString());

                tvName.setText(firebaseUser.getDisplayName());
                tvAddress.setText(profile.getAddress());
                tvFullname.setText(firebaseUser.getDisplayName());
                tvEmail.setText(firebaseUser.getEmail());
                tvFulladdress.setText(profile.getAddress());
                tvPhoneNumber.setText(profile.getPhoneNumber());
                tvWaNumber.setText(profile.getWaNumber());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        profileViewModel.loadData(firebaseUser.getUid());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_shop_profile:
                loadFragment(new TokoSayaFragment());
                break;

            case R.id.btn_edit_profile:
                loadFragment(new EditProfileFragment(this));
                break;

            case R.id.tv_about_profile:
            case R.id.btn_about_profile:
                startActivity(new Intent(getContext(), TentangAplikasi.class));
                break;

            case R.id.tv_help_center_profile:
            case R.id.btn_help_center_profile:
                startActivity(new Intent(getContext(), PusatBantuanActivity.class));
                break;

            case R.id.tv_logout_profile:
            case R.id.btn_logout_profile:
                new AlertDialog.Builder(getActivity())
                        .setMessage("Apakah Anda yakin ingin keluar?")
                        .setNegativeButton("Tidak", null)
                        .setNeutralButton("Batal", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                GoogleSignIn.getClient(getActivity(), gso).signOut();
                                firebaseAuth.signOut();

                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .create().show();
                break;
        }
    }

    private void loadFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpdate() {
        onStart();
    }
}
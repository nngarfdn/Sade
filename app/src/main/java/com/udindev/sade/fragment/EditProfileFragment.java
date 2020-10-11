package com.udindev.sade.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.udindev.sade.R;
import com.udindev.sade.model.Profile;
import com.udindev.sade.viewmodel.ProfileViewModel;

public class EditProfileFragment extends Fragment {
    private Button btnSave;
    private EditText edtName, edtAddress, edtPhone, edtWhatsapp;
    private FirebaseUser firebaseUser;
    private ProfileViewModel profileViewModel;
    private boolean isNewUser;

    public EditProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        edtName = view.findViewById(R.id.edt_name_edit_profile);
        edtAddress = view.findViewById(R.id.edt_address_edit_profile);
        edtPhone = view.findViewById(R.id.edt_phone_edit_profile);
        edtWhatsapp = view.findViewById(R.id.edt_whatsapp_edit_profile);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel.class);
        profileViewModel.loadData(firebaseUser.getUid());
        profileViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                edtName.setText(firebaseUser.getDisplayName());
                edtAddress.setText(profile.getAddress());
                edtPhone.setText(profile.getPhoneNumber());
                edtWhatsapp.setText(profile.getWaNumber());
                btnSave.setEnabled(true);

                // Kalau alamatnya kosong, berarti pengguna baru (belum pernah isi data)
                isNewUser = (edtAddress.getText().toString().equals(""));
            }
        });

        btnSave = view.findViewById(R.id.btn_save_edit_profile);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhone.getText().toString();
                String whatsapp = edtWhatsapp.getText().toString();

                if (name.equals("") || address.equals("") || phone.equals("") || whatsapp.equals("")){
                    if (name.equals("")) edtName.setError("Tidak boleh kosong");
                    if (address.equals("")) edtAddress.setError("Tidak boleh kosong");
                    if (phone.equals("")) edtPhone.setError("Tidak boleh kosong");
                    if (whatsapp.equals("")) edtWhatsapp.setError("Tidak boleh kosong");
                    return;
                }

                Profile profile = new Profile(address, phone, whatsapp);
                if (isNewUser) profileViewModel.insert(firebaseUser.getUid(), profile);
                else profileViewModel.update(firebaseUser.getUid(), profile);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                firebaseUser.updateProfile(profileUpdates);

                if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
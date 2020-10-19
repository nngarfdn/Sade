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
import com.udindev.sade.callback.OnProfileUpdateCallback;
import com.udindev.sade.customview.LoadingDialog;
import com.udindev.sade.model.Profile;
import com.udindev.sade.viewmodel.ProfileViewModel;

public class EditProfileFragment extends Fragment {
    private Button btnSave;
    private EditText edtName, edtAddress, edtPhoneNumber, edtWaNumber;
    private FirebaseUser firebaseUser;
    private ProfileViewModel profileViewModel;
    private boolean isNewUser;
    private LoadingDialog loadingDialog;
    private OnProfileUpdateCallback callback;

    public EditProfileFragment(OnProfileUpdateCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.show();

        edtName = view.findViewById(R.id.edt_name_ep);
        edtAddress = view.findViewById(R.id.edt_address_ep);
        edtPhoneNumber = view.findViewById(R.id.edt_phone_number_ep);
        edtWaNumber = view.findViewById(R.id.edt_wa_number_ep);

        profileViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel.class);
        profileViewModel.loadData(firebaseUser.getUid());
        profileViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                edtName.setText(firebaseUser.getDisplayName());
                edtAddress.setText(profile.getAddress());
                edtPhoneNumber.setText(profile.getPhoneNumber());
                edtWaNumber.setText(profile.getWaNumber());
                btnSave.setEnabled(true);

                // Kalau alamatnya kosong, berarti pengguna baru (belum pernah isi data)
                isNewUser = (edtAddress.getText().toString().equals(""));

                loadingDialog.dismiss();
            }
        });

        btnSave = view.findViewById(R.id.btn_save_ep);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhoneNumber.getText().toString();
                String whatsapp = edtWaNumber.getText().toString();

                if (name.equals("") || address.equals("") || phone.equals("") || whatsapp.equals("")){
                    if (name.equals("")) edtName.setError("Masukkan nama lengkap");
                    if (address.equals("")) edtAddress.setError("Masukkan alamat");
                    if (phone.equals("")) edtPhoneNumber.setError("Masukkan nomor telepon");
                    if (whatsapp.equals("")) edtWaNumber.setError("Masukkan nomor WhatsApp");
                    return;
                }

                Profile profile = new Profile(address, phone, whatsapp);
                if (isNewUser) profileViewModel.insert(firebaseUser.getUid(), profile); // Pengguna baru insert
                else profileViewModel.update(firebaseUser.getUid(), profile);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                firebaseUser.updateProfile(profileUpdates);

                callback.onUpdate();
                if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
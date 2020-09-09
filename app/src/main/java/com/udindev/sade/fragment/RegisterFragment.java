package com.udindev.sade.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.udindev.sade.R;
import com.udindev.sade.activity.MainActivity;
import com.udindev.sade.customview.LoadingDialog;

import static com.udindev.sade.utils.AppUtils.showSnackbar;

public class RegisterFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private FirebaseAuth firebaseAuth;
    private LoadingDialog loadingDialog;

    private EditText edtName, edtEmail, edtPassword;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initalize
        loadingDialog = new LoadingDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize view
        edtName = view.findViewById(R.id.edt_name_register);
        edtEmail = view.findViewById(R.id.edt_email_register);
        edtPassword = view.findViewById(R.id.edt_password_register);

        Button btnRegister = view.findViewById(R.id.btn_email_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithEmail(edtName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });
    }
    private void registerWithEmail(String name, String email, String password){
        if (!validateForm(name, email, password)) return;
        Log.d(TAG, "createAccount: " + email);

        loadingDialog.show();

        // Start create user with email
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Profile updates
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    });

                            Log.d(TAG, "createUserWithEmail: success");
                            launchMain();
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            showSnackbar(getActivity().findViewById(R.id.activity_login), "Email sudah terdaftar atau koneksi sedang bermasalah.");
                        }

                        loadingDialog.dismiss();
                    }
                });
    }

    private boolean validateForm(String name, String email, String password){
        boolean valid = true;

        if (TextUtils.isEmpty(name)){
            edtName.setError("Masukkan nama lengkap.");
            valid = false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid.");
            valid = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) { // Syarat Firebase Auth
            edtPassword.setError("Masukkan kata sandi minimal 6 karakter.");
            valid = false;
        }

        return valid;
    }

    private void launchMain(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
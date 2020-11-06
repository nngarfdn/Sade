package com.udindev.sade.fragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.udindev.sade.R;
import com.udindev.sade.activity.MainActivity;
import com.udindev.sade.customview.LoadingDialog;
import com.udindev.sade.dialogfragment.ResetPasswordDialog;

import static com.udindev.sade.utils.AppUtils.showSnackbar;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private LoadingDialog loadingDialog;

    private EditText edtEmail, edtPassword;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize
        loadingDialog = new LoadingDialog(getActivity());
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Initialize view
        edtEmail = view.findViewById(R.id.edt_email_login);
        edtPassword = view.findViewById(R.id.edt_password_login);

        Button btnLogin = view.findViewById(R.id.btn_email_login);
        Button btnGoogle = view.findViewById(R.id.btn_google_login);
        TextView tvPasswordReset = view.findViewById(R.id.tv_reset_password_login);

        btnLogin.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        tvPasswordReset.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_email_login:
                loginWithEmail(edtEmail.getText().toString(), edtPassword.getText().toString());
                break;

            case R.id.btn_google_login:
                loginWithGoogle();
                break;

            case R.id.tv_reset_password_login:
                ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog();
                resetPasswordDialog.show(getActivity().getSupportFragmentManager(), "ResetPasswordDialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                // Google Sign In failed or user press back button
                Log.w(TAG, "Google sign in failed", e);
                //showSnackbar(getActivity().findViewById(R.id.activity_login), "Email gagal diautentikasi.");
            }
        }
    }

    private void loginWithGoogle(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        loadingDialog.show();

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's informationZ
                            Log.d(TAG, "signInWithCredential: success");
                            launchMain();
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            showSnackbar(getActivity().findViewById(R.id.activity_login), "Email gagal diautentikasi.");
                        }

                        loadingDialog.dismiss();
                    }
                });
    }

    private void loginWithEmail(String email, String password){
        if (!validateForm(email, password)) return;
        Log.d(TAG, "signIn: " + email);

        loadingDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail: success");
                            launchMain();
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithEmail: failure", task.getException());
                            showSnackbar(getActivity().findViewById(R.id.activity_login), "Kata sandi salah atau koneksi sedang bermasalah.");
                        }

                        loadingDialog.dismiss();
                    }
                });
    }

    private boolean validateForm(String email, String password){
        boolean valid = true;

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Masukkan kata sandi");
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
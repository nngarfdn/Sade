package com.udindev.sade.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.udindev.sade.R;
import com.udindev.sade.customview.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private LoadingDialog loadingDialog;
    private EditText edtNameReg, edtEmailReg, edtPasswordReg, edtEmailLog, edtPasswordLog;
    private Button btnGoogle, btnLogin, btnRegister, btnPasswordReset, btnLogout, btnCheckVerify, btnSendVerify;
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initalize
        firebaseAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(this);

        // Login by Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle = findViewById(R.id.btn_google);
        btnGoogle.setOnClickListener(this);

        // Login by Email
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnPasswordReset = findViewById(R.id.btn_password_reset);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnPasswordReset.setOnClickListener(this);

        edtNameReg = findViewById(R.id.edt_name_reg);
        edtEmailReg = findViewById(R.id.edt_email_reg);
        edtPasswordReg = findViewById(R.id.edt_password_reg);
        edtEmailLog = findViewById(R.id.edt_email_log);
        edtPasswordLog = findViewById(R.id.edt_password_log);

        // Login success
        btnCheckVerify = findViewById(R.id.btn_check_verify);
        btnSendVerify = findViewById(R.id.btn_send_verify);
        btnLogout = findViewById(R.id.btn_logout);

        btnCheckVerify.setOnClickListener(this);
        btnSendVerify.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_google:
                loginWithGoogle();
                break;

            case R.id.btn_login:
                loginWithEmail(edtEmailLog.getText().toString(), edtPasswordLog.getText().toString());
                break;

            case R.id.btn_register:
                registerWithEmail(edtNameReg.getText().toString(), edtEmailReg.getText().toString(), edtPasswordReg.getText().toString());
                break;

            case R.id.btn_password_reset:
                sendPasswordReset(edtEmailLog.getText().toString());
                break;

            case R.id.btn_check_verify:
                showSnackbar(findViewById(R.id.activity_login),
                        String.valueOf(firebaseAuth.getCurrentUser().isEmailVerified()));
                break;

            case R.id.btn_send_verify:
                firebaseAuth.getCurrentUser().sendEmailVerification();
                showSnackbar(findViewById(R.id.activity_login), "Check your email.");
                break;

            case R.id.btn_logout:
                googleSignInClient.signOut();
                firebaseAuth.signOut();
                updateUI(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser != null);
    }

    /*
    * Google*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                showSnackbar(findViewById(R.id.activity_login), "Authentication failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        loadingDialog.showDialog();

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's informationZ
                            Log.d(TAG, "signInWithCredential: success");
                            updateUI(true);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            showSnackbar(findViewById(R.id.activity_login), "Authentication failed.");
                        }

                        loadingDialog.dismissDialog();
                    }
                });
    }

    private void loginWithGoogle(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*
     * Email
     * */
    private void registerWithEmail(final String fullname, String email, String password){
        Log.d(TAG, "createAccount: " + email);
        if (!validateForm(true)) return;

        loadingDialog.showDialog();

        // Start create user with email
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Profile updates
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname)
                                    .build();
                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    });

                            Log.d(TAG, "createUserWithEmail: success");
                            updateUI(true);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            showSnackbar(findViewById(R.id.activity_login), "Authentication failed.");
                        }

                        loadingDialog.dismissDialog();
                    }
                });
    }

    private void loginWithEmail(String email, String password){
        Log.d(TAG, "signIn: " + email);
        if (!validateForm(false)) return;

        loadingDialog.showDialog();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail: success");
                            updateUI(true);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithEmail: failure", task.getException());
                            showSnackbar(findViewById(R.id.activity_login), "Authentication failed.");
                        }

                        loadingDialog.dismissDialog();
                    }
                });
    }

    // Reset password
    private void sendPasswordReset(String email){
        if (email.isEmpty()) return;

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                            showSnackbar(findViewById(R.id.activity_login), "Email sent.");
                        }
                    }
                });
    }

    private boolean validateForm(boolean isReg){
        boolean valid = true;

        if (isReg){
            String fullname = edtNameReg.getText().toString();
            if (TextUtils.isEmpty(fullname)){
                edtNameReg.setError("Required.");
                valid = false;
            } else edtNameReg.setError(null);

            String email = edtEmailReg.getText().toString();
            if (TextUtils.isEmpty(email)){
                edtEmailReg.setError("Required.");
                valid = false;
            } else edtEmailReg.setError(null);

            String password = edtPasswordReg.getText().toString();
            if (TextUtils.isEmpty(password)){
                edtPasswordReg.setError("Required.");
                valid = false;
            }
        } else {
            String email = edtEmailLog.getText().toString();
            if (TextUtils.isEmpty(email)){
                edtEmailLog.setError("Required.");
                valid = false;
            } else edtEmailLog.setError(null);

            String password = edtPasswordLog.getText().toString();
            if (TextUtils.isEmpty(password)){
                edtPasswordLog.setError("Required.");
                valid = false;
            }
        }

        return valid;
    }

    /*
    *
    * */
    private void updateUI(boolean isLoginSuccess){
        if (isLoginSuccess){
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            tvName.setText(firebaseUser.getDisplayName());
            tvEmail.setText(firebaseUser.getEmail());
            btnLogout.setEnabled(true);
            btnCheckVerify.setEnabled(true);
            btnSendVerify.setEnabled(true);

            btnLogin.setEnabled(false);
            btnPasswordReset.setEnabled(false);
            btnRegister.setEnabled(false);
            btnGoogle.setEnabled(false);
        } else {
            tvName.setText("---");
            tvEmail.setText("---");
            btnLogout.setEnabled(false);
            btnCheckVerify.setEnabled(false);
            btnSendVerify.setEnabled(false);

            btnLogin.setEnabled(true);
            btnPasswordReset.setEnabled(true);
            btnRegister.setEnabled(true);
            btnGoogle.setEnabled(true);
        }
    }

    private void showSnackbar(View view, String message){
        Snackbar.make(view, message, 6000).show();
    }
}
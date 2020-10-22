package com.udindev.sade.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.udindev.sade.R;

import static com.udindev.sade.utils.AppUtils.showSnackbar;

public class ResetPasswordDialog extends DialogFragment {
    private final String TAG = getClass().getSimpleName();
    private AlertDialog dialog;
    private FirebaseAuth firebaseAuth;
    private EditText edtEmail;

    public ResetPasswordDialog(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reset_password, null);

        firebaseAuth = FirebaseAuth.getInstance();

        edtEmail = view.findViewById(R.id.edt_email_rp);
        Button btnSend = view.findViewById(R.id.btn_send_rp);
        Button btnCancel = view.findViewById(R.id.btn_cancel_rp);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetPassword(edtEmail.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Create alert dialog instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }

    private void sendResetPassword(String email){
        if (!validateForm(email)) return;

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                            showSnackbar(getActivity().findViewById(R.id.activity_login), "Permintaan setel ulang kata sandi telah dikirim ke email.");
                        } else {
                            Log.d(TAG, "Email sent failed.");
                            showSnackbar(getActivity().findViewById(R.id.activity_login), "Email tidak terdaftar atau koneksi sedang bermasalah.");
                        }
                        dialog.dismiss();
                    }
                });
    }

    private boolean validateForm(String email){
        boolean valid = true;

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        return valid;
    }
}

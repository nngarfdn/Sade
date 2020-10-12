package com.udindev.sade.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.udindev.sade.R;

public class FilterDialog extends DialogFragment {
    private final String TAG = getClass().getSimpleName();

    private Activity activity;
    private AlertDialog dialog;

    public FilterDialog(Activity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        // Create alert dialog instance
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }
}

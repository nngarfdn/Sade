package com.udindev.sade.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.udindev.sade.R;

public class LoadingDialog {
    private final AlertDialog dialog;

    public LoadingDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        dialog = builder.create();
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
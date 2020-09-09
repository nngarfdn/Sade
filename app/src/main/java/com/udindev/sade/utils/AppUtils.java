package com.udindev.sade.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AppUtils {
    public static void showSnackbar(View view, String message){
        Snackbar.make(view, message, 6000).show();
    }
}

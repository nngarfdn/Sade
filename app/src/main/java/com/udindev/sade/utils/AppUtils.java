package com.udindev.sade.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.udindev.sade.R;

public class AppUtils {
    public static void showSnackbar(View view, String message){
        Snackbar.make(view, message, 6000).show();
    }

    public static void loadImageFromUrl(Context context, ImageView imageView, String url){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.black)
                .error(R.color.colorPrimary);
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}

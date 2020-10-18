package com.udindev.sade.utils;

import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.udindev.sade.R;
import com.udindev.sade.model.Filter;

public class AppUtils {
    public static void showSnackbar(View view, String message){
        Snackbar.make(view, message, 6000).show();
    }

    public static void loadImageFromUrl(ImageView imageView, String url){
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.image_empty)
                .into(imageView);
    }

    public static Filter getDefaultFilter(){
        return new Filter("",
                true, true, true, true,
                false, false, false,
                true,
                "", "", "",
                0, 0, 0);
    }
}

package com.udindev.sade.utils;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.udindev.sade.R;
import com.udindev.sade.model.Filter;

import java.text.NumberFormat;
import java.util.Locale;

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

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

    public static String getSimpleKabupaten(String namaKabupaten){
        try {
            String[] arrayKabupaten = namaKabupaten.split(" ");
            StringBuilder namaBaru = new StringBuilder(arrayKabupaten[1]);
            for (int i = 2; i < arrayKabupaten.length; i++) namaBaru.append(" ").append(arrayKabupaten[i]);
            return namaBaru.toString();
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
            return namaKabupaten;
        }
    }

    public static String getRupiahFormat(int amount, boolean getUnit){
        String country = "ID", language = "in";

        String formattedAmount = NumberFormat.getNumberInstance(new Locale(language, country)).format(amount);
        if (getUnit) return "Rp" + formattedAmount;
        return formattedAmount;
    }
}

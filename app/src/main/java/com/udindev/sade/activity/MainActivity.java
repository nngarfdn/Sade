package com.udindev.sade.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.udindev.sade.R;
import com.udindev.sade.model.Produk;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nanangTest = findViewById(R.id.tesDataNanang);
        nanangTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, KotlinTest.class);
                startActivity(intent);
            }
        });

        Button btnLogin = findViewById(R.id.tesLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Produk produk = new  Produk("1", "Java",null,null,null,null,null,null,null,null, null);
        Log.d(TAG, "onCreate: " + produk.toString());

        Log.d(TAG, "onCreate: " + produk.component2());
    }
}
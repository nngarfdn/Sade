package com.udindev.sade.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.udindev.sade.R
import com.udindev.sade.model.Produk
import com.udindev.sade.viewmodel.ProdukViewModel

class KotlinTest : AppCompatActivity() {
    private val TAG = KotlinTest::class.java.simpleName
    private lateinit var produkViewModel: ProdukViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        produkViewModel = ViewModelProviders.of(this).get(ProdukViewModel::class.java)

        val produk1 = Produk(nama = "Bebek")
        val produk2 = Produk(nama = "Kudanil")

        val delete = Produk(id="qmRqDjgy2xcYLWZw60yE")

//        val update = Produk(id = "dO5s4MDS2AuWRCpgmiXh" , nama = "Bebek Baru Update")

//        produkViewModel.updateProduk(update)
//        dadi

//        deleteData(delete)
//        harusnya kudanil nggak ada (oke dadi)

        getData()

    }


    private fun insertData(produk: Produk) {
        produkViewModel.insertProduk(produk)
    }

    private fun deleteData(produk: Produk) {
        produkViewModel.deteteProduk(produk)
    }

    private fun getData() {
        produkViewModel.loadResult()
        produkViewModel.getResult().observe(this, Observer<List<Produk>> { result ->
            Log.d(TAG, "onCreate: $result")
        })
    }
}
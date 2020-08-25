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

        val produk1 = Produk(nama = "Kase" , kategori = "produk", wa = "083498524232")
        val produk2 = Produk(nama = "Dompet" , kategori = "produk" )
        val produk3 = Produk(nama = "Jagung" , kategori = "produk")
        val produk4 = Produk(nama= "Telur")
        val produk5 = Produk(nama = "Apel")


//        val list = arrayListOf<Produk>(produk1,produk2,produk3,produk4,produk5)
//        for (produk in list) {
//            insertData(produk)
//        }

        getData()
        produkViewModel.loadResultByKategory("produk")
        getDataByKategory()

    }

    private fun getDataByKategory() {
        produkViewModel.getResultByKategori().observe(this, Observer<List<Produk>> { result ->
            Log.d(TAG, "onCreate: $result")
        })
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